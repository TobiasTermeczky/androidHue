package nl.yzaazy.hue.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nl.yzaazy.hue.Models.HueBridge;
import nl.yzaazy.hue.R;

public class HueBridgeAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<HueBridge> mHueBridgeArrayList;

    public HueBridgeAdapter(Context context, LayoutInflater inflater, ArrayList<HueBridge> mHueBridgeArrayList ){
        this.context = context;
        this.inflater = inflater;
        this.mHueBridgeArrayList = mHueBridgeArrayList;
    }

    @Override
    public int getCount() {
        return mHueBridgeArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return mHueBridgeArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if(view == null){
            view = inflater.inflate(R.layout.bridge_row, null);

            viewHolder = new ViewHolder();
            viewHolder.name = view.findViewById(R.id.tvName);
            viewHolder.ip = view.findViewById(R.id.tvIP);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        HueBridge hueBridge = mHueBridgeArrayList.get(i);
        viewHolder.name.setText(hueBridge.getName());
        viewHolder.ip.setText(hueBridge.getIp());
        return view;
    }

    private static class ViewHolder{
        public TextView name;
        public TextView ip;
    }
}
