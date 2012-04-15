#!/bin/bash
mvn --batch-mode -f build/de.hajoeichler.jenkins.jobconfig.platform/pom.xml clean install
mvn --batch-mode -f plugins/pom.xml -U clean generate-sources -Pgen-xtext
mvn --batch-mode clean install
