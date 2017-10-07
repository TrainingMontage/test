.DEFAULT_GOAL := default
.PHONY: build run clean doc

default: build run

build:
	./gradlew build

run:
	./gradlew run

clean:
	./gradlew clean

doc:
	./gradlew javadoc
