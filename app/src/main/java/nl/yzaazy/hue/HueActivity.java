package nl.yzaazy.hue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import nl.yzaazy.hue.Adapters.HueAdapter;
import nl.yzaazy.hue.Helper.HueConnection;
import nl.yzaazy.hue.Interface.HueConnectionInterface;
import nl.yzaazy.hue.Models.Bridge;
import nl.yzaazy.hue.Models.Hue;

public class HueActivity extends AppCompatActivity implements HueConnectionInterface, AdapterView.OnItemClickListener {

    ArrayList<Hue> hueList = new ArrayList<>();
    HueAdapter adapter;
    ListView hueListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hue_main);
        Bundle extras = getIntent().getExtras();
        Bridge bridge = new Bridge(
                extras.getString("BridgeName"),
                extras.getString("BridgeIp"),
                extras.getString("BridgeToken"));
        Log.d("Bridge: ", bridge.getName());
        HueConnection hueConnection = new HueConnection(this);
        hueConnection.execute(bridge.getIp() + "/api/" + bridge.getToken() + "/lights/");
        hueListView = (ListView) findViewById(R.id.lvHue);
        adapter = new HueAdapter(getApplicationContext(), LayoutInflater.from(getApplicationContext()), hueList);
        hueListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        hueListView.setOnItemClickListener(this);
    }

    @Override
    public void OnCallCompleted(String json) {
        Log.d("RAW JSON: ", json);
        try {
            GetHueFromJson(json);
        } catch (JSONException e) {
            Log.d("JSON Error", "No valid JSON");
            e.printStackTrace();
        }
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
            adapter.notifyDataSetChanged();
        }

        Log.d("Hue List: ", hueList.toString());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("Selected Light: ", i + "");
        //Put intent here
    }
}
