import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

public class ScalePanel extends JPanel implements ActionListener {
	JLabel lStatus;
	JRadioButton rMaj,rMin;
	JRadioButton[] aScale=new JRadioButton[12];
	String sScale[]={"C","C# / Db","D","D# / Eb","E","F","F# / Gb","G","G# / Ab","A","A# / Bb","B"};
	ButtonGroup gScale,gMajor;
	chordplus parent;
	
	public ScalePanel(chordplus cp){
		super();
		int i;
		parent=cp;
		Chord.changeScale(0,0);
		
		setLayout(null);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		gScale = new ButtonGroup();
		gMajor = new ButtonGroup();
		
		for(i=0;i<12;i++){
			aScale[i]=new JRadioButton(sScale[i]);
			aScale[i].setBounds(4,8+20*i,84,20);
			add(aScale[i]);
			aScale[i].addActionListener(this);
			gScale.add(aScale[i]);
		}
		aScale[0].setSelected(true);
		
		rMaj=new JRadioButton("Major",true);
		rMaj.setBounds(4,260,84,20);
		rMaj.addActionListener(this);
		add(rMaj);
		gMajor.add(rMaj);
		
		rMin=new JRadioButton("Minor");
		rMin.setBounds(4,280,84,20);
		rMin.addActionListener(this);
		add(rMin);
		gMajor.add(rMin);
	}
	
	public void receiveChangeScale(int tonic,int minor){
		for(int i=0;i<12;i++){
			aScale[i].setSelected(i==tonic);
		}
		rMaj.setSelected(minor==0);
		rMin.setSelected(minor==1);
	}

	public void actionPerformed(ActionEvent arg0) {
		int i;
		JRadioButton target=(JRadioButton)arg0.getSource();
		
		for(i=0;i<12;i++){
			if(target==aScale[i]){
				parent.changeScale(i,Chord.minor);
				return;
			}
		}
		
		if(target==rMaj) parent.changeScale(Chord.tonic,0);
		else if(target==rMin) parent.changeScale(Chord.tonic,1);
	}
}
