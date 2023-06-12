package com.tish.config;

import com.google.api.services.forms.v1.FormsScopes;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.Objects;

@Component
public class AccessToken {

	@Autowired
	private ResourceLoader resourceLoader;

	public String getAccessToken() throws IOException {
		GoogleCredentials credential = GoogleCredentials.fromStream(Objects.requireNonNull(
				resourceLoader.getResource("classpath:e-book-my-batis-20ca4cfd7167.json").getInputStream())).createScoped(FormsScopes.all());
		return credential.getAccessToken() != null ?
				credential.getAccessToken().getTokenValue() :
				credential.refreshAccessToken().getTokenValue();
	}
}
