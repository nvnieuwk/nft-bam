package nvnieuwk.nf.test.bam;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Methods {

	public static AlignmentFile bam(String bamFile) {
		final Path bamPath = Paths.get(bamFile);
		return new AlignmentFile(bamPath);
	}
	
}