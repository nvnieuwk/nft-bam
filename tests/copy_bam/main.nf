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

process COPY_INDEX {
    input:
    path(index)

    output:
    path("*.{bai,crai,csi}"), emit: index

    script:
    """
    cp $index copy_${index.baseName}.${index.extension}
    """
}