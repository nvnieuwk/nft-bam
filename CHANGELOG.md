# Changelog

## v0.4.0

### New features

1. Added a new `getStatistics()` method to some general statistics:

    - `minReadLength`: The lowest read length
    - `maxReadLength`: The highest read length
    - `meanReadLength`: The mean read length
    - `minMappingQuality`: The lowest mapping quality
    - `maxMappingQuality`: The highest mapping quality
    - `meanMappingQuality`: The mean mapping quality
    - `readCount`: The amount of reads present in the file
    - `duplicateReadCount`: The amount of duplicate reads present in the file


2. adding .gitpod.yml file & precommit file

## v0.3.0

### New features

1. Added the `stringency` option to the `bam()` function and all its aliases. This option takes `lenient`, `silent` and `strict`(default) as possible values and is used to set the validation stringency of the HTSJDK library.

## v0.2.0

### New features

Added some new methods:

1. `getHeaderMD5()`: Returns the MD5 checksum of the header
2. `getReadsMD5()`: Returns the MD5 checksum of the raw reads
3. `getSamLinesMD5()`: Returns the MD5 checksum of the plain SAM lines

## v0.1.1

### Fixes

1. Recompiled the plugin with a lower version of htsjdk to ensure functionality on lower Java versions

## v0.1.0

The first release of nft-bam
