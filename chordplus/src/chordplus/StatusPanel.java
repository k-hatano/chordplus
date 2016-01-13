package chordplus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.*;

public class StatusPanel extends JPanel implements MouseListener,ActionListener {
	
	JLabel lMode;
	JButton bOption;
	JPopupMenu pPopup=null;
	JMenuItem miMidiOut;
	JMenuItem miReselectMidiOut;
	JRadioButton rSingle,rPiano,rGuitar;
	ButtonGroup gHowToPlay;
	
	chordplus rootview;
	
	public StatusPanel(chordplus cp){
		super();
		
		rootview=cp;
		
		setLayout(null);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		
		lMode=new JLabel("モード:",JLabel.LEFT);
		lMode.setBounds(10,8,48,24);
		add(lMode);
		
		bOption=new JButton("オプション");
		bOption.setBounds(526,8,112,24);
		bOption.addMouseListener(this);
		add(bOption);
		
		gHowToPlay = new ButtonGroup();
		rSingle = new JRadioButton("フリー",true);
		rSingle.setBounds(64,11,72,20);
		add(rSingle);
		rSingle.addActionListener(this);
		gHowToPlay.add(rSingle);
		rPiano = new JRadioButton("ピアノ",false);
		rPiano.setBounds(136,11,72,20);
		add(rPiano);
		rPiano.addActionListener(this);
		gHowToPlay.add(rPiano);
		rGuitar = new JRadioButton("ギター",false);
		rGuitar.setBounds(208,11,72,20);
		add(rGuitar);
		gHowToPlay.add(rGuitar);
		rGuitar.addActionListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getSource()==bOption){
			JPopupMenu pPopup=new JPopupMenu();
			miMidiOut=new JMenuItem("MIDI 出力: "+MIDI.device.getDeviceInfo().getName());
			miMidiOut.addActionListener(this);
			pPopup.add(miMidiOut);
			miReselectMidiOut=new JMenuItem("MIDI 出力先を変更する");
			miReselectMidiOut.addActionListener(this);
			pPopup.add(miReselectMidiOut);
			pPopup.show(bOption,0,bOption.getHeight());
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object target=arg0.getSource();
		if(arg0.getSource()==miMidiOut){
			String msg=MIDI.device.getDeviceInfo().getName()+" "+MIDI.device.getDeviceInfo().getVersion();
			msg+="\n製造元: "+MIDI.device.getDeviceInfo().getVendor();
			JOptionPane.showMessageDialog(null,msg);
		}else if(arg0.getSource()==miReselectMidiOut){
			rootview.reselectMidiDevice();
		}else if(target==rSingle){
			if(Chord.mode==0){
				rootview.changeTranspose(0);
			}
			rootview.changeMode(0);
		}
		if(target==rPiano){
			if(Chord.mode==1){
				rootview.changePianoBasement(0);
			}else{
				rootview.changeMode(1);
			}
			rootview.changeInstrument(0);
		}
		if(target==rGuitar){
			if(Chord.mode==2){
				rootview.changeGuitarBasement(0);
			}else{
				rootview.changeMode(2);
			}
			rootview.changeInstrument(24);
		}
	}
	
	public void receiveChangeMode(int mode){
		rSingle.setSelected(mode==0);
		rPiano.setSelected(mode==1);
		rGuitar.setSelected(mode==2);
	}
	
}
