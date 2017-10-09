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

import nl.yzaazy.hue.Models.Hue;
import nl.yzaazy.hue.R;

public class HueAdapter extends BaseAdapter{
    Context context;
    LayoutInflater inflater;
    ArrayList<Hue> hueList;

    public HueAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Hue> hueList){
        this.context = context;
        this.inflater = layoutInflater;
        this.hueList = hueList;
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

        Hue hue = hueList.get(i);
        viewHolder.name.setText(hue.getName());
        viewHolder.onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b){
//                    hue.turnOff();
//                }else {
//                    hue.turnOn();
//                }
            }
        });
        return view;
    }

    private static class ViewHolder{
        public TextView name;
        public Switch onOffSwitch;
    }
}
