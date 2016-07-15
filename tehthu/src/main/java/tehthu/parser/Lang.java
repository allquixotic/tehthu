package tehthu.parser;

public enum Lang {
	L, // Left hand language
	R, // Right hand language
	PO, // Non-word rope element with a space prepended ONLY: " ["
	AO, // Non-word rope element with a space appended ONLY: "] "
	S // Non-word rope element with spaces on both sides: " ? "
}
