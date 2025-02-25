cmake_minimum_required(VERSION 3.12)
project(ihmcllamacpp)

# Define the third-party library as an imported target
add_library(thirdparty::thirdparty IMPORTED SHARED)
# Find all .so files in thirdparty/lib
file(GLOB THIRDPARTY_LIBS "${CMAKE_SOURCE_DIR}/thirdparty/lib/*.so")

# Set the primary IMPORTED_LOCATION to the first .so file (required by CMake)
list(GET THIRDPARTY_LIBS 0 PRIMARY_SO)
set_target_properties(thirdparty::thirdparty PROPERTIES
        IMPORTED_LOCATION "${PRIMARY_SO}"
)

# Add additional .so files as link dependencies
set_target_properties(thirdparty::thirdparty PROPERTIES
        INTERFACE_LINK_LIBRARIES "${THIRDPARTY_LIBS}"
)
target_include_directories(thirdparty::thirdparty INTERFACE "${CMAKE_SOURCE_DIR}/thirdparty/include")

# Create the executable
add_executable(myexecutable src/ihmcllamacpp.cpp)

# Set include directories for the executable
target_include_directories(myexecutable PRIVATE "${CMAKE_SOURCE_DIR}/include")

# Link the executable to the third-party library
target_link_libraries(myexecutable PRIVATE thirdparty::thirdparty)

# Set RPATH so the shared library can be found at runtime
set_target_properties(myexecutable PROPERTIES
        BUILD_RPATH "${CMAKE_SOURCE_DIR}/thirdparty/lib"
)