package com.tocchisu.movies.parsers;

import static junit.framework.Assert.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;
import org.powermock.reflect.Whitebox;

public class AbstractParserTest<T extends Serializable> {

	/**
	 * Tests whether :
	 * <ul>
	 * <li> <code>parser</code> successfully parses <code>lineToTest</code></li>
	 * <li>the internal state of the returned object is similar to the internal state of <code>expectedObject</code></li>
	 * </ul>
	 */
	protected void testParser(PlainTextInterfaceParser<T> parser, String lineToTest, T expectedObject) throws Exception {
		T returnedObject = Whitebox.<T> invokeMethod(parser, "parseLine", lineToTest);
		assertNotNull(returnedObject);
		compareInternalStates(expectedObject, returnedObject);
	}

	private void compareInternalStates(Object expectedObject, Object returnedObject) throws SecurityException, NoSuchMethodException {
		Set<Field> fields = Whitebox.getAllInstanceFields(returnedObject);
		for (Field field : fields) {
			Object expectedFieldValue = Whitebox.getInternalState(expectedObject, field.getName());
			Object returnedFieldValue = Whitebox.getInternalState(returnedObject, field.getName());
			if (expectedFieldValue != null
					&& expectedFieldValue.getClass().getMethod("equals", Object.class).getDeclaringClass() != expectedFieldValue.getClass()) {
				compareInternalStates(expectedFieldValue, returnedFieldValue);
			}
			else {
				assertEquals("Field " + expectedObject.getClass().getSimpleName() + "." + field.getName(), expectedFieldValue, returnedFieldValue);
			}
		}
	}
}