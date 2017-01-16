package com.msg.smacktalkgaming.vaadin;

import com.msg.smacktalkgaming.backend.domain.Player;
import com.msg.smacktalkgaming.backend.repos.PlayerRepository;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.teemu.switchui.Switch;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@UIScope
@SpringComponent
public class PlayerForm extends AbstractForm<Player> {

	private static final long serialVersionUID = 1L;

	EventBus.UIEventBus eventBus;
	PlayerRepository repo;

	TextField name = new MTextField("Name");
	TextField email = new MTextField("Email");
	TextField phoneNumber = new MTextField("Phone");
	DateField birthDay = new DateField("Birthday");
	Switch colleague = new Switch("Colleague");

	PlayerForm(PlayerRepository r, EventBus.UIEventBus b) {
		this.repo = r;
		this.eventBus = b;

		// On save & cancel, publish events that other parts of the UI can
		// listen
		setSavedHandler(Player -> {
			// persist changes
			repo.save(Player);
			// send the event for other parts of the application
			eventBus.publish(this, new PlayerModifiedEvent(Player));
		});
		setResetHandler(p -> eventBus.publish(this, new PlayerModifiedEvent(p)));

		setSizeUndefined();
	}

	@Override
	protected Component createContent() {
		return new MVerticalLayout(new MFormLayout(name, email, phoneNumber, birthDay, colleague).withWidth(""),
				getToolbar()).withWidth("");
	}

}
