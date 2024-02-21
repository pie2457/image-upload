package image.yejin.multipartfile;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/image/multipart-file")
@RequiredArgsConstructor
public class MultipartController {

	private final MultipartService multipartService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public List<String> upload(@RequestPart("images") List<MultipartFile> multipartFiles) {
		return multipartService.uploadImages(multipartFiles);
	}
}
