package chordplus;

import java.awt.*;
import java.awt.event.*;

public class FullKeyboardCanvas extends Canvas implements MouseListener,MouseMotionListener {
	int mode=0;
	int whiteNote[]={0,2,4,5,7,9,11},blackNote[]={1,3,-1,6,8,10,-1};
	int notesPlaying[]=new int[144];
	int clickedX,originTranspose;
	FullKeyboardPanel superview;
	chordplus rootview;
	
	public FullKeyboardCanvas(FullKeyboardPanel cp,chordplus gp){
		int i;
		superview=cp;
		rootview=gp;
		for(i=0;i<notesPlaying.length;i++) notesPlaying[i]=0;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	void receiveNoteOn(int note,boolean onOrOff){
		notesPlaying[note]=onOrOff?1:0;
		repaint();
	}
	
	int isNotePlaying(int note){
		return notesPlaying[note];
	}
	
	public void receiveChangeTranspose(int t){
		repaint();
	}
	
	public void receiveChangePianoBasement(int t){
		repaint();
	}
	
	public void receiveChangeGuitarBasement(int t){
		repaint();
	}
	
	public void paint(final Graphics g){
		int i;
		
		Image img=createImage(getSize().width,getSize().height);
		Graphics grp=img.getGraphics();
		
		if(mode==0){
			for(i=0;i<72;i++){
				int note=(i/7)*12+whiteNote[i%7];
				if(note>=Chord.transpose+60&&note<=Chord.transpose+60+17){
					grp.setColor(new Color(0.85f,0.85f,0.85f));
					grp.fillRect(i*10,0,10,30);
					grp.setColor(Color.black);
					grp.drawRect(i*10,0,10,30);
				}else{
					grp.setColor(Color.white);
					grp.fillRect(i*10,0,10,30);
					grp.setColor(Color.black);
					grp.drawRect(i*10,0,10,30);
				}
				if(notesPlaying[note]>0){
					grp.setColor(Color.gray);
					grp.fillRect(i*10+2,20,7,9);
				}
			}
			grp.setColor(Color.black);
			for(i=0;i<72;i++){
				int note=(i/7)*12+blackNote[i%7];
				if(blackNote[i%7]<0) continue; 
				if(note>=Chord.transpose+60&&note<=Chord.transpose+60+17){
					grp.setColor(new Color(0.15f,0.15f,0.15f));
					grp.fillRect(i*10+6,0,8,18);
					grp.setColor(Color.black);
					grp.drawRect(i*10+6,0,8,18);
				}else{
					grp.setColor(Color.black);
					grp.fillRect(i*10+6,0,8,18);
					grp.setColor(Color.black);
					grp.drawRect(i*10+6,0,8,18);
				}
				if(notesPlaying[note]>0){
					grp.setColor(Color.gray);
					grp.fillRect(i*10+7,9,7,9);
				}
			}
		}else if(mode==1){
			for(i=0;i<72;i++){
				int note=(i/7)*12+whiteNote[i%7];
				if(note>=Chord.pianoBasement+48&&note<Chord.pianoBasement+60){
					grp.setColor(new Color(0.7f,0.85f,1.0f));
					grp.fillRect(i*10,0,10,30);
					grp.setColor(Color.black);
					grp.drawRect(i*10,0,10,30);
				}else if(note>=Chord.pianoBasement+60&&note<Chord.pianoBasement+72){
					grp.setColor(new Color(1.0f,0.85f,0.7f));
					grp.fillRect(i*10,0,10,30);
					grp.setColor(Color.black);
					grp.drawRect(i*10,0,10,30);
				}else{
					grp.setColor(Color.white);
					grp.fillRect(i*10,0,10,30);
					grp.setColor(Color.black);
					grp.drawRect(i*10,0,10,30);
				}
				if(notesPlaying[note]>0){
					grp.setColor(Color.gray);
					grp.fillRect(i*10+2,20,7,9);
				}
			}
			grp.setColor(Color.black);
			for(i=0;i<72;i++){
				int note=(i/7)*12+blackNote[i%7];
				if(blackNote[i%7]<0) continue; 
				if(note>=Chord.pianoBasement+48&&note<Chord.pianoBasement+60){
					grp.setColor(new Color(0.0f,0.15f,0.3f));
					grp.fillRect(i*10+6,0,8,18);
					grp.setColor(Color.black);
					grp.drawRect(i*10+6,0,8,18);
				}else if(note>=Chord.pianoBasement+60&&note<Chord.pianoBasement+72){
					grp.setColor(new Color(0.3f,0.15f,0.0f));
					grp.fillRect(i*10+6,0,8,18);
					grp.setColor(Color.black);
					grp.drawRect(i*10+6,0,8,18);
				}else{
					grp.setColor(Color.black);
					grp.fillRect(i*10+6,0,8,18);
					grp.setColor(Color.black);
					grp.drawRect(i*10+6,0,8,18);
				}
				if(notesPlaying[note]>0){
					grp.setColor(Color.gray);
					grp.fillRect(i*10+7,9,7,9);
				}
			}
		}else if(mode==2){
			for(i=0;i<72;i++){
				int note=(i/7)*12+whiteNote[i%7];
				if(note>=Chord.guitarBasement+40&&note<Chord.guitarBasement+45){
					grp.setColor(new Color(1.0f,0.8f,0.5f));
					grp.fillRect(i*10,0,10,30);
					grp.setColor(Color.black);
					grp.drawRect(i*10,0,10,30);
				}else if(note>=Chord.guitarBasement+45&&note<Chord.guitarBasement+50){
					grp.setColor(new Color(1.0f,1.0f,0.5f));
					grp.fillRect(i*10,0,10,30);
					grp.setColor(Color.black);
					grp.drawRect(i*10,0,10,30);
				}else if(note>=Chord.guitarBasement+50&&note<Chord.guitarBasement+55){
					grp.setColor(new Color(1.0f,0.8f,0.5f));
					grp.fillRect(i*10,0,10,30);
					grp.setColor(Color.black);
					grp.drawRect(i*10,0,10,30);
				}else if(note>=Chord.guitarBasement+55&&note<Chord.guitarBasement+59){
					grp.setColor(new Color(1.0f,1.0f,0.5f));
					grp.fillRect(i*10,0,10,30);
					grp.setColor(Color.black);
					grp.drawRect(i*10,0,10,30);
				}else if(note>=Chord.guitarBasement+59&&note<Chord.guitarBasement+64){
					grp.setColor(new Color(1.0f,0.8f,0.5f));
					grp.fillRect(i*10,0,10,30);
					grp.setColor(Color.black);
					grp.drawRect(i*10,0,10,30);
				}else if(note>=Chord.guitarBasement+64&&note<Chord.guitarBasement+69){
					grp.setColor(new Color(1.0f,1.0f,0.5f));
					grp.fillRect(i*10,0,10,30);
					grp.setColor(Color.black);
					grp.drawRect(i*10,0,10,30);
				}else{
					grp.setColor(Color.white);
					grp.fillRect(i*10,0,10,30);
					grp.setColor(Color.black);
					grp.drawRect(i*10,0,10,30);
				}
				if(notesPlaying[note]>0){
					grp.setColor(Color.gray);
					grp.fillRect(i*10+2,20,7,9);
				}
			}
			grp.setColor(Color.black);
			for(i=0;i<72;i++){
				int note=(i/7)*12+blackNote[i%7];
				if(blackNote[i%7]<0) continue; 
				if(note>=Chord.guitarBasement+40&&note<Chord.guitarBasement+45){
					grp.setColor(new Color(0.2f,0.0f,0.0f));
					grp.fillRect(i*10+6,0,8,18);
					grp.setColor(Color.black);
					grp.drawRect(i*10+6,0,8,18);
				}else if(note>=Chord.guitarBasement+45&&note<Chord.guitarBasement+50){
					grp.setColor(new Color(0.2f,0.2f,0.0f));
					grp.fillRect(i*10+6,0,8,18);
					grp.setColor(Color.black);
					grp.drawRect(i*10+6,0,8,18);
				}else if(note>=Chord.guitarBasement+50&&note<Chord.guitarBasement+55){
					grp.setColor(new Color(0.2f,0.2f,0.0f));
					grp.fillRect(i*10+6,0,8,18);
					grp.setColor(Color.black);
					grp.drawRect(i*10+6,0,8,18);
				}else if(note>=Chord.guitarBasement+55&&note<Chord.guitarBasement+59){
					grp.setColor(new Color(0.2f,0.2f,0.0f));
					grp.fillRect(i*10+6,0,8,18);
					grp.setColor(Color.black);
					grp.drawRect(i*10+6,0,8,18);
				}else if(note>=Chord.guitarBasement+59&&note<Chord.guitarBasement+64){
					grp.setColor(new Color(0.2f,0.0f,0.0f));
					grp.fillRect(i*10+6,0,8,18);
					grp.setColor(Color.black);
					grp.drawRect(i*10+6,0,8,18);
				}else if(note>=Chord.guitarBasement+64&&note<Chord.guitarBasement+69){
					grp.setColor(new Color(0.2f,0.2f,0.0f));
					grp.fillRect(i*10+6,0,8,18);
					grp.setColor(Color.black);
					grp.drawRect(i*10+6,0,8,18);
				}else{
					grp.setColor(Color.black);
					grp.fillRect(i*10+6,0,8,18);
					grp.setColor(Color.black);
					grp.drawRect(i*10+6,0,8,18);
				}
				if(notesPlaying[note]>0){
					grp.setColor(Color.gray);
					grp.fillRect(i*10+7,9,7,9);
				}
			}
		}else{
			for(i=0;i<72;i++){
				grp.setColor(Color.white);
				grp.fillRect(i*10,0,10,30);
				grp.setColor(Color.black);
				grp.drawRect(i*10,0,10,30);
			}
			grp.setColor(Color.black);
			for(i=0;i<72;i++){
				switch(i%7){
				case 0:	case 1: case 3: case 4: case 5:
					grp.fillRect(i*10+6,0,8,18);
					grp.drawRect(i*10+6,0,8,18);
				}
			}
		}
		grp.setColor(Color.black);
		grp.fillRect(353,32,4,4);
		
		g.drawImage(img,0,0,this);
	}
	
	void changeMode(int m){
		mode=m;
		repaint();
	}

	public void mouseDragged(MouseEvent arg0) {
		int newTranspose;
		newTranspose=(arg0.getX()-clickedX)/6+originTranspose;
		if(newTranspose<-36||newTranspose>36) return;
		if(mode==0){
			if(newTranspose!=Chord.transpose){
				rootview.changeTranspose(newTranspose);
			}
		}
		if(mode==1){
			if(newTranspose!=Chord.pianoBasement){
				rootview.changePianoBasement(newTranspose);
			}
		}
		if(mode==2){
			if(newTranspose!=Chord.guitarBasement){
				rootview.changeGuitarBasement(newTranspose);
			}
		}
	}

	public void mouseMoved(MouseEvent arg0) {
		
	}

	public void mouseClicked(MouseEvent arg0) {
		
	}

	public void mouseEntered(MouseEvent arg0) {
		
	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {
		clickedX=arg0.getX();
		if(mode==0)	originTranspose=Chord.transpose;
		if(mode==1)	originTranspose=Chord.pianoBasement;
		if(mode==2)	originTranspose=Chord.guitarBasement;
	}

	public void mouseReleased(MouseEvent arg0) {
		
	}
	
}
