package image.yejin.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://13.124.30.105:8080",
				"https://image-upload-cicd-bucket.s3.ap-northeast-2.amazonaws.com")
			.allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "DELETE", "PATCH");
	}
}
