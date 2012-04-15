# The Jenkins Job Config Generator
The Jenkins Job Config Generator is a small eclipse RCP based Java program that allows you to define your jobs with a textual notation.

This is much faster than clicking around in the Jenkins UI - especially, when you have a set of jobs that are similar or common configuration you want to share.

The DSL for the configurations therefor provides mechanism to inherit basic configurations.

The so defined configurations are generated as config.xml files and can simply copied to ${JENKINS\_HOME}/jobs 

## Building

1. run ./build.sh

## License

This software is distributed under the [Eclipse Public License - v 1.0](http://www.eclipse.org/legal/epl-v10.html).
