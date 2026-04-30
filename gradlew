#!/usr/bin/env bash
#
# Gradle wrapper script
#

if [ -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    java -jar gradle/wrapper/gradle-wrapper.jar "$@"
else
    # Fallback if wrapper is missing in this simplified environment
    gradle "$@"
fi
