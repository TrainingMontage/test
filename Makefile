
.DEFAULT_GOAL := default
.PHONY: build run clean doc test

default: build run

build:
	./gradlew build
	./gradlew trackModelJar

run:
	./gradlew run

clean:
	./gradlew clean

doc:
	./gradlew javadoc

test:
	./gradlew test --info
