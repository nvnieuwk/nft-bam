# Usage

The plugin adds one new function to be able to parse the SAM/BAM/CRAM files correctly. This function is called `bam()`.

## `bam()` function

The function has one mandatory argument:

- The BAM/CRAM/SAM file used as output of the process/workflow

```groovy title="main.nf.test"
then {
    bam("<process or workflow output that contains the SAM/BAM/CRAM file>")
}
```

The function has one optional argument:

- The reference FASTA file. This is necessary for some operations on CRAM files.

```groovy title="main.nf.test"
then {
    bam("<SAM/BAM/CRAM>", "<reference_fasta>")
}
```

The fasta can either be a local file or a file URL (currently only supports HTTP and HTTPS protocols)

The function can also be called as `sam()` or `cram()`. These are simply aliases of the `bam()` function and will do exactly the same thing.

Additionally, the `stringency` option can also be used to set the validation stringency of the HTSJDK library. This can be used to silence the validation errors emitted when an alignment file isn't correct. This options accepts 3 possible values: `lenient`, `silent` and `strict`(default).

```groovy title="main.nf.test"
then(
    bam("<SAM/BAM/CRAM>", stringency: "lenient")
)
```

This will create an `AlignmentFile` object which has several methods to access the content of the SAM/BAM/CRAM file.

### `.getHeader()` method

The `.getHeader()` method returns a list of all header lines:

=== "main.nf.test"

    ```groovy
    then {
        bam("...").getHeader()
    }
    ```

=== "Example output"

    ```groovy
    [
        "@HD\tVN:1.6\tSO:unsorted",
        "@SQ\tSN:MT192765.1\tLN:29829",
        "@RG\tID:1\tLB:lib1\tPL:ILLUMINA\tSM:test\tPU:barcode1",
        "@PG\tID:minimap2\tPN:minimap2\tVN:2.17-r941\tCL:minimap2 -ax sr tests/data/fasta/sarscov2/GCA_011545545.1_ASM1154554v1_genomic.fna tests/data/fastq/dna/sarscov2_1.fastq.gz tests/data/fastq/dna/sarscov2_2.fastq.gz",
        "@PG\tID:samtools\tPN:samtools\tPP:minimap2\tVN:1.11\tCL:samtools view -Sb sarscov2_aln.sam"
    ]
    ```

### `.getHeaderMD5()` method

The `.getHeaderMD5()` method returns the MD5 checksum of the header:

=== "main.nf.test"

    ```groovy
    then {
        bam("...").getHeaderMD5()
    }
    ```

=== "Example output"

    ```groovy
    "4ab674cd5f921b2b5c68642aeb0ab3f4"
    ```

### `.getReads()` method

!!! example "A reference is needed here for CRAM files"

The `.getReads()` method returns a list of all raw reads from the alignment file:

=== "main.nf.test"

    ```groovy
    then {
        bam("...").getReads()
    }
    ```

=== "Example output"

    ```groovy
    [
        "ACGCACATTGCTAACTAAGGGCACACTAGAACCAGAATATTTCAATTCAGTGTGTAGACTTATGAAAACTATAGGTCCAGACATGTTCCTCGGAACTTGTCGGCGTTGTCCTGCTGAAATTGTTGACACTGTGAGTGCTTTGGTTTATGA",
        "ATGTGTACATTGGCGACCCTGCTCAATTACCTGCACCACGCACATTGCTAACTAAGGGCACACTAGAACCAGAATATTTCAATTCAGTGTGTAGACTTATGAAAACTATAGGTCCAGACATGTTCCTCGGAACTTGTCGGCGTTGTCCTG",
        "GCATAGACGGTGCTTTACTTACAAAGTCCTCAGAATACAAAGGTCCTATTACGGATGTTTTCTACAAAGAAAACAGT",
        "GCATAGACGGTGCTTTACTTACAAAGTCCTCAGAATACAAAGGTCCTATTACGGATGTTTTCTACAAAGAAAACAGT",
        "TAGGTGAGTTAGGTGATGTTAGAGAAACAATGAGTTACTTGTTTCAACATGCCAATTTAGATTCTTGCAAAAGAGTCTTGAACGTGGTGTGTAAAACTTGTGGACAACAGCAGACAACCCTTAAGGGTGTAGAAGCTGTTATGTAC",
        "TTACAGAGCAAGGGCTGGTGAAGCTGCTAACTTTTGTGCACTTATCTTAGCCTACTGTAATAAGACAGTAGGTGAGTTAGGTGATGTTAGAGAAACAATGAGTTACTTGTTTCAACATGCCAATTTAGATTCTTGCAAAAGAGTCTTGAA",
        "GTCTACAAGCTGGTAATGCAACAGAAGTGCCTGCCAATTCAACTGTATTATCTTTCTGTGCTTTTGCTGTAGATGCTGCTAAAGCTTACAAAGATTATCTAGCTAGTGGGGGACAACCAATCACTAATTGTG",
        "GTCTACAAGCTGGTAATGCAACAGAAGTGCCTGCCAATTCAACTGTATTATCTTTCTGTGCTTTTGCTGTAGATGCTGCTAAAGCTTACAAAGATTATCTAGCTAGTGGGGGACAACCAATCACTAATTGTG",
        "AACCAGAATATTTCAATTCAGTGTGTAGACTTATGAAAACTATAGGTCCAGACATGTTCCTCGGAACTTGTCGGCGTTGT",
        "AACCAGAATATTTCAATTCAGTGTGTAGACTTATGAAAACTATAGGTCCAGACATGTTCCTCGGAACTTGTCGGCGTTGTCC",
        "ACTTTCCAAAGTGCAGTCAAAAGAACAATCACGGGTACACACCACTGGTTGTTACTCACAATTTTGACTTCACTTTTAG",
        ...
    ]
    ```

