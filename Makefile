all: build run

build: Picture.class Main.class

%.class: %.java
	javac $<

run: 
	java Main

clean: 
	rm *.class