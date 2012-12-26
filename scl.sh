#!/bin/sh

M2_REPO=$HOME/.m2/repository

CLASSPATH=$M2_REPO/commons-cli/commons-cli/1.2/commons-cli-1.2.jar:$M2_REPO/org/linuxgears/scl/1.0-SNAPSHOT/scl-1.0-SNAPSHOT.jar

java -Dorg.linuxgears.scl.java.compiler=$JAVA_HOME/bin/javac \
    -cp $CLASSPATH \
    org.linuxgears.App \
    $@
