package com.binarywaves.ghaneely.ghannelyadaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannelyactivites.FriendsTabs.FriendsTabActivity;
import com.binarywaves.ghaneely.ghannelyresponse.FacebookResponse;
import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;

public class FacebookFriendsAdapter extends ArrayAdapter<FacebookResponse>implements Filterable {

    private LayoutInflater inflater;
    private int selectedIndex;
    private ArrayList<FacebookResponse> mDisplayedValues;
    private ArrayList<FacebookResponse> mOriginalValues;
    Context context;

    public FacebookFriendsAdapter(Context context, int resource, int textViewResourceId, ArrayList<FacebookResponse> objects) {
        super(context, resource, textViewResourceId, objects);
        this.mOriginalValues = objects;
        this.mDisplayedValues = objects;
        this.context=context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        FacebookResponse menuItem = mDisplayedValues.get(position);
        convertView = null;
        ViewGroup nullparent = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.friendlistitem, nullparent);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.friendProfilePicture);
            holder.imageView.setProfileId(menuItem.getId());


            //holder.imageView.setImageResource(resourceId);
            holder.text = convertView.findViewById(R.id.textViewName);
            holder.text.setText(menuItem.getName());
            holder.llContainer = convertView.findViewById(R.id.frienditem);
            convertView.setTag(holder);

        }
        holder.llContainer.setOnClickListener(v -> {
            Intent albumIntent = new Intent(context,
                                            FriendsTabActivity.class);
                                    albumIntent.putExtra("profileid", menuItem.getId());
                                    albumIntent.putExtra("profilename",
                                            menuItem.getName());

                                    context.startActivity(albumIntent);
        });
        return convertView;
    }

    public static class ViewHolder {
        public ProfilePictureView imageView;
        public TextView text;
        public RelativeLayout llContainer;

    }
    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<FacebookResponse>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<FacebookResponse> FilteredArrList = new ArrayList<FacebookResponse>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<FacebookResponse>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getName();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new FacebookResponse( mOriginalValues.get(i).getName(), mOriginalValues.get(i).getId()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}


