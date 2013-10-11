cmake_minimum_required (VERSION 2.8)

project(zmqjni CXX Java)

find_package(Java REQUIRED)
find_package(JNI REQUIRED)
find_library(ZMQ_LIB libzmq)

# target_link_libraries(zmqjni ${ZMQ_LIB})

include_directories(
  ${JAVA_INCLUDE_PATH}
  ${JNI_INCLUDE_DIRS}
  target/include)

add_library(zmqjni SHARED src/main/c++/zmq.cpp)
