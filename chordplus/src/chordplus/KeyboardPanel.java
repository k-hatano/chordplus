package chordplus;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

public class KeyboardPanel extends JPanel implements ActionListener, MouseListener {
	JLabel lChord, lTranspose;
	JButton bFocus;
	chordplus rootview;
	KeyboardCanvas cKeyboard;

	public KeyboardPanel(chordplus cp) {
		super();

		rootview = cp;

		setLayout(null);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));

		cKeyboard = new KeyboardCanvas(this, rootview);
		cKeyboard.setBounds(16, 16, 353, 99);
		add(cKeyboard);
		cKeyboard.requestFocusInWindow();

		bFocus = new JButton("▲ Enter ▲");
		bFocus.setBounds(80, 125, 225, 20);
		bFocus.setOpaque(true);
		bFocus.addActionListener(this);
		rootview.getRootPane().setDefaultButton(bFocus);
		add(bFocus);

		lChord = new JLabel("", JLabel.CENTER);
		lChord.setBounds(113, 126, 160, 18);
		lChord.setOpaque(true);
		lChord.addMouseListener(this);
		add(lChord);

		lTranspose = new JLabel("", JLabel.RIGHT);
		lTranspose.setBounds(337, 126, 32, 18);
		lTranspose.setForeground(Color.gray);
		add(lTranspose);
	}

	void receiveChangeMode(int mode) {
		cKeyboard.receiveChangeMode(mode);
	}

	void receiveEstimatedChord(String name, int notes[], int bass, boolean mute) {
		if (mute) {
			lChord.setForeground(Color.gray);
		} else {
			lChord.setForeground(Color.black);
		}
		lChord.setText(name);
		cKeyboard.receiveEstimatedChordNotes(notes, bass);
	}

	void play() {
		lChord.setForeground(Color.black);
		rootview.play();
	}

	void stop() {
		rootview.keyPressed(-1);
		rootview.sendAllNotesOff();
	}

	public void receiveKeyboardBlured() {
		bFocus.setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == bFocus) {
			cKeyboard.requestFocusInWindow();
		}
	}

	public void receiveChangeTranspose(int t) {
		if (t == 0) {
			lTranspose.setText("");
		} else if (t > 0) {
			lTranspose.setText("+" + t);
		} else {
			lTranspose.setText("" + t);
		}
	}

	public void receiveKeyboardFocused() {
		bFocus.setVisible(false);
	}

	/* mouse events */

	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getButton() == MouseEvent.BUTTON3) {
			stop();
		} else {
			play();
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		if (arg0.getSource() == lChord) {
			if (lChord.getText().length() > 0) {
				lChord.setBackground(new Color(0.8f, 0.8f, 0.8f));
			}
		}
	}

	public void mouseExited(MouseEvent arg0) {
		if (arg0.getSource() == lChord) {
			lChord.setBackground(null);
		}
	}

	public void mousePressed(MouseEvent arg0) {

	}

	public void mouseReleased(MouseEvent arg0) {

	}
}
