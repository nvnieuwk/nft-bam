package nvnieuwk.nf.test.bam;

import java.io.IOException;
import java.nio.file.Path;

import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;
import htsjdk.samtools.SamInputResource;

public class BamUtils {
    public static SamReader load(Path bamFileName) throws IOException {
        final SamReader samReader = SamReaderFactory.makeDefault()
            .open(SamInputResource.of(bamFileName));
        return samReader;
    }
}