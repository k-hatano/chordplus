# Makefile
chordplus: src/chordplus/chordplus.java
	javac -encoding Shift_JIS -d classes src/chordplus/*.java src/LoopMaker/*.java
	jar cvfm chordplus.jar mani.mf -C classes/ .
clean:
	rm chordplus.jar
	rm classes/chordplus/*.class
	rm classes/LoopMaker/*.class
