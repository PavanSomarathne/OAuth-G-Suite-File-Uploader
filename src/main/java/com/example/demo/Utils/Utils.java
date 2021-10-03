package com.example.demo.Utils;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties
public class Utils {

	
	@Value("${google.oauth.callback.uri}")
	private String CALLBACK_URI;

	@Value("${google.secret.key.path}")
	private Resource driveSecretKeys;

	@Value("${google.credentials.folder.path}")
	private Resource credentialsFolder;

	@Value("${myapp.temp.path}")
	private String temporaryFolder;
	
	public String getCALLBACK_URI() {
		return CALLBACK_URI;
	}

	public void setCALLBACK_URI(String CALLBACK_URI_p) {
		CALLBACK_URI = CALLBACK_URI_p;
	}

	public Resource getDriveSecretKeys() {
		return driveSecretKeys;
	}

	public void setDriveSecretKeys(Resource driveSecretKeys) {
		this.driveSecretKeys = driveSecretKeys;
	}

	public Resource getCredentialsFolder() {
		return credentialsFolder;
	}

	public void setCredentialsFolder(Resource credentialsFolder) {
		this.credentialsFolder = credentialsFolder;
	}

	public String getTemporaryFolder() {
		return temporaryFolder;
	}

	public void setTemporaryFolder(String temporaryFolder) {
		this.temporaryFolder = temporaryFolder;
	}

	public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	public static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

	public static final String GOOGLE_CALLBACK_URL = "http://localhost:8083/oauth";

	public static final String RFC_3339_DATETIME_FORMAT = "%sT%s:00+05:30";
	public static final String UTC_SL_TIME_ZONE = "Asia/Colombo";

	public static final String REDIRECT_TO_EVENTS_PAGE = "redirect:/events";
	public static final String ERROR = "error";
}