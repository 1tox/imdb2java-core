package com.tocchisu.movies.services.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tocchisu.movies.parsers.PlainTextMovieGenreInterfaceParser;

public class PlainTextMovieGenreInterfaceParserTest {
	public static void main(String[] args) {
		printGroups(getRexexp().matcher("\"Artworks Scotland\" (2004)				Documentary"));
		printGroups(getRexexp().matcher("\"#1 Single\" (2006)					Reality-TV"));

	}

	private static void printGroups(Matcher matcher) {
		System.out.println("************");
		if (matcher.matches()) {
			int groupCount = matcher.groupCount();
			for (int i = 1; i <= groupCount; i++) {
				System.out.println(i + ":" + matcher.group(i));
			}
		}
	}

	private static Pattern getRexexp() {
		return Pattern.compile(new PlainTextMovieGenreInterfaceParser(null).getLinePattern());
	}
}
