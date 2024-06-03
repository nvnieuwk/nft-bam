package nvnieuwk.nf.test.bam;

import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;

import htsjdk.samtools.reference.FastaSequenceIndexCreator;

public class Methods {

	public static AlignmentFile bam(CharSequence bamFile, CharSequence reference) throws URISyntaxException, MalformedURLException, IOException {
		final Path bamPath = Paths.get(bamFile.toString());
		final String refString = reference.toString();
		Path referencePath;
		if(refString.startsWith("https://") || refString.startsWith("http://")) {
			// Download and access a reference file given through a URL
			InputStream refStream = new URL(refString).openStream();
			final String dir = System.getProperty("java.io.tmpdir") + "/nft-bam-urlfiles/";
			final File dirFile = new File(dir);
			if(!dirFile.exists()) {
				dirFile.mkdirs();
			}
			final String copyRefName = dir + refString.substring(refString.lastIndexOf("/") + 1).trim();
			Files.copy(refStream, Paths.get(copyRefName), StandardCopyOption.REPLACE_EXISTING);
			referencePath = Paths.get(copyRefName);

			// Handle index fetching or creation
			final String indexString = refString + ".fai";
			final URL indexUrl = new URL(indexString);
			final HttpURLConnection huc = (HttpURLConnection) indexUrl.openConnection();
			if(huc.getResponseCode() == 200) {
				InputStream indexStream = new URL(indexString).openStream();
				final String copyIndexName = dir + indexString.substring(indexString.lastIndexOf("/") + 1).trim();
				Files.copy(indexStream, Paths.get(copyIndexName), StandardCopyOption.REPLACE_EXISTING);
			} else {
				createRefIndex(referencePath);
			}
		} else if(reference != "") {
			referencePath = Paths.get(refString);
			if(!(new File(refString + ".fai").exists())) {
				createRefIndex(referencePath);
			}
		} else {
			referencePath = null;
		}
		return new AlignmentFile(bamPath, referencePath);
	}

	public static AlignmentFile bam(CharSequence bamFile) throws URISyntaxException, MalformedURLException, IOException {
		return bam(bamFile, "");
	}

	public static AlignmentFile cram(CharSequence cramFile, CharSequence reference) throws URISyntaxException, MalformedURLException, IOException {
		return bam(cramFile, reference);
	}

	public static AlignmentFile cram(CharSequence cramFile) throws URISyntaxException, MalformedURLException, IOException {
		return bam(cramFile, "");
	}

	public static AlignmentFile sam(CharSequence samFile, CharSequence reference) throws URISyntaxException, MalformedURLException, IOException {
		return bam(samFile, reference);
	}

	public static AlignmentFile sam(CharSequence samFile) throws URISyntaxException, MalformedURLException, IOException {
		return bam(samFile, "");
	}

	private static void createRefIndex(Path fasta) throws IOException {
		FastaSequenceIndexCreator.create(fasta, true);
	}

}