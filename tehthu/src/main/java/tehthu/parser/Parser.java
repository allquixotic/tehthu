package tehthu.parser;

import java.nio.file.Path;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tehthu.dictionary.Dictionary;
import tehthu.dictionary.FileTypeSensingDictionary;

public class Parser {
	private final Dictionary dict;
	private static final Pattern wordPat = Pattern.compile("[A-Za-z0-9]");
	private static final Pattern nonWordPat = Pattern.compile("[^A-Za-z0-9]");
	// soeSpecialPat: starts or ends with a non-alphanumeric character
	private static final Pattern soeSpecialPat = Pattern.compile("^(([^A-Za-z0-9]+)(.*?)$)|((.*?)([^A-Za-z0-9]+)$)");

	public Parser(Path p) {
		dict = new FileTypeSensingDictionary(p);
	}

	public Dictionary getDictionary() {
		return dict;
	}

	public String translateSentenceRich(String sent, Direction d) {
		Rope r = sanitizeAndRope(sent, d);
		Rope translated = translate(r, d);
		return translated.getText(true);
	}

	public String translateSentencePlain(String sent, Direction d) {
		Rope r = sanitizeAndRope(sent, d);
		Rope translated = translate(r, d);
		translated.debugRope();
		return translated.getText(false);
	}

	private Rope translate(Rope r, Direction d) {
		Rope retval = new Rope((d == Direction.LTR ? Lang.L : Lang.R));
		List<Entry<String, Lang>> elements = r.getElements();
		for (Entry<String, Lang> elt : elements) {
			String s = elt.getKey();
			Lang l = elt.getValue();
			if (l == Lang.L || l == Lang.R) {
				String tword = dict.translateString(s, d);
				if (tword == null) {
					// No match - don't know how to translate word, so add it
					// back in untranslated
					retval.add(s, l);
				} else {
					retval.add(tword, (l == Lang.L ? Lang.R : Lang.L));
				}
			} else {
				retval.add(s, l);
			}
		}
		return retval;
	}

	private Rope sanitizeAndRope(String input, Direction d) {
		Lang sourceLang = (d == Direction.LTR ? Lang.L : Lang.R);
		Rope retval = new Rope(sourceLang);
		String[] tokens = input.trim().replaceAll("\\s+", " ").split(" ");
		for (String s : tokens) {
			Matcher m = soeSpecialPat.matcher(s);
			if (m.matches()) { // If there are leading or trailing special
								// characters
				String specialPre = m.group(2), specialSuff = m.group(6),
						regular = (m.group(3) != null ? m.group(3) : "") + (m.group(5) != null ? m.group(5) : "");
				if (specialPre != null && specialPre.length() > 0) {
					retval.add(specialPre, Lang.PO);
				}
				retval.add(regular, sourceLang);
				if (specialSuff != null && specialSuff.length() > 0) {
					retval.add(specialSuff, Lang.AO);
				}
			} else {
				if (nonWordPat.matcher(s).find()) {
					// ONLY special characters
					retval.add(s, Lang.S);
				} else {
					// ONLY alphanumeric characters
					retval.add(s, sourceLang);
				}
			}
		}
		return retval;

	}
}
