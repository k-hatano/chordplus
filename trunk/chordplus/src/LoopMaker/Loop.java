package LoopMaker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import chordplus.Chord;
import chordplus.MIDI;

public class Loop {
	public static String[] loopKinds={"ÉsÉAÉm","ÉMÉ^Å["};
	public static String[] pianoTemplates={"êLÇŒÇµ"};
	public static String[] guitarTemplates={"êLÇŒÇµ"};
	
	public static final int iResolution=480;
	
	public static ArrayList<MyNoteEvent> myNoteEventListOfLoop(int mode,int template,int chords,int basics[],int tensions[],int roots[],int basses[],int lengths[]){
		ArrayList<MyNoteEvent> res=new ArrayList<MyNoteEvent>();
		int time=0;
		if(mode==1){
			for(int i=0;i<chords;i++){
				int[] notes=Chord.notesOfChordWithPianoBasement(basics[i],tensions[i],roots[i],basses[i],Chord.pianoBasement);
				for(int j=0;j<notes.length;j++){
					MyNoteEvent evt=new MyNoteEvent(true,notes[j],Chord.velocity,time);
					res.add(evt);
				}
				time+=lengths[i];
				for(int j=0;j<notes.length;j++){
					MyNoteEvent evt=new MyNoteEvent(false,notes[j],Chord.velocity,time);
					res.add(evt);
				}
			}
		}else if(mode==2){
			for(int i=0;i<chords;i++){
				int[] notes=Chord.notesOfChordWithGuitarBasement(basics[i],tensions[i],roots[i],basses[i],Chord.guitarBasement+40);
				for(int j=0;j<notes.length;j++){
					MyNoteEvent evt=new MyNoteEvent(true,notes[j],Chord.velocity,time);
					res.add(evt);
				}
				time+=lengths[i];
				for(int j=0;j<notes.length;j++){
					MyNoteEvent evt=new MyNoteEvent(false,notes[j],Chord.velocity,time);
					res.add(evt);
				}
			}
		}
		Collections.sort(res,new MyNoteEventComparator());
		return res;
	}
	
	public static Sequence sequenceOfLoop(int mode,int template,int chords,int basics[],int tensions[],int roots[],int basses[],int lengths[],int beats,int bar){
		try {
			Sequence seq=new Sequence(Sequence.PPQ,iResolution);
			Track trk=seq.createTrack();
			MidiMessage msg;
			msg=MIDI.messageTrackName(generateTrackName(chords,basics,tensions,roots,basses));
			trk.add(new MidiEvent(msg,0));
			msg=MIDI.messageTimeSignature(beats,bar);
			trk.add(new MidiEvent(msg,0));
			msg=MIDI.messageProgramChange(Chord.instrument);
			trk.add(new MidiEvent(msg,0));
			
			ArrayList<MyNoteEvent> events=myNoteEventListOfLoop(mode,template,chords,basics,tensions,roots,basses,lengths);
			long time=0;
			long last=0;
			for(int i=0;i<events.size();i++){
				msg=MIDI.messageNoteOn(events.get(i).note,events.get(i).onOrOff);
				time=events.get(i).time*iResolution;
				trk.add(new MidiEvent(msg,time));
			}
			return seq;
		} catch (InvalidMidiDataException e) {
			return null;
		}
	}
	
	public static String generateTrackName(int chords,int roots[],int basics[],int tensions[],int basses[]){
		String name="";
		for(int i=0;i<chords;i++){
			if(i>0) name+=" ";
			name+=Chord.chordName(roots[i],basics[i],tensions[i],basses[i],0);
		}
		return name;
	}
	
	
	static class MyNoteEvent {
		public boolean onOrOff;
		public int note;
		public int velocity;
		public int time;
		
		MyNoteEvent(){
			
		}
		
		MyNoteEvent(boolean o,int n,int v,int t){
			onOrOff=o;
			note=n;
			velocity=v;
			time=t;
		}
	}
	
	static class MyNoteEventComparator implements Comparator<MyNoteEvent>{

		@Override
		public int compare(MyNoteEvent arg0, MyNoteEvent arg1) {
			if(arg0.time<arg1.time) return 1;
			else if(arg0.time>arg1.time) return -1;
			else return 0;
		}
		
	}

}
