package chordplus;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FullKeyboardPanel extends JPanel implements ActionListener, ChangeListener {
	JLabel lTranspose,lInstLabel;
	JComboBox cInst;
	FullKeyboardCanvas cKeyboard;
	JRadioButton rSingle,rPiano,rGuitar;
	ButtonGroup gHowToPlay;
	chordplus rootview;
	JSpinner sTranspose;
	
	int transpose=0,pianoBasement=0,guitarBasement=0;
	
	
	public FullKeyboardPanel(chordplus cp){
		super();
		setLayout(null);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		
		rootview=cp;
		
		cKeyboard = new FullKeyboardCanvas(this,rootview);
		cKeyboard.setBounds(12,38,631,36);
		add(cKeyboard);
		
		lTranspose = new JLabel("トランスポーズ: ");
		lTranspose.setBounds(16,10,112,24);
		add(lTranspose);
		
		SpinnerNumberModel snm = new SpinnerNumberModel(0,-36,36,1);
		sTranspose = new JSpinner(snm);
		sTranspose.setBounds(128,10,64,22);
		sTranspose.addChangeListener(this);
		add(sTranspose);
		
		gHowToPlay = new ButtonGroup();
		rSingle = new JRadioButton("フリー",true);
		rSingle.setBounds(12,75,72,20);
		add(rSingle);
		rSingle.addActionListener(this);
		gHowToPlay.add(rSingle);
		rPiano = new JRadioButton("ピアノ",false);
		rPiano.setBounds(84,75,72,20);
		add(rPiano);
		rPiano.addActionListener(this);
		gHowToPlay.add(rPiano);
		rGuitar = new JRadioButton("ギター",false);
		rGuitar.setBounds(156,75,72,20);
		add(rGuitar);
		gHowToPlay.add(rGuitar);
		rGuitar.addActionListener(this);
		
		lInstLabel = new JLabel("音色:",JLabel.RIGHT);
		lInstLabel.setBounds(413,75,32,22);
		add(lInstLabel);
		
		cInst = new JComboBox(Chord.instrumentNameList());
		cInst.setBounds(451,75,192,22);
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
			rootview.changeMode(0);
		}
		if(target==rPiano){
			if(Chord.mode==1){
				rootview.changePianoBasement(0);
				changeInstrument(0);
			}else{
				rootview.changeMode(1);
			}
		}
		if(target==rGuitar){
			if(Chord.mode==2){
				rootview.changeGuitarBasement(0);
				changeInstrument(24);
			}else{
				rootview.changeMode(2);
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
				rootview.noteOn(i,false);
			}
		}
	}
	
	public void receiveChangeMode(int mode){
		cKeyboard.changeMode(mode);
		rSingle.setSelected(mode==0);
		rPiano.setSelected(mode==1);
		rGuitar.setSelected(mode==2);
	}
	
	public void receiveChangeTranspose(int t){
		cKeyboard.receiveChangeTranspose(t);
		sTranspose.setValue(t);
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
	
	public void receiveChangeInstrument(int i){
		cInst.setSelectedIndex(i);
	}
	
	@Override
	public void stateChanged(ChangeEvent arg0) {
		if(arg0.getSource()==sTranspose){
			rootview.changeTranspose((Integer)sTranspose.getValue());
		}
	}
}
