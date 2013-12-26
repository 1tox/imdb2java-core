package com.tocchisu.movies.interfaces;

import java.io.File;

@SuppressWarnings("serial")
public class FileAlreadyDownloaded extends Exception {
	private File	file;

	public FileAlreadyDownloaded(File file) {
		super("File " + file.getAbsolutePath() + " has already been downloaded");
		this.file = file;
	}

	public File getFile() {
		return file;
	}

}
