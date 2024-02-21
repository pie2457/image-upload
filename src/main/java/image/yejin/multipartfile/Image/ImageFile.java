package image.yejin.multipartfile.Image;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.sun.jdi.InternalException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ImageFile {

	private final String fileName;
	private final String contentType;
	private final Long fileSize;
	private final InputStream imageInputStream;

	private ImageFile(MultipartFile multipartFile) {
		this.fileName = getFileName(multipartFile);
		this.contentType = getImageContentType(multipartFile);
		this.fileSize = multipartFile.getSize();
		this.imageInputStream = getImageInputStream(multipartFile);
	}

	public static ImageFile from(MultipartFile multipartFile) {
		return new ImageFile(multipartFile);
	}

	public static List<ImageFile> from(List<MultipartFile> multipartFiles) {
		List<ImageFile> imageFiles = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFiles) {
			imageFiles.add(new ImageFile(multipartFile));
		}
		return imageFiles;
	}

	public InputStream getImageInputStream(MultipartFile multipartFile) {
		try {
			return multipartFile.getInputStream();
		} catch (IOException e) {
			throw new InternalException();
		}
	}

	private String getImageContentType(MultipartFile multipartFile) {
		return ImageContentType.findEnum(StringUtils.getFilenameExtension(multipartFile.getOriginalFilename()));
	}

	private String getFileName(MultipartFile multipartFile) {
		String ext = extractExt(multipartFile.getOriginalFilename());
		String uuid = UUID.randomUUID().toString();
		return uuid + "." + ext;
	}

	private String extractExt(String originalFileName) {
		int pos = originalFileName.lastIndexOf(".");
		return originalFileName.substring(pos + 1);
	}

	@Getter
	@RequiredArgsConstructor
	enum ImageContentType {

		JPEG("jpeg"),
		JPG("jpg"),
		PNG("png"),
		SVG("svg");

		private final String contentType;

		public static String findEnum(String contentType) {
			for (ImageContentType imageContentType : ImageContentType.values()) {
				if (imageContentType.getContentType().equals(contentType.toLowerCase())) {
					return imageContentType.getContentType();
				}
			}
			throw new IllegalArgumentException();
		}
	}
}
