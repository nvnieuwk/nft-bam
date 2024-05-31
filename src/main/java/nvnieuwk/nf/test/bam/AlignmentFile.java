package nvnieuwk.nf.test.bam;

import java.io.IOException;
import java.nio.file.Path;

import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;
import htsjdk.samtools.SamInputResource;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SAMRecordIterator;

public class AlignmentFile {

    final private static SamReader fileReader

    public static void AlignmentFile(Path alignmentFileName) {
        fileReader = SamReader samReader = SamReaderFactory.makeDefault()
            .open(SamInputResource.of(bamFileName));
    }

	public static String[] getBamHeader() throws IOException {
        final String[] header = fileReader.getFileHeader().getSAMString().split("\n");
        fileReader.close();
		return header;
	}

	public static ArrayList<String> getBamReads() throws IOException {
		final SAMRecordIterator recordsIterator = fileReader.iterator();
		ArrayList<String> reads = new ArrayList<String>();
		while(recordsIterator.hasNext()) {
			reads.add(recordsIterator.next().getReadString());
		}
        fileReader.close();
		return reads;
	}

}