package com.tocchisu.movies.services.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tocchisu.movies.parsers.PlainTextMovieInterfaceParser;

public class PlainTextMovieInterfaceParserTest {

	public static void main(String[] args) {
		String t1 = "\"Alfred J. Kwak\" (1989)					1989-????	(52 episodes)";
		String t2 = "\"Bente - En barrierekvinde\" (2001)			2001	(unreleased)";
		String t3 = "\"Big Brother\" (2000/IV)					2000-????";
		String t4 = "\"1, 2 oder 3\" (1977) {Science-Fiction}			2000";
		String t5 = "\"A tortas con la vida\" (????) {A tortas con el amor (#2.11)}	2010-2012";
		String t6 = "\"A tortas con la vida\" (2005) {A tortas con el amor (2010-12-03)}	????-2010";
		String t7 = "#1 (2010/I) (VG)						2010";
		print(getRexexp().matcher(t1));
		print(getRexexp().matcher(t2));
		print(getRexexp().matcher(t3));
		print(getRexexp().matcher(t4));
		print(getRexexp().matcher(t5));
		print(getRexexp().matcher(t6));
		print(getRexexp().matcher(t7));

		// matcher = getRexexp().matcher(t6);
		// print(matcher);
		// print(Pattern.compile("(?:(\\d{4})||\\?{4})(?:-(?:(\\d{4})||\\?{4}))?").matcher("????"));
		// print(Pattern.compile("(?:(\\d{4})||\\?{4})(?:-(?:(\\d{4})||\\?{4}))?").matcher("2001-????"));
		// print(Pattern.compile("(?:(\\d{4})||\\?{4})(?:-(?:(\\d{4})||\\?{4}))?").matcher("????-????"));
		// print(Pattern.compile("(?:(\\d{4})||\\?{4})(?:-(?:(\\d{4})||\\?{4}))?").matcher("????-2010"));

	}

	private static void print(Matcher matcher) {
		System.out.println("**** " + matcher.lookingAt() + " ********");
		if (matcher.matches()) {
			int groupCount = matcher.groupCount();
			for (int i = 1; i <= groupCount; i++) {
				System.out.println(i + ":" + matcher.group(i));
			}
		}
	}

	String s = "(?:\\{(.+)(?:\\((.+)\\))?\\})?";

	private static Pattern getRexexp() {
		return Pattern.compile(new PlainTextMovieInterfaceParser(null).getLinePattern());
	}
}
