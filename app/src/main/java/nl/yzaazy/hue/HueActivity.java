package nl.yzaazy.hue;

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
import nl.yzaazy.hue.Models.Bridge;
import nl.yzaazy.hue.Models.Hue;

public class HueActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<Hue> hueList = new ArrayList<>();
    HueAdapter adapter;
    ListView hueListView;
    Switch hueSwitch;
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
        getHueWithVolley();
        hueListView = (ListView) findViewById(R.id.lvHue);
        hueSwitch = (Switch) findViewById(R.id.allSwitch);
        hueSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //// TODO: 9-10-2017 hier zit nog een bug als ie aan is en je zet master aan gaat de lamp uit!
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
        adapter = new HueAdapter(getApplicationContext(), LayoutInflater.from(getApplicationContext()), hueList, bridge);
        hueListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        hueListView.setOnItemClickListener(this);
    }

    private void getHueWithVolley() {
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
            if(hue.getOn()){
                hueSwitch.setChecked(true);
            }
            adapter.notifyDataSetChanged();
        }
        Log.d("Hue List: ", hueList.toString());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("Selected Light: ", hueList.get(i).getId());
        //Put intent here
    }
}
