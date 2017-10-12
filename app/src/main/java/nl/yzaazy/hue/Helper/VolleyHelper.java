package nl.yzaazy.hue.Helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

import nl.yzaazy.hue.Models.Bridge;
import nl.yzaazy.hue.Models.Hue;

public class VolleyHelper {

    private static VolleyHelper sInstance = null;
    private Context context;
    private String requestResponse;

    public VolleyHelper(Context context){
        this.context = context;
    }

    public static VolleyHelper getsInstance(Context context){
        if (sInstance == null){
            sInstance = new VolleyHelper(context);
        }
    return sInstance;
    }

    public String doRequest(String requestUrl, final String requestBody, int requestMethod){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(requestMethod, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response: ", response);
                        requestResponse = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.toString());
                error.printStackTrace();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while getting the bytes of %s using %s",
                            requestBody, "utf-8");
                    uee.printStackTrace();
                    return null;
                }

            }

        };
        requestQueue.add(stringRequest);
        return requestResponse;
    }

    public void turnOn(Bridge bridge, Hue hue) {
        this.doRequest(bridge.getIp() + "/api/" + bridge.getToken() + "/lights/" + hue.getId() + "/state/", "{\"on\":true}", Request.Method.PUT);
        hue.setOn(true);
    }

    public void turnOff(Bridge bridge, Hue hue) {
        this.doRequest(bridge.getIp() + "/api/" + bridge.getToken() + "/lights/" + hue.getId() + "/state/", "{\"on\":false}", Request.Method.PUT);
        hue.setOn(false);
    }

    public void setColorloop(Bridge bridge, Hue hue, boolean b){
        String effect = "colorloop";
        if (!b){
            effect = "none";
        }
        this.doRequest(bridge.getIp() + "/api/" + bridge.getToken() + "/lights/" + hue.getId() + "/state/", "{\"effect\":\"" + effect + "\"}", Request.Method.PUT);
        hue.setEffect(effect);
    }

    public void setAlert(Bridge bridge, Hue hue, boolean b) {
        String alert = "lselect";
        if (!b){
            alert = "none";
        }
        this.doRequest(bridge.getIp() + "/api/" + bridge.getToken() + "/lights/" + hue.getId() + "/state/", "{\"alert\":\"" + alert + "\"}", Request.Method.PUT);
        hue.setAlert(alert);
    }

    public void setHue(Bridge bridge, Hue hue, int i) {
        this.doRequest(bridge.getIp() + "/api/" + bridge.getToken() + "/lights/" + hue.getId() + "/state/", "{\"hue\":" + i + "}", Request.Method.PUT);
        hue.setHue(i);
    }

    public void setSaturation(Bridge bridge, Hue hue, int i) {
        this.doRequest(bridge.getIp() + "/api/" + bridge.getToken() + "/lights/" + hue.getId() + "/state/", "{\"sat\":" + i + "}", Request.Method.PUT);
        hue.setSaturation(i);
    }

    public void setBrightness(Bridge bridge, Hue hue, int i) {
        this.doRequest(bridge.getIp() + "/api/" + bridge.getToken() + "/lights/" + hue.getId() + "/state/", "{\"bri\":" + i + "}", Request.Method.PUT);
        hue.setHue(i);
    }
}
