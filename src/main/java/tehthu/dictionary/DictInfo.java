package tehthu.dictionary;

public class DictInfo {
	private final ManyToMany<String,String> d;
	private final String leftName;
	private final String rightName;
	
	public DictInfo(ManyToMany<String,String> t, String l, String r) {
		d = t;
		leftName = l;
		rightName = r;
		if(d == null || l == null || r == null || leftName.length() == 0 || rightName.length() == 0) {
			throw new UnsupportedOperationException("Language names must not be empty and map must not be null!");
		}
	}
	
	public final ManyToMany<String,String> getTable() {
		return d;
	}
	
	public final String getLeft() {
		return leftName;
	}
	
	public final String getRight() {
		return rightName;
	}
}