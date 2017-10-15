package com.bombon.voxr;

/**
 * Created by Vaughn on 6/8/17.
 */

public class Constants {

    public static final String BASE_URL = "http://10.0.2.2:5001/voxr/api/";

    public interface Config{
        Integer TIMEOUT_MS = 15000;
        Integer ERROR_CODE = 999;
        Integer DB_RESPONES = 998;
        String PREFSKEY = "v0xR";
    }
}
