package com.tocchisu.movies.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PlainTextInterfaceParser<T extends Serializable> {
	private final static Logger			LOGGER				= LoggerFactory.getLogger(PlainTextInterfaceParser.class);
	protected static final DateFormat	YEAR_DATE_FORMAT	= new SimpleDateFormat("yyyy");
	private File						file;
	private long						currentLineNumber;
	private Pattern						pattern;

	protected PlainTextInterfaceParser(File file) {
		if (file != null && file.isDirectory()) {
			throw new IllegalArgumentException(file.getAbsolutePath() + " must be a directory");
		}
		this.file = file;
	}

	protected final void read() {
		if (file == null) {
			throw new IllegalArgumentException("File must not be empty");
		}
		String line = null;
		BufferedReader bufferedReader = null;
		Long startTime = new Date().getTime();
		try {
			beforeFileParsed();
			bufferedReader = new BufferedReader(new FileReader(getFile()));
			int nbParsedObjects = 0;
			while ((line = bufferedReader.readLine()) != null) {
				currentLineNumber++;
				LOGGER.info("Parsing L." + currentLineNumber + " : " + line);
				if (parseLine(line) != null) {
					nbParsedObjects++;
				}
				else {
					LOGGER.warn("Unparsable line " + currentLineNumber + " : -->" + line + "<--");
				}

			}
			LOGGER.info(nbParsedObjects + " objects parsed");
		}
		catch (FileNotFoundException e) {
			LOGGER.error("File not found", e);
		}
		catch (Exception e) {
			String message = "Error while processing file " + getFile().getAbsolutePath();
			if (line != null) {
				message += System.getProperty("line.separator") + "line " + currentLineNumber + " : " + line;
			}
			LOGGER.error(message, e);
		}
		finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				}
				catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			afterFileParsed();
			LOGGER.info("Treatment duration : " + (new Date().getTime() - startTime) + " ms");
		}
	}

	private T parseLine(String line) {
		Matcher matcher = getPattern().matcher(line);
		if (matcher.find()) {
			beforeLineParsed();
			T t = parseLine(matcher, line, currentLineNumber);
			afterLineParsed(t);
			return t;
		}
		else {
			return null;
		}
	}

	protected abstract T parseLine(Matcher matcher, String line, Long lineNumber);

	public final File getFile() {
		return file;
	}

	public final void setFile(File file) {
		this.file = file;
	}

	protected void beforeFileParsed() {}

	protected void beforeLineParsed() {}

	protected void afterFileParsed() {}

	protected void afterLineParsed(@SuppressWarnings("unused") T t) {}

	public void parse() {
		read();
	}

	protected abstract String getLinePattern();

	protected Date getDate(String date, DateFormat dateFormat) throws ParseException {
		return date == null ? null : dateFormat.parse(date);
	}

	protected final Pattern getPattern() {
		if (pattern == null) {
			pattern = Pattern.compile(getLinePattern());
		}
		return pattern;
	}

}
