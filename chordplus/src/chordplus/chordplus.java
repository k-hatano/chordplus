package chordplus;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class chordplus extends JFrame {
	int il, it;

	StatusPanel fStatus;
	KeyboardPanel fKeyboard;
	ChordPanel fChord;
	OptionPanel fOption;
	HistoryPanel fHistory;
	ScalePanel fScale;
	FullKeyboardPanel fFullKeyboard;

	public chordplus() {
		super();

		setTitle("chordplus");
		pack();
		Insets insets = this.getInsets();
		il = insets.left;
		it = insets.top;
		setSize(670 + il, 465 + it);
		setLocation(32, 48);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setLayout(null);

		fStatus = new StatusPanel(this);
		fStatus.setBounds(8, 8, 654, 40);
		add(fStatus);

		fScale = new ScalePanel(this);
		fScale.setBounds(8, 56, 92, 308);
		add(fScale);

		fKeyboard = new KeyboardPanel(this);
		fKeyboard.setBounds(108, 56, 385, 155);
		add(fKeyboard);

		fChord = new ChordPanel(this);
		fChord.setBounds(108, 219, 385, 145);
		add(fChord);

		fOption = new OptionPanel(this);
		fOption.setBounds(501, 56, 161, 308);
		add(fOption);

		fHistory = new HistoryPanel(this);
		fHistory.setBounds(501, 56, 161, 308);
		fHistory.setVisible(false);
		add(fHistory);

		fFullKeyboard = new FullKeyboardPanel(this);
		fFullKeyboard.setBounds(8, 372, 654, 85);
		add(fFullKeyboard);

		/*
		 * try{ receiver = MidiSystem.getReceiver(); }catch (MidiUnavailableException
		 * e1){ JOptionPane.showMessageDialog(null,"MIDI が使用できません。chordplus を終了します。");
		 * System.exit(1); }
		 */

		MIDI.selectMidiDevice();
		if (MIDI.receiver == null)
			System.exit(0);

		Runtime.getRuntime().addShutdownHook(new Shutdown());
	}

	public static void main(String[] args) {
		chordplus mainWindow = new chordplus();
		mainWindow.show();
	}

	class Shutdown extends Thread {
		public void run() {
			MIDI.close();
		}
	}

	public void sendAllNotesOff() {
		fFullKeyboard.receiveAllNotesOff();
	}

	public void noteOn(int note, boolean onOrOff) {
		MIDI.send(MIDI.messageNoteOn(note, onOrOff));
		fFullKeyboard.receiveNoteOn(note, onOrOff);
	}

	public void changeMode(int mode) {
		Chord.mode = mode;
		fChord.receiveChangeMode(mode);
		fKeyboard.receiveChangeMode(mode);
		fFullKeyboard.receiveChangeMode(mode);
		fStatus.receiveChangeMode(mode);
	}

	public void keyPressed(int which) {
		fChord.receiveKeyPressed(which);
	}

	public void shiftRoot(int db) {
		fChord.receiveShiftRoot(db);
	}

	public void receiveEstimatedChord(String name, int notes[], int bass, boolean mute) {
		fKeyboard.receiveEstimatedChord(name, notes, bass, mute);
	}

	public void play() {
		fChord.receivePlay();
	}

	public void bassChanged(int note) {
		fChord.receiveBassChanged(note);
	}

	public void selectRow(int which) {
		fChord.receiveSelectRow(which);
	}

	public void selectTension(int which) {
		fChord.receiveSelectTension(which);
	}

	public void shiftRow(int vx, int vy) {
		fChord.receiveShiftRow(vx, vy);
	}

	public void changeTranspose(int t) {
		fFullKeyboard.receiveAllNotesOff();
		Chord.changeTranspose(t);
		fFullKeyboard.receiveChangeTranspose(t);
		fKeyboard.receiveChangeTranspose(t);
	}

	public void shiftTranspose(int dt) {
		int t = Chord.transpose + dt;
		if (t < -36 || t > 36)
			return;
		changeTranspose(t);
	}

	public void changePianoBasement(int t) {
		fFullKeyboard.receiveAllNotesOff();
		fFullKeyboard.receiveChangePianoBasement(t);
		Chord.pianoBasement = t;
	}

	public void changeGuitarBasement(int t) {
		fFullKeyboard.receiveAllNotesOff();
		fFullKeyboard.receiveChangeGuitarBasement(t);
		Chord.guitarBasement = t;
	}

	public void changeVelocity(int v) {
		Chord.velocity = v;
		fOption.receiveChangeVelocity(v);
	}

	public void shiftVelocity(int v) {
		Chord.velocity = (Chord.velocity + v) / v * v;
		if (Chord.velocity < 0)
			Chord.velocity = 0;
		if (Chord.velocity >= 128)
			Chord.velocity = 127;
		fOption.receiveChangeVelocity(Chord.velocity);
	}

	public void changeInstrument(int inst) {
		MIDI.send(MIDI.messageProgramChange(inst));
		fFullKeyboard.receiveChangeInstrument(inst);
	}

	public void showHistoryPanel() {
		fHistory.setVisible(true);
		fHistory.receiveShowHistoryPanel();
		fOption.setVisible(false);
	}

	public void showOptionPanel() {
		fHistory.setVisible(false);
		fOption.setVisible(true);
	}

	public void pushChord(int d, int b, int t, int bass) {
		fHistory.pushChord(d, b, t, bass);
	}

	public void updateChordNames() {
		fHistory.updateChordNames();
	}

	public void changeScale(int t, int m) {
		Chord.tonic = t;
		Chord.minor = m;
		fScale.receiveChangeScale(t, m);
		fHistory.updateChordNames();
		MIDI.send(MIDI.messageKeySignature(Chord.numberOfSharps(t, m), m));
	}

	public void shiftScale(int delta) {
		int t = (Chord.tonic + delta + 36) % 12;
		changeScale(t, Chord.minor);
	}

	public void receivePlayingChord(int basic, int tension, int root, int bass) {
		Chord.basic = basic;
		Chord.tension = tension;
		Chord.root = root;
		Chord.bass = bass;
	}

	public void changeHarmonicMinor(boolean hm) {
		Chord.harmonicMinor = hm;
		fOption.receiveChangeHarmonicMinor(hm);
		fChord.receiveChangeHarmonicMinor(hm);
	}

	public void changeOmitTriad(boolean ot) {
		Chord.omitTriad = ot;
		fOption.receiveChangeOmitTriad(ot);
		fChord.receiveChangeOmitTriad(ot);
	}

	public void shiftBass(int delta) {
		fChord.receiveShiftBass(delta);
	}

	public void pitchBend(int val) {
		MIDI.send(MIDI.messagePitchBend(val));
	}

	public void reselectMidiDevice() {
		MIDI.close();
		this.setVisible(false);
		MIDI.selectMidiDevice();
		if (MIDI.receiver == null)
			System.exit(0);
		this.setVisible(true);
	}

}
