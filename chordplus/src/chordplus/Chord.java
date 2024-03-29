package chordplus;

public class Chord {
	static int nTriad[][] = { { 0, 4, 7 }, { 0, 3, 7 }, { 0, 3, 6 }, { 0, 4, 8 }, { 0, 5, 7 }, { 0, 4, 6 } };
	static int nSeventh[][] = { { 0, 4, 7, 10 }, { 0, 3, 7, 10 }, { 0, 3, 6, 10 }, { 0, 4, 8, 10 }, { 0, 5, 7, 10 },
			{ 0, 4, 6, 10 } };
	static int nMajorSeventh[][] = { { 0, 4, 7, 11 }, { 0, 3, 7, 11 }, { 0, 3, 6, 11 }, { 0, 4, 8, 11 },
			{ 0, 5, 7, 11 }, { 0, 4, 6, 11 } };
	static int nSixth[][] = { { 0, 4, 7, 9 }, { 0, 3, 7, 9 }, { 0, 3, 6, 9 }, {}, {}, {} };
	static int nAdd9[][] = { { 0, 4, 7, 14 }, { 0, 3, 7, 14 }, {}, {}, { 0, 4, 5, 7 }, {} };
	static int nNinth[][] = { { 0, 2, 4, 7, 10 }, { 0, 2, 3, 7, 10 }, { 0, 2, 3, 6, 10 }, { 0, 2, 4, 8, 10 }, {},
			{ 0, 2, 4, 6, 10 } };
	static int nMajorNinth[][] = { { 0, 2, 4, 7, 11 }, { 0, 2, 3, 7, 11 }, {}, {}, {}, {} };

	static String[] sTriad = { "maj", "m", "dim", "aug", "sus4", "-5" };
	static String[] sSeventh = { "7", "m7", "m7-5", "aug7", "7sus4", "7-5" };
	static String[] sMajorSeventh = { "M7", "mM7", "mM7-5", "augM7", "M7sus4", "M7-5" };
	static String[] sSixth = { "6", "m6", "dim7", "", "", "" };
	static String[] sAdd9 = { "add9", "madd9", "", "", "add4", "" };
	static String[] sNinth = { "9", "m9", "m9-5", "aug9", "", "9-5" };
	static String[] sMajorNinth = { "M9", "mM9", "", "", "", "" };

	static int nMajor[] = { 0, 2, 4, 5, 7, 9, 11 };
	static int nMinor[] = { 0, 2, 3, 5, 7, 8, 10 };
	static int nHarmonicMinor[] = { 0, 2, 3, 5, 7, 8, 11 };

	public static int tonic = 0, minor = 0, transpose = 0, pianoBasement = 0, guitarBasement = 0;
	public static int instrument = 0;
	public static int velocity = 64;
	public static int mode = 0;
	public static int smartRange = 0;

	public static int basic = -1, tension = -1, root = -1, bass = -1;

	static String[] sNoteSharp = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
	static String[] sNoteFlat = { "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B" };
	static String[] sDegreeMajor = { "I", "", "II", "", "III", "IV", "", "V", "", "VI", "", "VII" };
	static String[] sDegreeMinor = { "I", "", "II", "III", "", "IV", "", "V", "VI", "", "VII", "" };
	static String[] sDegreeHarmonicMinor = { "I", "", "II", "III", "", "IV", "", "V", "VI", "", "", "VII" };

	public static final int RIGHT_CLICK_BASE_NOTE = 1;
	public static final int RIGHT_CLICK_PLAY = 2;
	public static final int RIGHT_CLICK_STOP = 3;

	public static boolean omitTriad = false;
	public static boolean harmonicMinor = false;
	public static boolean playAtReleased = false;
	public static int rightClickAction = RIGHT_CLICK_BASE_NOTE;

