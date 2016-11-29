package com.msg.smacktalkgaming.security;

import org.springframework.stereotype.Component;

import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.ui.UI;

/**
 * This demonstrates how you can control access to views.
 */
@Component
public class SmackTalkGamingViewAccessControl implements ViewAccessControl {

    @Override
    public boolean isAccessGranted(UI ui, String beanName) {
        if (beanName.equals("adminView")) {
            return SecurityUtils.hasRole("ROLE_ADMIN");
        } else {
            return SecurityUtils.hasRole("ROLE_USER");
        }
    }
}
