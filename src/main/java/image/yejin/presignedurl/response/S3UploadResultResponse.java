package image.yejin.presignedurl.response;

public record S3UploadResultResponse(
	String url,
	String name,
	Long size
) {
}
