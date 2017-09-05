.DEFAULT_GOAL := default

default: build run

build:
	./gradlew build

run:
	./gradlew run

clean:
	./gradlew clean

doc:
	./gradlew generateJavadocs

# doc/index.html: doc src/main.java
# 	javadoc src/main.java -d doc

# bin/main.class: bin src/main.java
# 	javac src/main.java -d bin
