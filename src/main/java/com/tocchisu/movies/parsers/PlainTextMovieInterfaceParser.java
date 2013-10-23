package com.tocchisu.movies.parsers;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tocchisu.movies.objects.Movie;

public class PlainTextMovieInterfaceParser extends PlainTextInterfaceParser<Movie> {

	private final static Logger				LOGGER				= LoggerFactory.getLogger(PlainTextMovieInterfaceParser.class);
	private final Set<String>				distinctMovieType	= new HashSet<String>();
	private final Set<String>				distinctCommentary	= new HashSet<String>();
	private static final SimpleDateFormat	EPISODE_DATE_FORMAT	= new SimpleDateFormat("yyyy-MM-dd");

	public PlainTextMovieInterfaceParser(File file) {
		super(file);
	}

	@Override
	public Movie parseLine(Matcher matcher, String line, Long lineNumber) {
		return assembleMovie(matcher, line, lineNumber);
	}

	private Movie assembleMovie(Matcher matcher, String line, @SuppressWarnings("unused") Long lineNumber) {
		String movieName = matcher.group(1);
		String releaseDate = matcher.group(2);
		String episodeName = matcher.group(3);
		Integer episodeSeason = matcher.group(4) == null ? null : Integer.parseInt(matcher.group(4));
		Integer episodeNumber = matcher.group(5) == null ? null : Integer.parseInt(matcher.group(5));
		String episodeDate = matcher.group(6);
		String broadcastDateBegin = matcher.group(7);
		String broadcastDateEnd = matcher.group(8);
		String commentary = matcher.group(9);
		Movie movie = new Movie();
		movie.setName(movieName);
		try {
			movie.setReleaseDate(getDate(releaseDate, YEAR_DATE_FORMAT));
		}
		catch (ParseException e) {
			LOGGER.error("Bad release date format : " + releaseDate + System.getProperty("line.separator") + "Line -->" + line + "<--");
		}
		movie.setEpisodeName(episodeName);
		movie.setEpisodeSeason(episodeSeason);
		movie.setEpisodeNumber(episodeNumber);
		if (episodeDate != null) {
			try {
				movie.setEpisodeDate(getDate(episodeDate, EPISODE_DATE_FORMAT));
			}
			catch (Exception e) {
				LOGGER.error("Bad episode date format : " + episodeDate + System.getProperty("line.separator") + "Line -->" + line + "<--");
			}
		}
		try {
			movie.setBroadcastDateBegin(getDate(broadcastDateBegin, YEAR_DATE_FORMAT));
		}
		catch (Exception e) {
			LOGGER.error("Bad begin broadcast date format : " + broadcastDateBegin + System.getProperty("line.separator") + "Line -->" + line + "<--");
		}
		try {
			movie.setBroadcastDateEnd(getDate(broadcastDateEnd, YEAR_DATE_FORMAT));
		}
		catch (Exception e) {
			LOGGER.error("Bad end broadcast date format : " + broadcastDateEnd + System.getProperty("line.separator") + "Line -->" + line + "<--");
		}
		return movie;
	}

	@Override
	protected void afterFileParsed() {
		System.out.println(distinctMovieType);
	}

	@Override
	protected String getLinePattern() {
		final String MOVIE_NAME_PATTERN = "(.+?)";
		final String RELEASE_DATE_PATTERN = "\\s\\((?:(?:(\\d{4})||\\?{4})(?:/[IXV]+)?\\)?)\\)";
		final String TV_PATTERN = "(?:\\s\\((?:(?:V)||(?:TV)||(?:VG))\\))?";
		final String EPISODE_PATTERN = "(?:\\s+\\{(.+?)?(?:\\s+\\((?:#(?:(\\d+?)\\.(\\d+?)))?(\\d{4}-\\d{2}-\\d{2})?\\))?\\})?";
		final String BROADCAST_DATE_PATTERN = "(?:\\s*\\t(?:(\\d{4})||\\?{4})(?:-(?:(\\d{4})||\\?{4}))?)";
		final String COMMENTARY = "(?:\\s+(.+))?";
		return "^" + MOVIE_NAME_PATTERN + RELEASE_DATE_PATTERN + TV_PATTERN + EPISODE_PATTERN + BROADCAST_DATE_PATTERN + COMMENTARY + "$";
	}
}