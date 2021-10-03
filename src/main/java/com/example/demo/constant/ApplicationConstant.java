package com.example.demo.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.DriveScopes;

public class ApplicationConstant {

	public static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	public static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	// public  List<String> SCOPES1 = Collections.singletonList(DriveScopes.DRIVE);
	// public  List<String> SCOPES2 = Collections.singletonList( CalendarScopes.CALENDAR);

	public static final List<String> SCOPES =Arrays.asList(DriveScopes.DRIVE,CalendarScopes.CALENDAR);

	public static final String USER_IDENTIFIER_KEY = "MY_TEST_USER";
	public static final String APPLICATION_NAME = "SSD OAuth Spring App";
	public static final String PARENT_FOLDER_NAME = "OAuth Demo App Uploaded";
}