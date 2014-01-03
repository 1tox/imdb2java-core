package com.tocchisu.movies.objects;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Rating implements Serializable {
	private String	movie;
	private int		numberOfVotes;
	private float	rank;
	private int[]	distribution	= new int[10];

	public String getMovie() {
		return movie;
	}

	public void setMovie(String movie) {
		this.movie = movie;
	}

	public int getNumberOfVotes() {
		return numberOfVotes;
	}

	public void setNumberOfVotes(int numberOfVotes) {
		this.numberOfVotes = numberOfVotes;
	}

	public float getRank() {
		return rank;
	}

	public void setRank(float rank) {
		this.rank = rank;
	}

	public int[] getDistribution() {
		return distribution;
	}

	public void setDistribution(int[] distribution) {
		this.distribution = distribution;
	}

}

enum VOTE {
	ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10);
	private int	value;

	private VOTE(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
