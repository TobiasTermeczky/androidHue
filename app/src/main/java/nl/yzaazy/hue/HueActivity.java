package nl.yzaazy.hue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import nl.yzaazy.hue.Helper.HueConnection;
import nl.yzaazy.hue.Interface.HueConnectionInterface;
import nl.yzaazy.hue.Models.Bridge;

public class HueActivity extends AppCompatActivity implements HueConnectionInterface {

    private Bridge bridge;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        this.bridge = new Bridge(
                extras.getString("BridgeName"),
                extras.getString("BridgeIp"),
                extras.getString("BridgeToken"));
        Log.d("Bridge: ", bridge.getName());
        HueConnection hueConnection = new HueConnection(this);
        hueConnection.execute(bridge.getIp() + bridge.getToken() + "/lights/");
    }

    @Override
    public void OnCallCompleted(String json) {
        Log.d("RAW JSON: ", json);
    }
}
