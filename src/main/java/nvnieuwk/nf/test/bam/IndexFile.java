package nvnieuwk.nf.test.bam;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import htsjdk.samtools.BAMIndex;
import htsjdk.samtools.DiskBasedBAMFileIndex;
import htsjdk.samtools.BAMIndexMetaData;
import htsjdk.samtools.SamIndexes;
import htsjdk.samtools.SAMSequenceDictionary;
import htsjdk.samtools.seekablestream.SeekableStream;
import htsjdk.samtools.seekablestream.SeekableFileStream;

public class IndexFile {

    private static SeekableStream indexStream;

    IndexFile(CharSequence indexName) throws IOException {
        File indexFile = new File(indexName.toString());
        if (!indexFile.exists()) {
            throw new IllegalArgumentException("The index file does not exist: " + indexName);
        }
        SAMSequenceDictionary dict = new SAMSequenceDictionary();
        indexStream = new SeekableFileStream(indexFile);
        // Load the BAM index
        BAMIndex bamIndex = new DiskBasedBAMFileIndex(indexFile, new SAMSequenceDictionary());
        
        // Get number of references
        BAMIndexMetaData[] meta = BAMIndexMetaData.getIndexStats(new BamFileReader());
        System.out.println(meta.getAlignedRecordCount());
        System.out.println(meta.getUnalignedRecordCount());
    }

    public static String getIndexType() {
        return SamIndexes.getSAMIndexTypeFromStream(indexStream).toString();
    }

}