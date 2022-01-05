package com.zhy.lib_common.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.ArrayMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;




public class JsonUtils {
    private static final String AGREEMENT = "agreement";
    private static final String PRIVACY = "privacy";
    private static final String LICENSE = "license";
    private static final String TIME = "time";

    /**
     * 获取Assert下面的wet.json
     *
     * @param context
     * @return
     */
    public static String getAssertJSON(Context context) {
        AssetManager am = context.getAssets();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new
                    BufferedReader(new InputStreamReader(am.open("mobile/web.json"), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 获取File目录下的JSON
     *
     * @param path
     * @return
     */
    public static String getJSON(String path) {
        StringBuilder stringBuilder = new StringBuilder();

        File file = new File(path);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 简单生成JSON 工具类
     *
     * @param key
     * @param value
     * @return
     */
    public static String generationJSon(String key, String value) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 获取json解析code
     *
     * @param response
     * @return
     */
    public static int parseResultCode(String response) {
        LogUtils.i("parseResultCode ：" + response);
        try {
            return new JSONObject(response).getInt("code");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String parseResultString(String response, String key) throws JSONException {
        return parseJsonString(new JSONObject(response), key);
    }




    public static String parseJsonString(JSONObject jsonObject, String key) {
        return parseJsonString(jsonObject, key, "");
    }

    private static String parseJsonString(JSONObject jsonObject, String key, String defaultValue) {
        try {
            return jsonObject.has(key) && !TextUtils.equals("null", jsonObject.getString(key)) ? jsonObject.getString(key) : defaultValue;
        } catch (JSONException e) {
            return defaultValue;
        }
    }


    public static boolean parseJsonBoolean(JSONObject jsonObject, String key) {
        try {
            return jsonObject.has(key) && jsonObject.getBoolean(key);
        } catch (JSONException e) {
            return false;
        }
    }


    public static int parseJsonInt(JSONObject jsonObject, String key) {
        return parseJsonInt(jsonObject, key, -1);
    }

    private static int parseJsonInt(JSONObject jsonObject, String key, int defaultValue) {
        try {
            return jsonObject.has(key) ? jsonObject.getInt(key) : defaultValue;
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    public static JSONObject parseJsonObject(JSONObject jsonObject, String key) {
        try {
            return jsonObject.has(key) ? jsonObject.getJSONObject(key) : null;
        } catch (JSONException e) {
            return null;
        }
    }

    public static JSONArray parseJsonArray(JSONObject jsonObject, String key) {
        try {
            return jsonObject.has(key) ? jsonObject.getJSONArray(key) : null;
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * 客户端成员列表信息
     *
     * @param response
     * @return
     * @throws JSONException
     */
    public static HashMap<String, String> parseGroupListInfo(String response) throws JSONException {
        LogUtils.i("groupListInfo:" + response);
        HashMap<String, String> groupMap = new HashMap<>();
        JSONObject jsonData = new JSONObject(response);
        JSONArray jsonArray = jsonData.getJSONArray("network");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            groupMap.put("networkid", parseJsonString(object, "networkid"));
            groupMap.put("userid", parseJsonString(object, "userid"));
            groupMap.put("type", parseJsonString(object, "type"));
            groupMap.put("devicetype", parseJsonString(object, "devicetype"));
            groupMap.put("name", parseJsonString(object, "name"));
            groupMap.put("member", parseJsonString(object, "member"));
            groupMap.put("memberid", parseJsonString(object, "memberid"));
            groupMap.put("typeid", parseJsonString(object, "typeid"));
            groupMap.put("version", parseJsonString(object, "version"));
            groupMap.put("line", parseJsonString(object, "line"));
            groupMap.put("createtime", parseJsonString(object, "createtime"));
        }

        return groupMap;
    }

    /**
     * 客户端服务级别信息
     *
     * @param response
     * @return
     * @throws JSONException
     */
    public static HashMap<String, String> parseLevelInfo(String response) throws JSONException {
        LogUtils.i("levelInfo:" + response);
        HashMap<String, String> levelMap = new HashMap<>();

        JSONObject jsonData = new JSONObject(response);
        levelMap.put("userid", parseJsonString(jsonData, "userid"));
        levelMap.put("typeid", parseJsonString(jsonData, "typeid"));  //// 服务等级， 0：免费版，1：专业版，2：商业版，3：旗舰版，4：独立服务器版，5：铂金版
        levelMap.put("statuscode", parseJsonString(jsonData, "statuscode"));// 服务状态，0为正常
        levelMap.put("routernum", parseJsonString(jsonData, "routernum"));// 硬件成员数
        levelMap.put("clientnum", parseJsonString(jsonData, "clientnum"));// 软件成员数
        levelMap.put("serviceid", parseJsonString(jsonData, "serviceid"));// 硬件服务ID
        levelMap.put("expiredtime", parseJsonString(jsonData, "expiredtime"));// 硬件服务过期时间
        levelMap.put("visitorserviceid", parseJsonString(jsonData, "visitorserviceid"));  // 软件服务ID
        levelMap.put("visitorexpiredtime", parseJsonString(jsonData, "visitorexpiredtime"));// 软件服务过期时间
        levelMap.put("istry", parseJsonString(jsonData, "istry"));// 是否是体验服务
        levelMap.put("updatetime", parseJsonString(jsonData, "updatetime")); // 更新时间
        levelMap.put("createtime", parseJsonString(jsonData, "createtime"));// 创建时间
        levelMap.put("expiredays", parseJsonString(jsonData, "expiredays"));
        levelMap.put("isstandard", parseJsonString(jsonData, "isstandard"));// 是否是标准版服务
        levelMap.put("servicename", parseJsonString(jsonData, "servicename"));

        return levelMap;
    }


    /**
     * map 2 json
     *
     * @return
     */
    public static String parseMap2JSON(ArrayMap<String, Object> params) {
        try {
            if (null != params && params.size() > 0) {
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < params.size(); i++) {
                    jsonObject.put(params.keyAt(i), params.valueAt(i));
                }
                return String.valueOf(jsonObject);
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }




    public static JSONObject parseDnsJson(String isAuto, List<String> serverList, List<String> domainList) {
        JSONObject dnsJson = new JSONObject();
        JSONArray serverArray = new JSONArray();
        JSONArray domainArray = new JSONArray();
        if (!EmptyUtils.isEmpty(serverList)) {
            for (String s : serverList) {
                serverArray.put(s);
            }
        }
        if (!EmptyUtils.isEmpty(domainList)) {
            for (String s : domainList) {
                domainArray.put(s);
            }
        }
        try {
            dnsJson.put("isauto", isAuto);
            dnsJson.put("dns", serverArray);
            dnsJson.put("domains", domainArray);
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
        return dnsJson;
    }


}
