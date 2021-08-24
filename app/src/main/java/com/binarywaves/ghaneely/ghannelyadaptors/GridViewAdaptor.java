package com.binarywaves.ghaneely.ghannelyadaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.MoodsList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridViewAdaptor  extends RecyclerView.Adapter<GridViewAdaptor.ViewHolder>{

    private ArrayList<MoodsList> mComments;
    private Context mContext;

    public GridViewAdaptor(Context context, int resource,
                           ArrayList<MoodsList> objects) {
        mContext = context;
        mComments = objects;
    }
    @Override
    public GridViewAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_moodsitem, parent, false);
        return new GridViewAdaptor.ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(GridViewAdaptor.ViewHolder holder, int position) {

        String language = LanguageHelper.getCurrentLanguage(mContext);


        if (language.equalsIgnoreCase("ar")) {
            holder.txtTitle.setText(mComments.get(position).getPersonalDJIDAr());

        }
        if (language.equalsIgnoreCase("en")) {
            holder.txtTitle.setText(mComments.get(position).getPersonalDJName());

        }
        Log.e("DJ IMage Path", ServerConfig.Moods_Url
                + mComments.get(position).getDjImage());
        if (mComments.get(position).getDjImage() != null || mComments.get(position).getDjImage() != "") {
            Picasso.with(mContext).load((ServerConfig.Moods_Url
                    + mComments.get(position).getDjImage())).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(  holder.imageItem);
        } else {
            Picasso.with(mContext).load(R.mipmap.defualt_img).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(  holder.imageItem);
        }
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }



public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView txtTitle;
    public ImageView imageItem;


    public ViewHolder(View itemView) {
        super(itemView);
        txtTitle = itemView.findViewById(R.id.item_text);

        imageItem = itemView.findViewById(R.id.item_image);


    }
}}
