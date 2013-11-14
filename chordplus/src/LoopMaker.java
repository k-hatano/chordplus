import java.awt.Color;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;


public class LoopMaker extends JFrame {
	int il,it;
	
	HistoryPanel parent;
	chordplus grandparent;
	
	JPanel fTemplate;
	JLabel lTemplate;
	JComboBox cTemplateKind,cTemplate;
	
	JPanel fLoop;
	JLabel lChords[];
	JSeparator sHorizon;
	JSeparator sVertic[];
	
	JPanel fExport;
	JButton bExport;
	
	public LoopMaker(HistoryPanel cp,chordplus gp){
		super();
		
		parent=cp;
		grandparent=gp;
		
		setTitle("ループ作成");
		pack();
		Insets insets = this.getInsets();
		il=insets.left;
		it=insets.top;
		setSize(544+il,176+it);
		setLocation(48,64);
		setResizable(false);
		setLayout(null);
		
		fTemplate = new JPanel();
		fTemplate.setBounds(8,8,528,40);
		fTemplate.setLayout(null);
		fTemplate.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		lTemplate = new JLabel("テンプレート:",JLabel.RIGHT);
		lTemplate.setBounds(8,8,96,24);
		fTemplate.add(lTemplate);
		cTemplateKind = new JComboBox(Loop.loopKinds);
		cTemplateKind.setBounds(112,8,96,24);
		fTemplate.add(cTemplateKind);
		cTemplate = new JComboBox(Loop.pianoTemplates);
		cTemplate.setBounds(216,8,196,24);
		fTemplate.add(cTemplate);
		add(fTemplate);
		
		fLoop = new JPanel();
		fLoop.setBounds(8,56,528,64);
		fLoop.setLayout(null);
		fLoop.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		lChords=new JLabel[12];
		for(int i=0;i<12;i++){
			lChords[i]= new JLabel(""+i);
			lChords[i].setBounds(8+32*i,24,32,16);
			lChords[i].setBackground(i%2==0?Color.lightGray:Color.gray);
			lChords[i].setOpaque(true);
			fLoop.add(lChords[i]);
		}
		sHorizon=new JSeparator(SwingConstants.HORIZONTAL);
		sHorizon.setBounds(8,32,512,1);
		fLoop.add(sHorizon);
		sVertic=new JSeparator[9];
		for(int i=0;i<9;i++){
			sVertic[i]=new JSeparator(SwingConstants.VERTICAL);
			sVertic[i].setBounds(8+64*i,16,1,32);
			fLoop.add(sVertic[i]);
		}
		add(fLoop);
		
		fExport = new JPanel();
		fExport.setBounds(8,128,528,40);
		fExport.setLayout(null);
		fExport.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		bExport=new JButton("書き出し");
		bExport.setBounds(216,8,96,24);
		fExport.add(bExport);
		add(fExport);
	}
	
	public void receiveChords(int ds[],int bs[],int ts[],int bass[]){
		for(int i=0;i<12;i++){
			if(ds[i]<0) continue;
			lChords[i].setText(Chord.chordName((ds[i]+Chord.tonic+36)%12,bs[i],ts[i],(bass[i]+Chord.tonic+36)%12,0));
		}
	}
}
