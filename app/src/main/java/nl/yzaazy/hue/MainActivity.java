package nl.yzaazy.hue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import nl.yzaazy.hue.Adapters.HueBridgeAdapter;
import nl.yzaazy.hue.Models.HueBridge;

public class MainActivity extends AppCompatActivity {

    ArrayList<HueBridge> mHueBridgeArrayList = new ArrayList<>();
    HueBridgeAdapter mAdapter;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHueBridgeArrayList.add(new HueBridge("test name", "test ip"));
        mHueBridgeArrayList.add(new HueBridge("test name", "test ip"));
        mHueBridgeArrayList.add(new HueBridge("test name", "test ip"));
        mHueBridgeArrayList.add(new HueBridge("test name", "test ip"));
        mHueBridgeArrayList.add(new HueBridge("test name", "test ip"));
        mHueBridgeArrayList.add(new HueBridge("test name", "test ip"));
        mHueBridgeArrayList.add(new HueBridge("test name", "test ip"));
        mHueBridgeArrayList.add(new HueBridge("test name", "test ip"));
        mHueBridgeArrayList.add(new HueBridge("test name", "test ip"));
        mListView = (ListView) findViewById(R.id.lvHueBridge);
        mAdapter = new HueBridgeAdapter(getApplicationContext(), LayoutInflater.from(getApplicationContext()), mHueBridgeArrayList);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
