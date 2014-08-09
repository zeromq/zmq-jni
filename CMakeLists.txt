cmake_minimum_required (VERSION 2.8)

project(zmqjni CXX)
enable_language(C)

include (CheckIncludeFiles)
check_include_files(inttypes.h HAVE_INTTYPES_H)
check_include_files(stdint.h HAVE_STDINT_H)
check_include_files(sodium.h HAVE_LIBSODIUM_H)

find_package(JNI REQUIRED)
find_library(ZMQ_LIBRARY NAMES zmq libzmq REQUIRED)

configure_file(
  "${CMAKE_CURRENT_SOURCE_DIR}/src/main/c++/config.hpp.in"
  "${CMAKE_CURRENT_SOURCE_DIR}/src/main/c++/config.hpp" IMMEDIATE)

include_directories(
  ${JAVA_INCLUDE_PATH}
  ${JAVA_INCLUDE_PATH2}
  target/include)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -pedantic -O2")
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/target)

add_library(zmqjni SHARED ${CMAKE_SOURCE_DIR}/src/main/c++/zmq.cpp)

target_link_libraries(zmqjni ${ZMQ_LIBRARY})
