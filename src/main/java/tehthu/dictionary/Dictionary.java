package tehthu.dictionary;

import java.util.Set;

import tehthu.parser.Direction;

public abstract class Dictionary {
	protected final ManyToMany<String,String> dict;
	protected final String leftName;
	protected final String rightName;
	
	protected Dictionary(Object param) {
		DictInfo di = parse(param);
		dict = di.getTable();
		leftName = di.getLeft();
		rightName = di.getRight();
	}
	
	protected abstract DictInfo parse(Object arg);
	
	public final String getLeftLangName() {
		return leftName;
	}
	
	public final String getRightLangName() {
		return rightName;
	}
	
	public final void addMapping(String left, String right) {
		dict.put(left.toLowerCase(), right.toLowerCase());
	}
	
	public final Set<String> translate(String input, Direction d) {
		switch(d) {
		case LTR:
			return dict.getValues(input.toLowerCase());
		case RTL:
			return dict.getKeys(input.toLowerCase());
		default:
			throw new UnsupportedOperationException("Invalid direction enum value: " + d.name());
		}
	}
	
	public final String translateString(String input, Direction d) {
		Set<String> s = translate(input, d);
		if(s == null || s.size() == 0) {
			return null;
		}
		
		boolean allUpper = input.toUpperCase().equals(input);
		boolean allLower = input.toLowerCase().equals(input);
		boolean mixed = ((!allUpper) && (!allLower)) || (allUpper && input.length() == 1);
		StringBuilder retval = new StringBuilder();
		boolean first = true;
		for(String ss : s) {
			if(!first)
				retval.append("/");
			retval.append(ss);
			first = false;
		}
		String realval = retval.toString();
		if(mixed)
			return String.valueOf(realval.charAt(0)).toUpperCase() + realval.substring(1);
		if(allUpper)
			return realval.toUpperCase();
		if(allLower)
			return realval.toLowerCase();
		return retval.toString();
	}
	
	public final String[][] toArray() {
		String[] ks = dict.getAllKeys();
		String[] vs = dict.getAllValues();
		String[][] retval = new String[ks.length][2];
		for(int row = 0; row < ks.length; row++) {
			retval[row][0] = ks[row];
			retval[row][1] = vs[row];
		}
		return retval;
	}
}
