@echo off

set MAVEN_REPO=%M2_REPO%
set CLASSPATH="%MAVEN_REPO%\.m2\repository\commons-cli\commons-cli\1.2\commons-cli-1.2.jar;%MAVEN_REPO%\.m2\repository\org\linuxgears\scl\1.0-SNAPSHOT\scl-1.0-SNAPSHOT.jar"

java -Dorg.linuxgears.scl.java.compiler=%JAVA_HOME%\bin\javac ^
    -cp %CLASSPATH% ^
    org.linuxgears.App ^
    %*
