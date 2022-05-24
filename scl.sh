#!/bin/bash -e

_main() {
    local source_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
    local javac=${JAVAC:-/usr/bin/javac}

    M2_REPO=$HOME/.m2/repository

    local java_args=()
    local prog_args=()

    for arg in "$@"; do
        if [[ $arg =~ ^(-X|-D) ]]; then
            java_args+=( $arg )
        else
            prog_args+=( $arg )
        fi
    done

    CLASSPATH=${M2_REPO}/commons-cli/commons-cli/1.2/commons-cli-1.2.jar:${M2_REPO}/org/linuxgears/scl/1.0-SNAPSHOT/scl-1.0-SNAPSHOT.jar

    java -Dorg.linuxgears.scl.java.compiler=${javac} \
        -cp ${CLASSPATH} \
        "${java_args[@]}" \
        org.linuxgears.App \
        "${prog_args[@]}"
}

_main "${@:-}"

# End of file