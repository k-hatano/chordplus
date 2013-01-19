import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class OptionPanel extends JPanel implements ChangeListener,ActionListener {
	chordplus parent;
	JLabel lVelocityLabel,lVelocity;
	JSlider sVelocitySlider;
	JCheckBox cOmitTriad,cHarmonicMinor,cTransposed;
	
	public OptionPanel(chordplus cp){
		super();
		
		parent=cp;
		
		setLayout(null);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		
		lVelocityLabel = new JLabel("ベロシティ:",JLabel.LEFT);
		lVelocityLabel.setBounds(12,8,75,16);
		add(lVelocityLabel);
		
		lVelocity = new JLabel(""+Chord.velocity,JLabel.RIGHT);
		lVelocity.setBounds(95,8,24,16);
		add(lVelocity);
		
		sVelocitySlider = new JSlider(0,64,32);
		sVelocitySlider.setBounds(8,24,115,24);
		sVelocitySlider.addChangeListener(this);
		add(sVelocitySlider);
		
		cOmitTriad = new JCheckBox("三和音を無視");
		cOmitTriad.setBounds(4,50,115,24);
		cOmitTriad.addActionListener(this);
		add(cOmitTriad);
	}

	public void stateChanged(ChangeEvent arg0) {
		if(arg0.getSource()==sVelocitySlider){
			int v;
			v=sVelocitySlider.getValue()*2;
			if(v>=128) v=127;
			parent.changeVelocity(v);
		}
	}
	
	public void receiveChangeVelocity(int v){
		lVelocity.setText(""+v);
	}

	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==cOmitTriad){
			Chord.omitTriad=cOmitTriad.isSelected();
			parent.setOmitTriad(cOmitTriad.isSelected());
		}
	}
}
