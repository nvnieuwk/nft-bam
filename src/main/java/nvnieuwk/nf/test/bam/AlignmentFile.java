package nvnieuwk.nf.test.bam;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Arrays;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;
import htsjdk.samtools.ValidationStringency;
import htsjdk.samtools.SamInputResource;
import htsjdk.samtools.SAMRecordIterator;

public class AlignmentFile {

	private static SamReaderFactory factory;
    private static SamReader fileReader;

    public AlignmentFile(LinkedHashMap<String,Object> options, Path alignmentFile, Path reference) {
		factory = SamReaderFactory.makeDefault();
		if(reference != null) {
	        factory.referenceSequence(reference);
		}
		if(options.containsKey("stringency")) {
			String stringency = options.get("stringency").toString().toUpperCase();
			if( !Arrays.asList("STRICT", "LENIENT", "SILENT").contains(stringency) ) {
				throw new IllegalArgumentException("The validation stringency should be one of these: 'STRICT', 'LENIENT', 'SILENT'. Found: " + stringency);
			}
			factory.validationStringency(ValidationStringency.valueOf(stringency));
		}
		SamInputResource inputFile = SamInputResource.of(alignmentFile);
		fileReader = factory.open(inputFile);
	}

	public static String[] getHeader() throws IOException {
        final String[] header = fileReader.getFileHeader().getSAMString().split("\n");
        fileReader.close();
		return header;
	}

	public static String getHeaderMD5() throws IOException, NoSuchAlgorithmException {
        final byte[] header = fileReader.getFileHeader().getSAMString().getBytes("UTF-8");
        fileReader.close();
		MessageDigest md = MessageDigest.getInstance("MD5");
		return new BigInteger(1, md.digest(header)).toString(16);
	}

	public static ArrayList<String> getReads(int linesToReturn) throws IOException {
		final SAMRecordIterator recordsIterator = fileReader.iterator();
		ArrayList<String> reads = new ArrayList<String>();
		int count = 0;
		while(recordsIterator.hasNext() && (count < linesToReturn || linesToReturn == -1)) {
			count++;
			reads.add(recordsIterator.next().getReadString());
		}
        fileReader.close();
		return reads;
	}

	public static ArrayList<String> getReads() throws IOException {
		return getReads(-1);
	}

	public static String getReadsMD5() throws IOException, NoSuchAlgorithmException {
		final SAMRecordIterator recordsIterator = fileReader.iterator();
		MessageDigest md = MessageDigest.getInstance("MD5");
		while(recordsIterator.hasNext()) {
			md.update(recordsIterator.next().getReadString().getBytes("UTF-8"));
		}
        fileReader.close();
		return new BigInteger(1, md.digest()).toString(16);
	}

	public static ArrayList<String> getSamLines(int linesToReturn) throws IOException {
		final SAMRecordIterator recordsIterator = fileReader.iterator();
		ArrayList<String> reads = new ArrayList<String>();
		int count = 0;
		while(recordsIterator.hasNext() && (count < linesToReturn || linesToReturn == -1)) {
			count++;
			reads.add(recordsIterator.next().getSAMString());
		}
        fileReader.close();
		return reads;
	}

	public static ArrayList<String> getSamLines() throws IOException {
		return getSamLines(-1);
	}

	public static String getSamLinesMD5() throws IOException, NoSuchAlgorithmException {
		final SAMRecordIterator recordsIterator = fileReader.iterator();
		MessageDigest md = MessageDigest.getInstance("MD5");
		while(recordsIterator.hasNext()) {
			md.update(recordsIterator.next().getSAMString().getBytes("UTF-8"));
		}
        fileReader.close();
		return new BigInteger(1, md.digest()).toString(16);
	}

	public static String getFileType() {
		return fileReader.type().name();
	}

}