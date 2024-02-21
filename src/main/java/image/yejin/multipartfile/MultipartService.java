package image.yejin.multipartfile;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import image.yejin.multipartfile.Image.ImageFile;
import image.yejin.multipartfile.Image.ImageUploader;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MultipartService {

	private final ImageUploader imageUploader;

	@Async(value = "image-thread-pool")
	public String uploadImage(MultipartFile multipartFile) {
		ImageFile imageFile = ImageFile.from(multipartFile);
		return imageUploader.uploadImageToS3(imageFile);
	}

	public List<String> uploadImages(List<MultipartFile> multipartFiles) {
		List<ImageFile> imageFiles = ImageFile.from(multipartFiles);
		return imageUploader.uploadImagesToS3(imageFiles);
	}
}
