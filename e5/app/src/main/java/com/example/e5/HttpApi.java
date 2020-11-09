package com.example.e5;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;

public class HttpApi {
    private String url;
    private Activity activity;

    public HttpApi(Activity activity, String url) {
        this.activity = activity;
        this.url = url;
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }

    private static String encode(String param) {
        try {
            return URLEncoder.encode(param, "UTF-8");
        } catch (Exception e) {
            Log.e("encode", e.toString());
        }
        return param;
    }

    public void get(Map<String, String> params) {
        new Async().execute(params);
    }

    private static String getCharSet(URLConnection connection) {
        String contentType = connection.getHeaderField("Content-Type");
        String charset = null;

        for (String param : contentType.replace(" ", "").split(";")) {
            if (param.startsWith("charset=")) {
                charset = param.split("=", 2)[1];
                break;
            }
        }
        return charset;
    }

    private class Async extends AsyncTask<Map<String, String>, String, String> {

        @Override
        protected String doInBackground(Map<String, String>... parameters) {
            Map<String, String> params = parameters[0];
            String req = url + "?";
            for(String key : params.keySet()){
                String val = params.get(key);
                req += encode(key) + "=" + encode(val) + "&";
            }
            req = req.substring(0, req.length() - 1);
            String res = "";
            Log.i("url", req);
            try {
                URLConnection conn = new URL(req).openConnection();
                conn.setRequestProperty("Accept-Charset", "UTF-8");
                try (InputStream is = conn.getInputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(is, getCharSet(conn)))) {
                    for (String line; (line = br.readLine()) != null; ) {
                        res += line;
                    }
                }
            } catch (Exception e) {
                Log.e("get", e.toString() + "\n" + Arrays.toString(e.getStackTrace()).replaceAll(",", "\n"));
            }
            return res;
        }

        protected void onPostExecute(String res) {
            ((GameActivity)activity).setOutputText(res);
        }
    }
}
