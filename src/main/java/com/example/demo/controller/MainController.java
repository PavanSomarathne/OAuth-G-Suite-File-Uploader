package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.Utils.Utils;
import com.example.demo.model.CalendarEvent;
import com.example.demo.service.impl.GoogleCalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.UploadFile;
import com.example.demo.service.AuthorizationService;
import com.example.demo.service.DriveService;

import java.util.Date;

@Controller
public class MainController {

	private final Logger logger = LoggerFactory.getLogger(MainController.class);

	@Autowired
	AuthorizationService authorizationService;

	@Autowired
	DriveService driveService;

	@Autowired
	GoogleCalendarService calendarService;

	/**
	 * root route check for authenticated status.
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/")
	public String showHomePage() throws Exception {
		if (authorizationService.isUserAuthenticated()) {
			logger.debug("User is authenticated. Redirecting to home...");
			return "redirect:/home";
		} else {
			logger.debug("User is not authenticated. Redirecting to sso...");
			return "redirect:/login";
		}
	}

	/**
	 * render login page
	 * 
	 * @return
	 */
	@GetMapping("/login")
	public String goToLogin() {
		return "index.html";
	}

	/**
	 * render home page
	 * 
	 * @return
	 */
	@GetMapping("/home")
	public String goToHome() {
		return "home.html";
	}

	/**
	 *  authorize the app using oauth service
	 * 
	 * @param response
	 * @throws Exception
	 */
	@GetMapping("/googlesignin")
	public void doGoogleSignIn(HttpServletResponse response) throws Exception {
		logger.debug("SSO Called...");
		response.sendRedirect(authorizationService.authenticateUserViaGoogle());
	}

	/**
	 *Oauth Callback handling
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/oauth/callback")
	public String saveAuthorizationCode(HttpServletRequest request) throws Exception {
		logger.debug("SSO Callback invoked...");
		String code = request.getParameter("code");
		logger.debug("SSO Callback Code Value..., " + code);

		if (code != null) {
			authorizationService.exchangeCodeForTokens(code);

			return "redirect:/home.html";
		}
		return "redirect:/index.html";
	}

	/**
	 * Handle Logout
	 * 
	 * @return
	 * @throws Exception 
	 */
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) throws Exception {
		logger.debug("Logout invoked...");
		authorizationService.removeUserSession(request);
		return "redirect:/login";
	}

	/**
	 * File Upload handling
	 * 
	 * @param request
	 * @param uploadedFile
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/upload")
	public String uploadFile(HttpServletRequest request, @ModelAttribute UploadFile uploadedFile) throws Exception {
		MultipartFile multipartFile = uploadedFile.getMultipartFile();
		driveService.uploadFile(multipartFile);
		CalendarEvent event = new CalendarEvent();
		event.setDate("2021-10-03");
		event.setStartTime("12:20");
		event.setEndTime("13:20");
		event.setDescription("Test");
		event.setTitle("Test TT");
		calendarService.createNewEvent(event);
		return "redirect:/home?status=success";
	}

	@GetMapping("/createEvent")
	public String event() throws Exception {
		CalendarEvent event = new CalendarEvent();
		event.setDate("2021-10-03");
		event.setStartTime("12:20");
		event.setEndTime("13:20");
		event.setDescription("Test");
		event.setTitle("Your file has been uploaded");
		calendarService.createNewEvent(event);
		return "redirect:/home?status=success";
	}

	@PostMapping("/events")
    public String addNewEvent(@ModelAttribute CalendarEvent calendarEvent, Model model) {
        try {
            logger.info("Adding a new calendar event");
            calendarService.createNewEvent(calendarEvent);
            return Utils.REDIRECT_TO_EVENTS_PAGE;
        } catch (Exception e) {
            logger.error("Exception occured when adding new event. {}", e);
            model.addAttribute(Utils.ERROR, e.getMessage());
            return Utils.ERROR;
        }

    }
}
