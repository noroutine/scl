#!/bin/sh

M2_REPO=$HOME/.m2/repository

java_args=()
prog_args=()

for arg in "$@"; do
    if [[ $arg =~ ^(-X|-D) ]]; then
        java_args=( "${java_args[@]}" $arg )
    else
        prog_args=( "${prog_args[@]}" $arg )
    fi
done

CLASSPATH=$M2_REPO/commons-cli/commons-cli/1.2/commons-cli-1.2.jar:$M2_REPO/org/linuxgears/scl/1.0-SNAPSHOT/scl-1.0-SNAPSHOT.jar

java -Dorg.linuxgears.scl.java.compiler=$JAVA_HOME/bin/javac \
    -cp $CLASSPATH \
    "${java_args[@]}" \
    org.linuxgears.App \
    "${prog_args[@]}"
