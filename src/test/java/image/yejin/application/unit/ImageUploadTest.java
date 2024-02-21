package image.yejin.application.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import image.yejin.multipartfile.Image.ImageFile;
import image.yejin.multipartfile.Image.ImageUploader;
import image.yejin.multipartfile.MultipartService;

@SpringBootTest
public class ImageUploadTest {

	@Autowired
	private MultipartService multipartService;
	@MockBean
	private ImageUploader imageUploader;

	@DisplayName("이미지 파일이 주어지면 업로드에 성공한다. / MultipartFile")
	@Test
	void imageUploadTest() {
		// given
		MockMultipartFile mockMultipartFile = new MockMultipartFile(
			"test-image", "test.png",
			MediaType.IMAGE_PNG_VALUE, "imageBytes".getBytes(StandardCharsets.UTF_8));

		given(imageUploader.uploadImageToS3(any(ImageFile.class))).willReturn("url");

		// when & then
		assertThatCode(() -> multipartService.uploadImage(mockMultipartFile))
			.doesNotThrowAnyException();
	}
}
