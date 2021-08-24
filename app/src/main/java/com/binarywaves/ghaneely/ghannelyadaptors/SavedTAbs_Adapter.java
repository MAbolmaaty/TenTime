package com.binarywaves.ghaneely.ghannelyadaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannelyactivites.KareokePackage.KaraokeTabs.SavedTabsFragment;
import com.binarywaves.ghaneely.ghannelyresponse.KarokeSavedObject;

import java.util.List;

/**
 * Created by Amany on 24-Aug-17.
 */

public class SavedTAbs_Adapter extends RecyclerView.Adapter<SavedTAbs_Adapter.ViewHolder> {

    private Context context;
    private List<KarokeSavedObject> my_data;
    private LayoutInflater mInflater;
    private View view;

    public SavedTAbs_Adapter(Context context, List<KarokeSavedObject> my_data) {
        this.context = context;
        this.my_data = my_data;


        mInflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_row, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder,  int position) {

        holder.name_song.setText(my_data.get(position).getKaraokeName());
        holder.itemView.setOnClickListener(view -> SavedTabsFragment.PlaySavedSong(position));

    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name_song;


        public ViewHolder(View itemView) {
            super(itemView);
            name_song = itemView.findViewById(R.id.tvsongName);

        }
    }
}
