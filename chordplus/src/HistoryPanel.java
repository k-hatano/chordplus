import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;


public class HistoryPanel extends JPanel implements ActionListener {
	chordplus parent;
	JButton bLoopMaker,bCancel;
	JLabel lChords[];
	String chords[];
	
	public HistoryPanel(chordplus cp){
		super();
		
		parent=cp;
		
		chords=new String[12];
		for(int i=0;i<12;i++){
			chords[i]="";
		}
		
		setLayout(null);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		
		lChords=new JLabel[12];
		for(int i=0;i<12;i++){
			lChords[i]=new JLabel();
			lChords[i].setBounds(24,i*20+4,64,20);
			add(lChords[i]);
		}
		
		bLoopMaker = new JButton("ループメーカー");
		bLoopMaker.setBounds(4,248,123,24);
		bLoopMaker.addActionListener(this);
		add(bLoopMaker);
		
		bCancel = new JButton("キャンセル");
		bCancel.setBounds(4,276,123,24);
		bCancel.addActionListener(this);
		add(bCancel);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==bCancel){
			parent.receiveShowOptionPanel();
		}
	}
	
	public void pushChordName(String s){
		int i;
		for(i=0;i<11;i++){
			chords[i]=chords[i+1];
		}
		chords[11]=s;
		for(i=0;i<12;i++){
			lChords[i].setText(chords[i]);
		}
	}
	
}
