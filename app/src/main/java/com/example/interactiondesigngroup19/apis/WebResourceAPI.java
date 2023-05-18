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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * The means by which weather information is fetched.
 */
public class WebResourceAPI {

    private static final String WEATHER_API_KEY = "c8cb49646fe2260171f748a52a909320";
    public static final String CURRENT_WEATHER = "https://api.openweathermap.org/data/2.5/weather?lat=%f.4&lon=%f.4&appid=%s";
    public static final String FORECAST_WEATHER = "https://api.openweathermap.org/data/2.5/forecast?lat=%f.4&lon=%f.4&appid=%s";
    private static final String MAP_API_KEY = "Qk0S_zpXXOKVYqZ0lNRtmYdvTkVUMM8JlvALlb1YfZ0";
    public static final String MAP_SEARCH = "https://discover.search.hereapi.com/v1/discover?at=%f.5,%f.5&q=%s&limit=%d&apiKey=%s";
    private static RequestQueue requestQueue;
    private static Map<String, JSONObject> weatherRequestCache = new HashMap<>();
    private static Map<String, JSONObject> mapRequestCache = new HashMap<>();
    private static Map<String, WeatherResult> weatherResultCache = new HashMap<>();

    public static class WeatherResult {
        public final float windSpeed;
        public final int windDir;
        public final float windGust;
        public final float pop;
        public final float temp;
        public final float tempFeel;
        public final float tempMin;
        public final float tempMax;
        public final long time;
        public final String timeText;
        public final int vis;
        public final String main;
        public final String desc;
        public final int clouds;
        public final float rain;
        public final float snow;

        public WeatherResult(JSONObject data) throws org.json.JSONException {
            time = data.getLong("dt");
            JSONObject mainData = data.getJSONObject("main");
            temp = (float) mainData.getDouble("temp");
            tempFeel = (float) mainData.getDouble("feels_like");
            tempMin = (float) mainData.getDouble("temp_min");
            tempMax = (float) mainData.getDouble("temp_max");
            JSONObject weatherData = data.getJSONArray("weather").getJSONObject(0);
            main = weatherData.getString("main");
            desc = weatherData.getString("description");
            JSONObject windData = data.getJSONObject("wind");
            clouds = data.getJSONObject("clouds").getInt("all");
            windSpeed = (float) windData.getDouble("speed");
            windDir = windData.getInt("deg");
            windGust = (float) windData.getDouble("gust");
            vis = data.getInt("visibility");
            pop = (float) data.getDouble("pop");
            rain = !data.isNull("rain") ? (float) data.getJSONObject("rain").getDouble("3h") : 0;
            snow = !data.isNull("snow") ? (float) data.getJSONObject("snow").getDouble("3h") : 0;
            timeText = data.getString("dt_txt");
        }
    }

    private static class CacheResponse<T> implements Response.Listener<T> {
        private Response.Listener<T> response;
        private String request;
        private Map<String, T> cache;

        public CacheResponse(Response.Listener<T> response, String request, Map<String, T> cache) { this.response = response; this.request = request; this.cache = cache; }
        @Override
        public void onResponse(T response) {
            cache.put(request, response);
            this.response.onResponse(response);
        }
    }

    private static void ensureQueue(Context context) {
        if (requestQueue == null) {
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache, network);
            requestQueue.start();
        }
    }

    public static boolean getWeatherForecast(Context context, Location location, long time, OnSuccessListener<WeatherResult> response, OnFailureListener errorResponse) {
        if (location != null) {
            long cacheTime = time / (1000 * 60 * 60 * 3);
            String cacheRequest = String.format(Locale.UK, "F:T:%d Lat:%f.3 Lon:%f.3", cacheTime, location.getLongitude(), location.getLatitude());
            if (weatherResultCache.containsKey(cacheRequest)) {
                response.onSuccess(weatherResultCache.get(cacheRequest));
            } else {

            }
            return true;
        }
        return false;
    }

    private static void makeRequest(Context context, String url, Response.Listener<JSONObject> response, Response.ErrorListener errorResponse) {
        ensureQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest( Request.Method.GET, url,null, response, errorResponse);
        requestQueue.add(jsonRequest);
    }

    public static boolean fetchWeatherData(String resource, Context context, Location location,
                                      Response.Listener<JSONObject> response, Response.ErrorListener errorResponse) {
        if (location != null && resource != null) {
            if (Objects.equals(resource, CURRENT_WEATHER)) {
                long time = System.currentTimeMillis() / (1000 * 60 * 10);
                String cacheRequest = String.format(Locale.UK, "C:T:%d Lat:%f.3 Lon:%f.3", time, location.getLatitude(), location.getLongitude());
                if (weatherRequestCache.containsKey(cacheRequest)) {
                    response.onResponse(weatherRequestCache.get(cacheRequest));
                } else {
                    makeRequest(context, String.format(Locale.UK, resource, location.getLatitude(), location.getLongitude(), WEATHER_API_KEY),
                            new CacheResponse<>(response, cacheRequest, weatherRequestCache), errorResponse);
                }
            } else if (Objects.equals(resource, FORECAST_WEATHER)) {
                long time = System.currentTimeMillis() / (1000 * 60 * 60);
                String cacheRequest = String.format(Locale.UK, "F:T:%d Lat:%f.3 Lon:%f.3", time, location.getLatitude(), location.getLongitude());
                if (weatherRequestCache.containsKey(cacheRequest)) {
                    response.onResponse(weatherRequestCache.get(cacheRequest));
                } else {
                    makeRequest(context, String.format(Locale.UK, resource, location.getLatitude(), location.getLongitude(), WEATHER_API_KEY),
                            new CacheResponse<>(response, cacheRequest, weatherRequestCache), errorResponse);
                }
            }
            else {
                makeRequest(context, String.format(Locale.UK, resource, location.getLatitude(), location.getLongitude(), WEATHER_API_KEY), response, errorResponse);
            }
            return true;
        }
        return false;
    }

    public static boolean fetchMapData(String resource, Context context, Location location, String request, int limit,
                                      Response.Listener<JSONObject> response, Response.ErrorListener errorResponse) {
        if (location != null) {
            if (Objects.equals(resource, MAP_SEARCH)) {
                String cacheRequest = String.format(Locale.UK, "MS:Lat:%f.4 Lon:%f.4 Req:%s Lim:%d",
                        location.getLatitude(), location.getLongitude(), request, limit);
                if (mapRequestCache.containsKey(cacheRequest)) {
                    response.onResponse(mapRequestCache.get(cacheRequest));
                }
                makeRequest(context, String.format(Locale.UK, resource,
                                location.getLatitude(), location.getLongitude(), request, limit, WEATHER_API_KEY),
                        new CacheResponse<>(response, cacheRequest, mapRequestCache), errorResponse);
            } else {
                makeRequest(context, String.format(Locale.UK, resource, location.getLatitude(), location.getLongitude(), request, limit, WEATHER_API_KEY),
                        response, errorResponse);
            }
            return true;
        }
        return false;
    }


}
