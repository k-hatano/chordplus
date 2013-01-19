import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;

public class KeyboardCanvas extends Canvas implements MouseListener,KeyListener,FocusListener {
	int pressed[]=new int[18];
	int lastPressed[]=new int[18];
	int togglePressed[]=new int[18];
	int lastClicked;
	int whiteKeys[]={0,2,4,5,7,9,11,12,14,16,17};
	int blackKeys[]={1,3,6,8,10,13,15};
	int keyRects[][]={
			{0,2,32,96},
			{20,0,24,64},
			{32,2,32,96},
			{52,0,24,64},
			{64,2,32,96},
			{96,2,32,96},
			{116,0,24,64},
			{128,2,32,96},
			{148,0,24,64},
			{160,2,32,96},
			{180,0,24,64},
			{192,2,32,96},
			{224,2,32,96},
			{244,0,24,64},
			{256,2,32,96},
			{276,0,24,64},
			{288,2,32,96},
			{320,2,32,96},
	};
	char smallKeys[]={'a','w','s','e','d','f','t','g','y','h','u','j','k','o','l','p',';',':'};
	char largeKeys[]={'A','W','S','E','D','F','T','G','Y','H','U','J','K','O','L','P','+','*'};
	char smallRowKeys[]={'z','x','c','v','b','n','m'};
	char largeRowKeys[]={'Z','X','C','V','B','N','M'};
	char smallNumberKeys[]={'1','2','3','4','5','6','7','8','9'};
	char largeNumberKeys[]={'!','\"','#','$','%','&','\'','(',')'};
	KeyboardPanel parent;
	KeyWatcher keyWatcher;
	boolean haveFocus,shiftPushed;
	int mode=0;
	int transpose=1;
	int chordNotes[]={0,0,0,0,0,0,0,0,0,0,0,0};
	int keysSpotted[]={0,0,0,0,0,0,0,0,0,0,0,0};
	
	public KeyboardCanvas(KeyboardPanel cp){
		super();
		int i;
		parent=cp;
		haveFocus=false;
		shiftPushed=false;
		for(i=0;i<18;i++) pressed[i]=0;
		for(i=0;i<18;i++) lastPressed[i]=0;
		for(i=0;i<18;i++) togglePressed[i]=0;
		lastClicked=-1;
		addKeyListener(this);
		addMouseListener(this);
		addFocusListener(this);
		keyWatcher = new KeyWatcher();
		keyWatcher.start();
	}
	public void paint(final Graphics g){
		int i;
		for(i=0;i<whiteKeys.length;i++){
			g.setColor(colorOfKey(lastPressed[whiteKeys[i]],false));
			g.fillRect(keyRects[whiteKeys[i]][0],
					keyRects[whiteKeys[i]][1],
					keyRects[whiteKeys[i]][2],
					keyRects[whiteKeys[i]][3]);
		}
		
		g.setColor(Color.black);
		for(i=0;i<whiteKeys.length;i++){
			g.drawRect(keyRects[whiteKeys[i]][0],
					keyRects[whiteKeys[i]][1],
					keyRects[whiteKeys[i]][2],
					keyRects[whiteKeys[i]][3]);
		}
		
		for(i=0;i<blackKeys.length;i++){
			g.setColor(colorOfKey(lastPressed[blackKeys[i]],true));
			g.fillRect(keyRects[blackKeys[i]][0],
					keyRects[blackKeys[i]][1],
					keyRects[blackKeys[i]][2],
					keyRects[blackKeys[i]][3]);
		}
		
		g.setColor(Color.black);
		for(i=0;i<blackKeys.length;i++){
			g.drawRect(keyRects[blackKeys[i]][0],
					keyRects[blackKeys[i]][1],
					keyRects[blackKeys[i]][2],
					keyRects[blackKeys[i]][3]);
		}
		
		g.setColor(Color.gray);
		for(i=0;i<12;i++){
			if(keysSpotted[i]>0){
				int sx=(keyRects[i][0]+keyRects[i][2]/2)-11;
				int sy=keyRects[i][1]+keyRects[i][3]-27;
				g.fillOval(sx, sy, 22, 22);
			}
		}
	}
	int whichKey(int x,int y){
		int i;
		if(y>=0&&y<=64){
			for(i=0;i<blackKeys.length;i++){
				if(x>=keyRects[blackKeys[i]][0]&&
						x<=keyRects[blackKeys[i]][0]+keyRects[blackKeys[i]][2])
					return blackKeys[i];
			}
		}
		if(y>=2&&y<=98){
			for(i=0;i<whiteKeys.length;i++){
				if(x>=keyRects[whiteKeys[i]][0]&&
						x<=keyRects[whiteKeys[i]][0]+keyRects[whiteKeys[i]][2])
					return whiteKeys[i];
			}
		}
		return -1;
	}
	Color colorOfKey(int which,boolean blackOrWhite){
		switch(which){
		case 0:
			return blackOrWhite?Color.black:Color.white;
		case 1:
			return Color.gray;
		case 2:
			return Color.lightGray;
		default:
			return Color.white;
		}
	}
	
