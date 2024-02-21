package image.yejin.presignedurl;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;

import image.yejin.presignedurl.request.S3UploadAbortRequest;
import image.yejin.presignedurl.request.S3UploadCompleteRequest;
import image.yejin.presignedurl.request.S3UploadInitiateRequest;
import image.yejin.presignedurl.request.S3UploadPartsDetail;
import image.yejin.presignedurl.request.S3UploadSignedUrlRequest;
import image.yejin.presignedurl.response.S3PreSignedUrlResponse;
import image.yejin.presignedurl.response.S3UploadResponse;
import image.yejin.presignedurl.response.S3UploadResultResponse;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.AbortMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedUploadPartRequest;
import software.amazon.awssdk.services.s3.presigner.model.UploadPartPresignRequest;

@Service
@RequiredArgsConstructor
public class PreSignedUrlService {

	private final static String UPLOADED_IMAGES_DIR = "public/";

	private final S3Client s3Client;
	private final S3Presigner s3Presigner;
	private final AmazonS3Client amazonS3Client;

	public S3UploadResponse initiateUpload(S3UploadInitiateRequest request, String bucket) {
		String originalFileName = request.fileName();
		String fileType = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
		String newFileName = System.currentTimeMillis() + fileType;
		Instant now = Instant.now();

		CreateMultipartUploadRequest createMultipartUploadRequest = CreateMultipartUploadRequest.builder()
			.bucket(bucket)
			.key(UPLOADED_IMAGES_DIR + newFileName)
			.acl(ObjectCannedACL.PUBLIC_READ)
			.expires(now.plusSeconds(60 * 20))
			.build();

		CreateMultipartUploadResponse response = s3Client.createMultipartUpload(createMultipartUploadRequest);

		return S3UploadResponse.toEntity(response.uploadId(), newFileName);
	}

	public S3PreSignedUrlResponse getUploadSignedUrl(S3UploadSignedUrlRequest request, String targetBucket) {

		UploadPartRequest uploadPartRequest = UploadPartRequest.builder()
			.bucket(targetBucket)
			.key(UPLOADED_IMAGES_DIR + request.fileName())
			.uploadId(request.uploadId())
			.partNumber(request.partNumber())
			.build();

		UploadPartPresignRequest uploadPartPresignRequest = UploadPartPresignRequest.builder()
			.signatureDuration(Duration.ofMinutes(10))
			.uploadPartRequest(uploadPartRequest)
			.build();

		PresignedUploadPartRequest presignedUploadPartRequest
			= s3Presigner.presignUploadPart(uploadPartPresignRequest);

		return new S3PreSignedUrlResponse(presignedUploadPartRequest.url().toString());
	}

	public S3UploadResultResponse completeUpload(S3UploadCompleteRequest request, String targetBucket) {
		List<CompletedPart> completedParts = new ArrayList<>();

		for (S3UploadPartsDetail s3UploadPartsDetail : request.parts()) {
			CompletedPart completedPart = CompletedPart.builder()
				.partNumber(s3UploadPartsDetail.partNumber())
				.eTag(s3UploadPartsDetail.awsETag())
				.build();
			completedParts.add(completedPart);
		}

		CompletedMultipartUpload completedMultipartUpload = CompletedMultipartUpload.builder()
			.parts(completedParts)
			.build();

		String fileName = request.fileName();

		CompleteMultipartUploadRequest completeMultipartUploadRequest = CompleteMultipartUploadRequest.builder()
			.bucket(targetBucket)
			.key(UPLOADED_IMAGES_DIR + fileName)
			.uploadId(request.uploadId())
			.multipartUpload(completedMultipartUpload)
			.build();

		CompleteMultipartUploadResponse completeMultipartUploadResponse = s3Client.completeMultipartUpload(
			completeMultipartUploadRequest);

		String objectKey = completeMultipartUploadResponse.key();
		String url = amazonS3Client.getUrl(targetBucket, objectKey).toString();
		String bucket = completeMultipartUploadResponse.bucket();

		long fileSize = getFileSizeFromS3Url(bucket, objectKey);

		return new S3UploadResultResponse(
			url,
			fileName,
			fileSize
		);
	}

	public void abortUpload(S3UploadAbortRequest abortRequest, String targetBucket) {
		AbortMultipartUploadRequest abortMultipartUploadRequest = AbortMultipartUploadRequest.builder()
			.bucket(targetBucket)
			.key(UPLOADED_IMAGES_DIR + abortRequest.fileName())
			.uploadId(abortRequest.uploadId())
			.build();

		s3Client.abortMultipartUpload(abortMultipartUploadRequest);
	}

	private long getFileSizeFromS3Url(String bucketName, String fileName) {
		GetObjectMetadataRequest metadataRequest = new GetObjectMetadataRequest(bucketName, fileName);
		ObjectMetadata objectMetadata = amazonS3Client.getObjectMetadata(metadataRequest);
		return objectMetadata.getContentLength();
	}
}
