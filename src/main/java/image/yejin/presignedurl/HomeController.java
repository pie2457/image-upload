package image.yejin.presignedurl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/image")
	public String multipartS3() {
		return "multipart-upload-s3";
	}
}
