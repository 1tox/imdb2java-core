package com.tocchisu.movies.interfaces;

import java.io.File;

public interface DownloadStatusListener {
	void beforeDownload(Long fileSize);

	void onProgress(Long bytesCount, Long fileSize);

	void afterDownload(Long bytesCount, Long fileSize, File destinationFile);
}
