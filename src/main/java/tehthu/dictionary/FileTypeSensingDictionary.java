package tehthu.dictionary;

import java.nio.file.Path;

import com.google.common.io.Files;

public class FileTypeSensingDictionary extends FileBasedDictionary {

	private FileBasedDictionary encap = null;
	
	public FileTypeSensingDictionary(Path p) {
		super(p);
	}

	@Override
	protected DictInfo parseWithFile() {
		String ext = Files.getFileExtension(getPath().toString());
		if(ext.equalsIgnoreCase("ods")) {
			encap = new OdfDictionary(getPath());
		}
		else if(ext.equalsIgnoreCase("txt") || ext.equalsIgnoreCase("xml") || ext.equalsIgnoreCase("teh") || ext.equalsIgnoreCase("dic")) {
			encap = new TextDictionary(getPath());
		}
		else if(ext.equalsIgnoreCase("xls") || ext.equalsIgnoreCase("xlsx") || ext.equalsIgnoreCase("xlsm")) {
			encap = new ExcelDictionary(getPath());
		}
		else {
			throw new RuntimeException("Unknown file type: " + ext);
		}
		return encap.parse(this.getPath());
	}
}
