package image.yejin.presignedurl.response;

public record S3UploadResponse(
	String uploadId,
	String fileName
) {

	public static S3UploadResponse toEntity(String uploadId, String fileName) {
		return new S3UploadResponse(uploadId, fileName);
	}
}
