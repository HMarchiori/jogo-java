all:
	javac *.java
	jar cfm Jogo.jar Manifest.txt *.class
clean:
	rm -f *.class

distclean: clean
	rm -f *.jar

