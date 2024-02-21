package image.yejin.infrastructure.config.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.Getter;

@Getter
@ConfigurationProperties("aws")
public class S3Properties {

	private final Credentials credentials;
	private final S3 s3;
	private final String region;

	@ConstructorBinding
	public S3Properties(Credentials credentials, S3 s3, Map<String, String> region) {
		this.credentials = credentials;
		this.s3 = s3;
		this.region = region.get("static");
	}

	public record Credentials(
		String accessKey,
		String secretKey
	) {
	}

	public record S3(
		String bucket
	) {
	}
}
