package tehthu.dictionary;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Throwables;
import com.google.common.io.Closeables;
import com.google.common.io.LineReader;

public class TextDictionary extends FileBasedDictionary {

	private Pattern nameRegex = Pattern.compile("^\\[(.+)\\]\\|\\[(.+)\\]$");
	private Pattern lineRegex = Pattern.compile("^(.+)\\|(.+)$");

	public TextDictionary(Path p) {
		super(p);
	}

	@Override
	protected DictInfo parseWithFile() {
		ManyToMany<String, String> many = new ManyToMany<String, String>();
		String leftName = null, rightName = null;
		FileReader fr = null;
		try {
			fr = new FileReader(getPath().toString());
			LineReader lr = new LineReader(fr);
			String line = null;
			boolean first = true;
			do {
				line = lr.readLine().trim();
				Matcher m;
				if (first && (m = nameRegex.matcher(line)).matches()) {
					leftName = m.group(1);
					rightName = m.group(2);
				} else {
					m = lineRegex.matcher(line);
					if (!m.matches()) {
						throw new RuntimeException("Badly formatted dictionary line: \"" + line + "\"");
					}
					dict.put(m.group(1).toLowerCase(), m.group(2).toLowerCase());
				}
			} while (line != null);

		} catch (IOException e) {
			Throwables.propagate(e);
		} finally {
			Closeables.closeQuietly(fr);
		}

		return new DictInfo(many, leftName, rightName);
	}

}
