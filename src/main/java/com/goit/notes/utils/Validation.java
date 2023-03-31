package com.goit.notes.utils;

import org.springframework.stereotype.Component;

@Component
public class Validation {
    public boolean isValidName(String username) {
        return username != null && username.length() >=5 && username.length() <= 50 && username.chars().allMatch(Character::isLetterOrDigit);
    }

    public boolean isValidPassword(String password) {
        return password != null && password.length() >=8 && password.length() <= 100;
    }

    public boolean isValidTitle(String title) {
        return title != null && title.length() >=5 && title.length() <= 100;
    }

    public boolean isValidContent(String content) {
        return content != null && content.length() >=5 && content.length() <= 10000;
    }
}
