package image.yejin.application.unit;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3Client;

import image.yejin.infrastructure.config.properties.S3Properties;
import image.yejin.multipartfile.Image.ImageFile;
import image.yejin.multipartfile.Image.ImageUploader;

@Profile("test")
@Component
public class FakeImageUploader extends ImageUploader {

	public FakeImageUploader(AmazonS3Client amazonS3Client, S3Properties s3Properties) {
		super(amazonS3Client, s3Properties);
	}

	@Override
	public String uploadImageToS3(ImageFile imageFile) {
		return imageFile.getFileName();
	}

	@Override
	public List<String> uploadImagesToS3(List<ImageFile> imageFile) {
		return imageFile.stream()
			.map(ImageFile::getFileName)
			.toList();
	}
}
