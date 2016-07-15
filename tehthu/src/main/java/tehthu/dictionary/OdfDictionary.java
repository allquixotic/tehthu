package tehthu.dictionary;

import java.nio.file.Path;

import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

import com.google.common.base.Throwables;

public class OdfDictionary extends FileBasedDictionary {

	public OdfDictionary(Path p) {
		super(p);
	}

	@Override
	protected DictInfo parseWithFile() {
		DictInfo retval;
		ManyToMany<String, String> mm = new ManyToMany<>();
		String leftName = null, rightName = null;

		try {
			SpreadsheetDocument ss = SpreadsheetDocument.loadDocument(getPath().toFile());
			Table t = null;
			try {
				t = ss.getSheetByName("Dictionary");
			} catch (Exception e) {
				t = ss.getSheetByIndex(0);
			}
			if (t == null) {
				t = ss.getSheetByIndex(0);
			}

			boolean first = true;
			for (Row row : t.getRowList()) {
				String lv = row.getCellByIndex(0).getStringValue().trim();
				String rv = row.getCellByIndex(1).getStringValue().trim();
				if (first) {
					leftName = rmBrackets(lv);
					rightName = rmBrackets(rv);
				} else {
					if (lv == null || lv.length() == 0 || rv == null || rv.length() == 0)
						break;
					mm.put(lv.toLowerCase(), rv.toLowerCase());
				}

				first = false;
			}
			retval = new DictInfo(mm, leftName, rightName);
			return retval;
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return null;
	}

	private String rmBrackets(String s) {
		if (s.startsWith("[") && s.endsWith("]"))
			return s.substring(1, s.length() - 1);
		return s;
	}

}
