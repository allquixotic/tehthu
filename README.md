# tehthu

[![Build Status](https://travis-ci.org/allquixotic/tehthu.svg?branch=master)](https://travis-ci.org/allquixotic/tehthu)

## Build

(only if you can't run one of the files under Releases or need the latest development version)

 1. Make sure you have a Java Development Kit (JDK), version 1.8.0 (a.k.a. "Java 8") or later. For *running* an existing binary, you only need the Java Runtime (JRE), not the JDK, but it still needs to be at least version 8.
 2. Download Maven and add it to your PATH (For example, on Windows, `set "PATH=%PATH%;C:\maven\bin"` assuming you unzipped the maven ZIP file to `C:\maven` -- on Linux/Mac use `export PATH` instead of `set` and the syntax is slightly different)
 3. Clone or download the zip
 4. cd into the base directory of the git repo on your system
 5. Edit pom.xml in a text editor and uncomment *exactly one* dependency for the appropriate SWT platform you need...
  - Know what Java you have (32-bit or 64-bit, a.k.a. x86_64/win64 or x86/i386)
  - Know what platform you're running (MacOS, Windows, or Linux)
 6. Run `mvn package`
 7. Run `java -jar target/tehthu-VERSION-jar-with-dependencies.jar` replacing `VERSION` with the version you have

If on a Mac, you need to run `java -XstartOnFirstThread ... (the rest of the args as given above)`.

## Use

 1. File -> Open and select a dictionary file in one of the supported formats
 2. Make sure the language you want to translate **from** is selected in the combo box
 3. Type some text and press enter
 4. Repeat
 5. 
 
## To-Do

- [ ] Implement Excel file dictionaries.
- [x] Implement OpenOffice Spreadsheet dictionaries.
- [x] Implement text-based dictionaries (pipe-delimited).
- [ ] Extensively test various sentence structures.
- [ ] Add description of file format for each type of input file.
- [ ] Tidy up the UI.
- [ ] Java Web Start.
- [x] Binary releases.
- [x] *Automated* binary releases. (CI?)
