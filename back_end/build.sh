#!/bin/bash

print_error() {
    if [ $# -ne 1 ]; then
        echo -e "\033[1;31m[Error]:\033[0m The function print_error take one parameter <message>"
        exit 1
    fi

    local message="$1"
    echo -e "\033[1;31m[Error]:\033[0m $message"
}

print_warning() {
    if [ $# -ne 1 ]; then
        print_error "The function print_warning take one parameter <message>"
        exit 1
    fi
    local message="$1"
    echo -e "\033[1;33m[Warning]:\033[0m $message"
}

print_info() {
    if [ $# -ne 1 ]; then
        print_error "The function print_info take one parameter <message>"
        exit 1
    fi

    local message="$1"
    echo -e "\033[1;32m[Info]:\033[0m $message"
}

calling_script_path=$(pwd)
script_path="$(dirname "$(realpath "${BASH_SOURCE[0]}")")"
cd "$script_path" || exit 1

cd build

# Run the make command and check if it was successful
if make; then
    print_info "Build successful"
    # Check if the first argument is "run"
    if [ "$1" == "--run" ]; then
        # Run the program after building
        cd ..
        ./main
    fi
else
    print_error "Build failed"
    exit 1
fi


cd "$calling_script_path" || exit 1