package nvnieuwk.nf.test.bam;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Methods {

	public static AlignmentFile bam(CharSequence bamFile, CharSequence reference) {
		final Path bamPath = Paths.get(bamFile.toString());
		final Path referencePath = reference != "" ? Paths.get(reference.toString()) : null;
		return new AlignmentFile(bamPath, referencePath);
	}

	public static AlignmentFile bam(CharSequence bamFile) {
		return bam(bamFile, "");
	}

	public static AlignmentFile cram(CharSequence cramFile, CharSequence reference) {
		return bam(cramFile, reference);
	}

	public static AlignmentFile cram(CharSequence cramFile) {
		return bam(cramFile, "");
	}

	public static AlignmentFile sam(CharSequence samFile, CharSequence reference) {
		return bam(samFile, reference);
	}

	public static AlignmentFile sam(CharSequence samFile) {
		return bam(samFile, "");
	}

}