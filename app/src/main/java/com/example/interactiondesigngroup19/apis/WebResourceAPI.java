package com.example.interactiondesigngroup19.apis;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * The means by which weather information is fetched.
 */
public class WebResourceAPI {

    private static final String WEATHER_API_KEY = "c8cb49646fe2260171f748a52a909320";
    public static final String CURRENT_WEATHER = "https://api.openweathermap.org/data/2.5/weather?lat=%.4f&lon=%.4f&appid=%s";
    public static final String FORECAST_WEATHER = "https://api.openweathermap.org/data/2.5/forecast?lat=%.4f&lon=%.4f&appid=%s";
    private static final String MAP_API_KEY = "Qk0S_zpXXOKVYqZ0lNRtmYdvTkVUMM8JlvALlb1YfZ0";
    public static final String MAP_SEARCH = "https://discover.search.hereapi.com/v1/discover?at=%.4f,%.4f&q=%s&limit=%d&apiKey=%s";
    private static final String ROUTE_SEARCH = "https://router.hereapi.com/v8/routes?origin=%.5f,%.5f&destination=%.5f,%.5f&departureTime=%s&return=summary,typicalDuration&transportMode=bicycle&apikey=%s";
    private static RequestQueue requestQueue;
    private static Map<String, JSONObject> weatherRequestCache = new HashMap<>();
    private static Map<String, JSONObject> mapRequestCache = new HashMap<>();
    private static Map<String, WeatherResult> weatherResultCache = new HashMap<>();

    public static class WeatherResult {
        public final float windSpeed;
        public final int windDir;
        public final float windGust;
        public final float pop;
        public final float temp, tempFeel, tempMin, tempMax;
        public final long time;
        public final String timeText;
        public final int vis;
        public final String main, desc;
        public final int clouds;
        public final float rain, snow;
        public final boolean day;

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
            day = weatherData.getString("icon").contains("d");
            clouds = data.getJSONObject("clouds").getInt("all");
            JSONObject windData = data.getJSONObject("wind");
            windSpeed = (float) windData.optDouble("speed", 0);
            windDir = windData.getInt("deg");
            windGust = (float) windData.optDouble("gust", 0);
            vis = data.getInt("visibility");

            if (!data.isNull("rain")) {
                JSONObject rainData = data.getJSONObject("rain");
                if (!rainData.isNull("3h")) {
                    rain = (float) rainData.getDouble("3h");
                } else {
                    rain = (float) (3.0 * rainData.optDouble("1h", 0));
                }
            } else { rain = 0; }
            if (!data.isNull("snow")) {
                JSONObject snowData = data.getJSONObject("snow");
                if (!snowData.isNull("3h")) {
                    snow = (float) snowData.getDouble("3h");
                } else {
                    snow = (float) (3.0 * snowData.optDouble("1h", 0));
                }
            } else { snow = 0; }

            pop = !data.isNull("pop") ? (float) data.getDouble("pop") : ((rain != 0) || (snow != 0) ? 1 : 0);
            timeText = data.optString("dt_txt", "");
        }

