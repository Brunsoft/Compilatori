package runTime;

import java.util.Scanner;

/**
 * The class the implements Kitten strings. When we refer to Java's {@code java.lang.String},
 * we must use its fully qualified name.
 */

public class String {
	private java.lang.String value;

	/* creates the empty string */
	public String() {
		value = new java.lang.String();
	}

	/* clones a string */
	public String(String other) {
		value = other.value;
	}

	/* clones a Java string */
	public String(java.lang.String value) {
		this.value = value;
	}

	/* yields the length of a string */
	public int length() {
		return value.length();
	}

	/* converts a string into integer */
	public int toInt() {
		try {
			return Integer.parseInt(value);
		}
		catch (NumberFormatException e) {
			System.out.println("illegal integer format");
			return 0;
		}
	}

	/* converts a string into float */
	public float toFloat() {
		try {
			return Float.parseFloat(value);
		}
		catch (NumberFormatException e) {
			System.out.println("illegal float format");
			return 0;
		}
	}

	/* checks that two strings are equal */
	public boolean equals(String other) {
		return value.equals(other.value);
	}

	/* prints the string to the screen */
	public void output() {
		System.out.print(value);
	}

	private final Scanner keyboard = new Scanner(System.in);

	/* reads from the keyboard a sequence of character until the first newline and
	   stores it into the string */
	public void input() {
		value = keyboard.nextLine();
	}

	/* yields the concatenation of "this" and then "s" */
	public String concat(String s) {
		return new String(value + s.value);
	}

	/* yields the concatenation of "this" and then "f" */
	public String concat(float f) {
		return new String(value + f);
	}

	/* yields the concatenation of "this" and then "i" */
	public String concat(int i) {
		return new String(value + i);
	}

	/* yields the concatenation of "this" and then "b" */
	public String concat(boolean b) {
		return new String(value + b);
	}

	public String substring(int start, int end) {
		return new String(value.substring(start, end));
	}
	
	public java.lang.String toString(){
		return value;
	}
}