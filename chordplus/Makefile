# Makefile
chordplus: src/chordplus/chordplus.java
	javac -encoding utf8 -d classes src/chordplus/*.java src/LoopMaker/*.java
	jar cvfm chordplus.jar mani.mf -C classes/ .
clean:
	-rm *.jar
	-rm classes/chordplus/*.class
	-rm classes/LoopMaker/*.class
