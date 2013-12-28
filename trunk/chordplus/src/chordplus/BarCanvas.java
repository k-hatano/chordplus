package chordplus;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class BarCanvas extends Canvas {
	
	Color col=Color.black;
	
	public BarCanvas(){
		super();
	}
	
	public BarCanvas(Color c){
		super();
		col=c;
	}
	
	public void paint(final Graphics g){
		Image img=createImage(getSize().width,getSize().height);
		Graphics grp=img.getGraphics();
		
		grp.setColor(col);
		grp.fillRect(0,0,getSize().width,getSize().height);
		
		g.drawImage(img,0,0,this);
	}
}
