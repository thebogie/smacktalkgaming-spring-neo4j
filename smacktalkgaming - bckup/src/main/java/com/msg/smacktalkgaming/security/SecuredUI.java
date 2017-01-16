package com.msg.smacktalkgaming.security;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.spring.events.EventBus;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.msg.smacktalkgaming.backend.repos.PlayerRepository;
import com.msg.smacktalkgaming.backend.services.PlayerService;
import com.msg.smacktalkgaming.vaadin.DefaultView;
import com.msg.smacktalkgaming.vaadin.FrontView;
import com.msg.smacktalkgaming.vaadin.Greeter;
import com.msg.smacktalkgaming.vaadin.PlayerForm;
import com.msg.smacktalkgaming.vaadin.ViewScopedView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringViewProvider;

//import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

// No @Push annotation, we are going to enable it programmatically when the user
// logs on
@Theme(ValoTheme.THEME_NAME) // Looks nicer
// @Widgetset("com.vaadin.demo.parking.widgetset.ParkingWidgetset")
@Title("Smacktalk Gaming - throw scores at your friends")
@SpringUI
// @SpringViewDisplay
public class SecuredUI extends UI {

	private static final String[] VIEWS = { "Alternate", "Main", "Secondary", "Principal" };

	private Panel springViewDisplay;

	PlayerForm pForm;
	EventBus.UIEventBus eventBus;

	@Autowired
	PlayerService pService;

	@Autowired
	BackendService backendService;

	@Autowired
	SpringViewProvider viewProvider;

	@Autowired
	ErrorView errorView;

	@Autowired
	private Greeter greeter;

	private Label timeAndUser;

	private Timer timer;

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Smacktalkgaming - your goto for insults and victory");
		if (SecurityUtils.isLoggedIn()) {
			showMain();
		} else {
			showLogin();
		}
	}

	private void showLogin() {
		setContent(new LoginForm(this::login));
	}

	// buttons.addComponent(new Button("Logout", event -> logout()));
	// Navigator navigator = new Navigator(this, viewContainer);
	// navigator.addProvider(viewProvider);
	// navigator.setErrorView(errorView);
	// viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
	private void showMain() {

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();

		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);
		layout.addComponent(buttons);

		buttons.addComponent(new Button("Invoke user method", event -> {
			// Thismethod should be accessible by both 'user' and 'admin'. //
			Notification.show(backendService.userMethod());
			getNavigator().navigateTo("user");
		}));
		buttons.addComponent(new Button("Navigate to user view", event -> {
			getNavigator().navigateTo("");
		}));
		buttons.addComponent(new Button("Invoke admin method", event -> {
			// This method should be accessible by 'admin' only.
			Notification.show(backendService.adminMethod());
		}));
		buttons.addComponent(new Button("Navigate to admin view", event -> {
			getNavigator().navigateTo("admin");
		}));
		buttons.addComponent(new Button("Logout", event -> logout()));
		timeAndUser = new Label();
		timeAndUser.setSizeUndefined();
		buttons.addComponent(timeAndUser);
		buttons.setComponentAlignment(timeAndUser, Alignment.MIDDLE_LEFT);

		Panel viewContainer = new Panel();
		viewContainer.setSizeFull();
		layout.addComponent(viewContainer);
		layout.setExpandRatio(viewContainer, 1.0f);

		setContent(layout);
		setErrorHandler(this::handleError);

		Navigator navigator = new Navigator(this, viewContainer);
		navigator.addProvider(viewProvider);
		navigator.addView("TEST", new ViewScopedView());
		navigator.setErrorView(errorView);
		viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);

	}

	@Override
	public void detach() {
		// timer.cancel();
		super.detach();
	}

	private void updateTimeAndUser() {
		// Demonstrate that server push works and that you can even access the
		// security context from within the
		// access(...) method.
		access(() -> timeAndUser.setValue(String.format("The server-side time is %s and the current user is %s",
				LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
				SecurityContextHolder.getContext().getAuthentication().getName())));
	}

	private boolean login(String login, String password) {
		boolean retVal = false;
		Authentication token;
		token = pService.authenticatePlayer(login, password);
		if (null != token) {

			// Reinitialize the session to protect against session fixation
			// attacks. This does not work
			// with websocket communication.
			VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
			SecurityContextHolder.getContext().setAuthentication(token);
			// Now when the session is reinitialized, we can enable websocket
			// communication. Or we could have just
			// used WEBSOCKET_XHR and skipped this step completely.
			getPushConfiguration().setTransport(Transport.WEBSOCKET);
			getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
			// Show the main UI
			showMain();
			retVal = true;
		}
		return retVal;
	}

	private void logout() {
		getPage().reload();
		getSession().close();
	}

	private void handleError(com.vaadin.server.ErrorEvent event) {
		Throwable t = DefaultErrorHandler.findRelevantThrowable(event.getThrowable());
		if (t instanceof AccessDeniedException) {
			Notification.show("You do not have permission to perform this operation",
					Notification.Type.WARNING_MESSAGE);
		} else {
			DefaultErrorHandler.doDefault(event);
		}
	}

}
