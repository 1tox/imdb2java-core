package com.tocchisu.movies.parsers;

import org.junit.Test;
import com.tocchisu.movies.objects.Rating;

public class PlainTextRatingInterfaceParserTest extends AbstractParserTest<Rating> {
	@Test
	public void testParseLine() throws Exception {
		testParser(new PlainTextRatingInterfaceParser(), "      0.00022000      43   6.2  Up Up and Away (1989)",
				"      10.0002.03      19   6.7  Julia Roberts: An American Cinematheque Tribute (2007) (TV)");
	}
}