package com.tocchisu.movies.interfaces;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	 * @param interfaceName
	 *            The IMDB interface name, ie the IMDB file name without its prefix '.list.gz' (eg "movies" for movies.list.gz)
	 * @param destinationDirectory
	 *            The target directory where interfaces will be downloaded
	 * @param forceDownload
	 *            Should we re-download the file if it is already donwloaded
	 * @param listener
	 *            A listener which provides utilities for displaying information about download status. Could be null if useless
	 * @return An unzipped plain text file
	 * @throws IOException
	 */
	public static File download(String interfaceName, File destinationDirectory, DownloadStatusListener listener) throws IOException, FileAlreadyDownloaded {
		File zippedFile = getZippedFile(interfaceName, destinationDirectory);
		File unzippedFile = getUnzippedFile(zippedFile);
		if (unzippedFile.exists()) {
			throw new FileAlreadyDownloaded(unzippedFile);
		}
		return download(interfaceName, listener, zippedFile);
	}

	public static File reDownload(String interfaceName, File destinationDirectory, DownloadStatusListener listener) throws IOException {
		File zippedFile = getZippedFile(interfaceName, destinationDirectory);
		return download(interfaceName, listener, zippedFile);
	}

	private static File download(String interfaceName, DownloadStatusListener listener, File zippedFile) throws MalformedURLException, IOException,
			FileNotFoundException {
		URL sourceURL = getSourceURL(interfaceName);
		URLConnection connection = sourceURL.openConnection();
		long fileSize = connection.getContentLength();
		if (listener != null) {
			listener.beforeDownload(fileSize);
		}
		long totalDataRead = 0;
		BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
		FileOutputStream fos = new FileOutputStream(zippedFile);
		BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);
		byte[] data = new byte[1024];
		int i = 0;
		while ((i = in.read(data, 0, 1024)) >= 0) {
			totalDataRead = totalDataRead + i;
			if (listener != null) {
				listener.onProgress(totalDataRead, fileSize);
			}
			bos.write(data, 0, i);
		}
		try {
			bos.close();
			in.close();
		}
		finally {
			if (listener != null) {
				listener.afterDownload(totalDataRead, fileSize, zippedFile);
			}
		}
		File unzippedFile = unGzip(zippedFile);
		zippedFile.delete();
		return unzippedFile;
	}

	/**
	 * @param interfaceName
	 * @param destinationDirectory
	 * @return
	 */
	private static File getZippedFile(String interfaceName, File destinationDirectory) {
		return new File(destinationDirectory, MessageFormat.format(IMDB_INTERFACE_FILE_NAME, interfaceName));
	}

	private static File unGzip(File sourceFile) throws IOException {
		if (!sourceFile.exists()) {
			throw new IllegalArgumentException(MessageFormat.format("{0} not found", sourceFile));
		}
		if (sourceFile.isDirectory()) {
			throw new IllegalArgumentException(MessageFormat.format("{0} is a directory file. You have to provide a file instead.", sourceFile));
		}
		GZIPInputStream fis = new GZIPInputStream(new FileInputStream(sourceFile));
		File destinationFile = getUnzippedFile(sourceFile);
		FileUtils.copyInputStreamToFile(fis, destinationFile);
		return destinationFile;
	}

	private static File getUnzippedFile(File zippedFile) {
		return new File(zippedFile.getParentFile(), StringUtils.substringBeforeLast(zippedFile.getName(), "."));
	}

	private static URL getSourceURL(String interfaceName) throws MalformedURLException {
		return new URL(MessageFormat.format(IMDB_INTERFACE_URL, DEFAULT_MIRROR, interfaceName));
	}
}