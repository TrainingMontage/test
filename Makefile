defuault: bin/main.class docs
	java -cp bin/ main

docs: doc/index.html

doc/index.html: doc src/main.java
	javadoc src/main.java -d doc

clean:
	rm bin/*.class

bin/main.class: bin src/main.java
	javac src/*.java -cp bin/ -d bin

bin:
	mkdir bin

doc:
	mkdir doc