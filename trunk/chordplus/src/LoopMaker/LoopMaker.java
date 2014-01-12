package LoopMaker;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileOutputStream;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import chordplus.Chord;
import chordplus.HistoryPanel;
import chordplus.chordplus;

public class LoopMaker extends JFrame implements ActionListener,WindowListener {
	int il,it;
	
	HistoryPanel superview;
	chordplus rootview;
	
	JPanel fTemplate;
	JLabel lTemplate,lTime;
	JComboBox cTemplateKind,cTemplate;
	
	ChordPanel fChord;
	
	JPanel fExport;
	JButton bPlay,bExport;
	
	boolean playing=false;
	
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
		lTime = new JLabel("");
		lTime.setBounds(412,8,128,24);
		fTemplate.add(lTime);
		add(fTemplate);
		
		fChord = new ChordPanel(this,rootview);
		fChord.setBounds(8,56,544,64);
		add(fChord);
		
		fExport = new JPanel();
		fExport.setBounds(8,128,544,40);
		fExport.setLayout(null);
		fExport.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		bPlay=new JButton("再生");
		bPlay.setBounds(8,8,96,24);
		bPlay.addActionListener(this);
		fExport.add(bPlay);
		bExport=new JButton("書き出し");
		bExport.setBounds(440,8,96,24);
		fExport.add(bExport);
		bExport.addActionListener(this);
		add(fExport);
		
		addWindowListener(this);
		
		updateTime();
	}
	
	public void receiveChords(int n,int ds[],int bs[],int ts[],int bass[],int md){
		if(md>0) cTemplateKind.setSelectedIndex(md-1);
		fChord.receiveChords(n,ds,bs,ts,bass);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==bPlay){
			if(playing){
				stopPlaying();
			}else{
				startPlaying();
			}
		}else if(e.getSource()==bExport){
			FileDialog dialog = new FileDialog(this,"MIDI ファイルの保存先を指定してください。",FileDialog.SAVE);
			dialog.setFile("伸ばし.mid");
			dialog.setVisible(true);
			String fileName=dialog.getDirectory()+dialog.getFile();
			dialog.dispose();
			if(dialog.getFile()!=null){
				FileOutputStream output;
				try{
					output=new FileOutputStream(fileName);
					Sequence seq=Loop.sequenceOfLoop(cTemplateKind.getSelectedIndex()+1,0,fChord.chords,fChord.basics,fChord.tensions,fChord.roots,fChord.basses,fChord.lengths,fChord.beats,fChord.bar);
					MidiSystem.write(seq, 0, output);
					output.close();
				}catch (Exception exc){
					JOptionPane.showMessageDialog(null,"MIDI ファイルの保存に失敗しました。");
					exc.printStackTrace();
				}
			}
		}
	}
	
	public void stopPlaying(){
		bPlay.setLabel("再生");
		fChord.stopPlaying();
		playing=false;
		cTemplateKind.setEnabled(true);
		cTemplate.setEnabled(true);
		bExport.setEnabled(true);
	}
	
	public void startPlaying(){
		bPlay.setLabel("停止");
		fChord.startPlaying();
		playing=true;
		cTemplateKind.setEnabled(false);
		cTemplate.setEnabled(false);
		bExport.setEnabled(false);
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		stopPlaying();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void updateTime(){
		lTime.setText(""+fChord.beats+"/"+fChord.bar+" 拍子");
	}
}
