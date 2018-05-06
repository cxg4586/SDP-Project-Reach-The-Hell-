package com.exp.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ItemScoreAdapter extends BaseAdapter {

    private List<DBScore> objects = new ArrayList<>();

    private Context context;
    private LayoutInflater layoutInflater;

    public void setObjects(List<DBScore> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    public ItemScoreAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public DBScore getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_score, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(DBScore object, ViewHolder holder) {
        holder.name.setText(object.getName());
        holder.score.setText(String.valueOf(object.getScore()));
        holder.time.setText(object.getTime());
    }

    protected class ViewHolder {
        private TextView name;
        private TextView score;
        private TextView time;

        public ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.name);
            score = (TextView) view.findViewById(R.id.score);
            time = (TextView) view.findViewById(R.id.time);
        }
    }
}
