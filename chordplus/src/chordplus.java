import javax.sound.midi.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class chordplus extends JFrame {
	Receiver receiver=null;
	int il,it;
	
	StatusPanel fStatus;
	KeyboardPanel fKeyboard;
	ChordPanel fChord;
	OptionPanel fOption;
	FullKeyboardPanel fFullKeyboard;
	
	public chordplus(){
		super();
		
		setTitle("chordplus");
		pack();
		Insets insets = this.getInsets();
		il=insets.left;
		it=insets.top;
		setSize(640+il,471+it);
		setLocation(32,48);
		setResizable(false);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		setLayout(null);
		
		fStatus=new StatusPanel(this);
		fStatus.setBounds(8,8,624,32);
		add(fStatus);
		
		ScalePanel fScale=new ScalePanel(this);
		fScale.setBounds(8,48,92,308);
		add(fScale);
		
		fKeyboard=new KeyboardPanel(this);
		fKeyboard.setBounds(108,48,385,155);
		add(fKeyboard);
		
		fChord=new ChordPanel(this);
		fChord.setBounds(108,211,385,145);
		add(fChord);
		
		fOption=new OptionPanel(this);
		fOption.setBounds(501,48,131,308);
		add(fOption);
		
		fFullKeyboard=new FullKeyboardPanel(this);
		fFullKeyboard.setBounds(8,364,624,99);
		add(fFullKeyboard);
		
		try{
			receiver = MidiSystem.getReceiver();
		}catch (MidiUnavailableException e1){
			JOptionPane.showMessageDialog(null,"MIDI が使用できません。chordplus を終了します。");
			System.exit(1);
		}
		
		Runtime.getRuntime().addShutdownHook(new Shutdown());
	}
	public static void main(String[] args) {
		chordplus mainWindow = new chordplus();
		mainWindow.show();
	}
	
	class Shutdown extends Thread{
	    public void run(){
	    	if(receiver!=null) receiver.close();
	    }
	}
	
	void receiveNoteOn(int note,boolean onOrOff){
		ShortMessage message = new ShortMessage();
		try {
			if(onOrOff){
				message.setMessage(ShortMessage.NOTE_ON,note, Chord.velocity);
			}else{
				message.setMessage(ShortMessage.NOTE_OFF,note, Chord.velocity);
			}
			fFullKeyboard.receiveNoteOn(note,onOrOff);
		} catch (InvalidMidiDataException e) {
			// TODO 再生に失敗しました！
		}
		receiver.send(message,-1);
	}
	
	void receiveAllNotesOff(){
		fFullKeyboard.receiveAllNotesOff();
	}
	
	void receiveMidiMessage(MidiMessage message){
		
	}
	
	void receiveChangeMode(int mode,int transpose){
		Chord.mode=mode;
		fChord.receiveChangeMode(mode,transpose);
		fKeyboard.receiveChangeMode(mode,transpose);
	}
	
	void receiveKeyPressed(int which){
		fChord.receiveKeyPressed(which);
	}
	
	void receiveEstimatedChord(String name,int notes[]){
		fKeyboard.receiveEstimatedChord(name,notes);
		
	}
	
	void receivePlay(){
		fChord.receivePlay();
	}
	
	void receiveBassChanged(int note){
		fChord.receiveBassChanged(note);
	}
	
	void receiveSelectRow(int which){
		fChord.receiveSelectRow(which);
	}
	
	void receiveChangeScale(int t,int m){
		Chord.tonic=t;
		Chord.minor=m;
	}
	
	void changeTranspose(int t){
		fFullKeyboard.receiveAllNotesOff();
		fFullKeyboard.receiveChangeTranspose(t);
		fKeyboard.receiveChangeTranspose(t);
		Chord.changeTranspose(t);
	}
	
	void changePianoBasement(int t){
		fFullKeyboard.receiveAllNotesOff();
		fFullKeyboard.receiveChangePianoBasement(t);
		Chord.pianoBasement=t;
	}
	
	void changeGuitarBasement(int t){
		fFullKeyboard.receiveAllNotesOff();
		fFullKeyboard.receiveChangeGuitarBasement(t);
		Chord.guitarBasement=t;
	}
	
	void changeVelocity(int v){
		Chord.velocity=v;
		fOption.receiveChangeVelocity(v);
	}
	
	void changeInstrument(int inst){
		Chord.instrument=inst;
		ShortMessage message = new ShortMessage();
		try{
			message.setMessage(ShortMessage.PROGRAM_CHANGE ,inst, 0);
			receiver.send(message,-1);
		}catch (Exception e){
			// エラー処理
		}
	}
	
	void setOmitTriad(boolean ot){
		fChord.receiveOmitTriad(ot);
	}
}


