package com.roboadvice.utils;

import java.math.BigDecimal;

public class Constant {

    //DB ACCESS
    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://192.168.2.130:3306/pulpfiction?useSSL=false";
    public static final String DB_USER = "pulpfiction";
    public static final String DB_PASS = "passwordchespacca";


    //DEFAULT RESPONSE STATUS
    public static final int SUCCESS = 0;
    public static final int ERROR = 1;

    //DEFAULT RESPONSE MESSAGES
    public static final String SUCCES_MSG = "SUCCESS";
    public static final String ERROR_MSG = "ERROR";

    //NUMBER OF ASSETS CLASS AND ASSETS
    public static final int NUM_ASSETS_CLASS = 4;
    public static final int NUM_ASSETS = 13;
    public static final int INITIAL_INVESTMENT = 10000;

    //METHOD FOR PERCENTAGE WITH BIGDECIMAL
    public static BigDecimal percentage(BigDecimal num, BigDecimal pct){
        return num.multiply(pct).divide(new BigDecimal(100));
        //return num.multiply(pct).divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
    }

}
