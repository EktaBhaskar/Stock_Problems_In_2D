default: clean
	mkdir -p build/classes
	javac -sourcepath ./src -d ./build/classes ./src/Driver.java
	jar cfev Driver.jar Driver -C ./build/classes/ .

clean:
	rm -rf Driver.jar
	rm -rf build/
