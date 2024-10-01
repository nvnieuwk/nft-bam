# nft-bam

nft-bam is an nf-test plugin to make working with SAM, BAM or CRAM files easier.

## Start using the plugin

To start using the plugin please add it to your `nf-test.config` file:

```groovy title="nf-test.config"
config {
    plugins {
        load "nft-bam@0.4.0"
    }
}
```

Have a look at the [usage documentation](./usage.md) for more information on how to start working with the plugin.

## Use a development version

To use the development version, please do the following steps:

- Clone the [nft-bam repository](https://github.com/nvnieuwk/nft-bam)

=== "HTTPS"

    ```bash
    git clone git@github.com:nvnieuwk/nft-bam.git
    ```

=== "SSH"

    ```bash
    git clone https://github.com/nvnieuwk/nft-bam.git
    ```

- Run the build script

```bash
./build.sh
```

- Add the jar location (visible at the end of the build script output) to the `nf-test.config` file

```groovy title="nf-test.config"
config {
    plugins {
        loadFromFile "full/path/to/the/plugin/jar"
    }
}
```
