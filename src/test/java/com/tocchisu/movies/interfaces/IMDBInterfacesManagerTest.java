package com.tocchisu.movies.interfaces;

import static junit.framework.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
public class IMDBInterfacesManagerTest {
	public void testDownloadInterfaces() throws IOException, FileAlreadyDownloaded {
		File destinationDirectory = new File(System.getProperty("java.io.tmpdir"));
		IMDBInterfacesManager.download("iso-aka-titles", destinationDirectory, new DownloadStatusListener() {

			@Override
			public void beforeDownload(Long fileSize) {
				System.out.println(FileUtils.byteCountToDisplaySize(fileSize));
			}

			@Override
			public void onProgress(Long bytesCount, Long fileSize) {
				System.out.println(FileUtils.byteCountToDisplaySize(bytesCount));
			}

			@Override
			public void afterDownload(Long bytesCount, Long fileSize, File destinationFile) {

			}
		});
	}

	@Test
	public void testUnGzip() throws Exception {
		URL sourceURL = Thread.currentThread().getContextClassLoader().getResource("iso-aka-titles.list.gz");
		File destinationFile;
		try {
			destinationFile = new File(System.getProperty("java.io.tmpdir"), "Wrong filename");
			Whitebox.invokeMethod(IMDBInterfacesManager.class, "unGzip", destinationFile);
			fail("Should throw an IllegalArgumentException when trying to ungzip");
		}
		catch (IllegalArgumentException e) {

		}
		try {
			destinationFile = new File(System.getProperty("java.io.tmpdir"));
			Whitebox.invokeMethod(IMDBInterfacesManager.class, "unGzip", destinationFile);
			fail("Should throw an IllegalArgumentException when trying to ungzip");
		}
		catch (IllegalArgumentException e) {

		}
		destinationFile = new File(System.getProperty("java.io.tmpdir"), "iso-aka-titles.list.gz");
		FileUtils.copyURLToFile(sourceURL, destinationFile);
		File unzippedFile = Whitebox.invokeMethod(IMDBInterfacesManager.class, "unGzip", destinationFile);
		assertNotNull(unzippedFile);
		assertEquals("iso-aka-titles.list", unzippedFile.getName());
		assertTrue(IOUtils.toString(new FileInputStream(unzippedFile)).startsWith("CRC: 0x8D08329F"));
	}

	@Test
	public void getSourceURLTest() throws Exception {
		URL url = Whitebox.<URL> invokeMethod(IMDBInterfacesManager.class, "getSourceURL", "movies");
		assertEquals("ftp://ftp.fu-berlin.de/pub/misc/movies/database/movies.list.gz", url.toString());
	}
}