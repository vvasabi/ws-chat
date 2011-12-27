package ca.wasabistudio.chat.text;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestMessageHtmlParser {

	private MessageParser parser;

	@BeforeClass
	public void setUp() {
		parser = new MessageHtmlParser();
	}

	@Test
	public void testUrlLinkify() {
		String input = "blah http://www.google.ca/ blah";
		String output = "blah <a href=\"http://www.google.ca/\">"
			+ "http://www.google.ca/</a> blah";
		assertEquals(parser.process(input), output);
	}

	@Test
	public void testEmailLinkify() {
		String input = "Send to wasabi@wasabistudio.ca";
		String output = "Send to <a href=\"mailto:wasabi@wasabistudio.ca\">"
			+ "wasabi@wasabistudio.ca</a>";
		assertEquals(parser.process(input), output);
	}

	@Test
	public void testWeakEmphasis() {
		String input = "*weak em*";
		String output = "<em>weak em</em>";
		assertEquals(parser.process(input), output);
	}

	@Test
	public void testStrongEmphasis() {
		String input = "**strong em**";
		String output = "<strong>strong em</strong>";
		assertEquals(parser.process(input), output);
	}

	@Test
	public void testMixed1() {
		String input = "**http://www.google.ca**";
		String output = "<strong><a href=\"http://www.google.ca\">"
			+ "http://www.google.ca</a></strong>";
		assertEquals(parser.process(input), output);
	}

	@Test
	public void testMixed2() {
		String input = "*Link: http://www.google.ca blah**";
		String output = "<em>Link: <a href=\"http://www.google.ca\">"
			+ "http://www.google.ca</a> blah</em>*";
		assertEquals(parser.process(input), output);
	}

}
