package com.tocchisu.movies.parsers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import com.tocchisu.movies.objects.Movie;

@RunWith(PowerMockRunner.class)
public class PlainTextMovieInterfaceParserTest extends AbstractParserTest<Movie> {

	@Test
	public void testParseLine() throws Exception {
		String lineToTest = "\"Alfred J. Kwak\" (1989)					1989-????	(52 episodes)";
		PlainTextMovieInterfaceParser parser = new PlainTextMovieInterfaceParser();
		Movie movie = new Movie();
		movie.setName("Alfred J. Kwak");
		movie.setReleaseDate(PlainTextInterfaceParser.YEAR_DATE_FORMAT.parse("1989"));
		movie.setBroadcastDateBegin(PlainTextInterfaceParser.YEAR_DATE_FORMAT.parse("1989"));
		testParser(parser, lineToTest, movie);

		lineToTest = "\"Bente - En barrierekvinde\" (2001)			2001	(unreleased)";
		movie.setName("Bente - En barrierekvinde");
		movie.setReleaseDate(PlainTextInterfaceParser.YEAR_DATE_FORMAT.parse("2001"));
		movie.setBroadcastDateBegin(PlainTextInterfaceParser.YEAR_DATE_FORMAT.parse("2001"));
		testParser(parser, lineToTest, movie);

		lineToTest = "\"Big Brother\" (2000/IV)					2001-????";
		movie.setName("Big Brother");
		movie.setReleaseDate(PlainTextInterfaceParser.YEAR_DATE_FORMAT.parse("2000"));
		movie.setBroadcastDateBegin(PlainTextInterfaceParser.YEAR_DATE_FORMAT.parse("2001"));
		testParser(parser, lineToTest, movie);

		lineToTest = "\"1, 2 oder 3\" (1977) {Science-Fiction}			2000";
		movie.setName("1, 2 oder 3");
		movie.setReleaseDate(PlainTextInterfaceParser.YEAR_DATE_FORMAT.parse("1977"));
		movie.setBroadcastDateBegin(PlainTextInterfaceParser.YEAR_DATE_FORMAT.parse("2000"));
		movie.setEpisodeName("Science-Fiction");
		testParser(parser, lineToTest, movie);

		lineToTest = "\"A tortas con la vida\" (????) {A tortas con el amor (#2.11)}	2010-2012";
		movie.setName("A tortas con la vida");
		movie.setReleaseDate(null);
		movie.setBroadcastDateBegin(PlainTextInterfaceParser.YEAR_DATE_FORMAT.parse("2010"));
		movie.setBroadcastDateEnd(PlainTextInterfaceParser.YEAR_DATE_FORMAT.parse("2012"));
		movie.setEpisodeName("A tortas con el amor");
		movie.setEpisodeSeason(2);
		movie.setEpisodeNumber(11);
		testParser(parser, lineToTest, movie);

		lineToTest = "\"A tortas con la vida\" (2005) {A tortas con el amor (2010-12-03)}	????-2010";
		movie.setName("A tortas con la vida");
		movie.setReleaseDate(PlainTextInterfaceParser.YEAR_DATE_FORMAT.parse("2005"));
		movie.setBroadcastDateBegin(null);
		movie.setBroadcastDateEnd(PlainTextInterfaceParser.YEAR_DATE_FORMAT.parse("2010"));
		movie.setEpisodeName("A tortas con el amor");
		movie.setEpisodeSeason(null);
		movie.setEpisodeNumber(null);
		movie.setEpisodeDate(PlainTextMovieInterfaceParser.EPISODE_DATE_FORMAT.parse("2010-12-03"));
		testParser(parser, lineToTest, movie);

		// lineToTest = "#1 (2010/I) (VG)						2010";
		// movie.setName("#1");
		// movie.setReleaseDate(PlainTextInterfaceParser.YEAR_DATE_FORMAT.parse("2010"));
		// movie.setBroadcastDateBegin(PlainTextInterfaceParser.YEAR_DATE_FORMAT.parse("2010"));
		// movie.setBroadcastDateEnd(null);
		// movie.setEpisodeName(null);
		// movie.setEpisodeSeason(null);
		// movie.setEpisodeNumber(null);
		// movie.setEpisodeDate(null);
		// testParser(parser, lineToTest, movie);
	}
}