You can also supply an integer to the function to limit the amount of reads the function returns:

=== "main.nf.test"

    ```groovy
    then {
        bam("...").getReads(2)
    }
    ```

=== "Example output"

    ```groovy
    [
        "ACGCACATTGCTAACTAAGGGCACACTAGAACCAGAATATTTCAATTCAGTGTGTAGACTTATGAAAACTATAGGTCCAGACATGTTCCTCGGAACTTGTCGGCGTTGTCCTGCTGAAATTGTTGACACTGTGAGTGCTTTGGTTTATGA",
        "ATGTGTACATTGGCGACCCTGCTCAATTACCTGCACCACGCACATTGCTAACTAAGGGCACACTAGAACCAGAATATTTCAATTCAGTGTGTAGACTTATGAAAACTATAGGTCCAGACATGTTCCTCGGAACTTGTCGGCGTTGTCCTG"
    ]
    ```

### `.getReadsMD5()` method

The `.getReadsMD5()` method returns the MD5 checksum of the raw reads:

=== "main.nf.test"

    ```groovy
    then {
        bam("...").getReadsMD5()
    }
    ```

=== "Example output"

    ```groovy
    "762e859a3d0ed1553655cde77665c940"
    ```

### `.getSamLines()` method

!!! example "A reference is needed here for CRAM files"

The `.getSamLines()` method returns a list of all lines from the alignment file:

=== "main.nf.test"

    ```groovy
    then {
        bam("...").getSamLines()
    }
    ```

=== "Example output"

    ```groovy
    [
        "ERR5069949.2151832\t83\tMT192765.1\t17453\t60\t150M\t=\t17416\t-187\tACGCACATTGCTAACTAAGGGCACACTAGAACCAGAATATTTCAATTCAGTGTGTAGACTTATGAAAACTATAGGTCCAGACATGTTCCTCGGAACTTGTCGGCGTTGTCCTGCTGAAATTGTTGACACTGTGAGTGCTTTGGTTTATGA\tAAAA<EEEEEEAEEEAEAAAAEEEEEEEEEAAAEE<EEEEEAAEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEAEEEEAAEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEAAAAAA\ts1:i:183\ts2:i:0\tRG:Z:1\tAS:i:300\tde:f:0.0\trl:i:0\tcm:i:13\tnn:i:0\ttp:A:P\tms:i:300\n",
        "ERR5069949.2151832\t163\tMT192765.1\t17416\t60\t150M\t=\t17453\t187\tATGTGTACATTGGCGACCCTGCTCAATTACCTGCACCACGCACATTGCTAACTAAGGGCACACTAGAACCAGAATATTTCAATTCAGTGTGTAGACTTATGAAAACTATAGGTCCAGACATGTTCCTCGGAACTTGTCGGCGTTGTCCTG\tAAAAAEEEEEEEEEE/EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEAEEEEEEEEEE/EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEAAEEEEEEEAEEEEEAAEEEEEEEEEAAEAAA<<EAAEEEEEEEAAA<<<AE\ts1:i:183\ts2:i:47\tRG:Z:1\tAS:i:300\tde:f:0.0\trl:i:0\tcm:i:14\tnn:i:0\ttp:A:P\tms:i:300\n",
        "ERR5069949.576388\t83\tMT192765.1\t5798\t50\t77M\t=\t5798\t-77\tGCATAGACGGTGCTTTACTTACAAAGTCCTCAGAATACAAAGGTCCTATTACGGATGTTTTCTACAAAGAAAACAGT\tEA/AEEE/<EEEEEEEEEEEAA<EEEEEEEEEEEEEEEEEEEEEAEEEEEAEEEAEE6/EEEAEEEEEEEEEA6AAA\ts1:i:62\ts2:i:0\tRG:Z:1\tAS:i:154\tde:f:0.0\trl:i:0\tcm:i:1\tnn:i:0\ttp:A:P\tms:i:154\n",
        "ERR5069949.576388\t163\tMT192765.1\t5798\t60\t77M\t=\t5798\t77\tGCATAGACGGTGCTTTACTTACAAAGTCCTCAGAATACAAAGGTCCTATTACGGATGTTTTCTACAAAGAAAACAGT\tAAAAA6EEAEEEEEAEEAEEAEEEEEEA6EEEEAEEAEEEEE6EEEEEEAEEEEA///A<<EEEEEEEEEAEEEEEE\ts1:i:62\ts2:i:0\tRG:Z:1\tAS:i:154\tde:f:0.0\trl:i:0\tcm:i:10\tnn:i:0\ttp:A:P\tms:i:154\n",
        ...
    ]
    ```

