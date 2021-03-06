import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.UIManager;

public class SimFontEnd extends JFrame {
	static JTextField urlField = new MyTextField("http://", 15);
	static JTextField keyWordField = new MyTextField(5);
	static JTextField depthField = new JTextField(2);
	static JTextArea resultArea = new MyTextArea("Result is here");
	JScrollPane resultPane = new JScrollPane(resultArea);
	JPanel queryPanel = new JPanel();
	JButton searchBt = new MyButton("Search");
	ButtonGroup caseGrp = new ButtonGroup();
	JRadioButton ignoreCaseRb = new MyRadioButton("Ignore", true);
	JRadioButton strictCaseRb = new MyRadioButton("Strict", false);

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

		setSize(800, 500);
		queryPanel.add(new MyLabel("URL"), FlowLayout.LEFT);
		queryPanel.add(urlField);
		queryPanel.add(new MyLabel("KeyWord"));
		queryPanel.add(keyWordField);
		queryPanel.add(new MyLabel("Depth"));
		queryPanel.add(depthField);
		queryPanel.add(searchBt);
		caseGrp.add(ignoreCaseRb);
		caseGrp.add(strictCaseRb);
		queryPanel.add(ignoreCaseRb);
		queryPanel.add(strictCaseRb);
		add(queryPanel, BorderLayout.NORTH);
		add(resultPane, BorderLayout.CENTER);
		resultArea.setWrapStyleWord(true);
		searchBt.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				resultArea.setText("WAIT...");
				System.out.println(SimFontEnd.urlField.getText());
				Runnable r = new SearchThread(urlField.getText(), keyWordField
						.getText(), getCase(), Integer.parseInt(depthField
						.getText()));
				Thread t = new Thread(r);
				t.start();
			}

			private boolean getCase() {
				return ignoreCaseRb.isSelected();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
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

	public MyTextField(String string, int i) {
		super(string, i);
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

class MyRadioButton extends JRadioButton {

	public MyRadioButton(String string, boolean b) {
		// TODO Auto-generated constructor stub
		super(string, b);
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
