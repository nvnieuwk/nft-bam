#!/bin/bash

# Create target folder if it does not exist
mkdir -p target

# Delete old jar files
rm target/*.jar

# Get the version of the plugin 
version=$(grep moduleVersion src/main/resources/META-INF/nf-test-plugin | sed -e 's/moduleVersion=//')

# Create the jar file
jar cvf target/nft-bam-${version}.jar *