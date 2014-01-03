package com.tocchisu.movies.parsers;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tocchisu.movies.objects.Genre;
import com.tocchisu.movies.objects.Movie;

public class PlainTextMovieGenreInterfaceParser extends PlainTextInterfaceParser<Movie> {

	private final static Logger	LOGGER	= LoggerFactory.getLogger(PlainTextMovieInterfaceParser.class);

	@Override
	final protected Movie parseLine(Matcher matcher, String line, Long lineNumber) {

		try {
			String movieName = matcher.group(1);
			Date releaseDate = getDate(matcher.group(2), YEAR_DATE_FORMAT);
			String genreName = matcher.group(7);
			Genre genre = new Genre(genreName);
			Movie movie = new Movie();
			movie.setName(movieName);
			movie.setReleaseDate(releaseDate);
			movie.setGenre(genre);
			return movie;
		}
		catch (ParseException e) {
			final String message = "Bad release date format " + System.getProperty("line.separator") + "Line -->" + line + "<--";
			LOGGER.error(message);
			throw new RuntimeException(message);
		}
	}

	@Override
	protected String getLinePattern() {
		final String MOVIE_NAME_PATTERN = "\"(.+?)\"";
		final String RELEASE_DATE_PATTERN = "\\s\\((?:(?:(\\d{4})||\\?{4})(?:/[IXV]+)?\\)?)\\)";
		final String EPISODE_PATTERN = "(?:\\s+\\{(.+?)?(?:\\s+\\((?:#(?:(\\d+?)\\.(\\d+?)))?(\\d{4}-\\d{2}-\\d{2})?\\))?\\})?";
		final String GENRE_PATTERN = "\\s+([\\w\\-]+)";
		return "^" + MOVIE_NAME_PATTERN + RELEASE_DATE_PATTERN + EPISODE_PATTERN + GENRE_PATTERN + "$";
	}
}
