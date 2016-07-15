package tehthu.dictionary;

import java.io.IOException;
import java.nio.file.Path;

import com.google.common.base.Throwables;

public abstract class FileBasedDictionary extends Dictionary {

	private Path file;
	
	protected FileBasedDictionary(Path p) {
		super(p);
	}
	
	public Path getPath() {
		return file;
	}
	
	@Override
	protected DictInfo parse(Object o) {
		file = (Path) o;
		if(!file.toFile().exists()) {
			Throwables.propagate(new IOException("Can't find dictionary file: " + file.toString()));
		}
		return parseWithFile();
	}
	
	protected abstract DictInfo parseWithFile();
}
