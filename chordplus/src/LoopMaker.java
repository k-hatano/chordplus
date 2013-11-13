import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;


public class LoopMaker extends JFrame {
	int il,it;
	
	JPanel fTemplate;
	JLabel lTemplate;
	JComboBox cTemplate;
	
	JPanel fLoop;
	
	JPanel fExport;
	JButton bExport;
	
	public LoopMaker(){
		super();
		
		setTitle("ループ作成");
		pack();
		Insets insets = this.getInsets();
		il=insets.left;
		it=insets.top;
		setSize(544+il,176+it);
		setLocation(48,64);
		setResizable(false);
		setLayout(null);
		
		fTemplate = new JPanel();
		fTemplate.setBounds(8,8,528,40);
		fTemplate.setLayout(null);
		fTemplate.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		lTemplate = new JLabel("テンプレート:",JLabel.RIGHT);
		lTemplate.setBounds(8,8,96,24);
		fTemplate.add(lTemplate);
		cTemplate = new JComboBox(Loop.loopKind);
		cTemplate.setBounds(112,8,96,24);
		fTemplate.add(cTemplate);
		add(fTemplate);
		
		fLoop = new JPanel();
		fLoop.setBounds(8,56,528,64);
		fLoop.setLayout(null);
		fLoop.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		add(fLoop);
		
		fExport = new JPanel();
		fExport.setBounds(8,128,528,40);
		fExport.setLayout(null);
		fExport.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		bExport=new JButton("書き出し");
		bExport.setBounds(216,8,96,24);
		fExport.add(bExport);
		add(fExport);
	}
}
