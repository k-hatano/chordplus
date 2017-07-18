# Makefile
chordplus: src/chordplus/chordplus.java
	javac -encoding Shift_JIS src/chordplus/*.java src/LoopMaker/*.java
	jar cvfm chordplus.jar mani.mf src/LoopMaker/*.class src/chordplus/*.class
clean:
	rm chordplus.jar
	rm src/chordplus/*.class
	rm src/LoopMaker/*.class