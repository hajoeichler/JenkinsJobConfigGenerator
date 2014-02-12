Jenkins Job Config Generator
============================

The Jenkins Job Config Generator is a small eclipse RCP based Java program that allows you to define your jobs with a textual notation.

This is much faster than clicking around in the Jenkins UI - especially, when you have a set of jobs that are similar or common configuration you want to share.

The DSL for the configurations therefor provides mechanism to inherit basic configurations.

The so defined configurations are generated as `config.xml` files and can simply copied to `${JENKINS\_HOME}/jobs`

## Build

[![Build Status](https://hajo.ci.cloudbees.com/job/JenkinsJobConfigurator/badge/icon)](https://hajo.ci.cloudbees.com/job/JenkinsJobConfigurator/)

## Building

1. Just run `./build.sh`
  * Use `-c` to generate your configuration.
  * Use `-i` on a Mac to create a DMG installer for the tool.
1. Mac OS X product can be started via `./run.sh`

## Development

This little section should help to get familiar with the architecture and should enable you to enhance the generator.

The grammar for the configuration DSL is defined using [xtext](https://www.eclipse.org/Xtext/) in the file [JobConfig.xtext](https://github.com/hajoeichler/JenkinsJobConfigGenerator/blob/master/plugins/de.hajoeichler.jenkins.jobconfig/src/de/hajoeichler/jenkins/JobConfig.xtext)

Out of this grammar an EMF model (aka. AST) is generated.

Each define job is then an instance of this model. Those instances are passed to the actually generator.

## License

This software is distributed under the [Eclipse Public License - v 1.0](http://www.eclipse.org/legal/epl-v10.html).
