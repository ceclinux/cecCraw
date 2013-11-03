import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.UIManager;

public class SimFontEnd extends JFrame {
	JTextField urlField = new MyTextField(15);
	JTextField keyWord = new MyTextField(5);
	JTextArea resultArea = new MyTextArea("Result is here");
	JPanel queryPanel = new JPanel();
	JButton searchBt = new MyButton("Search");

	public static void main(String[] args) throws InstantiationException {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
		}
		System.setProperty("awt.useSystemAAFontSettings", "on");

		System.setProperty("swing.aatext", "true");

		JFrame jf = new SimFontEnd();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
		jf.setResizable(false);
	}

	public SimFontEnd() throws InstantiationException {

		setSize(700, 500);
	

		queryPanel.add(new MyLabel("URL"),FlowLayout.LEFT);
		queryPanel.add(urlField);
		queryPanel.add(new MyLabel("KeyWord"));
		queryPanel.add(keyWord);
		queryPanel.add(searchBt);
		add(queryPanel, BorderLayout.NORTH);
		add(resultArea, BorderLayout.CENTER);
	}
}

class MyTextArea extends JTextArea {
	public MyTextArea(String string) {
		super(string);
		setEditable(false);
		setFont(new Font("Monaco", Font.PLAIN, 20));
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		super.paintComponent(g2);
	}
}

class MyLabel extends JLabel {
	public MyLabel(String string) {
		super(string);

		setFont(new Font("Monaco", Font.PLAIN, 20));
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		super.paintComponent(g2);
	}
}

class MyButton extends JButton {
	public MyButton(String string) {
		super(string);

		setFont(new Font("Monaco", Font.PLAIN, 15));
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		super.paintComponent(g2);
	}
}

class MyTextField extends JTextField {
	public MyTextField(int i) {
		super(i);

		setFont(new Font("Monaco", Font.PLAIN, 15));
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		super.paintComponent(g2);
	}
}

