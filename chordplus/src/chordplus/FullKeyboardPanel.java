package chordplus;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FullKeyboardPanel extends JPanel implements ActionListener, ChangeListener {
	JLabel lTranspose, lInstLabel;
	JComboBox cInst;
	FullKeyboardCanvas cKeyboard;

	chordplus rootview;
	JSpinner sTranspose;

	int transpose = 0, pianoBasement = 0, guitarBasement = 0;

	public FullKeyboardPanel(chordplus cp) {
		super();
		setLayout(null);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));

		rootview = cp;

		cKeyboard = new FullKeyboardCanvas(this, rootview);
		cKeyboard.setBounds(12, 38, 631, 36);
		add(cKeyboard);

		lTranspose = new JLabel("トランスポーズ: ");
		lTranspose.setBounds(16, 10, 112, 24);
		add(lTranspose);

		SpinnerNumberModel snm = new SpinnerNumberModel(0, -36, 36, 1);
		sTranspose = new JSpinner(snm);
		sTranspose.setBounds(128, 10, 64, 22);
		sTranspose.addChangeListener(this);
		add(sTranspose);

		lInstLabel = new JLabel("音色:", JLabel.RIGHT);
		lInstLabel.setBounds(413, 10, 32, 22);
		add(lInstLabel);

		cInst = new JComboBox(Chord.instrumentNameList());
		cInst.setBounds(451, 10, 192, 22);
		cInst.addActionListener(this);
		add(cInst);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object target = arg0.getSource();
		if (target == cInst) {
			rootview.changeInstrument(cInst.getSelectedIndex());
		}
	}

	void receiveNoteOn(int note, boolean onOrOff) {
		cKeyboard.receiveNoteOn(note, onOrOff);
	}

	void receiveAllNotesOff() {
		for (int i = 0; i < 120; i++) {
			if (cKeyboard.isNotePlaying(i) > 0) {
				rootview.noteOn(i, false);
			}
		}
	}

	public void receiveChangeMode(int mode) {
		cKeyboard.changeMode(mode);
	}

	public void receiveChangeTranspose(int t) {
		cKeyboard.receiveChangeTranspose(t);
		sTranspose.setValue(t);
	}

	public void receiveChangePianoBasement(int t) {
		cKeyboard.receiveChangePianoBasement(t);
	}

	public void receiveChangeGuitarBasement(int t) {
		cKeyboard.receiveChangeGuitarBasement(t);
	}

	public void changeInstrument(int i) {
		cInst.setSelectedIndex(i);
		rootview.changeInstrument(i);
	}

	public void receiveChangeInstrument(int i) {
		cInst.setSelectedIndex(i);
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		if (arg0.getSource() == sTranspose) {
			rootview.changeTranspose((Integer) sTranspose.getValue());
		}
	}
}
