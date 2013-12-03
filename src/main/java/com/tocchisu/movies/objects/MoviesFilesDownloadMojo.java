package com.tocchisu.movies.objects;

import java.text.MessageFormat;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "download")
public class MoviesFilesDownloadMojo extends AbstractMojo {

	public void execute() throws MojoExecutionException {
		if (getLog().isWarnEnabled()) {
			getLog().warn(
					MessageFormat.format(
							"Movies files are downloaded in a default directory {0}. It is likely to define your own directory to receive movies files.",
							"toto"));
		}
	}
}