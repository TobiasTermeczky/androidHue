package nl.yzaazy.hue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import nl.yzaazy.hue.Adapters.HueAdapter;
import nl.yzaazy.hue.Helper.VolleyHelper;
import nl.yzaazy.hue.Interface.HueListCallback;
import nl.yzaazy.hue.Models.Bridge;
import nl.yzaazy.hue.Models.Hue;

public class HueActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, HueListCallback {

    ArrayList<Hue> hueList = new ArrayList<>();
    HueAdapter adapter;
    ListView hueListView;
    Switch allSwitch, discoSwitch;
    Bridge bridge;
    VolleyHelper volleyHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hue_main);
        volleyHelper = new VolleyHelper(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        bridge = new Bridge(
                extras.getString("BridgeName"),
                extras.getString("BridgeIp"),
                extras.getString("BridgeToken"));
        Log.d("Bridge: ", bridge.getName());
        hueListView = (ListView) findViewById(R.id.lvHue);
        allSwitch = (Switch) findViewById(R.id.allSwitch);
        setAllSwitchListener();
        discoSwitch = (Switch) findViewById(R.id.discoSwitch);
        discoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                for (Hue hue : hueList) {
                    if (b) {
                        allSwitch.setChecked(true);
                        volleyHelper.turnOn(bridge, hue);
                    }
                    volleyHelper.setAlert(bridge, hue, b);
                    volleyHelper.setColorloop(bridge, hue, b);
                }
                adapter.notifyDataSetChanged();
            }
        });
        adapter = new HueAdapter(getApplicationContext(), LayoutInflater.from(getApplicationContext()), hueList, bridge, this);
        hueListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        hueListView.setOnItemClickListener(this);
    }

    private void setAllSwitchListener() {
        allSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                for (Hue hue : hueList) {
                    if (b) {
                        volleyHelper.turnOn(bridge, hue);
                    } else {
                        volleyHelper.turnOff(bridge, hue);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getHuesWithVolley() {
        hueList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = bridge.getIp() + "/api/" + bridge.getToken() + "/lights/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            GetHueFromJson(response);
                        } catch (JSONException e) {
                            Log.d("JSON Error", "No valid JSON");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Connection Error", "No internet connection");
            }
        });
        queue.add(stringRequest);
    }

    private void GetHueFromJson(String json) throws JSONException {
        JSONObject jResults = new JSONObject(json);

        for (Iterator<String> iterator = jResults.keys(); iterator.hasNext(); ) {
            Hue hue = new Hue();
            hue.setId(iterator.next());
            hue.setName(jResults.getJSONObject(hue.getId()).getString("name"));
            hue.setOn(jResults.getJSONObject(hue.getId()).getJSONObject("state").getBoolean("on"));
            hue.setHue(jResults.getJSONObject(hue.getId()).getJSONObject("state").getInt("hue"));
            hue.setSaturation(jResults.getJSONObject(hue.getId()).getJSONObject("state").getInt("sat"));
            hue.setBrightness(jResults.getJSONObject(hue.getId()).getJSONObject("state").getInt("bri"));
            hue.setEffect(jResults.getJSONObject(hue.getId()).getJSONObject("state").getString("effect"));
            hue.setAlert(jResults.getJSONObject(hue.getId()).getJSONObject("state").getString("alert"));
            hueList.add(hue);
            if (hue.getOn()) {
                allSwitch.setChecked(true);
            }
            adapter.notifyDataSetChanged();
        }
        Log.d("Hue List: ", hueList.toString());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("Selected Light: ", hueList.get(i).getId());
        Intent intent = new Intent(getApplicationContext(), HueDetailActivity.class);
        Hue hue = this.hueList.get(i);

        //Intent Bridge
        intent.putExtra("BridgeName", bridge.getName());
        intent.putExtra("BridgeIp", bridge.getIp());
        intent.putExtra("BridgeToken", bridge.getToken());

        //Intent Hue
        intent.putExtra("HueId", hue.getId());
        intent.putExtra("HueName", hue.getName());
        intent.putExtra("HueOn", hue.getOn());
        intent.putExtra("HueHue", hue.getHue());
        intent.putExtra("HueSaturation", hue.getSaturation());
        intent.putExtra("HueBrightness", hue.getBrightness());
        intent.putExtra("HueEffect", hue.getEffect());
        intent.putExtra("HueAlert", hue.getAlert());

        startActivity(intent);
    }

    public void onResume() {
        super.onResume();
        getHuesWithVolley();
    }

    @Override
    public void lightOnCallback() {
        allSwitch.setOnCheckedChangeListener(null);
        allSwitch.setChecked(true);
        setAllSwitchListener();
    }

    @Override
    public void lightOffCallback() {
        Boolean allOff = true;
        for (Hue hue : hueList) {
            if (hue.getOn()) {
                allOff = false;
            }
        }
        if (allOff) {
            allSwitch.setOnCheckedChangeListener(null);
            allSwitch.setChecked(false);
            setAllSwitchListener();
        }
    }
}
