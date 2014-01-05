package LoopMaker;

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

import chordplus.Chord;
import chordplus.HistoryPanel;
import chordplus.chordplus;

public class LoopMaker extends JFrame {
	int il,it;
	
	HistoryPanel superview;
	chordplus rootview;
	
	JPanel fTemplate;
	JLabel lTemplate,lTime;
	JComboBox cTemplateKind,cTemplate;
	
	ChordPanel fChord;
	
	JPanel fExport;
	JButton bPlay,bExport;
	
	public LoopMaker(HistoryPanel cp,chordplus gp){
		super();
		
		superview=cp;
		rootview=gp;
		
		setTitle("ループ作成");
		pack();
		Insets insets = this.getInsets();
		il=insets.left;
		it=insets.top;
		setSize(560+il,176+it);
		setLocation(48,64);
		setResizable(false);
		setLayout(null);
		
		fTemplate = new JPanel();
		fTemplate.setBounds(8,8,544,40);
		fTemplate.setLayout(null);
		fTemplate.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		lTemplate = new JLabel("テンプレート:",JLabel.RIGHT);
		lTemplate.setBounds(8,8,96,24);
		fTemplate.add(lTemplate);
		cTemplateKind = new JComboBox(Loop.loopKinds);
		cTemplateKind.setBounds(112,8,96,24);
		fTemplate.add(cTemplateKind);
		cTemplate = new JComboBox(Loop.pianoTemplates);
		cTemplate.setBounds(208,8,196,24);
		fTemplate.add(cTemplate);
		lTime = new JLabel("8/8 拍子");
		lTime.setBounds(412,8,128,24);
		fTemplate.add(lTime);
		add(fTemplate);
		
		fChord = new ChordPanel(this);
		fChord.setBounds(8,56,544,64);
		add(fChord);
		
		fExport = new JPanel();
		fExport.setBounds(8,128,544,40);
		fExport.setLayout(null);
		fExport.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		bPlay=new JButton("再生");
		bPlay.setBounds(8,8,96,24);
		fExport.add(bPlay);
		bExport=new JButton("書き出し");
		bExport.setBounds(440,8,96,24);
		fExport.add(bExport);
		add(fExport);
	}
	
	public void receiveChords(int n,int ds[],int bs[],int ts[],int bass[]){
		fChord.receiveChords(n,ds,bs,ts,bass);
	}
}
