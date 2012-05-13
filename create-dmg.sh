#!/bin/bash

set -e

mvn --batch-mode -U -f build/pom.xml clean install
mvn --batch-mode -U clean generate-sources -Pgen-xtext
mvn --batch-mode -U install -Pcreate-installer
