#!/bin/bash

set -e

mvn --batch-mode -U -f build/pom.xml clean install
mvn --batch-mode -U clean generate-sources -Pgen-xtext
mvn --batch-mode -U install

cp -R products/de.hajoeichler.jenkins.jobconfig.product/target/products/jenkinsJobConfigGen.product/macosx/cocoa/x86_64/ .

zip -r JenkinsJobConfigGenerator.zip jenkinsJobConfigGenerator/*