	static String[] instruments = { "Acoustic Piano", "Bright Piano", "Electric Grand Piano", "Honky-tonk Piano",
			"Electric Piano", "Electric Piano 2", "Harpsichord", "Clavi", "Celesta", "Glockenspiel", "Musical box",
			"Vibraphone", "Marimba", "Xylophone", "Tubular Bell", "Dulcimer", "Drawbar Organ", "Percussive Organ",
			"Rock Organ", "Church organ", "Reed organ", "Accordion", "Harmonica", "Tango Accordion",
			"Acoustic Guitar (nylon)", "Acoustic Guitar (steel)", "Electric Guitar (jazz)", "Electric Guitar (clean)",
			"Electric Guitar (muted)", "Overdriven Guitar", "Distortion Guitar", "Guitar harmonics", "Acoustic Bass",
			"Electric Bass (finger)", "Electric Bass (pick)", "Fretless Bass", "Slap Bass 1", "Slap Bass 2",
			"Synth Bass 1", "Synth Bass 2", "Violin", "Viola", "Cello", "Double bass", "Tremolo Strings",
			"Pizzicato Strings", "Orchestral Harp", "Timpani", "String Ensemble 1", "String Ensemble 2",
			"Synth Strings 1", "Synth Strings 2", "Voice Aahs", "Voice Oohs", "Synth Voice", "Orchestra Hit", "Trumpet",
			"Trombone", "Tuba", "Muted Trumpet", "French horn", "Brass Section", "Synth Brass 1", "Synth Brass 2",
			"Soprano Sax", "Alto Sax", "Tenor Sax", "Baritone Sax", "Oboe", "English Horn", "Bassoon", "Clarinet",
			"Piccolo", "Flute", "Recorder", "Pan Flute", "Blown Bottle", "Shakuhachi", "Whistle", "Ocarina",
			"Lead 1 (square)", "Lead 2 (sawtooth)", "Lead 3 (calliope)", "Lead 4 (chiff)", "Lead 5 (charang)",
			"Lead 6 (voice)", "Lead 7 (fifths)", "Lead 8 (bass + lead)", "Pad 1 (Fantasia)", "Pad 2 (warm)",
			"Pad 3 (polysynth)", "Pad 4 (choir)", "Pad 5 (bowed)", "Pad 6 (metallic)", "Pad 7 (halo)", "Pad 8 (sweep)",
			"FX 1 (rain)", "FX 2 (soundtrack)", "FX 3 (crystal)", "FX 4 (atmosphere)", "FX 5 (brightness)",
			"FX 6 (goblins)", "FX 7 (echoes)", "FX 8 (sci-fi)", "Sitar", "Banjo", "Shamisen", "Koto", "Kalimba",
			"Bagpipe", "Fiddle", "Shanai", "Tinkle Bell", "Agogo", "Steel Drums", "Woodblock", "Taiko Drum",
			"Melodic Tom", "Synth Drum", "Reverse Cymbal", "Guitar Fret Noise", "Breath Noise", "Seashore",
			"Bird Tweet", "Telephone Ring", "Helicopter", "Applause", "Gunshot" };

	public static int[] notesOfChord(final int basic, final int tension) {
		int r[], b[], i;
		switch (tension) {
			case 0:
				b = nTriad[basic];
				break;
			case 1:
				b = nSeventh[basic];
				break;
			case 2:
				b = nMajorSeventh[basic];
				break;
			case 3:
				b = nSixth[basic];
				break;
			case 4:
				b = nAdd9[basic];
				break;
			case 5:
				b = nNinth[basic];
				break;
			case 6:
				b = nMajorNinth[basic];
				break;
			default:
				b = nTriad[basic];
		}
		r = new int[b.length];
		for (i = 0; i < b.length; i++)
			r[i] = b[i];
		return r;
	}

	public static int[] notesOfChordWithRoot(final int basic, final int tension, final int root) {
		final int r[] = notesOfChord(basic, tension);
		int i;
		for (i = 0; i < r.length; i++) {
			r[i] = (r[i] + root + 36) % 12;
		}
		return r;
	}

	public static int[] notesOfChordWithPianoBasement(final int basic, final int tension, final int root,
			final int bass, final int pianoBasement) {
		final int chordNotes[] = notesOfChordWithRoot(basic, tension, root);
		final int res[] = new int[chordNotes.length + 1];

		if (bass >= 0) {
			res[0] = bass;
		} else {
			res[0] = root;
		}
		while (true) {
			if (res[0] < pianoBasement + 48) {
				res[0] += 12;
			} else if (res[0] >= pianoBasement + 60) {
				res[0] -= 12;
			} else {
				break;
			}
		}

		for (int i = 0; i < chordNotes.length; i++) {
			res[i + 1] = chordNotes[i];
			while (true) {
				if (res[i + 1] < pianoBasement + 60) {
					res[i + 1] += 12;
				} else if (res[i + 1] >= pianoBasement + 72) {
					res[i + 1] -= 12;
				} else {
					break;
				}
			}
		}

		return res;
	}

