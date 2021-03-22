package chordplus;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import java.util.*;

public class ChordPanel extends JPanel implements MouseListener, MouseWheelListener {
	JLabel lScale, lDegree, lOn;
	JLabel[] aTriad = new JLabel[6];
	JLabel[] aSeventh = new JLabel[6];
	JLabel[] aMajorSeventh = new JLabel[6];
	JLabel[] aSixth = new JLabel[6];
	JLabel[] aAdd9 = new JLabel[6];
	JLabel[] aNinth = new JLabel[6];
	JLabel[] aMajorNinth = new JLabel[6];

	BarCanvas bcSeparator;

	chordplus rootview;
	int lastPressed;
	int root;
	int reality[][] = new int[6][7];
	int basic, tension;
	int bass;
	int row = -1;
	int rotated = 0;
	int autoselect = 0;

	int lastRoot, lastBass, lastBasic, lastTension;
	int emptyArray[] = {};

	boolean haveFocus;

	public ChordPanel(chordplus cp) {
		super();
		lastPressed = -1;

		rootview = cp;

		root = -1;
		bass = -1;
		basic = -1;
		tension = -1;

		lastRoot = -1;
		lastBass = -1;
		lastBasic = -1;
		lastTension = -1;

		setLayout(null);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));

		lScale = new JLabel("", JLabel.CENTER);
		lScale.setBounds(8, 11, 61, 16);
		add(lScale);

		lDegree = new JLabel("", JLabel.CENTER);
		lDegree.setForeground(Color.lightGray);
		lDegree.setBounds(130, 11, 122, 16);
		add(lDegree);

		lOn = new JLabel("", JLabel.CENTER);
		lOn.setBounds(313, 11, 61, 16);
		add(lOn);

		bcSeparator = new BarCanvas(Color.lightGray);
		bcSeparator.setBounds(8, 33, 366, 1);
		add(bcSeparator);

		for (int t = 0; t < 6; t++) {
			aTriad[t] = new JLabel(Chord.chordName(-1, t, 0, -1, 0), JLabel.CENTER);
			aTriad[t].setBounds(8 + t * 61, 37, 61, 20);
			add(aTriad[t]);

			aSeventh[t] = new JLabel(Chord.chordName(-1, t, 1, -1, 0), JLabel.CENTER);
			aSeventh[t].setBounds(8 + t * 61, 57, 61, 20);
			add(aSeventh[t]);

			aMajorSeventh[t] = new JLabel(Chord.chordName(-1, t, 2, -1, 0), JLabel.CENTER);
			aMajorSeventh[t].setBounds(8 + t * 61, 77, 61, 20);
			add(aMajorSeventh[t]);

			aSixth[t] = new JLabel(Chord.chordName(-1, t, 3, -1, 0), JLabel.CENTER);
			aSixth[t].setBounds(8 + t * 61, 97, 61, 20);
			add(aSixth[t]);

			aAdd9[t] = new JLabel(Chord.chordName(-1, t, 4, -1, 0), JLabel.CENTER);
			aAdd9[t].setBounds(8 + t * 61, 117, 61, 20);
			add(aAdd9[t]);

			aNinth[t] = new JLabel(Chord.chordName(-1, t, 5, -1, 0), JLabel.CENTER);
			aNinth[t].setBounds(8 + t * 61, 137, 61, 20);
			add(aNinth[t]);

			aMajorNinth[t] = new JLabel(Chord.chordName(-1, t, 6, -1, 0), JLabel.CENTER);
			aMajorNinth[t].setBounds(8 + t * 61, 157, 61, 20);
			add(aMajorNinth[t]);
		}

		for (int b = 0; b < 7; b++) {
			for (int t = 0; t < 6; t++) {
				JLabel label = this.chordLabel(b, t);
				label.setForeground(Color.gray);
				label.setOpaque(true);
				label.addMouseListener(this);
			}
		}

		lScale.setVisible(false);
		lDegree.setVisible(false);
		lOn.setVisible(false);

		lScale.addMouseListener(this);
		lDegree.addMouseListener(this);
		lOn.addMouseListener(this);

		bcSeparator.setVisible(false);
		for (int t = 0; t < aTriad.length; t++) {
			aTriad[t].setVisible(false);
			aSeventh[t].setVisible(false);
			aMajorSeventh[t].setVisible(false);
			aSixth[t].setVisible(false);
			aAdd9[t].setVisible(false);
			aNinth[t].setVisible(false);
			aMajorNinth[t].setVisible(false);
		}

		addMouseListener(this);
		addMouseWheelListener(this);

		resetReality();
	}

	JLabel chordLabel(int b, int t) {
		switch (b) {
			case 0:
				return aTriad[t];
			case 1:
				return aSeventh[t];
			case 2:
				return aMajorSeventh[t];
			case 3:
				return aSixth[t];
			case 4:
				return aAdd9[t];
			case 5:
				return aNinth[t];
			case 6:
				return aMajorNinth[t];
			default:
				return null;
		}
	}

	void receiveChangeMode(int md) {
		Chord.mode = md;
		boolean labelsVisible = md != 0;

		lScale.setVisible(labelsVisible);
		lDegree.setVisible(labelsVisible);
		lOn.setVisible(labelsVisible);
		bcSeparator.setVisible(labelsVisible);
		for (int i = 0; i < aTriad.length; i++) {
			aTriad[i].setVisible(labelsVisible && !Chord.omitTriad);
			aSeventh[i].setVisible(labelsVisible);
			aMajorSeventh[i].setVisible(labelsVisible);
			aSixth[i].setVisible(labelsVisible);
			aAdd9[i].setVisible(labelsVisible);
			aNinth[i].setVisible(labelsVisible);
			aMajorNinth[i].setVisible(labelsVisible);
		}
	}

	void receiveKeyPressed(int which) {
		if (Chord.mode == 0)
			return;
		if (which < 0) {
			root = -1;
			bass = -1;
			resetReality();
			rootview.receiveEstimatedChord("", emptyArray, -1, true);
		} else {
			if (lastPressed == which) {
				root = -1;
				bass = -1;
				resetReality();
			}
			bass = -1;
			estimate(which % 12);
			selectMax();
			rootview.receiveEstimatedChord(chordName(), Chord.notesOfChordWithRoot(basic, tension, root), root, true);
		}
		reflectReality();
		lastPressed = which;
		row = -1;
		rootview.receivePlayingChord(basic, tension, root, bass);
	}

	void estimate(int note) {
		int diatonic[], chord[];
		boolean flg, flg2;
		if (root == -1) {
			root = note % 12;
			diatonic = Chord.notesOfScale(Chord.tonic, Chord.minor);
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 6; j++) {
					if (i == 0 && Chord.omitTriad) {
						reality[j][i] = 0;
						continue;
					}
					chord = Chord.notesOfChordWithRoot(j, i, root);
					flg = true;
					for (int k = 1; k < chord.length; k++) {
						flg2 = false;
						for (int l = 0; l < diatonic.length; l++) {
							if (diatonic[l] == chord[k]) {
								flg2 = true;
								break;
							}
						}
						if (flg2 == false)
							flg = false;
					}
					if (flg) {
						reality[j][i]++;
					} else {
						reality[j][i] = 0;
					}
				}
			}
		} else {
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 6; j++) {
					if (i == 0 && Chord.omitTriad) {
						reality[j][i] = 0;
						continue;
					}
					chord = Chord.notesOfChordWithRoot(j, i, root);
					flg = false;
					for (int k = 0; k < chord.length; k++) {
						if (note == chord[k]) {
							flg = true;
							break;
						}
					}
					if (flg) {
						reality[j][i]++;
					} else {
						reality[j][i] = 0;
					}
				}
			}
		}
	}

	void selectMax() {
		int b = -1, t = -1, m = 0;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++) {
				if (reality[j][i] > m) {
					b = j;
					t = i;
					m = reality[j][i];
				}
			}
		}
		if (m > 0) {
			Chord.basic = b;
			Chord.tension = t;
			Chord.root = root;
			Chord.bass = bass;
			stressMax(b, t);
		}
	}

	void stressMax(int b, int t) {
		tension = t;
		basic = b;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++) {
				JLabel label = this.chordLabel(i, j);
				if (tension == i && basic == j) {
					label.setBackground(Color.lightGray);
				} else {
					label.setBackground(null);
				}
			}
		}
	}

	void resetReality() {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++)
				reality[j][i] = 0;
		}
		for (int j = 0; j < 6; j++) {
			aTriad[j].setBackground(null);
			aSeventh[j].setBackground(null);
			aMajorSeventh[j].setBackground(null);
			aSixth[j].setBackground(null);
			aAdd9[j].setBackground(null);
			aNinth[j].setBackground(null);
			aMajorNinth[j].setBackground(null);
		}
	}

	void reflectReality() {
		lScale.setText(root < 0 ? "" : Chord.nameOfNote(root, 0));
		if (root < 0) {
			lDegree.setText("");
		} else if (Chord.minor == 0) {
			lDegree.setText(Chord.sDegreeMajor[(root - Chord.tonic + 36) % 12]);
		} else if (Chord.harmonicMinor == false) {
			lDegree.setText(Chord.sDegreeMinor[(root - Chord.tonic + 36) % 12]);
		} else {
			lDegree.setText(Chord.sDegreeHarmonicMinor[(root - Chord.tonic + 36) % 12]);
		}
		if (bass >= 0 && bass != root) {
			lOn.setText("on " + Chord.nameOfNote(bass, 0));
		} else {
			lOn.setText("");
		}
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++) {
				JLabel label = this.chordLabel(i, j);
				label.setForeground(reality[j][i] > 0 ? Color.black : Color.gray);
			}
		}
	}

	String chordName() {
		return Chord.chordName(root, basic, tension, bass, 0);
	}

	void receivePlay() {
		int chord[];
		int n;

		if (root < 0) {
			if (lastRoot < 0) {
				return;
			}
			root = lastRoot;
			bass = lastBass;
			basic = lastBasic;
			tension = lastTension;
		}

		rootview.sendAllNotesOff();

		if (Chord.mode == 1) {
			chord = Chord.notesOfChordWithPianoBasement(basic, tension, root + (Chord.transpose() + 36) % 12,
				bass < 0 ? bass : bass + (Chord.transpose() + 36) % 12, Chord.pianoBasement);
			if (Chord.smartRange > 0) {
				int chordUnder[] = Chord.notesOfChordWithPianoBasement(basic, tension, root + (Chord.transpose() + 36) % 12,
						bass < 0 ? bass : bass + (Chord.transpose() + 36) % 12, Chord.pianoBasement - 1);
				Arrays.sort(chord);
				Arrays.sort(chordUnder);
				int chordNormalVariance = chord[chord.length - 1] - chord[0];
				int chordUnderVariance = chordUnder[chordUnder.length - 1] - chordUnder[0];

				if (chordUnderVariance < chordNormalVariance) {
					chord = chordUnder;
				}
			}
			for (int i = 0; i < chord.length; i++) {
				n = chord[i];
				rootview.noteOn(n, true);
			}
		} else if (Chord.mode == 2) {
			chord = Chord.notesOfChordWithGuitarBasement(basic, tension, root + (Chord.transpose() + 36) % 12,
					bass < 0 ? bass : bass + (Chord.transpose() + 36) % 12, Chord.guitarBasement + 40);
			for (int i = 0; i < chord.length; i++) {
				n = chord[i];
				rootview.noteOn(n, true);
			}
		}

		lastRoot = root;
		lastBass = bass;
		lastBasic = basic;
		lastTension = tension;

		rootview.receiveEstimatedChord(chordName(), Chord.notesOfChordWithRoot(basic, tension, root),
				bass < 0 ? root : bass, false);
		rootview.pushChord((root - Chord.tonic + 36) % 12, basic, tension,
				bass >= 0 ? (bass - Chord.tonic + 36) % 12 : (root - Chord.tonic + 36) % 12);

		root = -1;
		bass = -1;
		resetReality();
		reflectReality();
	}

	void receiveBassChanged(int note) {
		if (root < 0 && lastRoot >= 0) {
			root = lastRoot;
			basic = lastBasic;
			tension = lastTension;
		}
		bass = note;
		reflectReality();
		rootview.receiveEstimatedChord(chordName(), Chord.notesOfChordWithRoot(basic, tension, root),
				bass < 0 ? root : bass, true);
	}

	void receiveSelectRow(int which) {
		if (Chord.mode == 0)
			return;
		if (root < 0 && lastRoot >= 0) {
			root = lastRoot;
		}
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++) {
				reality[j][i] = (j == which) ? 1 : 0;
			}
		}
		if (row == which) {
			do {
				tension++;
				if (tension >= 7) {
					tension = Chord.omitTriad ? 1 : 0;
				}
			} while (Chord.notesOfChord(basic, tension).length <= 1);
		} else {
			basic = which;
			tension = Chord.omitTriad ? 1 : 0;
		}
		row = which;
		reality[basic][tension]++;
		reflectReality();
		selectMax();
		rootview.receiveEstimatedChord(chordName(), Chord.notesOfChordWithRoot(basic, tension, root),
				bass < 0 ? root : bass, true);
	}

	void receiveSelectTension(int which) {
		if (Chord.mode == 0)
			return;
		if (root < 0 && lastRoot >= 0) {
			root = lastRoot;
		}
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++)
				reality[j][i] = (i == which) ? 1 : 0;
		}
		tension = which;
		if (Chord.notesOfChord(basic, tension).length <= 1) {
			tension = Chord.omitTriad ? 1 : 0;
		}
		reality[basic][tension]++;
		reflectReality();
		selectMax();
		rootview.receiveEstimatedChord(chordName(), Chord.notesOfChordWithRoot(basic, tension, root),
				bass < 0 ? root : bass, true);
	}

	void receiveShiftRow(int vx, int vy) {
		if (Chord.mode == 0)
			return;
		if (root < 0 && lastRoot >= 0) {
			root = lastRoot;
		}
		basic += vx;
		if (basic < 0) {
			basic = 5;
		}
		if (basic > 5) {
			basic = 0;
		}
		row = basic;
		tension += vy;

		if (vy > 0) {
			if (tension >= 7) {
				tension = Chord.omitTriad ? 1 : 0;
			}
			while (Chord.notesOfChord(basic, tension).length <= 1) {
				tension++;
				if (tension >= 7) {
					tension = Chord.omitTriad ? 1 : 0;
				}
			}
		}

		if (vy <= 0) {
			if (tension < (Chord.omitTriad ? 1 : 0)) {
				tension = 6;
			}
			while (Chord.notesOfChord(basic, tension).length <= 1) {
				tension--;
			}
		}

		reflectReality();
		stressMax(basic, tension);
		rootview.receiveEstimatedChord(chordName(), Chord.notesOfChordWithRoot(basic, tension, root),
				bass < 0 ? root : bass, true);
	}

	void receiveChangeOmitTriad(boolean ot) {
		int i;
		if (Chord.mode == 0) {
			ot = true;
		}
		for (i = 0; i < 6; i++) {
			aTriad[i].setVisible(!ot);
		}

		if (root < 0) {
			return;
		}
		int r = root;
		root = -1;
		reEstimate(r);
	}

	void receiveChangeHarmonicMinor(boolean hm) {
		if (root < 0) {
			return;
		}
		int r = root;
		root = -1;
		reEstimate(r);
	}

	void receiveChangeRoot(int r) {
		root = -1;
		bass = -1;
		reEstimate(r);
	}

	void receiveShiftRoot(int db) {
		if (root < 0 && lastRoot >= 0) {
			root = lastRoot;
		}
		int r = (root + db + 12) % 12;
		root = -1;
		reEstimate(r);
	}

	void receiveShiftBass(int delta) {
		if (root < 0 && lastRoot >= 0) {
			root = lastRoot;
			basic = lastBasic;
			tension = lastTension;
			bass = lastBass;
		}
		if (bass < 0) {
			bass = root;
		}
		bass = (bass + delta + 36) % 12;
		reflectReality();
		rootview.receiveEstimatedChord(chordName(), Chord.notesOfChordWithRoot(basic, tension, root),
				bass < 0 ? root : bass, true);
	}

	void reEstimate(int r) {
		resetReality();
		estimate(r);
		reflectReality();
		selectMax();
		rootview.receiveEstimatedChord(chordName(), Chord.notesOfChordWithRoot(basic, tension, root),
				bass < 0 ? root : bass, true);
	}

	/* mouse events */

	@Override
	public void mouseClicked(MouseEvent e) {
		Object target = e.getSource();
		if (target == lDegree || target == lScale || target == lOn) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				rootview.play();
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				rootview.keyPressed(-1);
				rootview.sendAllNotesOff();
			}
			return;
		}

		for (int j = 0; j < 6; j++) {
			for (int i = 0; i < 7; i++) {
				if (target == chordLabel(i, j)) {
					if (chordLabel(i, j).getText().length() > 0) {
						rootview.selectRow(j);
						rootview.selectTension(i);
						if (e.getButton() == MouseEvent.BUTTON1) {
							rootview.play();
						} else {
							autoselect = 1;
						}
					} else {
						rootview.keyPressed(-1);
						rootview.sendAllNotesOff();
					}
					return;
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		Object target = e.getSource();
		if (autoselect > 0 || (e.getModifiers() & ActionEvent.ALT_MASK) == ActionEvent.ALT_MASK) {
			for (int j = 0; j < 6; j++) {
				for (int i = 0; i < 7; i++) {
					if (target == chordLabel(i, j)) {
						if (chordLabel(i, j).getText().length() > 0) {
							rootview.selectRow(j);
							rootview.selectTension(i);
							autoselect = 1;
						}
						return;
					}
				}
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		Object target = e.getSource();
		if (target == this) {
			autoselect = 0;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		rotated += arg0.getWheelRotation();
		if (Chord.root >= 0) {
			if (rotated >= 4) {
				rootview.shiftRoot(1);
				rotated -= 4;
			} else if (rotated <= -4) {
				rootview.shiftRoot(-1);
				rotated += 4;
			}
		}
	}
}
