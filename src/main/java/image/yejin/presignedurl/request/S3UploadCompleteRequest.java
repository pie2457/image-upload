package image.yejin.presignedurl.request;

import java.util.List;

public record S3UploadCompleteRequest(
	String uploadId,
	String fileName,
	List<S3UploadPartsDetail> parts
) {
}
