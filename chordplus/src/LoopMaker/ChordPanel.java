package LoopMaker;

import java.awt.Color;
import java.awt.Font;
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
	
	int chords;
	int bar=8;
	int lengths[]=new int[12];
	int pressed=-1,pressedX,pressedLength,pressedButton;
	
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
			lChords[i].addMouseListener(this);
			lChords[i].addMouseMotionListener(this);
			add(lChords[i]);
		}
		
		lLength=new JLabel("");
		lLength.setVisible(false);
		add(lLength);
		
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

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(pressed>=0){
			int x=arg0.getX();
			if(pressedButton==3){
				for(int i=pressed;i<chords;i++){
					lengths[i]=pressedLength+(x-pressedX)/bar;
					if(lengths[i]<=0) lengths[i]=1;
				}
			}else{
				lengths[pressed]=pressedLength+(x-pressedX)/bar;
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
	
}
