package image.yejin.presignedurl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import image.yejin.infrastructure.config.properties.S3Properties;
import image.yejin.presignedurl.request.S3UploadAbortRequest;
import image.yejin.presignedurl.request.S3UploadCompleteRequest;
import image.yejin.presignedurl.request.S3UploadInitiateRequest;
import image.yejin.presignedurl.request.S3UploadSignedUrlRequest;
import image.yejin.presignedurl.response.S3PreSignedUrlResponse;
import image.yejin.presignedurl.response.S3UploadResponse;
import image.yejin.presignedurl.response.S3UploadResultResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PreSignedController {

	private final S3Properties s3Properties;
	private final PreSignedUrlService preSignedUrlService;

	@PostMapping("/initiate-upload")
	public S3UploadResponse initiateUpload(@RequestBody S3UploadInitiateRequest initiateRequest) {
		return preSignedUrlService.initiateUpload(initiateRequest, s3Properties.getS3().bucket());
	}

	@PostMapping("/upload-signed-url")
	public S3PreSignedUrlResponse getUploadSignedUrl(@RequestBody S3UploadSignedUrlRequest signedUrlRequest) {
		return preSignedUrlService.getUploadSignedUrl(signedUrlRequest, s3Properties.getS3().bucket());
	}

	@PostMapping("/complete-upload")
	public S3UploadResultResponse completeUpload(@RequestBody S3UploadCompleteRequest completeRequest) {
		return preSignedUrlService.completeUpload(completeRequest, s3Properties.getS3().bucket());
	}

	@PostMapping("/abort-upload")
	public void abortUpload(@RequestBody S3UploadAbortRequest abortRequest) {
		preSignedUrlService.abortUpload(abortRequest, s3Properties.getS3().bucket());
	}
}
