package com.tish.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.forms.v1.Forms;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
public class Boilerplate {

	private static final String APPLICATION_NAME = "e-book";
	private static Drive driveService;
	private static Forms formsService;

	static {

		try {

			JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

			driveService = new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(),
					jsonFactory, null)
					.setApplicationName(APPLICATION_NAME).build();

			formsService = new Forms.Builder(GoogleNetHttpTransport.newTrustedTransport(),
					jsonFactory, null)
					.setApplicationName(APPLICATION_NAME).build();

		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	public static Drive getDriveService() {
		return driveService;
	}

	public static Forms getFormsService() {
		return formsService;
	}

}
