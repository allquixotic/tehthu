package tehthu.parser;

import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.LinkedListMultimap;

public class Rope {
	private final LinkedListMultimap<String, Lang> strings = LinkedListMultimap.create();
	private final Lang sourceLanguage;

	public Rope(Lang sourceLang) {
		sourceLanguage = sourceLang;
	}

	public void add(String s, Lang l) {
		strings.put(s, l);
	}

	public List<Entry<String, Lang>> getElements() {
		return strings.entries();
	}

	@Override
	public String toString() {
		return getText(false);
	}

	public String getText(boolean rich) {
		StringBuilder sb = new StringBuilder();
		Lang prevLang = null;
		for (java.util.Map.Entry<String, Lang> entry : strings.entries()) {
			if (prevLang != null)
				switch (prevLang) {
				case AO:
				case L:
				case R:
				case S:
					if (entry.getValue() != Lang.AO)
						sb.append(" ");
					break;
				default:
					break;
				}

			switch (entry.getValue()) {
			case L:
				if (rich && sourceLanguage == Lang.L) {
					encloseInBold(sb, entry.getKey());
				} else {
					sb.append(entry.getKey());
				}
				break;
			case R:
				if (rich && sourceLanguage == Lang.R) {
					encloseInBold(sb, entry.getKey());
				} else {
					sb.append(entry.getKey());
				}
				break;
			case S:
			case AO:
			case PO:
				sb.append(entry.getKey());
				break;
			default:
				throw new UnsupportedOperationException(
						"Invalid or unhandled enum constant " + entry.getValue().name());
			}

			prevLang = entry.getValue();
		}
		return sb.toString().trim().replaceAll("\\s+", " ");
	}

	private static StringBuilder encloseInBold(StringBuilder sb, String orig) {
		return sb.append("<b>").append(orig).append("</b>");
	}
}
