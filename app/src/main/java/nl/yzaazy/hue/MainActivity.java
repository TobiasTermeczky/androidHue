package nl.yzaazy.hue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import nl.yzaazy.hue.Adapters.BridgeAdapter;
import nl.yzaazy.hue.Models.Bridge;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<Bridge> bridgeList = new ArrayList<>();
    BridgeAdapter adapter;
    ListView bridgeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bridgeList.add(new Bridge("LA134", "http://192.168.1.179/api/", "M4MLKGnNIs-FIcksCcAGGGt-Kjb3hXpEkMUFEIco"));
        bridgeList.add(new Bridge("Avans Aula LA", "http://145.48.205.33/api/", "iYrmsQq1wu5FxF9CPqpJCnm1GpPVylKBWDUsNDhB"));
        bridgeListView = (ListView) findViewById(R.id.lvBridge);
        adapter = new BridgeAdapter(getApplicationContext(), LayoutInflater.from(getApplicationContext()), bridgeList);
        bridgeListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        bridgeListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("Selected Bridge: ", i + "");

        Intent intent = new Intent(getApplicationContext(), HueActivity.class);
        Bridge bridge = (Bridge) this.bridgeList.get(i);
        intent.putExtra("BridgeName", bridge.getName());
        intent.putExtra("BridgeIp", bridge.getIp());
        intent.putExtra("BridgeToken", bridge.getToken());

        startActivity(intent);
    }
}
