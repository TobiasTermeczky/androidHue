package nl.yzaazy.hue.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nl.yzaazy.hue.Models.Bridge;
import nl.yzaazy.hue.R;

public class BridgeAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<Bridge> mBridgeArrayList;

    public BridgeAdapter(Context context, LayoutInflater inflater, ArrayList<Bridge> mBridgeArrayList){
        this.context = context;
        this.inflater = inflater;
        this.mBridgeArrayList = mBridgeArrayList;
    }

    @Override
    public int getCount() {
        return mBridgeArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return mBridgeArrayList.get(i);
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

        Bridge bridge = mBridgeArrayList.get(i);
        viewHolder.name.setText(bridge.getName());
        viewHolder.ip.setText(bridge.getIp());
        return view;
    }

    private static class ViewHolder{
        public TextView name;
        public TextView ip;
    }
}
