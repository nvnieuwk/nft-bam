package nvnieuwk.nf.test.bam;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import htsjdk.samtools.SamReader;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SAMRecordIterator;

public class PathExtension {

	public static String[] getBamHeader(Path bamFileName) throws IOException {
		final SamReader bamFile = BamUtils.load(bamFileName);
		SAMFileHeader header = bamFile.getFileHeader();
		bamFile.close();
		return header.getSAMString().split("\n");
	}

	public static ArrayList<String> getBamReads(Path bamFileName) throws IOException {
		final SamReader bamFile = BamUtils.load(bamFileName);
		final SAMRecordIterator recordsIterator = bamFile.iterator();
		ArrayList<String> reads = new ArrayList<String>();
		while(recordsIterator.hasNext()) {
			reads.add(recordsIterator.next().getReadString());
		}
		return reads;
	}

}