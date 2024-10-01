package nvnieuwk.nf.test.bam;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;
import htsjdk.samtools.ValidationStringency;
import htsjdk.samtools.SamInputResource;
import htsjdk.samtools.SAMRecord;
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
			try {
				factory.validationStringency(ValidationStringency.valueOf(stringency));
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("The validation stringency should be one of these: 'STRICT', 'LENIENT', 'SILENT'. Found: " + stringency);
			}
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

	public LinkedHashMap<String,Object> getStatistics() throws InterruptedException {
		// Read length
		Integer minReadLength = 2000000000; // I don't think we'll have reads this long soon
		Integer maxReadLength = 0;
		Integer totalReadLength = 0;

		// Mapping quality
		Integer totalQuality = 0;
		Integer maxQuality = 0;
		Integer minQuality = 10000; // Mapping quality 10000 should never be reached

		// General stats
		Integer totalReads = 0;

		final SAMRecordIterator recordsIterator = fileReader.iterator();
		while(recordsIterator.hasNext()) {
			totalReads++;
			SAMRecord record = recordsIterator.next();

			// Read length statistics
			Integer readLength = record.getReadLength();
			totalReadLength += readLength;
			if (maxReadLength < readLength) {
				maxReadLength = readLength;
			}
			if (minReadLength > readLength) {
				minReadLength = readLength;
			}

			// Mapping quality statistics
			Integer quality = record.getMappingQuality();
			totalQuality += quality;
			if (maxQuality < quality) {
				maxQuality = quality;
			}
			if (minQuality > quality) {
				minQuality = quality;
			}
		}

		LinkedHashMap<String,Object> result = new LinkedHashMap<String,Object>();
		result.put("maxReadLength", maxReadLength);
		result.put("minReadLength", minReadLength);
		result.put("meanReadLength", totalReadLength / totalReads);
		result.put("maxQuality", maxQuality);
		result.put("minQuality", minQuality);
		result.put("meanQuality", totalQuality / totalReads);
		result.put("readCount", totalReads);

		return result;
	}

}