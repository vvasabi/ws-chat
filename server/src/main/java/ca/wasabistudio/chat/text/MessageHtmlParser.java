package ca.wasabistudio.chat.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Convert the provided raw message text into HTML formatted text.
 *
 * @author wasabi
 */
public class MessageHtmlParser implements MessageParser {

	/**
	 * Link and email regex taken from MarkdownJ. Link can't end with an
	 * asterisk.
	 */
	private static final Pattern URL_PATTERN
		= Pattern.compile("((https?):[^'\">\\s]+[^'\">\\s\\*])");

	private static final String URL_REPLACEMENT = "<a href=\"$1\">$1</a>";

	private static final Pattern EMAIL_PATTERN
		= Pattern.compile("([-.\\w]+\\@[-a-z0-9]+(\\.[-a-z0-9]+)*\\.[a-z]+)",
			Pattern.CASE_INSENSITIVE);

	private static final String EMAIL_REPLACEMENT
		= "<a href=\"mailto:$1\">$1</a>";

	private static final Pattern WEAK_EM_PATTERN
		= Pattern.compile("\\*([^\\*]+)\\*", Pattern.CASE_INSENSITIVE);

	private static final String WEAK_EM_REPLACEMENT = "<em>$1</em>";

	private static final Pattern STRONG_EM_PATTERN
		= Pattern.compile("\\*\\*([^\\*]+)\\*\\*", Pattern.CASE_INSENSITIVE);

	private static final String STRONG_EM_REPLACEMENT = "<strong>$1</strong>";

	@Override
	public String process(String raw) {
		String result = escapeHtml(raw);
		result = urlLinkify(result);
		result = emailLinkify(result);
		result = replaceStrongEm(result);
		return replaceWeakEm(result);
	}

	private String escapeHtml(String raw) {
		return raw.replace("&", "&amp;").replace("<", "&lt;")
				.replace(">", "&gt;");
	}

	private String urlLinkify(String raw) {
		return replaceAll(URL_PATTERN, URL_REPLACEMENT, raw);
	}

	private String emailLinkify(String raw) {
		return replaceAll(EMAIL_PATTERN, EMAIL_REPLACEMENT, raw);
	}

	private String replaceStrongEm(String raw) {
		return replaceAll(STRONG_EM_PATTERN, STRONG_EM_REPLACEMENT, raw);
	}

	private String replaceWeakEm(String raw) {
		return replaceAll(WEAK_EM_PATTERN, WEAK_EM_REPLACEMENT, raw);
	}

	private String replaceAll(Pattern pattern, String replacement, String raw) {
		Matcher matcher = pattern.matcher(raw);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, replacement);
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

}
