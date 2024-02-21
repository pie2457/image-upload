package image.yejin.presignedurl.request;

public record S3UploadAbortRequest(
	String uploadId,
	String fileName
) {
}
