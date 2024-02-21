package image.yejin.presignedurl.request;

public record S3UploadPartsDetail(
	String awsETag,
	int partNumber
) {
}