        public WeatherResult(float windSpeed, int windDir, float windGust, float pop, float temp, float tempFeel, float tempMin, float tempMax, long time, String timeText, int vis, String main, String desc, int clouds, float rain, float snow, boolean day) {
            this.windSpeed = windSpeed;
            this.windDir = windDir;
            this.windGust = windGust;
            this.pop = pop;
            this.temp = temp;
            this.tempFeel = tempFeel;
            this.tempMin = tempMin;
            this.tempMax = tempMax;
            this.time = time;
            this.timeText = timeText;
            this.vis = vis;
            this.main = main;
            this.desc = desc;
            this.clouds = clouds;
            this.rain = rain;
            this.snow = snow;
            this.day = day;
        }
    }

    public static class MapLocation {
        public final String title, addressLabel, country;
        public final double lat, lon;
        private static final MapLocation defaultLocation = new MapLocation("Cambridge", "Cambridge", "United Kingdom", 52.21109, 0.09135);
        public static MapLocation getDefaultMapLocation() {
            return defaultLocation;
        }
        public MapLocation(String title, String addressLabel, String country, double lat, double lon) {
            this.title = title; this.addressLabel = addressLabel; this.country = country; this.lat = lat; this.lon = lon;
        }
        public MapLocation(Location location) {
            this.title = location.getLatitude() + ", " + location.getLongitude();
            this.addressLabel = location.getLatitude() + ", " + location.getLongitude();
            this.country = "";
            this.lat = location.getLatitude();
            this.lon = location.getLongitude();
        }
        public MapLocation(JSONObject obj) throws JSONException {
            title = obj.getString("title");
            JSONObject addr = obj.getJSONObject("address");
            addressLabel = addr.getString("label");
            country = addr.getString("countryName");
            JSONObject pos = obj.getJSONObject("position");
            lat = pos.getDouble("lat");
            lon = pos.getDouble("lng");
        }
    }

    public static class Route {
        public final double origLat, origLon, destLat, destLon;
        public final int secsDur;

        public Route(JSONObject obj) throws JSONException {
            JSONObject route = obj.getJSONArray("routes").getJSONObject(0);
            JSONObject section = route.getJSONArray("sections").getJSONObject(0);
            JSONObject origLoc = section.getJSONObject("departure").getJSONObject("place").getJSONObject("location");
            origLat = origLoc.getDouble("lat");
            origLon = origLoc.getDouble("lng");
            JSONObject destLoc = section.getJSONObject("arrival").getJSONObject("place").getJSONObject("location");
            destLat = destLoc.getDouble("lat");
            destLon = destLoc.getDouble("lng");
            JSONObject summary = section.getJSONObject("summary");
            secsDur = summary.optInt("typicalDuration", summary.getInt("duration"));
        }
    }

    private static <T> void cacheResponse(T response, Response.Listener<T> responseListener, String request, Map<String, T> cache) {
        cache.put(request, response);
        responseListener.onResponse(response);
    }

    private static void getCachedWeatherRequest(String cacheRequest, OnSuccessListener<WeatherResult> response, OnFailureListener errorResponse) {
        if (weatherRequestCache.containsKey(cacheRequest)) {
            response.onSuccess(weatherResultCache.get(cacheRequest));
        } else {
            errorResponse.onFailure(new NullPointerException());
        }
    }

    private static void processForecastJson(JSONObject response, double lat, double lon) throws JSONException{
        int num = response.getInt("cnt");
        String cacheRequest;
        for (int i = 0; i < num; i++) {
            try {
                WeatherResult w = new WeatherResult(response.getJSONArray("list").getJSONObject(i));
                long cacheTime = w.time / (1000 * 60 * 60 * 3);
                cacheRequest = String.format(Locale.UK, "F:T:%d Lat:%.3f Lon:%.3f", cacheTime, lat, lon);
                weatherResultCache.put(cacheRequest, w);
            } finally {

            }
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
            return getWeatherForecast(context, location.getLatitude(), location.getLongitude(), time, response, errorResponse);
        }
        return false;
    }

    public static boolean getWeatherForecast(Context context, double lat, double lon, long time, OnSuccessListener<WeatherResult> response, OnFailureListener errorResponse) {
        long cacheTime = time / (1000 * 60 * 60 * 3);
        String cacheRequest = String.format(Locale.UK, "F:T:%d Lat:%.3f Lon:%.3f", cacheTime, lat, lon);
        if (weatherResultCache.containsKey(cacheRequest)) {
            response.onSuccess(weatherResultCache.get(cacheRequest));
        } else {
            fetchWeatherData(FORECAST_WEATHER, context, lat, lon, (JSONObject res) -> {
                try {
                    processForecastJson(res, lat, lon);
                } catch (JSONException e) {
                    errorResponse.onFailure(e);
                }
                getCachedWeatherRequest(cacheRequest, response, errorResponse);
            }, errorResponse::onFailure);
        }
        return true;
    }

    public static boolean getWeatherCurrent(Context context, Location location, OnSuccessListener<WeatherResult> response, OnFailureListener errorResponse) {
        if (location != null) {
            return getWeatherCurrent(context, location.getLatitude(), location.getLongitude(), response, errorResponse);
        }
        return false;
    }

    public static boolean getWeatherCurrent(Context context, double lat, double lon, OnSuccessListener<WeatherResult> response, OnFailureListener errorResponse) {
        return fetchWeatherData(CURRENT_WEATHER, context, lat, lon, (JSONObject res) -> {
            try { response.onSuccess(new WeatherResult(res)); }
            catch (JSONException e) { errorResponse.onFailure(e); }
        }, errorResponse::onFailure);
    }

    public static boolean listWeatherForecast(Context context, Location location, OnSuccessListener<List<WeatherResult>> response, OnFailureListener errorResponse) {
        if (location != null) {
            return listWeatherForecast(context, location.getLatitude(), location.getLongitude(), response, errorResponse);
        }
        return false;
    }

    public static boolean listWeatherForecast(Context context, double lat, double lon, OnSuccessListener<List<WeatherResult>> response, OnFailureListener errorResponse) {
        List<WeatherResult> list = new ArrayList<>();
        long currTime = System.currentTimeMillis() / (1000 * 60 * 60 * 3);
        String cacheRequest;
        for (int i = 0; i < 40; i++) {
            cacheRequest = String.format(Locale.UK, "F:T:%d Lat:%.3f Lon:%.3f", currTime + i, lat, lon);
            if (weatherResultCache.containsKey(cacheRequest)) {
                list.add(weatherResultCache.get(cacheRequest));
            } else {
                fetchWeatherData(FORECAST_WEATHER, context, lat, lon, (JSONObject res) -> {
                    try {
                        processForecastJson(res, lat, lon);
                    } catch (JSONException e) {
                        errorResponse.onFailure(e);
                    }
                    getForecastList(lat, lon, response, errorResponse);
                }, errorResponse::onFailure);
            }
        }
        response.onSuccess(list);
        return true;
    }

    private static void getForecastList(double lat, double lon, OnSuccessListener<List<WeatherResult>> response, OnFailureListener errorResponse) {
        List<WeatherResult> list = new ArrayList<>();
        long currTime = System.currentTimeMillis() / (1000 * 60 * 60 * 3);
        String cacheRequest;
        for (int i = 0; i < 40; i++) {
            cacheRequest = String.format(Locale.UK, "F:T:%d Lat:%.3f Lon:%.3f", currTime + i, lat, lon);
            if (weatherResultCache.containsKey(cacheRequest)) {
                list.add(weatherResultCache.get(cacheRequest));
            } else {
                errorResponse.onFailure(new NullPointerException());
            }
        }
        response.onSuccess(list);
    }

    public static boolean getSearchLocation(Context context, Location location, String request, OnSuccessListener<MapLocation> response, OnFailureListener errorResponse) {
        if (location != null) {
            return getSearchLocation(context, location.getLatitude(), location.getLongitude(), request, response, errorResponse);
        }
        return false;
    }

    public static boolean getSearchLocation(Context context, double lat, double lon, String request, OnSuccessListener<MapLocation> response, OnFailureListener errorResponse) {
        return fetchMapData(MAP_SEARCH, context, lat, lon, request, 1, (JSONObject res) -> {
            try {
                response.onSuccess(new MapLocation(res.getJSONArray("items").getJSONObject(0)));
            } catch (JSONException e) {
                errorResponse.onFailure(e);
            }
        }, errorResponse::onFailure);
    }

    public static boolean listSearchLocation(Context context, Location location, String request, int limit, OnSuccessListener<List<MapLocation>> response, OnFailureListener errorResponse) {
        if (location != null) {
            return listSearchLocation(context, location.getLatitude(), location.getLongitude(), request, limit, response, errorResponse);
        }
        return false;
    }

    public static boolean listSearchLocation(Context context, double lat, double lon, String request, int limit, OnSuccessListener<List<MapLocation>> response, OnFailureListener errorResponse) {
        return fetchMapData(MAP_SEARCH, context, lat, lon, request, limit, (JSONObject res) -> {
            try {
                JSONArray items = res.getJSONArray("items");
                List<MapLocation> list = new ArrayList<MapLocation>();
                for (int i = 0; !items.isNull(i); i++) {
                    list.add(new MapLocation(items.getJSONObject(i)));
                }
                response.onSuccess(list);
            } catch (JSONException e) {
                errorResponse.onFailure(e);
            }
        }, errorResponse::onFailure);
    }

    public static boolean getRoute(Context context, double origLat, double origLon, double destLat, double destLon, long time, OnSuccessListener<Route> response, OnFailureListener errorResponse) {
        return fetchRouteData(ROUTE_SEARCH, context, origLat, origLon, destLat, destLon, time, (JSONObject res) -> {
            try {
                response.onSuccess(new Route(res));
            } catch (JSONException e) {
                errorResponse.onFailure(e);
            }}, errorResponse::onFailure
        );
    }

    private static void makeRequest(Context context, String url, Response.Listener<JSONObject> response, Response.ErrorListener errorResponse) {
        ensureQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest( Request.Method.GET, url,null, response, errorResponse);
        System.out.println(jsonRequest.toString());
        requestQueue.add(jsonRequest);
    }

    private static boolean fetchWeatherData(String resource, Context context, double lat, double lon,
                                           Response.Listener<JSONObject> response, Response.ErrorListener errorResponse) {
        if (resource != null) {
            if (Objects.equals(resource, CURRENT_WEATHER)) {
                long time = System.currentTimeMillis() / (1000 * 60 * 10);
                String cacheRequest = String.format(Locale.UK, "C:T:%d Lat:%.3f Lon:%.3f", time, lat, lon);
                if (weatherRequestCache.containsKey(cacheRequest)) {
                    response.onResponse(weatherRequestCache.get(cacheRequest));
                } else {
                    makeRequest(context, String.format(Locale.UK, resource, lat, lon, WEATHER_API_KEY), (JSONObject res) ->
                        cacheResponse(res, response, cacheRequest, weatherRequestCache), errorResponse);
                }
                return true;
            } else if (Objects.equals(resource, FORECAST_WEATHER)) {
                long time = System.currentTimeMillis() / (1000 * 60 * 60);
                String cacheRequest = String.format(Locale.UK, "F:T:%d Lat:%.3f Lon:%.3f", time, lat, lon);
                if (weatherRequestCache.containsKey(cacheRequest)) {
                    response.onResponse(weatherRequestCache.get(cacheRequest));
                } else {
                    makeRequest(context, String.format(Locale.UK, resource, lat, lon, WEATHER_API_KEY),(JSONObject res) ->
                        cacheResponse(res, response, cacheRequest, weatherRequestCache), errorResponse);
                }
                return true;
            }
            else {
                makeRequest(context, String.format(Locale.UK, resource, lat, lon, WEATHER_API_KEY), response, errorResponse);
                return true;
            }
        }
        return false;
    }

    private static boolean fetchMapData(String resource, Context context, double lat, double lon, String request, int limit,
                                       Response.Listener<JSONObject> response, Response.ErrorListener errorResponse) {
        if (response != null) {
            if (Objects.equals(resource, MAP_SEARCH)) {
                String cacheRequest = String.format(Locale.UK, "MS:Lat:%.4f Lon:%.4f Req:%s Lim:%d",
                        lat, lon, request, limit);
                if (mapRequestCache.containsKey(cacheRequest)) {
                    response.onResponse(mapRequestCache.get(cacheRequest));
                    return true;
                } else {
                    makeRequest(context, String.format(Locale.UK, resource, lat, lon, request.replace(" ", "+")
                            .replace(",", "%2C"), limit, MAP_API_KEY), (JSONObject res) ->
                                    cacheResponse(res, response, cacheRequest, weatherRequestCache), errorResponse);
                    return true;
                }
            } else {
                makeRequest(context, String.format(Locale.UK, resource, lat, lon, request.replace(" ", "+")
                                .replace(",", "%2C"), limit, MAP_API_KEY),
                        response, errorResponse);
                return true;
            }
        }
        return false;
    }

    private static boolean fetchRouteData(String resource, Context context, double origLat, double origLon, double destLat, double destLon,
                                          long departureTime, Response.Listener<JSONObject> response, Response.ErrorListener errorResponse) {
        if (response != null) {
            if (Objects.equals(resource, ROUTE_SEARCH)) {
                long hourTime = departureTime / (1000 * 60 * 60);
                String cacheRequest = String.format(Locale.UK, "RS:OLat:%.4f OLon:%.4f DLat:%.4f DLon:%.4f T:%d",
                        origLat, origLon, destLat, destLon, hourTime);
                if (mapRequestCache.containsKey(cacheRequest)) {
                    response.onResponse(mapRequestCache.get(cacheRequest));
                    return true;
                } else {
                    makeRequest(context, String.format(Locale.UK, resource, origLat, origLon, destLat, destLon,
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date(departureTime)), MAP_API_KEY), (JSONObject res) ->
                            cacheResponse(res, response, cacheRequest, mapRequestCache), errorResponse);
                    return true;
                }
            }
        }
        return false;
    }
}
