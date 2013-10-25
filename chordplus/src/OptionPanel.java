import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class OptionPanel extends JPanel implements ChangeListener,ActionListener {
	chordplus parent;
	JButton bHistory;
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
		
		cHarmonicMinor = new JCheckBox("和声的短音階");
		cHarmonicMinor.setBounds(4,50,115,24);
		cHarmonicMinor.addActionListener(this);
		add(cHarmonicMinor);
		
		cOmitTriad = new JCheckBox("三和音を無視");
		cOmitTriad.setBounds(4,70,115,24);
		cOmitTriad.addActionListener(this);
		add(cOmitTriad);
		
		bHistory = new JButton("コード再生履歴");
		bHistory.setBounds(4,108,123,24);
		bHistory.addActionListener(this);
		add(bHistory);
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
		sVelocitySlider.setValue(v/2);
		lVelocity.setText(""+v);
	}

	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==cOmitTriad){
			Chord.omitTriad=cOmitTriad.isSelected();
			parent.setOmitTriad(cOmitTriad.isSelected());
		}else if(arg0.getSource()==bHistory){
			parent.receiveShowHistoryPanel();
		}else if(arg0.getSource()==cHarmonicMinor){
			Chord.harmonicMinor=cHarmonicMinor.isSelected();
		}
	}
}
