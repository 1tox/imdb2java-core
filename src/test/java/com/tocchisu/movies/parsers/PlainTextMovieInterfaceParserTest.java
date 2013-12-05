package com.tocchisu.movies.parsers;

import static junit.framework.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
public class PlainTextMovieInterfaceParserTest {

	@Test
	public void testParseLine() throws Exception {
		String t1 = "\"Alfred J. Kwak\" (1989)					1989-????	(52 episodes)";
		String t2 = "\"Bente - En barrierekvinde\" (2001)			2001	(unreleased)";
		String t3 = "\"Big Brother\" (2000/IV)					2000-????";
		String t4 = "\"1, 2 oder 3\" (1977) {Science-Fiction}			2000";
		String t5 = "\"A tortas con la vida\" (????) {A tortas con el amor (#2.11)}	2010-2012";
		String t6 = "\"A tortas con la vida\" (2005) {A tortas con el amor (2010-12-03)}	????-2010";
		String t7 = "#1 (2010/I) (VG)						2010";
		PlainTextMovieInterfaceParser spy = new PlainTextMovieInterfaceParser(null);
		testLines(spy, t1, t2, t3, t4, t5, t6, t7);

	}

	private void testLines(PlainTextMovieInterfaceParser testedObject, String... lineToTests) throws Exception {
		for (String lineToTest : lineToTests) {
			assertNotNull(Whitebox.invokeMethod(testedObject, "parseLine", lineToTest));
		}
	}
}