	public static int[] notesOfChordWithGuitarBasement(final int basic, final int tension, final int root, int bass,
			final int guitarBasement) {
		int n, lowest = -1, highScore = 0;
		final int tmpNotes[] = { -1, -1, -1, -1, -1, -1 };
		final int chordNotes[] = notesOfChordWithRoot(basic, tension, root);
		if (bass < 0)
			bass = root;
		for (int a = -1; a < 5; a++) {
			for (int b = -1; b < 5; b++) {
				for (int c = -1; c < 5; c++) {
					for (int d = -1; d < 4; d++) {
						for (int e = -1; e < 5; e++) {
							for (int f = -1; f < 5; f++) {
								lowest = -1;
								if (lowest < 0 && a >= 0) {
									lowest = a + guitarBasement;
								}
								if (lowest < 0 && b >= 0) {
									lowest = b + guitarBasement + 5;
								}
								if (lowest < 0 && c >= 0) {
									lowest = c + guitarBasement + 10;
								}
								if (lowest < 0 && d >= 0) {
									lowest = d + guitarBasement + 15;
								}
								if (lowest < 0 && e >= 0) {
									lowest = e + guitarBasement + 19;
								}
								if (lowest < 0 && f >= 0) {
									lowest = f + guitarBasement + 24;
								}
								if (lowest % 12 != bass % 12) {
									continue;
								}

								final int currentNotes[] = new int[6];
								int score = 0;
								int flag;
								currentNotes[0] = a < 0 ? -1 : a + guitarBasement;
								currentNotes[1] = b < 0 ? -1 : b + guitarBasement + 5;
								currentNotes[2] = c < 0 ? -1 : c + guitarBasement + 10;
								currentNotes[3] = d < 0 ? -1 : d + guitarBasement + 15;
								currentNotes[4] = e < 0 ? -1 : e + guitarBasement + 19;
								currentNotes[5] = f < 0 ? -1 : f + guitarBasement + 24;
								for (int i = 0; i < chordNotes.length; i++) {
									flag = 0;
									for (int j = 0; j < 6; j++) {
										if (currentNotes[j] >= 0 && currentNotes[j] % 12 == chordNotes[i] % 12) {
											flag++;
										}
										if (flag == 1) {
											flag = 2;
										}
									}
									score += flag;
								}
								if (score > highScore) {
									for (int i = 0; i < 6; i++) {
										tmpNotes[i] = currentNotes[i];
									}
									highScore = score;
								}
							}
						}
					}
				}
			}
		}
		n = 0;
		for (int i = 0; i < 6; i++) {
			if (tmpNotes[i] >= 0) {
				n++;
			}
		}
		final int notes[] = new int[n];
		int j = 0;
		for (int i = 0; i < 6; i++) {
			if (tmpNotes[i] >= 0) {
				notes[j] = tmpNotes[i];
				j++;
			}
		}
		return notes;
	}

	public static int[] notesOfScale(final int tonic, final int minor) {
		final int[] r = new int[7];
		int[] b;
		switch (minor) {
			case 0:
				b = nMajor;
				break;
			case 1:
				if (harmonicMinor) {
					b = nHarmonicMinor;
				} else {
					b = nMinor;
				}
				break;
			default:
				b = nMajor;
		}
		for (int i = 0; i < 7; i++) {
			r[i] = (b[i] + tonic) % 12;
		}
		return r;
	}

	public static boolean scaleContainsNote(final int tonic, final int minor, final int note) {
		final int[] r = notesOfScale(tonic, minor);
		for (int i = 0; i < r.length; i++) {
			if (r[i] == note) {
				return true;
			}
		}
		return false;
	}

	public static String nameOfNote(final int note, final int flat) {
		if (flat > 0) {
			return sNoteFlat[note];
		} else {
			return sNoteSharp[note];
		}
	}

	public static String chordName(final int root, final int basic, final int tension, final int bass, final int flat) {
		String name;

		if (root < 0) {
			name = "";
		} else if (flat > 0) {
			name = sNoteFlat[root];
		} else {
			name = sNoteSharp[root];
		}

		switch (tension) {
			case 0:
				if (root < 0 || basic > 0) {
					name = name + sTriad[basic];
				}
				break;
			case 1:
				name = name + sSeventh[basic];
				break;
			case 2:
				name = name + sMajorSeventh[basic];
				break;
			case 3:
				name = name + sSixth[basic];
				break;
			case 4:
				name = name + sAdd9[basic];
				break;
			case 5:
				name = name + sNinth[basic];
				break;
			case 6:
				name = name + sMajorNinth[basic];
				break;
			default:
				name = name + sTriad[basic];
		}

		if (bass >= 0 && bass != root) {
			if (flat > 0) {
				name = name + "/" + sNoteFlat[bass];
			} else {
				name = name + "/" + sNoteSharp[bass];
			}
		}

		return name;
	}

	public static void changeScale(final int t, final int m) {
		tonic = t;
		minor = m;
	}

	public static int tonic() {
		return tonic;
	}

	public static int minor() {
		return minor;
	}

	public static int transpose() {
		return transpose;
	}

	public static void changeTranspose(final int t) {
		transpose = t;
	}

	public static String[] instrumentNameList() {
		final String[] list = new String[128];
		int i;
		for (i = 0; i < 128; i++) {
			list[i] = "" + (i + 1) + " - " + instruments[i];
		}
		return list;
	}

	public static int numberOfSharps(int tonic, final int minor) {
		if (minor > 0) {
			tonic = (tonic + 9) % 12;
		}
		int res = (tonic * 5) % 12;
		if (res > 6) {
			res -= 12;
		}
		return res;
	}
}
