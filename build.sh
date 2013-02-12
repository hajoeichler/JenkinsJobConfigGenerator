#!/bin/bash

set -e

usage() {
    echo "$(basename $0) - Builds Jenkins Job Config Generator and generates the configs."
    echo ""
    echo "Arguments:"
    echo "-h: show this help text"
    echo "Optional:"
    echo "-c <config dir>: directory of your configuration files. Otherwise only generator will be built"
    echo "-i: top create installer (currelty only DMG on a Mac is supported.)"
}

build_generator() {
    mvn --batch-mode -U -f build/pom.xml clean install
    mvn --batch-mode -U clean generate-sources -Pgen-xtext
    mvn --batch-mode -U install
}

build_config() {
    mvn --batch-mode install -f plugins/pom.xml -Djob.config.file="${CONFIG_DIR}"
}

build_installer() {
    mvn --batch-mode -U install -Pcreate-installer
}

while getopts "hc:i" OPT; do
    case "${OPT}" in
        h)
            usage
            exit 1
            ;;
        c)
            readonly CONFIG_DIR="${OPTARG}"
            ;;
        i)
            readonly CREATE_INSTALLER="true"
            ;;
    esac
done

build_generator
if [ -n "${CONFIG_DIR}" ]; then
    build_config
fi
if [ "${CREATE_INSTALLER}" = "true" ]; then
    build_installer
fi
