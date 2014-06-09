cmake_minimum_required (VERSION 2.8)

project(zmqjni CXX Java)

find_package(JNI REQUIRED)
find_library(ZMQ_LIBRARY NAMES zmq libzmq REQUIRED)

include_directories(
  ${JAVA_INCLUDE_PATH}
  ${JAVA_INCLUDE_PATH2}
  target/include)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -O2")
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/target)

add_library(zmqjni SHARED ${CMAKE_SOURCE_DIR}/src/main/c++/zmq.cpp)

target_link_libraries(zmqjni ${ZMQ_LIBRARY})
