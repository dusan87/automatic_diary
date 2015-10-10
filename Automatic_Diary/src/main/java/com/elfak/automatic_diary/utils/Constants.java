package com.elfak.automatic_diary.utils;

/**
 * Created by Dušan Ristić on 10/10/15.
 */
public interface Constants {

    // registration user keys data
    public static final String USER_FIRST_NAME = "first_name";
    public static final String USER_LAST_NAME = "last_name";
    public static final String USER_BIRTHDAY = "birth_day";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_GENDER = "gender";

    // validation user keys data
    public static final String USER_PASSWORD1 = "password1";
    public static final String USER_PASSWORD2 = "password1";

    // user values data
    public static final String USER_GENDER_MALE = "M";
    public static final String USER_GENDER_FEMALE = "F";


    // general
    public static final String COUNTRY = "country";
    public static final String CITY = "city";
    public static final String IMAGE = "image";
    public static final String PHONE = "phone";


    // Error messages
    public  static final String REQUIRED_FIELD = "This fields is required!";

    // Rest API endpoints

    public static final String API_CREATE_USER = "create_user/";
    public static final String API_LOGIN_USER = "auth_user/";
    public static final String API_VALIDATE_USER = "validate/";



}
