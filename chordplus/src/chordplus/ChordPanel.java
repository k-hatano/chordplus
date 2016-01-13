package chordplus;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class ChordPanel extends JPanel {
	JLabel lScale,lDegree,lOn;
	JLabel[] aTriad=new JLabel[6];
	JLabel[] aSeventh=new JLabel[6];
	JLabel[] aMajorSeventh=new JLabel[6];
	JLabel[] aSixth=new JLabel[6];
	JLabel[] aAdd9=new JLabel[6];
	
	BarCanvas bcSeparator;
	
	chordplus rootview;
	int lastPressed;
	int root;
	int reality[][] = new int[6][5];
	int basic,tension;
	int bass;
	int row=-1;
	
	int lastRoot,lastBass,lastBasic,lastTension;
	int emptyArray[]={};
	
	boolean haveFocus;
	public ChordPanel(chordplus cp){
		super();
		int i;
		
		lastPressed=-1;
		
		rootview=cp;
		
		root=-1;
		bass=-1;
		basic=-1;
		tension=-1;
		
		lastRoot=-1;
		lastBass=-1;
		lastBasic=-1;
		lastTension=-1;
		
		setLayout(null);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		
		lScale = new JLabel("",JLabel.CENTER);
		lScale.setBounds(8,11,61,16);
		add(lScale);
		
		lDegree = new JLabel("",JLabel.CENTER);
		lDegree.setForeground(Color.lightGray);
		lDegree.setBounds(130,11,122,16);
		add(lDegree);
		
		lOn = new JLabel("",JLabel.CENTER);
		lOn.setBounds(313,11,61,16);
		add(lOn);
		
		bcSeparator = new BarCanvas(Color.lightGray);
		bcSeparator.setBounds(8,34,366,1);
		add(bcSeparator);
		
		for(i=0;i<6;i++){
			aTriad[i] = new JLabel(Chord.chordName(-1,i,0,-1,0),JLabel.CENTER);
			aTriad[i].setBounds(8+i*61,41,61,16);
			aTriad[i].setForeground(Color.gray);
			aTriad[i].setOpaque(true);
			add(aTriad[i]);
		}
		for(i=0;i<6;i++){
			aSeventh[i] = new JLabel(Chord.chordName(-1,i,1,-1,0),JLabel.CENTER);
			aSeventh[i].setBounds(8+i*61,60,61,16);
			aSeventh[i].setForeground(Color.gray);
			aSeventh[i].setOpaque(true);
			add(aSeventh[i]);
		}
		for(i=0;i<6;i++){
			aMajorSeventh[i] = new JLabel(Chord.chordName(-1,i,2,-1,0),JLabel.CENTER);
			aMajorSeventh[i].setBounds(8+i*61,80,61,16);
			aMajorSeventh[i].setForeground(Color.gray);
			aMajorSeventh[i].setOpaque(true);
			add(aMajorSeventh[i]);
		}
		for(i=0;i<6;i++){
			aSixth[i] = new JLabel(Chord.chordName(-1,i,3,-1,0),JLabel.CENTER);
			aSixth[i].setBounds(8+i*61,100,61,16);
			aSixth[i].setForeground(Color.gray);
			aSixth[i].setOpaque(true);
			add(aSixth[i]);
		}
		for(i=0;i<6;i++){
			aAdd9[i] = new JLabel(Chord.chordName(-1,i,4,-1,0),JLabel.CENTER);
			aAdd9[i].setBounds(8+i*61,120,61,16);
			aAdd9[i].setForeground(Color.gray);
			aAdd9[i].setOpaque(true);
			add(aAdd9[i]);
		}
		
		lScale.setVisible(false);
		lDegree.setVisible(false);
		lOn.setVisible(false);
		bcSeparator.setVisible(false);
		for(i=0;i<aTriad.length;i++){
			aTriad[i].setVisible(false);
			aSeventh[i].setVisible(false);
			aMajorSeventh[i].setVisible(false);
			aSixth[i].setVisible(false);
			aAdd9[i].setVisible(false);
		}
		
		resetReality();
	}
	void receiveChangeMode(int md){
		int i;
		Chord.mode=md;
		if(Chord.mode==0){
			lScale.setVisible(false);
			lDegree.setVisible(false);
			lOn.setVisible(false);
			bcSeparator.setVisible(false);
			for(i=0;i<aTriad.length;i++){
				aTriad[i].setVisible(false);
				aSeventh[i].setVisible(false);
				aMajorSeventh[i].setVisible(false);
				aSixth[i].setVisible(false);
				aAdd9[i].setVisible(false);
			}
		}else{
			lScale.setVisible(true);
			lDegree.setVisible(true);
			lOn.setVisible(true);
			bcSeparator.setVisible(true);
			for(i=0;i<aTriad.length;i++){
				aTriad[i].setVisible(!Chord.omitTriad);
				aSeventh[i].setVisible(true);
				aMajorSeventh[i].setVisible(true);
				aSixth[i].setVisible(true);
				aAdd9[i].setVisible(true);
			}
		}
	}
	void receiveKeyPressed(int which){
		if(Chord.mode==0) return;
		if(which<0){
			root=-1;
			bass=-1;
			resetReality();
			rootview.receiveEstimatedChord("",emptyArray,-1,true);
		}else{
			if(lastPressed==which){
				root=-1;
				bass=-1;
				resetReality();
			}
			bass=-1;
			estimate(which%12);
			selectMax();
			rootview.receiveEstimatedChord(chordName(),Chord.notesOfChordWithRoot(basic,tension,root),root,true);
		}
		reflectReality();
		lastPressed=which;
		row=-1;
		rootview.receivePlayingChord(basic, tension, root, bass);
	}
	void estimate(int note){
		int i,j,k,l,diatonic[],chord[];
		boolean flg,flg2;
		if(root==-1){
			root=note%12;
			diatonic=Chord.notesOfScale(Chord.tonic,Chord.minor);
			for(i=0;i<5;i++){
				for(j=0;j<6;j++){
					if(i==0&&Chord.omitTriad){
						reality[j][i]=0;
						continue;
					}
					chord=Chord.notesOfChordWithRoot(j,i,root);
					flg=true;
					for(k=1;k<chord.length;k++){
						flg2=false;
						for(l=0;l<diatonic.length;l++){
							if(diatonic[l]==chord[k]){
								flg2=true;
								break;
							}
						}
						if(flg2==false) flg=false;
					}
					if(flg){
						reality[j][i]++;
					}else{
						reality[j][i]=0;
					}
				}
			}
		}else{
			for(i=0;i<5;i++){
				for(j=0;j<6;j++){
					if(i==0&&Chord.omitTriad){
						reality[j][i]=0;
						continue;
					}
					chord=Chord.notesOfChordWithRoot(j,i,root);
					flg=false;
					for(k=0;k<chord.length;k++){
						if(note==chord[k]){
							flg=true;
							break;
						}
					}
					if(flg){
						reality[j][i]++;
					}else{
						reality[j][i]=0;
					}
				}
			}
		}
	}
	void selectMax(){
		int i,j,b=-1,t=-1,m=0;
		for(i=0;i<5;i++){
			for(j=0;j<6;j++){
				if(reality[j][i]>m){
					b=j;
					t=i;
					m=reality[j][i];
				}
			}
		}
		if(m>0) stressMax(b,t);
	}
	void stressMax(int b,int t){
		int j;
		tension=t;
		basic=b;
		for(j=0;j<6;j++){
			if(tension==0&&basic==j) aTriad[j].setBackground(Color.lightGray);
			else aTriad[j].setBackground(null);
		}
		for(j=0;j<6;j++){
			if(tension==1&&basic==j) aSeventh[j].setBackground(Color.lightGray);
			else aSeventh[j].setBackground(null);
		}
		for(j=0;j<6;j++){
			if(tension==2&&basic==j) aMajorSeventh[j].setBackground(Color.lightGray);
			else aMajorSeventh[j].setBackground(null);
		}
		for(j=0;j<6;j++){
			if(tension==3&&basic==j) aSixth[j].setBackground(Color.lightGray);
			else aSixth[j].setBackground(null);
		}
		for(j=0;j<6;j++){
			if(tension==4&&basic==j) aAdd9[j].setBackground(Color.lightGray);
			else aAdd9[j].setBackground(null);
		}
	}
	void resetReality(){
		int i,j;
		for(i=0;i<5;i++){
			for(j=0;j<6;j++) reality[j][i]=0;
		}
		for(j=0;j<6;j++){
			aTriad[j].setBackground(null);
			aSeventh[j].setBackground(null);
			aMajorSeventh[j].setBackground(null);
			aSixth[j].setBackground(null);
			aAdd9[j].setBackground(null);
		}
	}
	void reflectReality(){
		int j;
		lScale.setText(root<0?"":Chord.nameOfNote(root,0));
		if(root<0){
			lDegree.setText("");
		}else if(Chord.minor==0){
			lDegree.setText(Chord.sDegreeMajor[(root-Chord.tonic+36)%12]);
		}else if(Chord.harmonicMinor==false){
			lDegree.setText(Chord.sDegreeMinor[(root-Chord.tonic+36)%12]);
		}else{
			lDegree.setText(Chord.sDegreeHarmonicMinor[(root-Chord.tonic+36)%12]);
		}
		if(bass>=0&&bass!=root) lOn.setText("on "+Chord.nameOfNote(bass,0));
		else lOn.setText("");
		for(j=0;j<6;j++){
			aTriad[j].setForeground(reality[j][0]>0?Color.black:Color.gray);
		}
		for(j=0;j<6;j++){
			aSeventh[j].setForeground(reality[j][1]>0?Color.black:Color.gray);
		}
		for(j=0;j<6;j++){
			aMajorSeventh[j].setForeground(reality[j][2]>0?Color.black:Color.gray);
		}
		for(j=0;j<6;j++){
			aSixth[j].setForeground(reality[j][3]>0?Color.black:Color.gray);
		}
		for(j=0;j<6;j++){
			aAdd9[j].setForeground(reality[j][4]>0?Color.black:Color.gray);
		}
	}
	String chordName(){
		String r=Chord.chordName(root,basic,tension,bass,0);
		
		return r;
	}
	void receivePlay(){
		int chord[];
		int n,i;
		
		if(root<0){
			if(lastRoot<0) return;
			root=lastRoot;
			bass=lastBass;
			basic=lastBasic;
			tension=lastTension;
		}
		
		
		rootview.sendAllNotesOff();
		
		if(Chord.mode==1){
			chord=Chord.notesOfChordWithPianoBasement(basic,tension,root+(Chord.transpose()+36)%12,bass<0?bass:bass+(Chord.transpose()+36)%12,Chord.pianoBasement);
			for(i=0;i<chord.length;i++){
				n=chord[i];
				rootview.noteOn(n,true);
			}
		}else if(Chord.mode==2){
			chord=Chord.notesOfChordWithGuitarBasement(basic,tension,root+(Chord.transpose()+36)%12,bass<0?bass:bass+(Chord.transpose()+36)%12,Chord.guitarBasement+40);
			for(i=0;i<chord.length;i++){
				n=chord[i];
				rootview.noteOn(n,true);
			}
		}
		
		lastRoot=root;
		lastBass=bass;
		lastBasic=basic;
		lastTension=tension;
		
		rootview.receiveEstimatedChord(chordName(),Chord.notesOfChordWithRoot(basic,tension,root),bass<0?root:bass,false);
		rootview.pushChord((root-Chord.tonic+36)%12,basic,tension,bass>=0?(bass-Chord.tonic+36)%12:(root-Chord.tonic+36)%12);
		
		root=-1;
		bass=-1;
		resetReality();
		reflectReality();
	}
	void receiveBassChanged(int note){
		if(root<0&&lastRoot>=0){
			root=lastRoot;
			basic=lastBasic;
			tension=lastTension;
		}
		bass=note;
		reflectReality();
		rootview.receiveEstimatedChord(chordName(),Chord.notesOfChordWithRoot(basic,tension,root),bass<0?root:bass,true);
	}
	
	void receiveSelectRow(int which){
		int i,j;
		if(Chord.mode==0) return;
		if(root<0&&lastRoot>=0){
			root=lastRoot;
		}
		for(i=0;i<5;i++){
			for(j=0;j<6;j++) reality[j][i]=(j==which)?1:0;
		}
		if(row==which){
			do{
				tension++;
				if(tension>=5) tension=Chord.omitTriad?1:0;
			}while(Chord.notesOfChord(basic,tension).length<=1);
		}else{
			basic=which;
			tension=Chord.omitTriad?1:0;
		}
		row=which;
		reality[basic][tension]++;
		reflectReality();
		selectMax();
		rootview.receiveEstimatedChord(chordName(),Chord.notesOfChordWithRoot(basic,tension,root),bass<0?root:bass,true);
	}
	
	void receiveSelectTension(int which){
		int i,j;
		if(Chord.mode==0) return;
		if(root<0&&lastRoot>=0){
			root=lastRoot;
		}
		for(i=0;i<5;i++){
			for(j=0;j<6;j++) reality[j][i]=(i==which)?1:0;
		}
		tension=which;
		if(Chord.notesOfChord(basic,tension).length<=1) tension=Chord.omitTriad?1:0;
		reality[basic][tension]++;
		reflectReality();
		selectMax();
		rootview.receiveEstimatedChord(chordName(),Chord.notesOfChordWithRoot(basic,tension,root),bass<0?root:bass,true);
	}
	
	void receiveShiftRow(int vx,int vy){
		if(Chord.mode==0) return;
		if(root<0&&lastRoot>=0){
			root=lastRoot;
		}
		basic+=vx;
		if(basic<0) basic=5;
		if(basic>5) basic=0;
		row=basic;
		tension+=vy;
		if(tension>=5) tension=Chord.omitTriad?1:0;
		if(Chord.notesOfChord(basic,tension).length<=1) tension=Chord.omitTriad?1:0;
		
		if(tension<(Chord.omitTriad?1:0)) tension=4;
		while(Chord.notesOfChord(basic,tension).length<=1) tension--;
		reflectReality();
		stressMax(basic,tension);
		rootview.receiveEstimatedChord(chordName(),Chord.notesOfChordWithRoot(basic,tension,root),bass<0?root:bass,true);
	}
	
	void receiveChangeOmitTriad(boolean ot){
		int i;
		if(Chord.mode==0) ot=true;
		for(i=0;i<6;i++){
			aTriad[i].setVisible(!ot);
		}
		
		if(root<0) return;
		int r=root;
		root=-1;
		resetReality();
		estimate(r);
		reflectReality();
		selectMax();
		rootview.receiveEstimatedChord(chordName(),Chord.notesOfChordWithRoot(basic,tension,root),bass<0?root:bass,true);
	}
	
	void receiveChangeHarmonicMinor(boolean hm){
		if(root<0) return;
		int r=root;
		root=-1;
		resetReality();
		estimate(r);
		reflectReality();
		selectMax();
		rootview.receiveEstimatedChord(chordName(),Chord.notesOfChordWithRoot(basic,tension,root),bass<0?root:bass,true);
	}
	
	void receiveShiftRoot(int db){
		if(root<0&&lastRoot>=0){
			root=lastRoot;
		}
		int r=(root+db+12)%12;
		root=-1;
		resetReality();
		estimate(r);
		reflectReality();
		selectMax();
		rootview.receiveEstimatedChord(chordName(),Chord.notesOfChordWithRoot(basic,tension,root),bass<0?root:bass,true);
	}
	
	void receiveShiftBass(int delta){
		if(root<0&&lastRoot>=0){
			root=lastRoot;
			basic=lastBasic;
			tension=lastTension;
			bass=lastBass;
		}
		if(bass<0) bass=root;
		bass=(bass+delta+36)%12;
		reflectReality();
		rootview.receiveEstimatedChord(chordName(),Chord.notesOfChordWithRoot(basic,tension,root),bass<0?root:bass,true);
	
	}
}