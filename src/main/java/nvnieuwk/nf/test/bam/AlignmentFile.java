package nvnieuwk.nf.test.bam;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;
import htsjdk.samtools.SamInputResource;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SAMRecordIterator;

public class AlignmentFile {

    private static SamReader fileReader;

    public AlignmentFile(Path alignmentFile, Path reference) {
		if(reference != null) {
	        fileReader = SamReaderFactory.makeDefault()
				.referenceSequence(reference)
        	    .open(SamInputResource.of(alignmentFile));
		} else {
			fileReader = SamReaderFactory.makeDefault()
    	        .open(SamInputResource.of(alignmentFile));
		};
    }

	public static String[] getHeader() throws IOException {
        final String[] header = fileReader.getFileHeader().getSAMString().split("\n");
        fileReader.close();
		return header;
	}

	public static ArrayList<String> getReads() throws IOException {
		final SAMRecordIterator recordsIterator = fileReader.iterator();
		ArrayList<String> reads = new ArrayList<String>();
		while(recordsIterator.hasNext()) {
			reads.add(recordsIterator.next().getReadString());
		}
        fileReader.close();
		return reads;
	}

	public static String getFileType() {
		return fileReader.type().name();
	}

}