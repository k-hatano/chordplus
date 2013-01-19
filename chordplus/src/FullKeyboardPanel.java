import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

public class FullKeyboardPanel extends JPanel implements ActionListener {
	JLabel lInstLabel;
	JComboBox cInst;
	FullKeyboardCanvas cKeyboard;
	JRadioButton rSingle,rPiano,rGuitar;
	ButtonGroup gHowToPlay;
	chordplus parent;
	
	int transpose=0,pianoBasement=0,guitarBasement=0;
	
	
	public FullKeyboardPanel(chordplus cp){
		super();
		setLayout(null);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		
		parent=cp;
		
		cKeyboard = new FullKeyboardCanvas(this);
		cKeyboard.setBounds(12,32,601,36);
		add(cKeyboard);
		
		gHowToPlay = new ButtonGroup();
		rSingle = new JRadioButton("単音",true);
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
		lInstLabel.setBounds(393,70,32,22);
		add(lInstLabel);
		
		cInst = new JComboBox(Chord.instrumentNameList());
		cInst.setBounds(425,70,192,22);
		cInst.addActionListener(this);
		add(cInst);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object target=arg0.getSource();
		if(target==rSingle){
			cKeyboard.changeMode(0);
			parent.receiveChangeMode(0,Chord.transpose());
		}
		if(target==rPiano){
			if(Chord.mode==1){
				changeInstrument(0);
			}else{
				cKeyboard.changeMode(1);
				parent.receiveChangeMode(1,transpose);
			}
		}
		if(target==rGuitar){
			if(Chord.mode==2){
				changeInstrument(24);
			}else{
				cKeyboard.changeMode(2);
				parent.receiveChangeMode(2,transpose);
			}
		}
		if(target==cInst){
			parent.changeInstrument(cInst.getSelectedIndex());
		}
	}
	
	void receiveNoteOn(int note,boolean onOrOff){
		cKeyboard.receiveNoteOn(note,onOrOff);
	}
	
	void receiveAllNotesOff(){
		int i=0;
		for(i=0;i<120;i++){
			if(cKeyboard.isNotePlaying(i)>0){
				parent.receiveNoteOn(i,false);
			}
		}
	}
	
	public void receiveChangeTranspose(int t){
		cKeyboard.receiveChangeTranspose(t);
	}
	
	public void receiveChangePianoBasement(int t){
		cKeyboard.receiveChangePianoBasement(t);
	}
	
	public void receiveChangeGuitarBasement(int t){
		cKeyboard.receiveChangeGuitarBasement(t);
	}
	
	public void changeTranspose(int t){
		parent.changeTranspose(t);
	}

	public void changePianoBasement(int t){
		parent.changePianoBasement(t);
	}

	public void changeGuitarBasement(int t){
		parent.changeGuitarBasement(t);
	}
	
	public void changeInstrument(int i){
		cInst.setSelectedIndex(i);
		parent.changeInstrument(i);
	}
}
