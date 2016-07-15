package tehthu.tui;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Scanner;

import tehthu.parser.Direction;
import tehthu.parser.Parser;

public class Driver {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		if (args == null || args.length != 1) {
			System.err.println("Require one argument - file name of dictionary");
			System.exit(1);
		}
		Path dictfile = FileSystems.getDefault().getPath(args[0]);
		if (!dictfile.toFile().exists()) {
			System.err.println("ERROR: Dictionary file does not exist: " + args[0]);
			System.exit(1);
		}
		Parser p = new Parser(dictfile);
		Scanner s = new Scanner(System.in);
		String leftLang = p.getDictionary().getLeftLangName(), rightLang = p.getDictionary().getRightLangName();
		Direction dir = null;
		while (true) {

			System.out.println("Please enter the language you want to use: (Default: "
					+ (dir == Direction.LTR ? leftLang : (dir == Direction.RTL ? rightLang : leftLang)) + ")");
			String lang = s.nextLine();
			if (lang == null || lang.trim().length() == 0) {
				if (dir == null) {
					dir = Direction.LTR;
				}
			} else if (lang.equalsIgnoreCase(leftLang)) {
				dir = Direction.LTR;
			} else if (lang.equalsIgnoreCase(rightLang)) {
				dir = Direction.RTL;
			} else {
				System.err.println("Unknown language: " + lang);
				continue;
			}

			System.out.println("Enter a sentence in " + (dir == Direction.LTR ? leftLang : rightLang)
					+ " to be translated into " + (dir == Direction.LTR ? rightLang : leftLang));

			String sentence = s.nextLine();
			System.out.println("=> " + p.translateSentencePlain(sentence, dir));
		}
	}

}
