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
		
		lVelocityLabel = new JLabel("�x���V�e�B:",JLabel.LEFT);
		lVelocityLabel.setBounds(12,8,75,16);
		add(lVelocityLabel);
		
		lVelocity = new JLabel(""+Chord.velocity,JLabel.RIGHT);
		lVelocity.setBounds(99,8,46,16);
		add(lVelocity);
		
		sVelocitySlider = new JSlider(0,127,64);
		sVelocitySlider.setBounds(16,24,134,24);
		sVelocitySlider.addChangeListener(this);
		add(sVelocitySlider);
		
		cHarmonicMinor = new JCheckBox("�a���I�Z���K");
		cHarmonicMinor.setBounds(12,50,137,24);
		cHarmonicMinor.addActionListener(this);
		add(cHarmonicMinor);
		
		cOmitTriad = new JCheckBox("�O�a���𖳎�");
		cOmitTriad.setBounds(12,70,137,24);
		cOmitTriad.addActionListener(this);
		add(cOmitTriad);
		
		bHistory = new JButton("�R�[�h�Đ�����");
		bHistory.setBounds(12,108,137,24);
		bHistory.addActionListener(this);
		add(bHistory);
	}

	public void stateChanged(ChangeEvent arg0) {
		if(arg0.getSource()==sVelocitySlider){
			int v;
			v=sVelocitySlider.getValue();
			if(v>=128) v=127;
			parent.changeVelocity(v);
		}
	}
	
	public void receiveChangeVelocity(int v){
		sVelocitySlider.setValue(v);
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
