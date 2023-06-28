package Logger;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

public final class Main {
	private JFrame frame;
	private JTextArea textArea;
	private PrintStream consolePrintStream;
	private class ConsoleOutputStream extends OutputStream {
		@Override
		public void write(int b) {
			textArea.append(String.valueOf((char) b));
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
	}

	/** Don't let anyone instantiate this class */
	private Main() {
		frame = new JFrame();
		frame.setTitle("My Console");
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.getContentPane().setBackground(Color.darkGray);

		// Create the JTextArea to display the console output
		textArea = new JTextArea(20, 40);
		JScrollPane scrollPane = new JScrollPane(textArea);
		frame.add(scrollPane);

		// Create a custom PrintStream that redirects output to the JTextArea
		consolePrintStream = new PrintStream(new ConsoleOutputStream());

		// Redirect System.out and System.err to the custom PrintStream
		System.setOut(consolePrintStream);
		System.setErr(consolePrintStream);
		this.show();
	}

	public void show() {
		frame.setVisible(true);
	}

	public synchronized void printText(int n) {
		textArea.append(String.valueOf(n));
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

	public synchronized void printText(String text) {
		textArea.append(text);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

	public synchronized void newLine() {
		textArea.append("\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

	public static void main(String[] args) {
		Main console = new Main();
		console.printText("Meow");
		console.newLine();
	}
}
