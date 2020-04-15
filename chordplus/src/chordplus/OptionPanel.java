package chordplus;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class OptionPanel extends JPanel implements ChangeListener, ActionListener {
	chordplus rootview;
	JButton bHistory;
	JLabel lVelocityLabel, lVelocity, lRightClick;
	JSlider sVelocitySlider;
	JCheckBox cOmitTriad, cHarmonicMinor, cPlayAtReleased, cTransposed;
	JRadioButton rBaseNote, rPlay, rStop;
	ButtonGroup rRightClick;

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

		lRightClick = new JLabel("右クリック", JLabel.LEFT);
		lRightClick.setBounds(12, 128, 137, 16);
		add(lRightClick);

		rRightClick = new ButtonGroup();
		rBaseNote = new JRadioButton("ベース音", true);
		rBaseNote.setBounds(12, 148, 137, 20);
		add(rBaseNote);
		rBaseNote.addActionListener(this);
		rRightClick.add(rBaseNote);
		rPlay = new JRadioButton("再生", false);
		rPlay.setBounds(12, 168, 137, 20);
		add(rPlay);
		rPlay.addActionListener(this);
		rRightClick.add(rPlay);
		rStop = new JRadioButton("停止", false);
		rStop.setBounds(12, 188, 137, 20);
		add(rStop);
		rRightClick.add(rStop);
		rStop.addActionListener(this);

		bHistory = new JButton("コード再生履歴");
		bHistory.setBounds(12, 220, 137, 24);
		bHistory.addActionListener(this);
		add(bHistory);
	}

	public void stateChanged(ChangeEvent arg0) {
		if (arg0.getSource() == sVelocitySlider) {
			int v;
			v = sVelocitySlider.getValue();
			if (v >= 128) {
				v = 127;
			}
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
		Object target = arg0.getSource();
		if (target == cOmitTriad) {
			Chord.omitTriad = cOmitTriad.isSelected();
			rootview.changeOmitTriad(cOmitTriad.isSelected());
		} else if (target == bHistory) {
			rootview.showHistoryPanel();
		} else if (target == cPlayAtReleased) {
			Chord.playAtReleased = cPlayAtReleased.isSelected();
		} else if (target == cHarmonicMinor) {
			Chord.harmonicMinor = cHarmonicMinor.isSelected();
		} else if (target == rBaseNote) {
			Chord.rightClickAction = Chord.RIGHT_CLICK_BASE_NOTE;
		} else if (target == rPlay) {
			Chord.rightClickAction = Chord.RIGHT_CLICK_PLAY;
		} else if (target == rStop) {
			Chord.rightClickAction = Chord.RIGHT_CLICK_STOP;
		}
	}
}
