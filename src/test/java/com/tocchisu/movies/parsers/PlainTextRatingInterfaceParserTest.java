package com.tocchisu.movies.parsers;

import org.junit.Test;
import com.tocchisu.movies.objects.Rating;

public class PlainTextRatingInterfaceParserTest extends AbstractParserTest<Rating> {
	@Test
	public void testParseLine() throws Exception {
		Rating rating = new Rating();
		rating.setMovie("Julia Roberts: An American Cinematheque Tribute (2007) (TV)");
		rating.setNumberOfVotes(19);
		rating.setRank(6.7f);
		rating.setDistribution(new int[] { 1, 0, 0, 0, 0, 0, 2, 0, 0, });
		testParser(new PlainTextRatingInterfaceParser(), "      10.0002.03      19   6.7  Julia Roberts: An American Cinematheque Tribute (2007) (TV)", rating);
	}
}