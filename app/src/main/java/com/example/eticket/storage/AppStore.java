package com.example.eticket.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class AppStore {

    public static final String SHARED_PREFERENCES_KEY ="E_TICKET_DATA_KEY";
    public static final String TOKEN = "token";
    public static final String WAS_SHOWN_GUIDE_PAGES = "WAS_SHOWN_GUIDE_PAGES";

    public static final String LOGIN ="E_TICKET_LOGIN_CONTENT_KEY";

    public static boolean isLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_KEY, context.MODE_PRIVATE);
        String user_id = sp.getString(TOKEN, "");
        if ("".equals(user_id) || user_id.equals(""))
            return false;
        else
            return true;
    }

    public static void removeToken(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_KEY, context.MODE_PRIVATE).edit();
        editor.remove(TOKEN);
        editor.commit();
    }

    public static boolean wasShownGuidePages(Context context){
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_KEY, context.MODE_PRIVATE);
        return sp.getBoolean(WAS_SHOWN_GUIDE_PAGES, false);
    }

    public static void shownGuidePages(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_KEY, context.MODE_PRIVATE).edit();
        editor.putBoolean(WAS_SHOWN_GUIDE_PAGES, true);
        editor.commit();
    }

    public static void saveLoginContent(Context context, JSONObject content) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_KEY, context.MODE_PRIVATE).edit();
        editor.putString(LOGIN, content.toString());
        try {
            editor.putString(TOKEN, content.getJSONObject("content").getString("app_token"));
        } catch (JSONException e) {
            Log.e("AppStore","error in parsing response of login",e);
        }
        editor.commit();

    }


    public static String getLoginContent(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_KEY, context.MODE_PRIVATE);
        String content = sp.getString(LOGIN, "");
        return  content;

    }

    public static String getToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_KEY, context.MODE_PRIVATE);
        String user_id = sp.getString(TOKEN, "");
        return user_id;
    }

}
