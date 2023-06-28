package Logger;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public final class MyConsole {
	private static JFrame frame;
	private static JTextArea textArea;
	private PrintStream consolePrintStream;

	private final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

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
//	off-white	rgb(204, 204, 204)
//	gray	rgb(44, 42, 40)
//		Font f = new Font(Font., 3, 5);
//		Font customFont = loadCustomFont("CascadiaMono.ttf", 14F);InputStream is = MyClass.class.getResourceAsStream("TestFont.ttf");
//		File font_file = new File("resources/CascadiaMono.ttf");
//		Font font = Font.createFont(Font.TRUETYPE_FONT, font_file);
//		textArea.setFont(font);
		final InputStream is = MyConsole.class.getResourceAsStream("resources/CascadiaMono.ttf");
		try {
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, is));
		} catch (FontFormatException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		Color customWhite = new Color(204, 204, 204);
		textArea.setForeground(customWhite);
		Color customGray = new Color(44, 42, 40);
		textArea.setBackground(customGray);
		JScrollPane scrollPane = new JScrollPane(textArea);
		frame.add(scrollPane);
	}

	public void showConsole() {
		frame.setVisible(true);
	}

	public void printText(String text) {
		textArea.append(text);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

	private Font loadCustomFont(String fontFileName, float fontSize) {
		Font customFont = null;
		try {
			InputStream fontStream = getClass().getResourceAsStream("/resources/" + fontFileName);
			customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(fontSize);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(customFont);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		return customFont;
	}


	public static void main(String[] args) throws IOException, FontFormatException {
		MyConsole console = new MyConsole();
		console.showConsole();

		// Separate thread to read input and display in the console
//		Thread inputThread = new Thread(() -> {
//			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//			String line;
//			try {
//				while ((line = reader.readLine()) != null) {
//					console.printText(line + "\n");
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		});
//		inputThread.start();

		// Your code here
		System.out.println("Hello, world!");
		console.printText("Hello world!");
		System.out.println("This is a custom console.");
		console.printText("\n");
		console.printText(String.valueOf(frame.getBackground()));
	}
}

