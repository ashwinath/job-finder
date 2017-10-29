#!/bin/bash

./gradlew jar
cp --parents ./database/sqlite.db ./build/libs/
cp ./chromedriver.exe ./build/libs/
