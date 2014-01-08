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
		setBackground(c);
	}
	
	public void paint(final Graphics g){
		
	}
}
