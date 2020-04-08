package chordplus;

import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.MidiDevice.Info;
import javax.swing.JOptionPane;

public class MIDI {

	static Receiver receiver = null;
	static MidiDevice device = null;

	public static MidiDevice selectMidiDevice() {
		try {
			Info[] infos = MidiSystem.getMidiDeviceInfo();
			if (infos.length == 0) {
				JOptionPane.showMessageDialog(null, "MIDI が使用できません。chordplus を終了します。");
				System.exit(0);
			} else {
				Boolean omits[] = new Boolean[infos.length];
				for (int i = 0; i < infos.length; i++) {
					omits[i] = true;
				}

				ArrayList<String> names = new ArrayList<String>();
				for (int i = 0; i < infos.length; i++) {
					MidiDevice dev = MidiSystem.getMidiDevice(infos[i]);
					try {
						dev.open();
						if (dev.getReceiver() != null) {
							names.add(infos[i].getName());
							omits[i] = false;
						}
					} catch (MidiUnavailableException e) {
					} finally {
						dev.close();
					}
				}
				String params[];
				params = (String[]) names.toArray(new String[0]);
				/*
				 * String params[] = new String[infos.length]; for(int i=0;i<infos.length;i++){
				 * params[i]=infos[i].getName(); }
				 */
				if (params.length <= 0)
					throw new MidiUnavailableException();
				String res = (String) JOptionPane.showInputDialog(null, "使用する MIDI デバイスを選択してください。", "chordplus",
						JOptionPane.INFORMATION_MESSAGE, null, params, params[0]);
				if (res == null)
					System.exit(1);
				for (int i = 0; i < infos.length; i++) {
					if (omits[i])
						continue;
					if (res.equals(infos[i].getName())) {
						device = MidiSystem.getMidiDevice(infos[i]);
						device.open();
						receiver = device.getReceiver();
						break;
					}
				}
			}
		} catch (MidiUnavailableException e) {
			JOptionPane.showMessageDialog(null, "MIDI が使用できません。chordplus を終了します。");
			e.printStackTrace();
			System.exit(0);
		}
		return device;
	}

	public static void close() {
		if (device != null)
			device.close();
		if (receiver != null)
			receiver.close();
	}

	public static MidiMessage messageNoteOn(int note, boolean onOrOff) {
		ShortMessage message = new ShortMessage();
		try {
			if (onOrOff) {
				message.setMessage(ShortMessage.NOTE_ON, note, Chord.velocity);
			} else {
				message.setMessage(ShortMessage.NOTE_OFF, note, Chord.velocity);
			}
		} catch (InvalidMidiDataException e) {
			JOptionPane.showMessageDialog(null, "MIDI が使用できません。chordplus を終了します。");
			e.printStackTrace();
			System.exit(0);
		}
		return message;
	}

	public static MidiMessage messagePitchBend(int b) {
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(ShortMessage.PITCH_BEND, 0, 64 + b);
		} catch (InvalidMidiDataException e) {
			JOptionPane.showMessageDialog(null, "MIDI が使用できません。chordplus を終了します。");
			e.printStackTrace();
			System.exit(0);
		}
		return message;
	}

	public static MidiMessage messageProgramChange(int inst) {
		Chord.instrument = inst;
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(ShortMessage.PROGRAM_CHANGE, inst, 0);
		} catch (InvalidMidiDataException e) {
			JOptionPane.showMessageDialog(null, "MIDI が使用できません。chordplus を終了します。");
			e.printStackTrace();
			System.exit(0);
		}
		return message;
	}

	public static MidiMessage messageKeySignature(int sharps, int minor) {
		MetaMessage message = new MetaMessage();
		try {
			byte bytes[] = new byte[2];
			bytes[0] = (byte) sharps;
			bytes[1] = (byte) minor;
			message.setMessage(0x59, bytes, 2);
		} catch (InvalidMidiDataException e) {
			JOptionPane.showMessageDialog(null, "MIDI が使用できません。chordplus を終了します。");
			e.printStackTrace();
			System.exit(0);
		}
		return message;
	}

	public static MidiMessage messageTimeSignature(int beats, int bar) {
		MetaMessage message = new MetaMessage();
		try {
			byte bytes[] = new byte[4];
			bytes[0] = (byte) beats;
			if (bar == 2) {
				bytes[1] = (byte) 1;
			} else if (bar == 4) {
				bytes[1] = (byte) 2;
			} else if (bar == 8) {
				bytes[1] = (byte) 3;
			} else if (bar == 16) {
				bytes[1] = (byte) 4;
			} else if (bar == 32) {
				bytes[1] = (byte) 5;
			} else {
				bytes[1] = (byte) 1;
			}
			bytes[2] = (byte) 24;
			bytes[3] = (byte) 8;
			message.setMessage(0x58, bytes, 4);
		} catch (InvalidMidiDataException e) {
			JOptionPane.showMessageDialog(null, "MIDI が使用できません。chordplus を終了します。");
			e.printStackTrace();
			System.exit(0);
		}
		return message;
	}

	public static MidiMessage messageTrackName(String name) {
		MetaMessage message = new MetaMessage();
		try {
			byte bytes[] = name.getBytes();
			message.setMessage(0x01, bytes, bytes.length);
		} catch (InvalidMidiDataException e) {
			JOptionPane.showMessageDialog(null, "MIDI が使用できません。chordplus を終了します。");
			e.printStackTrace();
			System.exit(0);
		}
		return message;
	}

	public static boolean send(MidiMessage message) {
		receiver.send(message, -1);
		return true;
	}

}
