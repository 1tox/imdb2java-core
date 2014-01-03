package com.tocchisu.movies.objects;

import java.io.Serializable;
import java.util.Date;

public class Movie implements Serializable {
	private static final long	serialVersionUID	= 487003885408949095L;
	private String				name;
	private Date				releaseDate;
	private String				episodeName;
	private Integer				episodeSeason;
	private Integer				episodeNumber;
	private Date				episodeDate;
	private Date				broadcastDateBegin;
	private Date				broadcastDateEnd;
	private boolean				xRated;
	private String				commentary;
	private Genre				genre;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Date getBroadcastDateBegin() {
		return broadcastDateBegin;
	}

	public void setBroadcastDateBegin(Date broadcastDate) {
		broadcastDateBegin = broadcastDate;
	}

	public boolean isxRated() {
		return xRated;
	}

	public void setxRated(boolean xRated) {
		this.xRated = xRated;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	public String getEpisodeName() {
		return episodeName;
	}

	public void setEpisodeName(String episodeName) {
		this.episodeName = episodeName;
	}

	public Integer getEpisodeSeason() {
		return episodeSeason;
	}

	public void setEpisodeSeason(Integer episodeSequence) {
		episodeSeason = episodeSequence;
	}

	public Integer getEpisodeNumber() {
		return episodeNumber;
	}

	public void setEpisodeNumber(Integer episodeNumber) {
		this.episodeNumber = episodeNumber;
	}

	public Date getEpisodeDate() {
		return episodeDate;
	}

	public void setEpisodeDate(Date episodeDate) {
		this.episodeDate = episodeDate;
	}

	public Date getBroadcastDateEnd() {
		return broadcastDateEnd;
	}

	public void setBroadcastDateEnd(Date broadcastDateEnd) {
		this.broadcastDateEnd = broadcastDateEnd;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

}
