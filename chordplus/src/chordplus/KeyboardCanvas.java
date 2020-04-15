package chordplus;

import java.awt.*;
import java.awt.event.*;

public class KeyboardCanvas extends Canvas
		implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, FocusListener {
	int pressed[] = new int[18];
	int lastPressed[] = new int[18];
	int togglePressed[] = new int[18];
	int spotted[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	int lastClicked;
	int lastKey;
	int whiteKeys[] = { 0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 17 };
	int blackKeys[] = { 1, 3, 6, 8, 10, 13, 15 };
	int keyRects[][] = { { 0, 2, 32, 96 }, { 20, 0, 24, 64 }, { 32, 2, 32, 96 }, { 52, 0, 24, 64 }, { 64, 2, 32, 96 },
			{ 96, 2, 32, 96 }, { 116, 0, 24, 64 }, { 128, 2, 32, 96 }, { 148, 0, 24, 64 }, { 160, 2, 32, 96 },
			{ 180, 0, 24, 64 }, { 192, 2, 32, 96 }, { 224, 2, 32, 96 }, { 244, 0, 24, 64 }, { 256, 2, 32, 96 },
			{ 276, 0, 24, 64 }, { 288, 2, 32, 96 }, { 320, 2, 32, 96 }, };
	char smallKeys[] = { 'a', 'w', 's', 'e', 'd', 'f', 't', 'g', 'y', 'h', 'u', 'j', 'k', 'o', 'l', 'p', ';', ':' };
	char largeKeys[] = { 'A', 'W', 'S', 'E', 'D', 'F', 'T', 'G', 'Y', 'H', 'U', 'J', 'K', 'O', 'L', 'P', '+', '*' };
	char smallRowKeys[] = { 'z', 'x', 'c', 'v', 'b', 'n' };
	char largeRowKeys[] = { 'Z', 'X', 'C', 'V', 'B', 'N', 'M' };
	char smallNumberKeys[] = { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	char largeNumberKeys[] = { '!', '\"', '#', '$', '%', '&', '\'', '(', ')' };
	KeyboardPanel superview;
	chordplus rootview;
	KeyWatcher keyWatcher;
	boolean haveFocus, shiftPushed;
	int mode = 0;
	int transpose = 1;
	int chordNotes[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	Point startPoint;
	int rotated = 0;
	int bassNote = -1, oldBassNote = -1;
	int draggingButton = -1;

	boolean debugMode = false;
	boolean keyPressedAfterReleased = false;
	boolean repaintTrigger = false;

	public KeyboardCanvas(KeyboardPanel cp, chordplus gp) {
		super();
		superview = cp;
		rootview = gp;
		haveFocus = false;
		shiftPushed = false;
		for (int i = 0; i < 18; i++) {
			pressed[i] = 0;
			lastPressed[i] = 0;
			togglePressed[i] = 0;
		}
		lastClicked = -1;
		lastKey = -1;
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addFocusListener(this);
		keyWatcher = new KeyWatcher();
		keyWatcher.start();
	}

	public void paint(final Graphics g) {
		Image img = createImage(getSize().width, getSize().height);
		Graphics grp = img.getGraphics();

		for (int i = 0; i < whiteKeys.length; i++) {
			grp.setColor(colorOfKey(lastPressed[whiteKeys[i]], false));
			grp.fillRect(keyRects[whiteKeys[i]][0], keyRects[whiteKeys[i]][1], keyRects[whiteKeys[i]][2],
					keyRects[whiteKeys[i]][3]);
		}

		grp.setColor(Color.black);
		for (int i = 0; i < whiteKeys.length; i++) {
			grp.drawRect(keyRects[whiteKeys[i]][0], keyRects[whiteKeys[i]][1], keyRects[whiteKeys[i]][2],
					keyRects[whiteKeys[i]][3]);
		}

		for (int i = 0; i < blackKeys.length; i++) {
			grp.setColor(colorOfKey(lastPressed[blackKeys[i]], true));
			grp.fillRect(keyRects[blackKeys[i]][0], keyRects[blackKeys[i]][1], keyRects[blackKeys[i]][2],
					keyRects[blackKeys[i]][3]);
		}

		grp.setColor(Color.black);
		for (int i = 0; i < blackKeys.length; i++) {
			grp.drawRect(keyRects[blackKeys[i]][0], keyRects[blackKeys[i]][1], keyRects[blackKeys[i]][2],
					keyRects[blackKeys[i]][3]);
		}

		for (int i = 0; i < 12; i++) {
			if (spotted[i] > 0) {
				if (Chord.scaleContainsNote(Chord.tonic, Chord.minor, i)) {
					grp.setColor(Color.gray);
				} else {
					grp.setColor(Color.lightGray);
				}
				int sx = (keyRects[i][0] + keyRects[i][2] / 2) - 10;
				int sy = keyRects[i][1] + keyRects[i][3] - 26;
				grp.fillOval(sx, sy, 20, 20);
			}
			if (i == bassNote) {
				if (Chord.scaleContainsNote(Chord.tonic, Chord.minor, i)) {
					grp.setColor(Color.gray);
				} else {
					grp.setColor(Color.lightGray);
				}
				int sx = (keyRects[i][0] + keyRects[i][2] / 2) - 12;
				int sy = keyRects[i][1] + keyRects[i][3] - 28;
				grp.drawOval(sx, sy, 24, 24);
			}
		}
		g.drawImage(img, 0, 0, this);
	}

	int whichKey(int x, int y) {
		if (y >= 0 && y <= 64) {
			for (int i = 0; i < blackKeys.length; i++) {
				if (x >= keyRects[blackKeys[i]][0] && x <= keyRects[blackKeys[i]][0] + keyRects[blackKeys[i]][2])
					return blackKeys[i];
			}
		}
		if (y >= 2 && y <= 98) {
			for (int i = 0; i < whiteKeys.length; i++) {
				if (x >= keyRects[whiteKeys[i]][0] && x <= keyRects[whiteKeys[i]][0] + keyRects[whiteKeys[i]][2])
					return whiteKeys[i];
			}
		}
		return -1;
	}

	Color colorOfKey(int which, boolean blackOrWhite) {
		switch (which) {
			case 0:
				return blackOrWhite ? Color.black : Color.white;
			case 1:
				return Color.gray;
			case 2:
				return Color.lightGray;
			default:
				return Color.white;
		}
	}

	void repaintKey(int which) {
		if (which < 0) {
			return;
		}
		repaint(keyRects[which][0], keyRects[which][1], keyRects[which][2], keyRects[which][3]);
	}

	void playNote(int note, boolean playOrStop, boolean mute) {
		switchPressed(note, playOrStop ? 1 : 0);
		if (!mute) {
			rootview.noteOn(60 + note + Chord.transpose(), playOrStop);
		}
	}

	class KeyWatcher extends Thread {
		public void run() {
			int pressing;
			while (true) {
				synchronized (this) {
					try {
						for (int j = 0; j < 10; j++) {
							sleep(10);
							if (repaintTrigger)
								break;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					pressing = 0;
					for (int i = 0; i < smallKeys.length; i++) {
						if (pressed[i] == 1)
							pressing++;
						if (pressed[i] == 1 && lastPressed[i] == 0) {
							playNote(i, true, mode != 0);
							lastPressed[i] = 1;
							rootview.keyPressed(i);
							repaintKey(i);
						}
						if (pressed[i] == 0 && lastPressed[i] == 1) {
							playNote(i, false, mode != 0);
							lastPressed[i] = 0;
							repaintKey(i);
						}
					}
					for (int i = 0; i < 12; i++) {
						if (chordNotes[i] != spotted[i]) {
							spotted[i] = chordNotes[i];
							repaintKey(i);
						}
					}
					if (bassNote != oldBassNote) {
						if (bassNote >= 0) {
							repaintKey(bassNote);
						}
						if (oldBassNote >= 0) {
							repaintKey(oldBassNote);
						}
						oldBassNote = bassNote;
					}
					if (pressing == 0 && keyPressedAfterReleased && Chord.playAtReleased) {
						keyPressedAfterReleased = false;
						rootview.play();
					}
					repaintTrigger = false;
				}
			}
		}
	}

	void receiveChangeMode(int md) {
		mode = md;
		if (mode == 0) {
			for (int i = 0; i < 18; i++) {
				togglePressed[i] = 0;
			}
		}
	}

	void receiveEstimatedChordNotes(int notes[], int bass) {
		for (int i = 0; i < 12; i++) {
			chordNotes[i] = 0;
		}
		for (int i = 0; i < notes.length; i++) {
			chordNotes[notes[i]] = 1;
		}
		bassNote = bass;
	}

	public void switchPressed(int index, int value) {
		synchronized (this) {
			pressed[index] = value;
		}
	}

	public void switchSpotted(int index, int value) {
		synchronized (this) {
			spotted[index] = value;
		}
	}

	/* mouse events */

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		if (mode == 0) {
			return;
		}
		if (lastClicked >= 0) {
			return;
		}
		if (Chord.playAtReleased) {
			rootview.keyPressed(-1);
			rootview.sendAllNotesOff();
		}
		return;
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		Point pt = arg0.getPoint();
		int which = whichKey(pt.x, pt.y);;

		draggingButton = arg0.getButton();
		if (lastClicked > -1) {
			return;
		}
		if (arg0.getButton() == MouseEvent.BUTTON3) {
			if (Chord.rightClickAction == Chord.RIGHT_CLICK_BASE_NOTE && Chord.mode != 0) {
				if (which > -1) {
					bassNote = which;
					rootview.bassChanged(bassNote);
				}
			} else if (Chord.rightClickAction == Chord.RIGHT_CLICK_PLAY) {
				superview.play();
			} else if (Chord.rightClickAction == Chord.RIGHT_CLICK_STOP) {
				superview.stop();
			}
			return;
		}
		if (arg0.getButton() == MouseEvent.BUTTON2) {
			superview.play();
			return;
		}
		if (which > -1 && pressed[which] == 0) {
			lastClicked = which;
			switchPressed(which, 1);
		}
		startPoint = pt;
		repaintTrigger = true;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (lastClicked < 0) {
			return;
		}
		if (arg0.getButton() == MouseEvent.BUTTON2 || arg0.getButton() == MouseEvent.BUTTON3) {
			return;
		}
		switchPressed(lastClicked, 0);
		lastClicked = -1;
		if (mode == 0) {
			rootview.pitchBend(0);
		}
		if (Chord.playAtReleased) {
			superview.play();
		}
		repaintTrigger = true;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point pt = e.getPoint();
		if (draggingButton != MouseEvent.BUTTON1) {
			return;
		}
		if (mode == 0) {
			if (pt.x - startPoint.x > 32) {
				rootview.pitchBend(63);
			} else if (pt.x - startPoint.x < -32) {
				rootview.pitchBend(-63);
			} else {
				rootview.pitchBend(0);
			}
		} else {
			if (pt.x - startPoint.x > 32) {
				rootview.shiftRow(1, 0);
				startPoint = pt;
			}
			if (pt.x - startPoint.x < -32) {
				rootview.shiftRow(-1, 0);
				startPoint = pt;
			}
			if (pt.y - startPoint.y > 32) {
				rootview.shiftRow(0, 1);
				startPoint = pt;
			}
			if (pt.y - startPoint.y < -32) {
				rootview.shiftRow(0, -1);
				startPoint = pt;
			}
		}
		repaintTrigger = true;
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		rotated += arg0.getWheelRotation();
		if (rotated >= 4) {
			if (rootview.fChord.root >= 0) {
				rootview.play();
			}
			rotated = 0;
		} else if (rotated <= -4) {
			rootview.keyPressed(-1);
			rootview.sendAllNotesOff();
			rotated = 0;
		}
	}

	/* key events */

	@Override
	public void keyPressed(KeyEvent arg0) {
		char key = arg0.getKeyChar();
		int code = arg0.getKeyCode();

		if (debugMode) { // コマンド入力モード
			if (key == 'z') {
				rootview.changeMode(0);
			} else if (key == 'x') {
				rootview.changeMode(1);
			} else if (key == 'c') {
				rootview.changeMode(2);
			} else if (key == 'q') {
				rootview.changeScale(Chord.tonic, 1 - Chord.minor);
			} else if (key == '1') {
				rootview.changeInstrument(0);
			} else if (key == '2') {
				rootview.changeInstrument(8);
			} else if (key == '3') {
				rootview.changeInstrument(16);
			} else if (key == '4') {
				rootview.changeInstrument(24);
			} else if (key == '5') {
				rootview.changeInstrument(32);
			} else if (key == '6') {
				rootview.changeInstrument(40);
			} else if (key == '7') {
				rootview.changeInstrument(48);
			} else if (key == '8') {
				rootview.changeInstrument(56);
			} else if (key == '!') {
				rootview.changeInstrument(64);
			} else if (key == '\"') {
				rootview.changeInstrument(72);
			} else if (key == '#') {
				rootview.changeInstrument(80);
			} else if (key == '$') {
				rootview.changeInstrument(88);
			} else if (key == '%') {
				rootview.changeInstrument(96);
			} else if (key == '&') {
				rootview.changeInstrument(104);
			} else if (key == '\'') {
				rootview.changeInstrument(112);
			} else if (key == '(') {
				rootview.changeInstrument(120);
			} else if (key == '9') {
				rootview.changeInstrument(64);
			} else if (key == ')') {
				rootview.changeInstrument(0);
			}
			for (int i = 0; i < smallKeys.length; i++) {
				if ((key == smallKeys[i] || key == largeKeys[i]) && lastClicked != i) {
					rootview.changeScale(i, Chord.minor);
					break;
				}
			}
			switch (code) {
				case KeyEvent.VK_LEFT:
					rootview.shiftTranspose(-1);
					break;
				case KeyEvent.VK_RIGHT:
					rootview.shiftTranspose(1);
					break;
				case KeyEvent.VK_UP:
					rootview.shiftVelocity(1);
					break;
				case KeyEvent.VK_DOWN:
					rootview.shiftVelocity(-1);
					break;
				case KeyEvent.VK_SHIFT:
				case KeyEvent.VK_ALT:
				case KeyEvent.VK_CONTROL:
				case KeyEvent.VK_CAPS_LOCK:
					return;
			}
			debugMode = false;
			superview.lChord.setForeground(Color.black);
			superview.lChord.setText("");
			return;
		} else if (code == KeyEvent.VK_ESCAPE) {
			debugMode = true;
			superview.lChord.setForeground(Color.red);
			superview.lChord.setText("コマンド入力モード");
			return;
		}

		if (code == KeyEvent.VK_SHIFT)
			shiftPushed = true;

		if (key == '.' || key == '\b' || key == '0') {
			rootview.keyPressed(-1);
			rootview.sendAllNotesOff();
		}

		for (int i = 0; i < smallKeys.length; i++) {
			if ((key == smallKeys[i] || key == largeKeys[i]) && lastClicked != i) {
				if (Chord.playAtReleased)
					keyPressedAfterReleased = true;
				if (shiftPushed) {
					rootview.bassChanged(i % 12);
				} else {
					switchPressed(i, 1);
				}
				break;
			}
		}

		for (int i = 0; i < smallRowKeys.length; i++) {
			if (key == smallRowKeys[i]) {
				rootview.selectRow(i);
				break;
			}
		}
		for (int i = 0; i < largeRowKeys.length; i++) {
			if (key == largeRowKeys[i]) {
				rootview.selectTension(i);
			}
		}

		if (mode == 0) { // フリーモード
			if (key == 'z') {
				rootview.shiftTranspose(-12);
			} else if (key == 'x') {
				rootview.shiftTranspose(12);
			} else if (key == 'c') {
				rootview.shiftVelocity(-8);
			} else if (key == 'v') {
				rootview.shiftVelocity(8);
			} else if (key == 'Z') {
				rootview.shiftTranspose(-1);
			} else if (key == 'X') {
				rootview.shiftTranspose(1);
			} else if (key == 'C') {
				rootview.shiftVelocity(-1);
			} else if (key == 'V') {
				rootview.shiftVelocity(1);
			} else if (key == '1') {
				rootview.pitchBend(-63);
			} else if (key == '2') {
				rootview.pitchBend(63);
			}
			switch (code) {
				case KeyEvent.VK_LEFT:
					rootview.shiftTranspose(-12);
					break;
				case KeyEvent.VK_RIGHT:
					rootview.shiftTranspose(12);
					break;
				case KeyEvent.VK_UP:
					rootview.shiftVelocity(8);
					break;
				case KeyEvent.VK_DOWN:
					rootview.shiftVelocity(-8);
					break;
				case KeyEvent.VK_PAGE_UP:
					rootview.shiftScale(1);
					break;
				case KeyEvent.VK_PAGE_DOWN:
					rootview.shiftScale(-1);
					break;
				case KeyEvent.VK_HOME:
					rootview.changeScale(Chord.tonic, 0);
					break;
				case KeyEvent.VK_END:
					rootview.changeScale(Chord.tonic, 1);
					break;
			}
		} else if (mode > 0) { // ピアノ・ギターモード
			if ((key == ' ' || key == '\n') && mode > 0) {
				rootview.play();
			}

			for (int i = 0; i < 7; i++) {
				int j = Chord.notesOfScale(Chord.tonic(), Chord.minor())[i];
				if ((key == smallNumberKeys[i] || key == largeNumberKeys[i]) && lastClicked != j) {
					if (shiftPushed) {
						rootview.bassChanged(j % 12);
					} else {
						rootview.keyPressed(j);
					}
					break;
				}
			}
			if (key == 'q' || key == 'Q') {
				rootview.shiftRoot(6);
				rootview.selectRow(3);
				rootview.selectTension(0);
				rootview.bassChanged(Chord.root);
			} else if (key == '8') {
				rootview.shiftRoot(-1);
			} else if (key == '9') {
				rootview.shiftRoot(1);
			} else if (key == '(') {
				rootview.shiftBass(-1);
			} else if (key == ')') {
				rootview.shiftBass(1);
			} else if (key == '/') {
				rootview.changeHarmonicMinor(!Chord.harmonicMinor);
			} else if (key == '_' || key == '\\') {
				rootview.changeOmitTriad(!Chord.omitTriad);
			}
			if (Chord.root < 0) { // コード停止後
				switch (code) {
					case KeyEvent.VK_UP:
						rootview.shiftVelocity(8);
						break;
					case KeyEvent.VK_DOWN:
						rootview.shiftVelocity(-8);
						break;
					case KeyEvent.VK_LEFT:
						if (mode == 1 && Chord.pianoBasement > -36) {
							rootview.changePianoBasement(Chord.pianoBasement - 1);
						} else if (mode == 2 && Chord.guitarBasement > -36) {
							rootview.changeGuitarBasement(Chord.guitarBasement - 1);
						}
						break;
					case KeyEvent.VK_RIGHT:
						if (mode == 1 && Chord.pianoBasement < 36) {
							rootview.changePianoBasement(Chord.pianoBasement + 1);
						} else if (mode == 2 && Chord.guitarBasement < 36) {
							rootview.changeGuitarBasement(Chord.guitarBasement + 1);
						}
						break;
					case KeyEvent.VK_PAGE_UP:
						rootview.shiftScale(-1);
						break;
					case KeyEvent.VK_PAGE_DOWN:
						rootview.shiftScale(1);
						break;
					case KeyEvent.VK_HOME:
						rootview.changeScale(Chord.tonic, 0);
						break;
					case KeyEvent.VK_END:
						rootview.changeScale(Chord.tonic, 1);
						break;
				}
			} else { // コード再生中
				switch (code) {
					case KeyEvent.VK_UP:
						rootview.shiftRow(0, -1);
						break;
					case KeyEvent.VK_DOWN:
						rootview.shiftRow(0, 1);
						break;
					case KeyEvent.VK_LEFT:
						rootview.shiftRow(-1, 0);
						break;
					case KeyEvent.VK_RIGHT:
						rootview.shiftRow(1, 0);
						break;
					case KeyEvent.VK_PAGE_UP:
						rootview.shiftScale(-1);
						break;
					case KeyEvent.VK_PAGE_DOWN:
						rootview.shiftScale(1);
						break;
					case KeyEvent.VK_HOME:
						rootview.changeScale(Chord.tonic, 0);
						break;
					case KeyEvent.VK_END:
						rootview.changeScale(Chord.tonic, 1);
						break;
				}
			}
		}

		lastKey = key;
		repaintTrigger = true;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		char key = arg0.getKeyChar();

		if (key == '1' || key == '2') {
			rootview.pitchBend(0);
		}
		if (arg0.getKeyCode() == KeyEvent.VK_SHIFT) {
			shiftPushed = false;
		}

		for (int i = 0; i < smallKeys.length; i++) {
			if ((key == smallKeys[i] || key == largeKeys[i]) && lastClicked != i) {
				switchPressed(i, 0);
				break;
			}
		}
		repaintTrigger = true;
	}

	/* focus evnets*/

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusGained(FocusEvent arg0) {
		haveFocus = true;
		superview.receiveKeyboardFocused();
		keyWatcher.resume();
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		haveFocus = false;
		rootview.keyPressed(-1);
		rootview.sendAllNotesOff();
		superview.receiveKeyboardBlured();
		keyWatcher.suspend();
	}
}
