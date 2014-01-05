package LoopMaker;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import chordplus.BarCanvas;
import chordplus.Chord;

public class ChordPanel extends JPanel {
	LoopMaker rootview;
	
	JLabel lChords[];
	BarCanvas cHorizon;
	BarCanvas cVertic[];
	
	int chords;
	int bar=8;
	int lengths[]=new int[12];
	
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
			add(lChords[i]);
		}
		
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
			lengths[i]=bar;
		}
		chords=n;
		updateLengths();
	}
	
	public void updateLengths(){
		int lastX=16;
		for(int i=0;i<chords;i++){
			lChords[i].setBounds(lastX,24,lengths[i]*64/bar,16);
			lastX+=lengths[i]*64/bar;
		}
	}
	
}
