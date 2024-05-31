process COPY_BAM {
    input:
    path(bam)

    output:
    path("*.{bam,cram,sam}"), emit: bam

    script:
    """
    cp $bam ${bam.baseName}.copy.${bam.extension}
    """
}