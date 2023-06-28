package logger.logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

// DO NOT USE
public final class MyConsole {
	private static JFrame frame;
	private static JTextArea textArea;

	private MyConsole() throws IOException, FontFormatException {
		// Initialize your custom console
		frame = new JFrame();
		frame.setTitle("My Console");
		frame.setSize(1920, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);

		// Create the JTextArea to display the console output
		textArea = new JTextArea(20, 40);
		File font_file = new File("D:\\Private\\Github\\golemmod\\src\\test\\java\\Logger\\resources\\CascadiaMono.ttf");
		Font font = Font.createFont(Font.TRUETYPE_FONT, font_file);
		textArea.setFont(font.deriveFont(15F));

//	off-white	rgb(204, 204, 204)
		Color customWhite = new Color(204, 204, 204);
		textArea.setForeground(customWhite);
//	gray	rgb(44, 42, 40)
		Color customGray = new Color(44, 42, 40);
		textArea.setBackground(customGray);

		JScrollPane scrollPane = new JScrollPane(textArea);
		frame.add(scrollPane);

		Cursor cursor = new Cursor(Cursor.TEXT_CURSOR);
		frame.setCursor(cursor);

		textArea.setCaretColor(customWhite);

	}

	public void showConsole() {
		frame.setVisible(true);
	}

	public synchronized void printText(String text) {
		textArea.append(text);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

	public static void main(String[] args) throws IOException, FontFormatException {
		MyConsole console = new MyConsole();
		console.showConsole();

		// Your code here
		System.out.println("Hello, world!");
		console.printText("Hello world!");
		System.out.println("This is a custom console.");
		console.printText("\n");
		console.printText(String.valueOf(frame.getBackground()));
	}
}

