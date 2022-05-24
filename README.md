scl
===

Run your Java without compilation

## Installation

```
mvn clean package install
sudo ln -s `pwd`/scl.sh /usr/local/bin/scl
```

## How to use


```
$ scl --help

usage: scl
 -cp,--classpath <arg>    class path location
 -d,--build <arg>         where to store compiled classes
 -ea,--enableassertions   enable assertions processing
 -h,--help                this help
 -sp,--sourcepath <arg>   source path locations
 -v,--verbose             enable more output
```

### Examples

```
❯❯❯ ./scl.sh -sp sourcepath AssertTest
Hello
assertions disabled

❯❯❯ ./scl.sh -ea -sp sourcepath AssertTest
Hello
assertions enabled

❯❯❯ ./scl.sh -sp sourcepath org.linuxgears.Test
yo
```