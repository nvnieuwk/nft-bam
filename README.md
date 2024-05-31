# nft-bam
An nf-test plugin for reading the contents of SAM/BAM/CRAM files in assertions

Full documentation coming later!

How to use the plugin:

1. Git clone this repository and run `./build.sh`
2. Add the full path to the `target/nft-bam-1.0.0.jar` to the `nf-test.config` file:

```java
config {

    testsDir "tests"
    workDir ".nf-test"
    configFile "tests/nextflow.config"
    profile ""
    plugins {
        loadFromFile "full/path/to/the/plugin/jar"
    }

}

```

3. Have a look at the [test implementation](tests/copy_bam/main.nf.test) on how to use the plugin
