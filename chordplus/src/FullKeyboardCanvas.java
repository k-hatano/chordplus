import java.awt.*;
import java.awt.event.*;

public class FullKeyboardCanvas extends Canvas implements MouseListener,MouseMotionListener {
	int mode=0;
	int whiteNote[]={0,2,4,5,7,9,11},blackNote[]={1,3,-1,6,8,10,-1};
	int notesPlaying[]=new int[120];
	int clickedX,originTranspose;
	FullKeyboardPanel parent;
	
	public FullKeyboardCanvas(FullKeyboardPanel cp){
		int i;
		parent=cp;
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
		if(mode==0){
			for(i=0;i<60;i++){
				int note=(i/7)*12+whiteNote[i%7];
				if(note>=Chord.transpose+60&&note<=Chord.transpose+60+17){
					g.setColor(new Color(0.85f,0.85f,0.85f));
					g.fillRect(i*10,0,10,30);
					g.setColor(Color.black);
					g.drawRect(i*10,0,10,30);
				}else{
					g.setColor(Color.white);
					g.fillRect(i*10,0,10,30);
					g.setColor(Color.black);
					g.drawRect(i*10,0,10,30);
				}
				if(notesPlaying[note]>0){
					g.setColor(Color.gray);
					g.fillRect(i*10+2,20,7,9);
				}
			}
			g.setColor(Color.black);
			for(i=0;i<60;i++){
				int note=(i/7)*12+blackNote[i%7];
				if(blackNote[i%7]<0) continue; 
				if(note>=Chord.transpose+60&&note<=Chord.transpose+60+17){
					g.setColor(new Color(0.15f,0.15f,0.15f));
					g.fillRect(i*10+6,0,8,18);
					g.setColor(Color.black);
					g.drawRect(i*10+6,0,8,18);
				}else{
					g.setColor(Color.black);
					g.fillRect(i*10+6,0,8,18);
					g.setColor(Color.black);
					g.drawRect(i*10+6,0,8,18);
				}
				if(notesPlaying[note]>0){
					g.setColor(Color.gray);
					g.fillRect(i*10+7,9,7,9);
				}
			}
		}else if(mode==1){
			for(i=0;i<60;i++){
				int note=(i/7)*12+whiteNote[i%7];
				if(note>=Chord.pianoBasement+48&&note<Chord.pianoBasement+60){
					g.setColor(new Color(0.7f,0.85f,1.0f));
					g.fillRect(i*10,0,10,30);
					g.setColor(Color.black);
					g.drawRect(i*10,0,10,30);
				}else if(note>=Chord.pianoBasement+60&&note<Chord.pianoBasement+72){
					g.setColor(new Color(1.0f,0.85f,0.7f));
					g.fillRect(i*10,0,10,30);
					g.setColor(Color.black);
					g.drawRect(i*10,0,10,30);
				}else{
					g.setColor(Color.white);
					g.fillRect(i*10,0,10,30);
					g.setColor(Color.black);
					g.drawRect(i*10,0,10,30);
				}
				if(notesPlaying[note]>0){
					g.setColor(Color.gray);
					g.fillRect(i*10+2,20,7,9);
				}
			}
			g.setColor(Color.black);
			for(i=0;i<60;i++){
				int note=(i/7)*12+blackNote[i%7];
				if(blackNote[i%7]<0) continue; 
				if(note>=Chord.pianoBasement+48&&note<Chord.pianoBasement+60){
					g.setColor(new Color(0.0f,0.15f,0.3f));
					g.fillRect(i*10+6,0,8,18);
					g.setColor(Color.black);
					g.drawRect(i*10+6,0,8,18);
				}else if(note>=Chord.pianoBasement+60&&note<Chord.pianoBasement+72){
					g.setColor(new Color(0.3f,0.15f,0.0f));
					g.fillRect(i*10+6,0,8,18);
					g.setColor(Color.black);
					g.drawRect(i*10+6,0,8,18);
				}else{
					g.setColor(Color.black);
					g.fillRect(i*10+6,0,8,18);
					g.setColor(Color.black);
					g.drawRect(i*10+6,0,8,18);
				}
				if(notesPlaying[note]>0){
					g.setColor(Color.gray);
					g.fillRect(i*10+7,9,7,9);
				}
			}
		}else if(mode==2){
			for(i=0;i<60;i++){
				int note=(i/7)*12+whiteNote[i%7];
				if(note>=Chord.guitarBasement+40&&note<Chord.guitarBasement+45){
					g.setColor(new Color(1.0f,0.8f,0.5f));
					g.fillRect(i*10,0,10,30);
					g.setColor(Color.black);
					g.drawRect(i*10,0,10,30);
				}else if(note>=Chord.guitarBasement+45&&note<Chord.guitarBasement+50){
					g.setColor(new Color(1.0f,1.0f,0.5f));
					g.fillRect(i*10,0,10,30);
					g.setColor(Color.black);
					g.drawRect(i*10,0,10,30);
				}else if(note>=Chord.guitarBasement+50&&note<Chord.guitarBasement+55){
					g.setColor(new Color(1.0f,0.8f,0.5f));
					g.fillRect(i*10,0,10,30);
					g.setColor(Color.black);
					g.drawRect(i*10,0,10,30);
				}else if(note>=Chord.guitarBasement+55&&note<Chord.guitarBasement+59){
					g.setColor(new Color(1.0f,1.0f,0.5f));
					g.fillRect(i*10,0,10,30);
					g.setColor(Color.black);
					g.drawRect(i*10,0,10,30);
				}else if(note>=Chord.guitarBasement+59&&note<Chord.guitarBasement+64){
					g.setColor(new Color(1.0f,0.8f,0.5f));
					g.fillRect(i*10,0,10,30);
					g.setColor(Color.black);
					g.drawRect(i*10,0,10,30);
				}else if(note>=Chord.guitarBasement+64&&note<Chord.guitarBasement+69){
					g.setColor(new Color(1.0f,1.0f,0.5f));
					g.fillRect(i*10,0,10,30);
					g.setColor(Color.black);
					g.drawRect(i*10,0,10,30);
				}else{
					g.setColor(Color.white);
					g.fillRect(i*10,0,10,30);
					g.setColor(Color.black);
					g.drawRect(i*10,0,10,30);
				}
				if(notesPlaying[note]>0){
					g.setColor(Color.gray);
					g.fillRect(i*10+2,20,7,9);
				}
			}
			g.setColor(Color.black);
			for(i=0;i<60;i++){
				int note=(i/7)*12+blackNote[i%7];
				if(blackNote[i%7]<0) continue; 
				if(note>=Chord.guitarBasement+40&&note<Chord.guitarBasement+45){
					g.setColor(new Color(0.2f,0.0f,0.0f));
					g.fillRect(i*10+6,0,8,18);
					g.setColor(Color.black);
					g.drawRect(i*10+6,0,8,18);
				}else if(note>=Chord.guitarBasement+45&&note<Chord.guitarBasement+50){
					g.setColor(new Color(0.2f,0.2f,0.0f));
					g.fillRect(i*10+6,0,8,18);
					g.setColor(Color.black);
					g.drawRect(i*10+6,0,8,18);
				}else if(note>=Chord.guitarBasement+50&&note<Chord.guitarBasement+55){
					g.setColor(new Color(0.2f,0.2f,0.0f));
					g.fillRect(i*10+6,0,8,18);
					g.setColor(Color.black);
					g.drawRect(i*10+6,0,8,18);
				}else if(note>=Chord.guitarBasement+55&&note<Chord.guitarBasement+59){
					g.setColor(new Color(0.2f,0.2f,0.0f));
					g.fillRect(i*10+6,0,8,18);
					g.setColor(Color.black);
					g.drawRect(i*10+6,0,8,18);
				}else if(note>=Chord.guitarBasement+59&&note<Chord.guitarBasement+64){
					g.setColor(new Color(0.2f,0.0f,0.0f));
					g.fillRect(i*10+6,0,8,18);
					g.setColor(Color.black);
					g.drawRect(i*10+6,0,8,18);
				}else if(note>=Chord.guitarBasement+64&&note<Chord.guitarBasement+69){
					g.setColor(new Color(0.2f,0.2f,0.0f));
					g.fillRect(i*10+6,0,8,18);
					g.setColor(Color.black);
					g.drawRect(i*10+6,0,8,18);
				}else{
					g.setColor(Color.black);
					g.fillRect(i*10+6,0,8,18);
					g.setColor(Color.black);
					g.drawRect(i*10+6,0,8,18);
				}
				if(notesPlaying[note]>0){
					g.setColor(Color.gray);
					g.fillRect(i*10+7,9,7,9);
				}
			}
		}else{
			for(i=0;i<60;i++){
				g.setColor(Color.white);
				g.fillRect(i*10,0,10,30);
				g.setColor(Color.black);
				g.drawRect(i*10,0,10,30);
			}
			g.setColor(Color.black);
			for(i=0;i<60;i++){
				switch(i%7){
				case 0:	case 1: case 3: case 4: case 5:
					g.fillRect(i*10+6,0,8,18);
					g.drawRect(i*10+6,0,8,18);
				}
			}
		}
		g.setColor(Color.black);
		g.fillRect(353,32,4,4);
	}
	
	void changeMode(int m){
		mode=m;
		repaint();
	}

	public void mouseDragged(MouseEvent arg0) {
		int newTranspose;
		newTranspose=(arg0.getX()-clickedX)/6+originTranspose;
		if(mode==0){
			if(newTranspose!=Chord.transpose){
				parent.changeTranspose(newTranspose);
			}
		}
		if(mode==1){
			if(newTranspose!=Chord.pianoBasement){
				parent.changePianoBasement(newTranspose);
			}
		}
		if(mode==2){
			if(newTranspose!=Chord.guitarBasement){
				parent.changeGuitarBasement(newTranspose);
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
