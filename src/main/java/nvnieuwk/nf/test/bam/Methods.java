package nvnieuwk.nf.test.bam;

import java.nio.file.Path;

public class Methods {

	public static AlignmentFile bam(String bamFile) {
		return new AlignmentFile(path(bamFile))
	}
	
}