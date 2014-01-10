package LoopMaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import chordplus.BarCanvas;
import chordplus.Chord;

public class ChordPanel extends JPanel implements MouseListener,MouseMotionListener {
	LoopMaker rootview;
	
	JLabel lChords[];
	JLabel lLength;
	BarCanvas cHorizon;
	BarCanvas cVertic[];
	BarCanvas cPlaying;
	Sequencer sequencer;
	
	int chords;
	int beats=4;
	int bar=4;
	int roots[];
	int basics[];
	int tensions[];
	int basses[];
	int lengths[]=new int[12];
	int pressed=-1,pressedX,pressedLength,pressedButton;
	int playingPosition=-1;
	int loopLength=-1;
	
	long startTime=-1;
	
	ChordPanel(LoopMaker rv){
		super();
		rootview=rv;

		setLayout(null);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		
		/*
		cHorizon=new BarCanvas(Color.lightGray);
		cHorizon.setBounds(16,32,512,1);
		add(cHorizon);
		*/
		
		cPlaying=new BarCanvas(Color.red);
		cPlaying.setBounds(16,12,2,40);
		cPlaying.setVisible(false);
		add(cPlaying);
		
		cVertic=new BarCanvas[9];
		for(int i=0;i<9;i++){
			cVertic[i]=new BarCanvas(Color.lightGray);
			cVertic[i].setBounds(16+64*i,16,1,32);
			add(cVertic[i]);
		}
		
		lChords=new JLabel[12];
		for(int i=0;i<12;i++){
			lChords[i]= new JLabel(""+i);
			lChords[i].setBounds(16+32*i,24,32,16);
			lChords[i].setBackground(i%2==0?Color.lightGray:Color.gray);
			lChords[i].setOpaque(true);
			lChords[i].addMouseListener(this);
			lChords[i].addMouseMotionListener(this);
			add(lChords[i]);
		}
		
		lLength=new JLabel("");
		lLength.setVisible(false);
		add(lLength);
		
		sequencer = new Sequencer();
		sequencer.start();
	}
	
	public void receiveChords(int n,int ds[],int bs[],int ts[],int bass[]){
		for(int i=0;i<12;i++){
			if(i<n){
				lChords[i].setText(Chord.chordName((ds[i]+Chord.tonic+36)%12,bs[i],ts[i],(bass[i]+Chord.tonic+36)%12,0));
				lChords[i].setVisible(true);
			}else{
				lChords[i].setText("");
				lChords[i].setVisible(false);
			}
		}
		for(int i=0;i<n;i++){
			lengths[i]=beats;
		}
		roots=new int[n];
		for(int i=0;i<n;i++){
			roots[i]=(ds[i]+Chord.tonic+36)%12;
		}
		basics=bs;
		tensions=ts;
		basses=bass;
		chords=n;
		updateLengths();
	}
	
	public void updateLengths(){
		int lastX=16;
		for(int i=0;i<chords;i++){
			lChords[i].setBounds(lastX,24,lengths[i]*64/beats,16);
			lastX+=lengths[i]*64/beats;
		}
		loopLength=0;
		for(int i=0;i<chords;i++){
			loopLength+=lengths[i];
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(pressed>=0){
			int x=arg0.getX();
			if(pressedButton==3){
				for(int i=pressed;i<chords;i++){
					lengths[i]=pressedLength+(x-pressedX)/beats;
					if(lengths[i]<=0) lengths[i]=1;
				}
			}else{
				lengths[pressed]=pressedLength+(x-pressedX)/beats;
				if(lengths[pressed]<=0) lengths[pressed]=1;
			}
			lLength.setText(""+lengths[pressed]);
			updateLengths();
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
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
		for(int i=0;i<chords;i++){
			if(arg0.getSource()==lChords[i]){
				pressed=i;
				break;
			}
		}
		if(pressed>=0){
			JLabel src=(JLabel)arg0.getSource();
			lLength.setText(""+lengths[pressed]);
			lLength.setForeground(Color.gray);
			lLength.setBounds(src.getBounds().x+2,8,32,16);
			lLength.setVisible(true);
			pressedLength=lengths[pressed];
			pressedX=arg0.getX();
			pressedButton=arg0.getButton();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		lLength.setVisible(false);
	}
	
	class Sequencer extends Thread{
		public void run(){
			while(true){
	    		try {
	    			sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    		if(playingPosition>=0){
	    			long time=System.currentTimeMillis()-startTime;
	    			long loopLengthTime=120/60*1000*loopLength/beats;
	    			time=time%loopLengthTime;
	    			playingPosition=(int)(time*64*60/120/1000);
	    			cPlaying.setLocation(16+playingPosition,12);
	    		}
			}
		}
	}
	
	public void startPlaying(){
		startTime=System.currentTimeMillis();
		playingPosition=0;
		cPlaying.setLocation(16,12);
		cPlaying.setVisible(true);
	}
	
	public void stopPlaying(){
		cPlaying.setVisible(false);
		startTime=-1;
		playingPosition=-1;
	}
	
}