You can also supply an integer to the function to limit the amount of lines the function returns:

=== "main.nf.test"

    ```groovy
    then {
        bam("...").getSamLines(2)
    }
    ```

=== "Example output"

    ```groovy
    [
        "ERR5069949.2151832\t83\tMT192765.1\t17453\t60\t150M\t=\t17416\t-187\tACGCACATTGCTAACTAAGGGCACACTAGAACCAGAATATTTCAATTCAGTGTGTAGACTTATGAAAACTATAGGTCCAGACATGTTCCTCGGAACTTGTCGGCGTTGTCCTGCTGAAATTGTTGACACTGTGAGTGCTTTGGTTTATGA\tAAAA<EEEEEEAEEEAEAAAAEEEEEEEEEAAAEE<EEEEEAAEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEAEEEEAAEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEAAAAAA\ts1:i:183\ts2:i:0\tRG:Z:1\tNM:i:0\tAS:i:300\tde:f:0.0\trl:i:0\tcm:i:13\tnn:i:0\ttp:A:P\tms:i:300\n",
        "ERR5069949.2151832\t163\tMT192765.1\t17416\t60\t150M\t=\t17453\t187\tATGTGTACATTGGCGACCCTGCTCAATTACCTGCACCACGCACATTGCTAACTAAGGGCACACTAGAACCAGAATATTTCAATTCAGTGTGTAGACTTATGAAAACTATAGGTCCAGACATGTTCCTCGGAACTTGTCGGCGTTGTCCTG\tAAAAAEEEEEEEEEE/EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEAEEEEEEEEEE/EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEAAEEEEEEEAEEEEEAAEEEEEEEEEAAEAAA<<EAAEEEEEEEAAA<<<AE\ts1:i:183\ts2:i:47\tRG:Z:1\tNM:i:0\tAS:i:300\tde:f:0.0\trl:i:0\tcm:i:14\tnn:i:0\ttp:A:P\tms:i:300\n"
    ]
    ```

### `.getSamLinesMD5()` method

The `.getSamLinesMD5()` method returns the MD5 checksum of the plain SAM lines:

=== "main.nf.test"

    ```groovy
    then {
        bam("...").getSamLinesMD5()
    }
    ```

=== "Example output"

    ```groovy
    "448bf7ab4ce921321beca4ca3bd78f06"
    ```

### `.getFileType()` method

The `.getFileType()` method returns the type ("SAM", "BAM" or "CRAM") of the input file:

=== "main.nf.test"

    ```groovy
    then {
        bam("test.bam").getFileType()
    }
    ```

=== "Example output"

    ```groovy
    "BAM"
    ```

### `.getStatistics()` method

The `.getStatistics()` method returns a Map structure containing several statistics for the input file:

=== "main.nf.test"

    ```groovy
    then {
        bam("test.bam").getStatistics()
    }
    ```

=== "Example output"

    ```groovy
    {
        "maxReadLength": 151,
        "minReadLength": 53,
        "meanReadLength": 138,
        "maxQuality": 60,
        "minQuality": 0,
        "meanQuality": 57,
        "readCount": 200,
        "duplicateReadCount": 345,
    }
    ```

This method contains some additional options:

1. `include`: this option takes a list of statistic names (the names in the returned map) and returns a map containing these values.
2. `exclude`: this option takes a list of statistic names and returns a map without these values.

Examples:

=== "include"

    ```groovy
    then {
        bam("test.bam").getStatistics(include:["readCount","meanQuality"])
    }
    ```

=== "exclude"

    ```groovy
    then {
        bam("test.bam").getStatistics(exclude:["maxReadLength","minReadLength"])
    }
    ```

### Supplying a `s3://` reference

The plugin also supports `s3://` references for the reference fasta file. This can be used to download the reference fasta file from an S3 bucket. By default it will look at `us-east-1` as the region, for non-public buckets specify the region & credentials in the `~/.aws/credentials` file or in [system environment variables](https://docs.aws.amazon.com/cli/v1/userguide/cli-configure-envvars.html).

```groovy title="main.nf.test"
then {
    bam("test.bam", "s3://bucket-name/reference.fasta")
}
```

## Examples

Have a look at the [plugin tests](https://github.com/nvnieuwk/nft-bam/tree/main/tests/copy_bam) to see some example implementations.
