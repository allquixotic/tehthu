package tehthu.gui;

import java.io.File;
import java.nio.file.FileSystems;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import tehthu.parser.Direction;
import tehthu.parser.Parser;

public class MainWindow {

	protected Shell shlTehthuTranslator;
	private Text text;
	private Parser parser = null;
	private StringBuilder sb = new StringBuilder();
	private Combo combo;
	private String fileOnStartup = null;
	private Display disp = null;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
			if (args != null && args.length > 0 && args[0].trim().length() > 0 && new File(args[0]).exists())
				window.fileOnStartup = args[0];
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		disp = display;
		createContents();
		shlTehthuTranslator.open();
		shlTehthuTranslator.layout();
		if (fileOnStartup != null)
			this.loadFile(fileOnStartup);
		while (!shlTehthuTranslator.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void loadFile(String filename) {
		parser = new Parser(FileSystems.getDefault().getPath(filename));
		combo.add(parser.getDictionary().getLeftLangName());
		combo.add(parser.getDictionary().getRightLangName());
		combo.select(0);
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlTehthuTranslator = new Shell();
		shlTehthuTranslator.setSize(450, 300);
		shlTehthuTranslator.setText("Tehthu Translator");
		shlTehthuTranslator.setLayout(new GridLayout(3, false));

		Browser browser = new Browser(shlTehthuTranslator, SWT.NONE);
		browser.setJavascriptEnabled(true);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		new Label(shlTehthuTranslator, SWT.NONE);

		combo = new Combo(shlTehthuTranslator, SWT.READ_ONLY);
		GridData gd_combo = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
		gd_combo.widthHint = 113;
		combo.setLayoutData(gd_combo);

		text = new Text(shlTehthuTranslator, SWT.BORDER);
		text.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent arg0) {
				if (arg0.detail == SWT.TRAVERSE_RETURN && parser != null && combo.getSelectionIndex() >= 0) {
					// User pressed enter and parser exists
					Direction dir = (combo.getSelectionIndex() == 0 ? Direction.LTR : Direction.RTL);
					String line = text.getText();
					String rich = parser.translateSentenceRich(line, dir);
					sb.append(line).append("<br>").append(" => ").append(rich).append("<br>");
					text.setText("");
					browser.setText(sb.toString());
				}
			}
		});
		text.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));

		Menu menu = new Menu(shlTehthuTranslator, SWT.BAR);
		shlTehthuTranslator.setMenuBar(menu);

		MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmNewSubmenu.setText("File");

		Menu menu_1 = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(menu_1);

		MenuItem openMenuItem = new MenuItem(menu_1, SWT.NONE);
		openMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog fd = new FileDialog(shlTehthuTranslator, SWT.OPEN);
				fd.setFilterNames(
						new String[] { "OpenOffice Spreadsheets (*.ods)", "Excel Spreadsheets (*.xlsx, *.xls, *.xlsm)",
								"Text Dictionaries (*.teh, *.txt, *.xml)", "All Files (*.*)" });
				fd.setFilterExtensions(new String[] { "*.ods", "*.xlsx;*.xls;*.xlsm", "*.teh;*.txt;*.xml", "*.*" });
				String filepath = fd.open();
				if (filepath != null && filepath.length() > 0) {
					loadFile(filepath);
				}
			}
		});
		openMenuItem.setText("Open");

		browser.addProgressListener(new ProgressAdapter() {
			@Override
			public void completed(ProgressEvent arg0) {
				browser.execute("window.scrollTo(0,document.body.scrollHeight)");
			}
		});

		MenuItem quitMenuItem = new MenuItem(menu_1, SWT.NONE);
		quitMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.exit(0);
			}
		});
		quitMenuItem.setText("Quit");

	}
}
