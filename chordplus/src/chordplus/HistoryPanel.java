package chordplus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import LoopMaker.LoopMaker;

public class HistoryPanel extends JPanel implements ActionListener {
	LoopMaker loopMaker = null;
	chordplus rootview;
	JButton bLoopMaker, bCancel;
	JCheckBox cChords[];

	int degrees[], basics[], tensions[], basses[];

	public HistoryPanel(chordplus cp) {
		super();

		rootview = cp;

		degrees = new int[12];
		for (int i = 0; i < 12; i++) {
			degrees[i] = -1;
		}
		basics = new int[12];
		for (int i = 0; i < 12; i++) {
			basics[i] = -1;
		}
		tensions = new int[12];
		for (int i = 0; i < 12; i++) {
			tensions[i] = -1;
		}
		basses = new int[12];
		for (int i = 0; i < 12; i++) {
			basses[i] = -1;
		}

		setLayout(null);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));

		cChords = new JCheckBox[12];
		for (int i = 0; i < 12; i++) {
			cChords[i] = new JCheckBox();
			cChords[i].setBounds(16, i * 20 + 4, 120, 20);
			add(cChords[i]);
		}

		bLoopMaker = new JButton("ループ作成");
		bLoopMaker.setBounds(12, 248, 137, 24);
		bLoopMaker.addActionListener(this);
		add(bLoopMaker);

		bCancel = new JButton("キャンセル");
		bCancel.setBounds(12, 276, 137, 24);
		bCancel.addActionListener(this);
		add(bCancel);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == bCancel) {
			rootview.showOptionPanel();
		} else if (arg0.getSource() == bLoopMaker) {
			int ds[] = new int[12];
			int bs[] = new int[12];
			int ts[] = new int[12];
			int bss[] = new int[12];
			int k = 0;
			for (int i = 0; i < 12; i++) {
				if (cChords[i].isSelected()) {
					ds[k] = degrees[i];
					bs[k] = basics[i];
					ts[k] = tensions[i];
					bss[k] = basses[i];
					k++;
				}
			}
			if (k <= 0) {
				JOptionPane.showMessageDialog(null, "ループに使用するコードを 1 つ以上選択してください。");
			} else {
				if (loopMaker == null) {
					loopMaker = new LoopMaker(this, rootview);
				}
				loopMaker.receiveChords(k, ds, bs, ts, bss, Chord.mode);
				loopMaker.show();
			}
		}
	}

	public void pushChord(int d, int b, int t, int bass) {
		if (degrees[11] == d && basics[11] == b && tensions[11] == t && basses[11] == bass)
			return;
		for (int i = 0; i < 11; i++) {
			degrees[i] = degrees[i + 1];
			basics[i] = basics[i + 1];
			tensions[i] = tensions[i + 1];
			basses[i] = basses[i + 1];
		}
		degrees[11] = d;
		basics[11] = b;
		tensions[11] = t;
		basses[11] = bass;
		updateChordNames();
	}

	public void updateChordNames() {
		for (int i = 0; i < 12; i++) {
			if (degrees[i] < 0) {
				cChords[i].setEnabled(false);
				cChords[i].setText("");
			} else {
				cChords[i].setEnabled(true);
				cChords[i].setText(Chord.chordName((degrees[i] + Chord.tonic + 36) % 12, basics[i], tensions[i],
						(basses[i] + Chord.tonic + 36) % 12, 0));
			}
		}
	}

	public void receiveShowHistoryPanel() {
		updateChordNames();
		for (int i = 0; i < 12; i++) {
			cChords[i].setSelected(i >= 4 && degrees[i] >= 0);
		}
	}

}
