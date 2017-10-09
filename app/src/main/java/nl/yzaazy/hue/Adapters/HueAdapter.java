package nl.yzaazy.hue.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import nl.yzaazy.hue.Helper.VolleyHelper;
import nl.yzaazy.hue.Models.Bridge;
import nl.yzaazy.hue.Models.Hue;
import nl.yzaazy.hue.R;

public class HueAdapter extends BaseAdapter{
    Context context;
    LayoutInflater inflater;
    ArrayList<Hue> hueList;
    Bridge bridge;
    private VolleyHelper volleyHelper;

    public HueAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Hue> hueList, Bridge bridge){
        this.context = context;
        this.inflater = layoutInflater;
        this.hueList = hueList;
        this.bridge = bridge;
        volleyHelper = new VolleyHelper(context);
    }

    @Override
    public int getCount() {
        return hueList.size();
    }

    @Override
    public Object getItem(int i) {
        return hueList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if(view == null){
            view = inflater.inflate(R.layout.hue_row, null);

            viewHolder = new ViewHolder();
            viewHolder.name = view.findViewById(R.id.tvHueName);
            viewHolder.onOffSwitch = view.findViewById(R.id.onOffSwitch);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Hue hue = hueList.get(i);
        viewHolder.name.setText(hue.getName());
        viewHolder.onOffSwitch.setChecked(hue.getOn());
        viewHolder.onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    volleyHelper.turnOn(bridge, hue);
                }else {
                    volleyHelper.turnOff(bridge, hue);
                }
            }
        });
        return view;
    }

    private static class ViewHolder{
        public TextView name;
        public Switch onOffSwitch;
    }
}
