cmake_minimum_required (VERSION 2.8)

project(zmqjni CXX Java)

find_package(Java REQUIRED)
find_package(JNI REQUIRED)
find_library(ZMQ_LIB libzmq)

include_directories(
  ${JAVA_INCLUDE_PATH}
  ${JAVA_INCLUDE_PATH2}
  target/include)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -O2")

add_library(zmqjni SHARED src/main/c++/zmq.cpp)

find_library(ZMQ_LIB zmq)
target_link_libraries(zmqjni ${ZMQ_LIB})

# target_link_libraries(zmqjni zmq)
