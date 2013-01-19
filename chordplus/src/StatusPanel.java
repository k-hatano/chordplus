import javax.swing.*;
import javax.swing.border.*;

public class StatusPanel extends JPanel {
	JLabel lStatus;
	chordplus parent;
	public StatusPanel(chordplus cp){
		super();
		
		parent=cp;
		
		setLayout(null);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		
		lStatus=new JLabel("chordplus",JLabel.LEFT);
		lStatus.setBounds(8,8,608,16);
		add(lStatus);
	}
}
