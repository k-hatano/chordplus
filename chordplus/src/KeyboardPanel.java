import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

public class KeyboardPanel extends JPanel implements ActionListener,MouseListener {
	JLabel lChord,lTranspose;
	JButton bFocus;
	chordplus parent;
	KeyboardCanvas cKeyboard;
	
	public KeyboardPanel(chordplus cp){
		super();
		
		parent=cp;
		
		setLayout(null);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		
		cKeyboard = new KeyboardCanvas(this);
		cKeyboard.setBounds(16,16,353,99);
		add(cKeyboard);
		cKeyboard.requestFocusInWindow();
		
		bFocus = new JButton("£");
		bFocus.setBounds(80,125,225,20);
		bFocus.addActionListener(this);
		parent.getRootPane().setDefaultButton(bFocus);
		add(bFocus);
		
		lChord = new JLabel("",JLabel.CENTER);
		lChord.setBounds(8,127,369,16);
		lChord.addMouseListener(this);
		add(lChord);
		
		lTranspose = new JLabel("",JLabel.RIGHT);
		lTranspose.setBounds(337,127,32,16);
		lTranspose.setForeground(Color.gray);
		add(lTranspose);
	}
	
	void receiveNoteOn(int note,boolean onOrOff){
		parent.receiveNoteOn(note,onOrOff);
	}
	void receiveAllNotesOff(){
		parent.receiveAllNotesOff();
	}
	void receiveChangeMode(int mode,int transpose){
		cKeyboard.receiveChangeMode(mode,transpose);
	}
	void receiveKeyPressed(int which){
		parent.receiveKeyPressed(which);
	}
	void receiveEstimatedChord(String name,int notes[]){
		lChord.setForeground(Color.gray);
		lChord.setText(name);
		cKeyboard.receiveEstimatedChordNotes(notes);
	}
	void receivePlay(){
		lChord.setForeground(Color.black);
		parent.receivePlay();
	}
	void receiveBassChanged(int note){
		parent.receiveBassChanged(note);
	}
	void receiveKeyboardFocused(){
		bFocus.setVisible(false);
	}
	void receiveKeyboardBlured(){
		bFocus.setVisible(true);
	}
	void receiveSelectRow(int which){
		parent.receiveSelectRow(which);
	}
	void receiveShiftRow(int vx,int vy){
		parent.receiveShiftRow(vx, vy);
	}
	public void receiveShiftVelocity(int v){
		parent.shiftVelocity(v);
	}
	public void receiveShiftTranspose(int dt){
		parent.shiftTranspose(dt);
	}
	void receiveShiftBasic(int db){
		parent.receiveShiftBasic(db);
	}

	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==bFocus){
			cKeyboard.requestFocusInWindow();
		}
	}
	
	public void receiveChangeTranspose(int t){
		if(t==0) lTranspose.setText("");
		else if(t>0) lTranspose.setText("+"+t);
		else lTranspose.setText(""+t);
	}

	public void mouseClicked(MouseEvent arg0) {
		receivePlay();
	}

	public void mouseEntered(MouseEvent arg0) {
		
	}

	public void mouseExited(MouseEvent arg0) {
		
	}

	public void mousePressed(MouseEvent arg0) {
		
	}

	public void mouseReleased(MouseEvent arg0) {
		
	}
}
