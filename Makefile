.DEFAULT_GOAL := default

default: build run

build:
	./gradlew build

run:
	./gradlew run

clean:
	./gradlew clean

doc:
	./gradlew javadoc
