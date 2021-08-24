package com.binarywaves.ghaneely.ghannelyadaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


class DialogAdapter extends ArrayAdapter<String> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<String> items = null;

    public DialogAdapter(Context context, int layoutResourceId,
                         ArrayList<String> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final LanguagesHolder holder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getApplicationContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new LanguagesHolder();
            //holder.tvLanguagesName = (TextView) row.findViewById(R.id.tvItemName);
            //holder.rlItemsRow = (RelativeLayout) row
            //		.findViewById(R.id.rlItemRow);
            row.setTag(holder);
        } else {
            holder = (LanguagesHolder) row.getTag();
        }
        final String language = items.get(position);
        holder.tvLanguagesName.setText(language);

        return row;
    }


    static class LanguagesHolder {
        TextView tvLanguagesName;
        RelativeLayout rlItemsRow;
    }
}