	void repaintKey(int which){
		if(which<0) return;
		repaint(keyRects[which][0],keyRects[which][1],
				keyRects[which][2],keyRects[which][3]);
	}
	
	public void mouseClicked(MouseEvent arg0) {
		
	}
	
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mousePressed(MouseEvent arg0) {
		int which;
		
		if(lastClicked>-1) return;
		Point pt = arg0.getPoint();
		which=whichKey(pt.x,pt.y);
		if(which>-1&&pressed[which]==0){
			lastClicked=which;
			pressed[which]=1;
		}
	}
	
	public void mouseReleased(MouseEvent arg0) {
		if(lastClicked<0) return;
		pressed[lastClicked]=0;
		lastClicked=-1;
	}
	
	void playNote(int note,boolean playOrStop,boolean mute){
		pressed[note]=playOrStop?1:0;
		if(!mute) parent.receiveNoteOn(60+note+Chord.transpose(),playOrStop);
	}
	
	public void keyPressed(KeyEvent arg0) {
		int i;
		char key=arg0.getKeyChar();
		
		if(arg0.getKeyCode()==KeyEvent.VK_SHIFT) shiftPushed=true;
		
		if(key=='.'||key=='\b'||key=='0'){
			parent.receiveKeyPressed(-1);
			parent.receiveAllNotesOff();
		}
		
		if((key==' '||key=='\n')&&mode>0){
			parent.receivePlay();
		}
		
		for(i=0;i<smallKeys.length;i++){
			if((key==smallKeys[i]||key==largeKeys[i])&&lastClicked!=i){
				if(shiftPushed){
					parent.receiveBassChanged(i%12);
				}else{
					pressed[i]=1;
				}
				break;
			}
		}
		
		for(i=0;i<smallRowKeys.length;i++){
			if(key==smallRowKeys[i]||key==largeRowKeys[i]){
				parent.receiveSelectRow(i);
				break;
			}
		}
		
		if(mode>0){
			for(i=0;i<7;i++){
				int j=Chord.notesOfScale(Chord.tonic(),Chord.minor())[i];
				if((key==smallNumberKeys[i]||key==largeNumberKeys[i])&&lastClicked!=j){
					if(shiftPushed){
						parent.receiveBassChanged(j%12);
					}else{
						parent.receiveKeyPressed(j);
					}
					break;
				}
			}
		}
	}
	
	public void keyReleased(KeyEvent arg0) {
		int i;
		char key=arg0.getKeyChar();
		
		if(arg0.getKeyCode()==KeyEvent.VK_SHIFT) shiftPushed=false;
		
		for(i=0;i<smallKeys.length;i++){
			if((key==smallKeys[i]||key==largeKeys[i])&&
					lastClicked!=i){
				pressed[i]=0;
				break;
			}
		}
	}
	
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	class KeyWatcher extends Thread{
	    public void run(){
	    	int i;
	    	while(true){
	    		try {
					sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    		for(i=0;i<smallKeys.length;i++){
	    			if(pressed[i]==1&&lastPressed[i]==0){
	    				playNote(i,true,mode!=0);
	    				lastPressed[i]=1;
	    				parent.receiveKeyPressed(i);
	    				repaintKey(i);
	    			}
	    			if(pressed[i]==0&&lastPressed[i]==1){
	    				playNote(i,false,mode!=0);
	    				lastPressed[i]=0;
	    				repaintKey(i);
	    			}
	    		}
	    		for(i=0;i<12;i++){
	    			if(chordNotes[i]!=keysSpotted[i]){
	    				keysSpotted[i]=chordNotes[i];
	    				repaintKey(i);
	    			}
	    		}
	    	}
	    }
	}

	public void focusGained(FocusEvent arg0) {
		haveFocus=true;
		parent.receiveKeyboardFocused();
	}
	
	public void focusLost(FocusEvent arg0) {
		haveFocus=false;
		//parent.receiveKeyPressed(-1);
		//parent.receiveAllNotesOff();
		parent.receiveKeyboardBlured();
	}
	
	void receiveChangeMode(int md,int transpose){
		int i;
		mode=md;
		if(mode==0){
			for(i=0;i<18;i++) togglePressed[i]=0;
		}
	}
	
	void receiveEstimatedChordNotes(int notes[]){
		int i;
		for(i=0;i<12;i++) chordNotes[i]=0;
		for(i=0;i<notes.length;i++){
			chordNotes[notes[i]]=1;
		}
	}
}
