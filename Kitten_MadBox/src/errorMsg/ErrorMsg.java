package errorMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * An error reporting utility. It allows one to print error messages
 * referring to a given position inside a source Kitten program.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ErrorMsg {

	/**
	 * The sequence of newline positions in the source {@link #fileName}.
	 * This is useful to know where source lines stop.
	 */

	private final List<Integer> linePos = new ArrayList<>();

	/**
	 * The name of the file to which this error reporting utility is associated.
	 */

	private final String fileName;

	/**
	 * Has any error occurred up to now?
	 */

	private boolean anyErrors;

	/**
	 * Creates an error reporting utility for the specified source file.
	 *
	 * @param fileName the name of the source file
	 */

	public ErrorMsg(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Yields the name of the source file.
	 *
	 * @return the name of the source file
	 */

	public String getFileName() {
		return fileName;
	}

	/**
	 * Determines if any error has been reported up to now.
	 *
	 * @return true if some error has been reported, false otherwise
	 */

	public boolean anyErrors() {
		return anyErrors;
	}

	/**
	 * Records that a new line character has been found at the given position.
	 *
	 * @param pos the position of the new line character in the source file
	 *            (number of characters from the beginning of the file)
	 */

	public void newline(int pos) {
		linePos.add(pos);
	}

	/**
	 * Reports an error message occurring at the given position
	 * in the source file.
	 *
	 * @param pos the position where the error must be reported
	 *            (number of characters from the beginning of the file).
	 *            If this is negative, the message is printed without
	 *            any line number reference
	 * @param msg the message to be reported
	 */

	public void error(int pos, String msg) {
		anyErrors = true; // an error has been reported at least

		String where;
		if (pos >= 0) {
			int last = 0, n = 1;

			// we look for the last new line before position pos
			for (int line: linePos) {
				if (line >= pos) break;

				last = line;
				n++;
			}

			where = n + "." + (pos - last);
		}
		else
			where = "";

		System.out.println(fileName + "::" + where + ": " + msg);
	}
	
	public String calcPos(int pos) {
		String where;
		if (pos >= 0) {
			int last = 0, n = 1;

			// we look for the last new line before position pos
			for (int line: linePos) {
				if (line >= pos) break;

				last = line;
				n++;
			}

			where = n + "." + (pos - last);
		}
		else
			where = "";
		
		return where;
	}
}