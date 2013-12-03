package com.tocchisu.movies.interfaces;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;

public class IMDBUtils {
	// URL for downloading plain text interfaces
	private static final String	IMDB_INTERFACES_URL	= "http://www.website.com/information.asp";

	private IMDBUtils() {}

	/**
	 * @param targetDirectory
	 *            The target directory where interfaces will be downloaded
	 * @return
	 * @throws IOException
	 */
	public static File downloadInterfaces(File targetDirectory) throws IOException {
		File destinationFile = new File(targetDirectory, "movies.txt");
		FileUtils.copyURLToFile(new URL(IMDB_INTERFACES_URL), destinationFile);
		return destinationFile;
	}
}
