package chordplus;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

public class FullKeyboardPanel extends JPanel implements ActionListener {
	JLabel lMessage,lInstLabel;
	JComboBox cInst;
	FullKeyboardCanvas cKeyboard;
	JRadioButton rSingle,rPiano,rGuitar;
	ButtonGroup gHowToPlay;
	chordplus rootview;
	
	int transpose=0,pianoBasement=0,guitarBasement=0;
	
	
	public FullKeyboardPanel(chordplus cp){
		super();
		setLayout(null);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		
		rootview=cp;
		
		cKeyboard = new FullKeyboardCanvas(this,rootview);
		cKeyboard.setBounds(12,32,631,36);
		add(cKeyboard);
		
		lMessage = new JLabel("トランスポーズ: 0");
		lMessage.setBounds(16,8,601,20);
		add(lMessage);
		
		gHowToPlay = new ButtonGroup();
		rSingle = new JRadioButton("フリー",true);
		rSingle.setBounds(12,71,72,20);
		add(rSingle);
		rSingle.addActionListener(this);
		gHowToPlay.add(rSingle);
		rPiano = new JRadioButton("ピアノ",false);
		rPiano.setBounds(84,71,72,20);
		add(rPiano);
		rPiano.addActionListener(this);
		gHowToPlay.add(rPiano);
		rGuitar = new JRadioButton("ギター",false);
		rGuitar.setBounds(156,71,72,20);
		add(rGuitar);
		gHowToPlay.add(rGuitar);
		rGuitar.addActionListener(this);
		
		lInstLabel = new JLabel("音色:",JLabel.RIGHT);
		lInstLabel.setBounds(413,70,32,22);
		add(lInstLabel);
		
		cInst = new JComboBox(Chord.instrumentNameList());
		cInst.setBounds(451,70,192,22);
		cInst.addActionListener(this);
		add(cInst);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object target=arg0.getSource();
		if(target==rSingle){
			if(Chord.mode==0){
				rootview.changeTranspose(0);
			}
			cKeyboard.changeMode(0);
			rootview.changeMode(0,Chord.transpose());
		}
		if(target==rPiano){
			if(Chord.mode==1){
				rootview.changePianoBasement(0);
				changeInstrument(0);
			}else{
				cKeyboard.changeMode(1);
				rootview.changeMode(1,transpose);
			}
		}
		if(target==rGuitar){
			if(Chord.mode==2){
				rootview.changeGuitarBasement(0);
				changeInstrument(24);
			}else{
				cKeyboard.changeMode(2);
				rootview.changeMode(2,transpose);
			}
		}
		if(target==cInst){
			rootview.changeInstrument(cInst.getSelectedIndex());
		}
	}
	
	void receiveNoteOn(int note,boolean onOrOff){
		cKeyboard.receiveNoteOn(note,onOrOff);
	}
	
	void receiveAllNotesOff(){
		int i=0;
		for(i=0;i<120;i++){
			if(cKeyboard.isNotePlaying(i)>0){
				rootview.sendNoteOn(i,false);
			}
		}
	}
	
	public void receiveChangeTranspose(int t){
		cKeyboard.receiveChangeTranspose(t);
		updateMessage();
	}
	
	public void receiveChangePianoBasement(int t){
		cKeyboard.receiveChangePianoBasement(t);
	}
	
	public void receiveChangeGuitarBasement(int t){
		cKeyboard.receiveChangeGuitarBasement(t);
	}
	
	public void changeInstrument(int i){
		cInst.setSelectedIndex(i);
		rootview.changeInstrument(i);
	}
	
	public void updateMessage(){
		String message="";
		if(Chord.transpose==0){
			message+="トランスポーズ: 0";
		}else{
			if(Chord.transpose>0){
				message+="トランスポーズ: +"+Chord.transpose;
			}else{
				message+="トランスポーズ: "+Chord.transpose;
			}
			message+=" ("+Chord.nameOfNote(Chord.tonic,0)+" -> "+Chord.nameOfNote((Chord.tonic+Chord.transpose+36)%12,0)+")";
		}
		/*
		if(Chord.transpose>0){
			message="トランスポーズ: +"+Chord.transpose;
		}else{
			message="トランスポーズ: "+Chord.transpose;
		}
		*/
		lMessage.setText(message);
	}
}
