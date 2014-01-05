package chordplus;

import javax.sound.midi.*;
import javax.sound.midi.MidiDevice.Info;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class chordplus extends JFrame {
	MidiDevice device=null;
	Receiver receiver=null;
	int il,it;
	
	StatusPanel fStatus;
	KeyboardPanel fKeyboard;
	ChordPanel fChord;
	OptionPanel fOption;
	HistoryPanel fHistory;
	ScalePanel fScale;
	FullKeyboardPanel fFullKeyboard;
	
	public chordplus(){
		super();
		
		setTitle("chordplus");
		pack();
		Insets insets = this.getInsets();
		il=insets.left;
		it=insets.top;
		setSize(670+il,475+it);
		setLocation(32,48);
		setResizable(false);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		setLayout(null);
		
		fStatus=new StatusPanel(this);
		fStatus.setBounds(8,8,654,32);
		add(fStatus);
		
		fScale=new ScalePanel(this);
		fScale.setBounds(8,48,92,308);
		add(fScale);
		
		fKeyboard=new KeyboardPanel(this);
		fKeyboard.setBounds(108,48,385,155);
		add(fKeyboard);
		
		fChord=new ChordPanel(this);
		fChord.setBounds(108,211,385,145);
		add(fChord);
		
		fOption=new OptionPanel(this);
		fOption.setBounds(501,48,161,308);
		add(fOption);
		
		fHistory=new HistoryPanel(this);
		fHistory.setBounds(501,48,161,308);
		fHistory.setVisible(false);
		add(fHistory);
		
		fFullKeyboard=new FullKeyboardPanel(this);
		fFullKeyboard.setBounds(8,364,654,103);
		add(fFullKeyboard);
		
		/*
		try{
			receiver = MidiSystem.getReceiver();
		}catch (MidiUnavailableException e1){
			JOptionPane.showMessageDialog(null,"MIDI が使用できません。chordplus を終了します。");
			System.exit(1);
		}
		*/
		
		try{
			Info[] infos = MidiSystem.getMidiDeviceInfo();
			if(infos.length==0){
				JOptionPane.showMessageDialog(null,"MIDI が使用できません。chordplus を終了します。");
				System.exit(0);
			}else if(infos.length==1){
				receiver=MidiSystem.getMidiDevice(infos[0]).getReceiver();
			}else{
				String params[] = new String[infos.length];
				for(int i=0;i<infos.length;i++){
					params[i]=infos[i].getName();
				}
				String res=(String)JOptionPane.showInputDialog(this,
						"使用可能な MIDI デバイスが複数存在します。使用する MIDI デバイスを選択してください。",
						"chordplus",
						JOptionPane.INFORMATION_MESSAGE,
						null,
						params,
						params[0]);
				if(res==null) System.exit(1);
				for(int i=0;i<infos.length;i++){
					if(res.equals(infos[i].getName())){
						device=MidiSystem.getMidiDevice(infos[i]);
						device.open();
						receiver=device.getReceiver();
						break;
					}
				}
			}
		}catch(MidiUnavailableException e){
			JOptionPane.showMessageDialog(null,"MIDI が使用できません。chordplus を終了します。");
			e.printStackTrace();
			System.exit(0);
		}
		if(receiver==null) System.exit(1);
		
		
		Runtime.getRuntime().addShutdownHook(new Shutdown());
	}
	public static void main(String[] args) {
		chordplus mainWindow = new chordplus();
		mainWindow.show();
	}
	
	class Shutdown extends Thread{
	    public void run(){
	    	if(device!=null) device.close();
	    	if(receiver!=null) receiver.close();
	    }
	}
	
	void sendNoteOn(int note,boolean onOrOff){
		ShortMessage message = new ShortMessage();
		try {
			if(onOrOff){
				message.setMessage(ShortMessage.NOTE_ON,note, Chord.velocity);
			}else{
				message.setMessage(ShortMessage.NOTE_OFF,note, Chord.velocity);
			}
			fFullKeyboard.receiveNoteOn(note,onOrOff);
		} catch (InvalidMidiDataException e) {
			JOptionPane.showMessageDialog(null,"MIDI が使用できません。chordplus を終了します。");
			e.printStackTrace();
			System.exit(0);
		}
		receiver.send(message,-1);
	}
	
	void sendAllNotesOff(){
		fFullKeyboard.receiveAllNotesOff();
	}
	
	void sendMidiMessage(MidiMessage message){
		
	}
	
	void changeMode(int mode){
		Chord.mode=mode;
		fChord.receiveChangeMode(mode);
		fKeyboard.receiveChangeMode(mode);
		fFullKeyboard.receiveChangeMode(mode);
	}
	
	void keyPressed(int which){
		fChord.receiveKeyPressed(which);
	}
	
	void shiftRoot(int db){
		fChord.receiveShiftRoot(db);
	}
	
	void receiveEstimatedChord(String name,int notes[],int bass,boolean mute){
		fKeyboard.receiveEstimatedChord(name,notes,bass,mute);
	}
	
	void play(){
		fChord.receivePlay();
	}
	
	void bassChanged(int note){
		fChord.receiveBassChanged(note);
	}
	
	void selectRow(int which){
		fChord.receiveSelectRow(which);
	}
	
	void selectTension(int which){
		fChord.receiveSelectTension(which);
	}
	
	void shiftRow(int vx,int vy){
		fChord.receiveShiftRow(vx, vy);
	}
	
	void changeTranspose(int t){
		fFullKeyboard.receiveAllNotesOff();
		Chord.changeTranspose(t);
		fFullKeyboard.receiveChangeTranspose(t);
		fKeyboard.receiveChangeTranspose(t);
	}
	
	void shiftTranspose(int dt){
		int t=Chord.transpose+dt;
		if(t<-36||t>36) return;
		changeTranspose(t);
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
	
	void shiftVelocity(int v){
		Chord.velocity=(Chord.velocity+v)/v*v;
		if(Chord.velocity<0) Chord.velocity=0;
		if(Chord.velocity>=128) Chord.velocity=127;
		fOption.receiveChangeVelocity(Chord.velocity);
	}
	
	void changeInstrument(int inst){
		Chord.instrument=inst;
		ShortMessage message = new ShortMessage();
		try{
			message.setMessage(ShortMessage.PROGRAM_CHANGE ,inst, 0);
			receiver.send(message,-1);
		}catch (InvalidMidiDataException e){
			JOptionPane.showMessageDialog(null,"MIDI が使用できません。chordplus を終了します。");
			e.printStackTrace();
			System.exit(0);
		}
		fFullKeyboard.receiveChangeInstrument(inst);
	}
	
	void showHistoryPanel(){
		fHistory.setVisible(true);
		fHistory.receiveShowHistoryPanel();
		fOption.setVisible(false);
	}
	
	void showOptionPanel(){
		fHistory.setVisible(false);
		fOption.setVisible(true);
	}
	
	void pushChord(int d,int b,int t,int bass){
		fHistory.pushChord(d,b,t,bass);
	}
	
	void updateChordNames(){
		fHistory.updateChordNames();
	}
	
	void setPitchBend(int b){
		ShortMessage message = new ShortMessage();
		try{
			message.setMessage(ShortMessage.PITCH_BEND,0,64+b);
			receiver.send(message,-1);
		}catch (InvalidMidiDataException e){
			JOptionPane.showMessageDialog(null,"MIDI が使用できません。chordplus を終了します。");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	void changeScale(int t,int m){
		Chord.tonic=t;
		Chord.minor=m;
		fScale.receiveChangeScale(t, m);
		fHistory.updateChordNames();
	}
	
	void shiftScale(int delta){
		int t=(Chord.tonic+delta+36)%12;
		changeScale(t,Chord.minor);
	}
	
	void receivePlayingChord(int basic,int tension,int root,int bass){
		Chord.basic=basic;
		Chord.tension=tension;
		Chord.root=root;
		Chord.bass=bass;
	}
	
	void changeHarmonicMinor(boolean hm){
		Chord.harmonicMinor=hm;
		fOption.receiveChangeHarmonicMinor(hm);
		fChord.receiveChangeHarmonicMinor(hm);
	}
	
	void changeOmitTriad(boolean ot){
		Chord.omitTriad=ot;
		fOption.receiveChangeOmitTriad(ot);
		fChord.receiveChangeOmitTriad(ot);
	}
	
	void shiftBass(int delta){
		fChord.receiveShiftBass(delta);
	}
}


