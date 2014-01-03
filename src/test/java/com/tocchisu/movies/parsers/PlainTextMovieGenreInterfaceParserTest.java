package com.tocchisu.movies.parsers;

import java.text.SimpleDateFormat;
import org.junit.Test;
import com.tocchisu.movies.objects.Genre;
import com.tocchisu.movies.objects.Movie;

public class PlainTextMovieGenreInterfaceParserTest extends AbstractParserTest<Movie> {
	@Test
	public void testParseLine() throws Exception {
		PlainTextMovieGenreInterfaceParser parser = new PlainTextMovieGenreInterfaceParser();
		Movie expectedObject = new Movie();
		expectedObject.setName("Artworks Scotland");
		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
		expectedObject.setReleaseDate(yearFormat.parse("2004"));
		Genre genre = new Genre("Documentary");
		expectedObject.setGenre(genre);
		testParser(parser, "\"Artworks Scotland\" (2004)				Documentary", expectedObject);
		expectedObject.setName("#1 Single");
		expectedObject.setReleaseDate(yearFormat.parse("2006"));
		genre.setName("Reality-TV");
		testParser(parser, "\"#1 Single\" (2006)					Reality-TV", expectedObject);
	}
}