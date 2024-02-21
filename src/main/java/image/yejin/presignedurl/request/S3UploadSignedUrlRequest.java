package image.yejin.presignedurl.request;

public record S3UploadSignedUrlRequest(
	String uploadId,
	String fileName,
	int partNumber
) {
}
