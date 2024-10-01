#!/bin/bash

rm -f target/*.jar
mvn package

echo "Built $(readlink -f target/nft-bam*.jar)"