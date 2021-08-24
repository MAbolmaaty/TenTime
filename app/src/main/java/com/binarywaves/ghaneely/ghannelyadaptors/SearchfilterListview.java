package com.binarywaves.ghaneely.ghannelyadaptors;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.binarywaves.ghaneely.R;

import java.util.ArrayList;
import java.util.List;

public class SearchfilterListview extends ArrayAdapter<String> implements Filterable {

    private int layoutResourceId;
    private ArrayList<String> data = new ArrayList<>();
    private Context mContext;
    private ModelFilter filter;
    private List<String> filteredModelItemsArray;
    private List<String> allModelItemsArray;

    public SearchfilterListview(Context context, int layoutResourceId,
                                ArrayList<String> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
        this.allModelItemsArray = new ArrayList<>();
        allModelItemsArray.addAll(data);
        this.filteredModelItemsArray = new ArrayList<>();
        filteredModelItemsArray.addAll(allModelItemsArray);

        Log.i("category size in image", data.size() + "");
        getFilter();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ModelFilter();
        }
        return filter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View row = convertView;
        GalleryAdaptor.RecordHolder holder = null;
        if (row == null) {

            Log.i("NULL VIEW", "here");
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new GalleryAdaptor.RecordHolder();
            holder.txtTitle = row.findViewById(R.id.rank);
            // holder.albumTitle = (TextView) row.findViewById(R.id.item_text1);

            row.setTag(holder);
        }
        Log.i("Not NULL VIEW", "here");
        holder = (GalleryAdaptor.RecordHolder) row.getTag();
        if (position >= filteredModelItemsArray.size()) {
            position = position % filteredModelItemsArray.size();
            // position=0;

        } else {

        }
        String mtTrack = filteredModelItemsArray.get(position);
        holder.txtTitle.setText(mtTrack);
        // holder.albumTitle.setText(mtTrack.getNationalityEnName());
        // imageLoader.DisplayImage(
        // Constants.RADIO_IMAGE_PATH + mtTrack.getSingerImgPath(),
        // holder.imageItem);


        return row;
    }

    private class ModelFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<String> filteredItems = new ArrayList<>();

                for (int i = 0, l = allModelItemsArray.size(); i < l; i++) {
                    String m = allModelItemsArray.get(i);
                    if (m.toLowerCase().contains(constraint))
                        filteredItems.add(m);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.values = allModelItemsArray;
                    result.count = allModelItemsArray.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filteredModelItemsArray = (ArrayList<String>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filteredModelItemsArray.size(); i < l; i++)
                add(filteredModelItemsArray.get(i));
            notifyDataSetInvalidated();
        }
    }

}
