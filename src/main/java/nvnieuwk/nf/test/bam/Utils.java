package nvnieuwk.nf.test.bam;

import java.util.ArrayList;

import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.S3Client;


public class Utils {
    public static ArrayList<String> castToStringArray(Object input) {
        ArrayList<String> result = new ArrayList<String>();
        if (input instanceof ArrayList<?>) {
            ArrayList<?> inputArray = (ArrayList<?>)input;
            for (int i = 0; i < inputArray.size(); i++) {
                Object item = inputArray.get(i);
                if(item instanceof String) {
                    result.add((String)item);
                }
            }
        }
        return result;
    }

    /**
     * Creates an S3 client with a specified region, using AWS's default credentials provider chain.
     * Falls back to anonymous access if no credentials are found.
     *
     * @param region the AWS region to use
     * @return configured S3Client instance
     */
    // create a custom chain that includes the default one and a Anonynoums
    public static S3Client createS3Client(Region region) {

        AwsCredentialsProviderChain providerChain = AwsCredentialsProviderChain.builder()
                .addCredentialsProvider(SystemPropertyCredentialsProvider.create())
                .addCredentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .addCredentialsProvider(AnonymousCredentialsProvider.create())
                .build();

        return S3Client.builder()
                .region(region)
                .credentialsProvider(providerChain)
                .build();
    }
}

