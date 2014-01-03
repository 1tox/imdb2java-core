package com.tocchisu.movies.objects;

import java.io.Serializable;

public class Genre implements Serializable {
	private static final long	serialVersionUID	= -8971509566400391461L;
	private String				name;

	public Genre(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
