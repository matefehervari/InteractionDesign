package com.example.interactiondesigngroup19.apis;

import android.content.Context;
import android.location.Location;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Locale;

/**
 * The means by which weather information is fetched.
 */
public class WebResourceAPI {

    private static final String WEATHER_API_KEY = "c8cb49646fe2260171f748a52a909320";
    public static final String CURRENT_WEATHER = "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s";
    public static final String FORECAST_WEATHER = "https://api.openweathermap.org/data/2.5/forecast/hourly?lat=%f&lon=%f&appid=%s";
    private static final String MAP_API_KEY = "Qk0S_zpXXOKVYqZ0lNRtmYdvTkVUMM8JlvALlb1YfZ0";
    public static final String MAP_SEARCH = "https://discover.search.hereapi.com/v1/discover?at=%f,%f&q=%s&limit=%d&apiKey=%s";
    private static RequestQueue requestQueue;

    private static void ensureQueue(Context context) {
        if (requestQueue == null) {
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache, network);
            requestQueue.start();
        }
    }

    public static boolean getWeatherData(String resource, Context context, Location location,
                                      Response.Listener<JSONObject> response, Response.ErrorListener errorResponse) {
        ensureQueue(context);
        if (location != null) {
            JsonObjectRequest jsonRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    String.format(Locale.UK, resource, location.getLatitude(), location.getLongitude(), WEATHER_API_KEY),
                    null,
                    response, errorResponse
            );
            requestQueue.add(jsonRequest);
            return true;
        }
        return false;
    }

    public static boolean getMapData(String resource, Context context, Location location, String request, int limit,
                                      Response.Listener<JSONObject> response, Response.ErrorListener errorResponse) {
        ensureQueue(context);
        if (location != null) {
            JsonObjectRequest jsonRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    String.format(Locale.UK, resource, location.getLatitude(), location.getLongitude(), request, limit, WEATHER_API_KEY),
                    null,
                    response, errorResponse
            );
            requestQueue.add(jsonRequest);
            return true;
        }
        return false;
    }
}
