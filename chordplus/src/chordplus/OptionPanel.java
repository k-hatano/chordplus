package chordplus;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class OptionPanel extends JPanel implements ChangeListener, ActionListener {
	chordplus rootview;
	JButton bHistory;
	JLabel lVelocityLabel, lVelocity;
	JSlider sVelocitySlider;
	JCheckBox cOmitTriad, cHarmonicMinor, cPlayAtReleased, cTransposed;

	public OptionPanel(chordplus cp) {
		super();

		rootview = cp;

		setLayout(null);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));

		lVelocityLabel = new JLabel("ベロシティ:", JLabel.LEFT);
		lVelocityLabel.setBounds(12, 8, 75, 16);
		add(lVelocityLabel);

		lVelocity = new JLabel("" + Chord.velocity, JLabel.RIGHT);
		lVelocity.setBounds(99, 8, 46, 16);
		add(lVelocity);

		sVelocitySlider = new JSlider(0, 127, 64);
		sVelocitySlider.setBounds(16, 24, 134, 24);
		sVelocitySlider.addChangeListener(this);
		add(sVelocitySlider);

		cHarmonicMinor = new JCheckBox("和声的短音階");
		cHarmonicMinor.setBounds(12, 50, 137, 24);
		cHarmonicMinor.addActionListener(this);
		add(cHarmonicMinor);

		cOmitTriad = new JCheckBox("三和音を無視");
		cOmitTriad.setBounds(12, 70, 137, 24);
		cOmitTriad.addActionListener(this);
		add(cOmitTriad);

		cPlayAtReleased = new JCheckBox("離したら再生");
		cPlayAtReleased.setBounds(12, 90, 137, 24);
		cPlayAtReleased.addActionListener(this);
		add(cPlayAtReleased);

		bHistory = new JButton("コード再生履歴");
		bHistory.setBounds(12, 128, 137, 24);
		bHistory.addActionListener(this);
		add(bHistory);
	}

	public void stateChanged(ChangeEvent arg0) {
		if (arg0.getSource() == sVelocitySlider) {
			int v;
			v = sVelocitySlider.getValue();
			if (v >= 128)
				v = 127;
			rootview.changeVelocity(v);
		}
	}

	public void receiveChangeVelocity(int v) {
		sVelocitySlider.setValue(v);
		lVelocity.setText("" + v);
	}

	public void receiveChangeHarmonicMinor(boolean hm) {
		cHarmonicMinor.setSelected(hm);
	}

	public void receiveChangeOmitTriad(boolean ot) {
		cOmitTriad.setSelected(ot);
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == cOmitTriad) {
			Chord.omitTriad = cOmitTriad.isSelected();
			rootview.changeOmitTriad(cOmitTriad.isSelected());
		} else if (arg0.getSource() == bHistory) {
			rootview.showHistoryPanel();
		} else if (arg0.getSource() == cPlayAtReleased) {
			Chord.playAtReleased = cPlayAtReleased.isSelected();
		} else if (arg0.getSource() == cHarmonicMinor) {
			Chord.harmonicMinor = cHarmonicMinor.isSelected();
		}
	}
}
