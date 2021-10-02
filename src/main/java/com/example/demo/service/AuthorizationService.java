package com.example.demo.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.google.api.client.auth.oauth2.Credential;

public interface AuthorizationService {

	boolean isUserAuthenticated() throws Exception;

	Credential getCredentials() throws IOException;

	String authenticateUserViaGoogle() throws Exception;

	void exchangeCodeForTokens(String code) throws Exception;
	
	void removeUserSession(HttpServletRequest request) throws Exception;
}
