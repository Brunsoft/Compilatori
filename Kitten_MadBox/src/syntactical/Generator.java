package syntactical;

import java.io.FileInputStream;
import java.io.IOException;

public class Generator {

	public static void main(String[] args) throws IOException, Exception {
		try (FileInputStream fis = new FileInputStream("resources/Kitten.cup")) {
			System.setIn(fis);
			java_cup.Main.main(new String[] { "-parser", "Parser" });
		}
	}
}