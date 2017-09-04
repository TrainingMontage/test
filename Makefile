defuault: bin/main.class doc/index.html
	java -cp bin/ main

doc/index.html: doc src/main.java
	javadoc src/main.java -d doc

clean:
	rm bin/*

bin/main.class: bin src/main.java
	javac src/main.java -d bin

bin:
	mkdir bin

doc:
	mkdir doc