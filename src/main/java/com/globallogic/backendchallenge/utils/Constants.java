package com.globallogic.backendchallenge.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final String EMAIL_FORMAT_VALIDATION_REGEX = "^[\\w-\\.]+@[\\w-\\.]+\\.[a-zA-Z]{2,}$";
    public static final String PASSWORD_FORMAT_VALIDATION_REGEX = "^(?=.*[A-Z])(?=(.*\\d.*){2})[a-zA-Z\\d]{8,12}$";
}
