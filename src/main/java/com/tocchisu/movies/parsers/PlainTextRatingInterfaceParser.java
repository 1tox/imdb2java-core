package com.tocchisu.movies.parsers;

import static java.lang.Character.*;
import java.util.regex.Matcher;
import com.tocchisu.movies.objects.Rating;

public class PlainTextRatingInterfaceParser extends PlainTextInterfaceParser<Rating> {

	@Override
	final protected Rating parseLine(Matcher matcher, String line, Long lineNumber) {
		String distribution = matcher.group(1);
		String numberOfVotes = matcher.group(2);
		String rank = matcher.group(3);
		String movie = matcher.group(4);
		int[] distributionAsInteger = new int[10];
		for (int i = 0; i < distribution.length(); i++) {
			if (Character.isDigit(distribution.charAt(i))) {
				distributionAsInteger[i] = digit(distribution.charAt(i), MAX_RADIX);
			}
		}
		Rating rating = new Rating();
		rating.setDistribution(distributionAsInteger);
		rating.setNumberOfVotes(Integer.parseInt(numberOfVotes));
		rating.setRank(Float.parseFloat(rank));
		rating.setMovie(movie);
		return rating;
	}

	@Override
	protected String getLinePattern() {
		final String INDENT = "\\s{6}";
		final String DISTRIBUTION = "([\\d\\.]{10})";
		final String NB_VOTES = "\\s{0,8}(\\d{0,8})";
		final String RANK = "\\s{3}(\\d\\.\\d)";
		final String MOVIE = "\\s{2}(.+)";
		return "^" + INDENT + DISTRIBUTION + NB_VOTES + RANK + MOVIE + "$";
	}
}
