package com.tocchisu.movies.interfaces;

import static org.apache.commons.io.FileUtils.*;
import static org.apache.commons.io.IOUtils.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.zip.GZIPInputStream;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.commons.lang3.StringUtils;

/**
 * IMDB regularly provides plain text data files, called "interfaces", that stores useful informations about movies, actors, genre, ratings, etc. These files,
 * can be held locally by downloading them at <a href="http://www.imdb.com/interfaces">http://www.imdb.com/interfaces</a>.
 * This class provides utilities methods for managing IMDB interfaces, such like downloading interfaces, unzipping them, etc.
 */
public class IMDBInterfacesManager {
	/**
	 * URL for downloading plain text interfaces
	 */
	private static final String	IMDB_INTERFACE_URL			= "{0}/{1}.list.gz";
	private static final String	IMDB_INTERFACE_FILE_NAME	= "{0}.list.gz";

	// Mirrors for FTP downloads
	private static final String	DE_MIRROR					= "ftp://ftp.fu-berlin.de/pub/misc/movies/database";
	@SuppressWarnings("unused")
	private static final String	FI_MIRROR					= "ftp://ftp.funet.fi/pub/mirrors/ftp.imdb.com/pub";
	@SuppressWarnings("unused")
	private static final String	SW_MIRROR					= "ftp://ftp.sunet.se/pub/tv+movies/imdb";
	private static final String	DEFAULT_MIRROR				= DE_MIRROR;

	private IMDBInterfacesManager() {}

	/**
	 * @param destinationDirectory
	 *            The target directory where interfaces will be downloaded
	 * @return An unzipped plain text file
	 * @throws IOException
	 */

	public static File download(String interfaceName, File destinationDirectory, final DownloadListener listener) throws IOException {
		File destinationFile = new File(destinationDirectory, MessageFormat.format(IMDB_INTERFACE_FILE_NAME, interfaceName));
		URL sourceURL = getSourceURL(interfaceName);
		URLConnection urlConnection = sourceURL.openConnection();
		String headerField = urlConnection.getHeaderField("Content-Length");
		listener.beforeDownload(StringUtils.isBlank(headerField) ? 0 : Long.parseLong(headerField));
		CountingInputStream input = new CountingInputStream(urlConnection.getInputStream()) {

			@Override
			protected synchronized void afterRead(int n) {
				listener.onProgress(getByteCount());
			}

		};
		try {
			// Checks
			FileOutputStream output = openOutputStream(destinationFile);
			try {
				copy(input, output);
			}
			finally {
				closeQuietly(output);
			}
		}
		finally {
			closeQuietly(input);
		}
		File unzippedFile = unGzip(destinationFile);
		destinationFile.delete();
		return unzippedFile;
	}

	private static File unGzip(File sourceFile) throws IOException {
		if (!sourceFile.exists()) {
			throw new IllegalArgumentException(MessageFormat.format("{0} not found", sourceFile));
		}
		if (sourceFile.isDirectory()) {
			throw new IllegalArgumentException(MessageFormat.format("{0} is a directory file. You have to provide a file instead.", sourceFile));
		}
		GZIPInputStream fis = new GZIPInputStream(new FileInputStream(sourceFile));
		File destinationFile = new File(sourceFile.getParentFile(), StringUtils.substringBeforeLast(sourceFile.getName(), "."));
		FileOutputStream fos = new FileOutputStream(destinationFile);
		doCopy(fis, fos);
		return destinationFile;
	}

	private static void doCopy(InputStream is, OutputStream os) throws IOException {
		try {
			int oneByte;
			while ((oneByte = is.read()) != -1) {
				os.write(oneByte);
			}
		}
		finally {
			if (os != null) {
				os.close();
			}
			if (is != null) {
				is.close();
			}
		}
	}

	private static URL getSourceURL(String interfaceName) throws MalformedURLException {
		return new URL(MessageFormat.format(IMDB_INTERFACE_URL, DEFAULT_MIRROR, interfaceName));
	}
}