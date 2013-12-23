package com.tocchisu.movies.interfaces;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.zip.GZIPInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * IMDB regularly provides plain text data files, called "interfaces", that stores useful informations about movies, actors, genre, ratings, etc. These files,
 * can be held locally by downloading them at <a href="http://www.imdb.com/interfaces">http://www.imdb.com/interfaces</a>.
 * This class provides utilities methods for managing IMDB interfaces, such like downloading interfaces, unzipping them, etc.
 */
public class IMDBInterfacesManager {
	/**
	 * URL pattern for downloading plain text interfaces
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

	public static File download(String interfaceName, File destinationDirectory, final DownloadStatusListener listener) throws IOException {
		File destinationFile = new File(destinationDirectory, MessageFormat.format(IMDB_INTERFACE_FILE_NAME, interfaceName));
		URL sourceURL = getSourceURL(interfaceName);
		URLConnection connection = sourceURL.openConnection();
		long fileSize = connection.getContentLength();
		listener.beforeDownload(fileSize);
		long totalDataRead = 0;
		BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
		FileOutputStream fos = new FileOutputStream(destinationFile);
		BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);
		byte[] data = new byte[1024];
		int i = 0;
		while ((i = in.read(data, 0, 1024)) >= 0) {
			totalDataRead = totalDataRead + i;
			listener.onProgress(totalDataRead, fileSize);
			bos.write(data, 0, i);
		}
		try {
			bos.close();
			in.close();
		}
		finally {
			listener.afterDownload(totalDataRead, fileSize, destinationFile);
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
		FileUtils.copyInputStreamToFile(fis, destinationFile);
		return destinationFile;
	}

	private static URL getSourceURL(String interfaceName) throws MalformedURLException {
		return new URL(MessageFormat.format(IMDB_INTERFACE_URL, DEFAULT_MIRROR, interfaceName));
	}
}