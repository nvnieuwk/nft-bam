process COPY_BAM {
    input:
    path(bam)

    output:
    path(copy)

    script:
    """
    cp $bam ${bam.baseName}.copy.${bam.extension}
    """
}