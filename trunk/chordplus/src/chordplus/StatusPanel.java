package chordplus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.*;

public class StatusPanel extends JPanel implements MouseListener {
	
	JLabel lStatus;
	JButton bOption;
	JPopupMenu pPopup=null;
	
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
			JMenuItem miMidiOut=new JMenuItem("MIDI 出力: "+rootview.device.getDeviceInfo().getName());
			miMidiOut.setEnabled(false);
			pPopup.add(miMidiOut);
			JMenuItem miReselectMidiOut=new JMenuItem("MIDI 出力先を変更する");
			miReselectMidiOut.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					rootview.reselectMidiDevice();
				}
			});
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
	
}
