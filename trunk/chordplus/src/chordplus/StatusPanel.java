package chordplus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.*;

public class StatusPanel extends JPanel implements MouseListener,ActionListener {
	
	JLabel lStatus;
	JButton bOption;
	JPopupMenu pPopup=null;
	JMenuItem miMidiOut;
	JMenuItem miReselectMidiOut;
	
	chordplus rootview;
	
	public StatusPanel(chordplus cp){
		super();
		
		rootview=cp;
		
		setLayout(null);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		
		lStatus=new JLabel("chordplus",JLabel.LEFT);
		lStatus.setBounds(8,8,608,24);
		add(lStatus);
		
		bOption=new JButton("オプション");
		bOption.setBounds(550,8,96,24);
		bOption.addMouseListener(this);
		add(bOption);
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
		if(arg0.getSource()==miMidiOut){
			String msg=MIDI.device.getDeviceInfo().getName()+" "+MIDI.device.getDeviceInfo().getVersion();
			msg+="\n製造元: "+MIDI.device.getDeviceInfo().getVendor();
			JOptionPane.showMessageDialog(null,msg);
		}else if(arg0.getSource()==miReselectMidiOut){
			rootview.reselectMidiDevice();
		}
	}
	
}
