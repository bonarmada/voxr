package com.bombon.voxr;

/**
 * Created by Vaughn on 6/8/17.
 */

public class Constants {

    public static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public interface Config{
        Integer TIMEOUT_MS = 15000;
        Integer ERROR_CODE = 999;
        Integer DB_RESPONES = 998;
        String PREFSKEY = "v0xR";
    }
}
