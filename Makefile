LIBRARY := libzmqjni.so
OBJFILES := src/main/c++/zmq.o
INCLUDES := -Wall -fPIC -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux -Itarget/include -O2
CXXFLAGS := $(INCLUDES)

$(LIBRARY): $(OBJFILES)
	$(CXX) -shared -o $@ $^ -lzmq
clean:
	rm -f $(LIBRARY) $(OBJFILES)
