package nl.yzaazy.hue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import nl.yzaazy.hue.Helper.VolleyHelper;
import nl.yzaazy.hue.Models.Bridge;
import nl.yzaazy.hue.Models.Hue;

public class HueDetailActivity extends AppCompatActivity {

    TextView name;
    Switch switchOnOff, switchAlert, switchColorloop;
    SeekBar seekBarHue, seekBarSaturation, seekBarBrightness;
    private VolleyHelper volleyHelper;
    private Hue hue;
    private Bridge bridge;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hue_detail);
        volleyHelper = new VolleyHelper(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        bridge = new Bridge(
                extras.getString("BridgeName"),
                extras.getString("BridgeIp"),
                extras.getString("BridgeToken"));
        hue = new Hue();
        hue.setId(extras.getString("HueId"));
        hue.setName(extras.getString("HueName"));
        hue.setOn(extras.getBoolean("HueOn"));
        hue.setHue(extras.getInt("HueHue"));
        hue.setSaturation(extras.getInt("HueSaturation"));
        hue.setBrightness(extras.getInt("HueBrightness"));
        hue.setEffect(extras.getString("HueEffect"));
        hue.setAlert(extras.getString("HueAlert"));

        name = (TextView) findViewById(R.id.detailHueName);
        switchOnOff = (Switch) findViewById(R.id.switchDetailOnOff);
        seekBarHue = (SeekBar) findViewById(R.id.seekBarHue);
        seekBarSaturation = (SeekBar) findViewById(R.id.seekBarSaturation);
        seekBarBrightness = (SeekBar) findViewById(R.id.seekBarBrightness);
        switchAlert = (Switch) findViewById(R.id.switchDetailAlert);
        switchColorloop = (Switch) findViewById(R.id.switchDetailColorloop);

        name.setText(hue.getName());
        switchOnOff.setChecked(hue.getOn());
        seekBarHue.setProgress(hue.getHue());
        seekBarSaturation.setProgress(hue.getSaturation());
        seekBarBrightness.setProgress(hue.getBrightness());
        switchAlert.setChecked(hue.getAlert().equals("lselect"));
        switchColorloop.setChecked(hue.getEffect().equals("colorloop"));

        setSwitchListeners();
        setSeekBarListeners();
    }

    private void setSwitchListeners() {
        switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    volleyHelper.turnOn(bridge, hue);
                } else {
                    volleyHelper.turnOff(bridge, hue);
                }
            }
        });

        switchAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                volleyHelper.setAlert(bridge, hue, b);
            }
        });

        switchColorloop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                volleyHelper.setColorloop(bridge, hue, b);
            }
        });
    }

    private void setSeekBarListeners() {
        seekBarHue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                volleyHelper.setHue(bridge, hue, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarSaturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                volleyHelper.setSaturation(bridge, hue, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                volleyHelper.setBrightness(bridge, hue, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
