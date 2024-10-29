package nvnieuwk.nf.test.bam;

import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;

import htsjdk.samtools.reference.FastaSequenceIndexCreator;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

public class Methods {

	public static AlignmentFile bam(LinkedHashMap<String,Object> options, CharSequence bamFile, CharSequence reference) throws URISyntaxException, MalformedURLException, IOException {
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

		} else if(refString.startsWith("s3:/")) {
			try {
				String s3Path = refString.replace("s3://", "").replace("s3:/", "");
				String[] parts = s3Path.split("/", 2);
				if (parts.length != 2) {
					throw new IllegalArgumentException("Invalid S3 URI format. Expected: s3://bucket-name/key");
				}
				String bucketName = parts[0];
				String key = parts[1];

				Region region;
				// To improve later on when we find out how we can set it from the nextflow.config
				// if is.set(params.aws_region) {
				//	region = Region.of(params.aws_region)
				// } else {
				try {
					region = new DefaultAwsRegionProviderChain().getRegion();
				} catch (SdkClientException e) {
					System.out.println("\n \u001B[33m WARNING: Failed to get the bucket region of " + refString + " defaulting to us-east-1. \n" + e.getMessage() + "\u001B[0m");
					region = Region.US_EAST_1;
				}
				// }
				S3Client s3Client = Utils.createS3Client(region);

				final String dir = System.getProperty("java.io.tmpdir") + "/nft-bam-s3files/";
				final File dirFile = new File(dir);
				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}

				String fileName = key.substring(key.lastIndexOf("/") + 1).trim();
				final String copyRefName = dir + fileName;
				referencePath = Paths.get(copyRefName);

				// Download the reference file from S3
				GetObjectRequest getObjectRequest = GetObjectRequest.builder()
					.bucket(bucketName)
					.key(key)
					.build();

				ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
				Files.copy(s3Object, referencePath, StandardCopyOption.REPLACE_EXISTING);

				createRefIndex(referencePath);

			} catch (Exception e) {
				throw new IOException("Failed to download file from S3: \n" + e.getMessage(), e);
			}
		} else if(reference != "") {
			referencePath = Paths.get(refString);
			if(!(new File(refString + ".fai").exists())) {
				createRefIndex(referencePath);
			}
		} else {
			referencePath = null;
		}
		return new AlignmentFile(options, bamPath, referencePath);
	}

	public static AlignmentFile bam(CharSequence bamFile) throws URISyntaxException, MalformedURLException, IOException {
		return bam(new LinkedHashMap<>(), bamFile, "");
	}

	public static AlignmentFile bam(CharSequence bamFile, CharSequence reference) throws URISyntaxException, MalformedURLException, IOException {
		return bam(new LinkedHashMap<>(), bamFile, reference);
	}

	public static AlignmentFile cram(CharSequence cramFile, CharSequence reference) throws URISyntaxException, MalformedURLException, IOException {
		return bam(new LinkedHashMap<>(), cramFile, reference);
	}

	public static AlignmentFile cram(CharSequence cramFile) throws URISyntaxException, MalformedURLException, IOException {
		return bam(new LinkedHashMap<>(), cramFile, "");
	}

	public static AlignmentFile sam(CharSequence samFile, CharSequence reference) throws URISyntaxException, MalformedURLException, IOException {
		return bam(new LinkedHashMap<>(), samFile, reference);
	}

	public static AlignmentFile sam(CharSequence samFile) throws URISyntaxException, MalformedURLException, IOException {
		return bam(new LinkedHashMap<>(), samFile, "");
	}

	public static AlignmentFile bam(LinkedHashMap<String,Object> options, CharSequence bamFile) throws URISyntaxException, MalformedURLException, IOException {
		return bam(options, bamFile, "");
	}

	public static AlignmentFile cram(LinkedHashMap<String,Object> options, CharSequence cramFile, CharSequence reference) throws URISyntaxException, MalformedURLException, IOException {
		return bam(options, cramFile, reference);
	}

	public static AlignmentFile cram(LinkedHashMap<String,Object> options, CharSequence cramFile) throws URISyntaxException, MalformedURLException, IOException {
		return bam(options, cramFile, "");
	}

	public static AlignmentFile sam(LinkedHashMap<String,Object> options, CharSequence samFile, CharSequence reference) throws URISyntaxException, MalformedURLException, IOException {
		return bam(options, samFile, reference);
	}

	public static AlignmentFile sam(LinkedHashMap<String,Object> options, CharSequence samFile) throws URISyntaxException, MalformedURLException, IOException {
		return bam(options, samFile, "");
	}

	private static void createRefIndex(Path fasta) throws IOException {
		FastaSequenceIndexCreator.create(fasta, true);
	}

}