package image.yejin.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import image.yejin.infrastructure.config.properties.S3Properties;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@EnableConfigurationProperties(S3Properties.class)
public class S3Config {

	@Bean
	public AmazonS3Client amazonS3Client(S3Properties s3Properties) {
		BasicAWSCredentials credentials = new BasicAWSCredentials(s3Properties.getCredentials().accessKey(),
			s3Properties.getCredentials().secretKey());

		return (AmazonS3Client)AmazonS3ClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(credentials))
			.withRegion(s3Properties.getRegion())
			.build();
	}

	@Bean
	public S3Client s3Client(S3Properties s3Properties) {
		AwsBasicCredentials credentials = getCredentials(s3Properties);

		return S3Client.builder()
			.region(Region.of(s3Properties.getRegion()))
			.credentialsProvider(StaticCredentialsProvider.create(credentials))
			.build();
	}

	@Bean
	public S3Presigner s3Presigner(S3Properties s3Properties) {
		AwsBasicCredentials credentials = getCredentials(s3Properties);

		return S3Presigner.builder()
			.region(Region.of(s3Properties.getRegion()))
			.credentialsProvider(StaticCredentialsProvider.create(credentials))
			.build();
	}

	private AwsBasicCredentials getCredentials(S3Properties s3Properties) {
		return AwsBasicCredentials.create(s3Properties.getCredentials().accessKey(),
			s3Properties.getCredentials().secretKey());
	}
}
