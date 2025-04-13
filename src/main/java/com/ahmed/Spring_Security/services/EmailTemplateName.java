package com.ahmed.Spring_Security.services;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("activation_account");
    private final String name;
    EmailTemplateName(String name){
        this.name=name;
    }
    // why should i make a constructor when i need to put a parameter to the ACTIVATE_ACCOUNT
}
