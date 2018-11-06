package com.funenc.eticket.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.funenc.eticket.engine.AppEngine;
import com.funenc.eticket.model.StationListResponse;
import com.funenc.eticket.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppStore {

    public static final String SHARED_PREFERENCES_KEY ="E_TICKET_DATA_KEY";
    public static final String TOKEN = "token";
    public static final String WAS_SHOWN_GUIDE_PAGES = "WAS_SHOWN_GUIDE_PAGES";
    public static final String TICKET_CODE_CREATE_KEY = "TICKET_CODE_CREATE_KEY";
    public static final String TICKET_CODE_CREATE_SEED = "TICKET_CODE_CREATE_SEED";

    public static final String LOGIN ="E_TICKET_LOGIN_CONTENT_KEY";

    public static final String SUBWAY_SEARCH_URL = "https://operator-app.funenc.com/page/#/subwayLine";
    public static final String MICRO_INTERACTION_URL = "https://operator-app.funenc.com/page/#/weinteraction?app_token=";
    public static final String WX_APP_ID = "wx81e5f5dc97c875f0";

    private static final String SELF_USER_PHONE_KEY = "SELF_USER_PHONE";
    public static final String ORDER = "ORDER";
    public static final SimpleDateFormat OUTPUT_TIME_FORMAT = new SimpleDateFormat("HH:mm");

    private static User selfUser = null;

    private static List<StationListResponse.Station> stationList;

    private static Map<String, List<StationListResponse.Station>> stationLineInfo = new HashMap<>();

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

    public static String getTicketCodeCreateKey(Context context, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_KEY, context.MODE_PRIVATE);
        String key = sp.getString(TICKET_CODE_CREATE_KEY, defValue);
        return key;
    }

    public static void setTicketCodeCreateKey(Context context, String key){
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_KEY, context.MODE_PRIVATE).edit();
        editor.putString(TICKET_CODE_CREATE_KEY, key);
        editor.commit();
    }

    public static String getTicketCodeCreateSeed(Context context, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_KEY, context.MODE_PRIVATE);
        String seed = sp.getString(TICKET_CODE_CREATE_SEED, defValue);
        return seed;
    }

    public static void setTicketCodeCreateSeed(Context context, String key){
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_KEY, context.MODE_PRIVATE).edit();
        editor.putString(TICKET_CODE_CREATE_SEED, key);
        editor.commit();
    }

    public static User getSelfUser() {
        if(selfUser == null){
            Context context = AppEngine.getSystemContext();
            if(isLogin(context)){
                selfUser = new User();
                SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_KEY, context.MODE_PRIVATE);
                selfUser.setPhone(sp.getString(SELF_USER_PHONE_KEY, ""));
            }
        }
        return selfUser;
    }

    public static void setSelfUser(User selfUser) {
        AppStore.selfUser = selfUser;
        Context context = AppEngine.getSystemContext();
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_KEY, context.MODE_PRIVATE).edit();
        editor.putString(SELF_USER_PHONE_KEY, selfUser.getPhone());
        editor.commit();
    }

    public static List<StationListResponse.Station> getStationList() {
        return stationList;
    }

    public static void setStationList(List<StationListResponse.Station> stationList) {
        AppStore.stationList = stationList;
        for(StationListResponse.Station station : stationList){
            List<StationListResponse.Station> line = stationLineInfo.get(station.getLineNo());
            if(line == null){
                line = new ArrayList<>(5);
                stationLineInfo.put(station.getLineNo(), line);
            }
            line.add(station);
        }
    }

    public static Map<String, List<StationListResponse.Station>> getStationLineInfo() {
        return stationLineInfo;
    }
}
