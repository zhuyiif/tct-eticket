package com.example.eticket.storage;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

public class AppStore {

    public static final String SHARED_PREFERENCES_KEY ="E_TICKET_DATA_KEY";
    public static final String TOKEN = "token";

    public static final String LOGIN ="E_TICKET_LOGIN_CONTENT_KEY";

    public static boolean isLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_KEY, context.MODE_PRIVATE);
        String user_id = sp.getString(TOKEN, "");
        if ("".equals(user_id) || user_id.equals(""))
            return false;
        else
            return true;
    }

    public static void saveLoginContent(Context context, JSONObject content) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_KEY, context.MODE_PRIVATE).edit();
        editor.putString(LOGIN, content.toString());
        editor.commit();

    }

}
