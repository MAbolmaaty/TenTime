package com.binarywaves.ghaneely.ghannelyadaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.KareokePackage.KaraokeTabs.KaraokeTabActivity;
import com.binarywaves.ghaneely.ghannelyactivites.KareokePackage.KareokePlayerActivity;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Amany on 24-Aug-17.
 */

public class SongsTAbs_Adapter extends ArrayAdapter<TrackObject> {

    private Context context;
    private List<TrackObject> my_data;
    private LayoutInflater mInflater;
    private View view;

    public SongsTAbs_Adapter(Context context, int layoutResourceId, List<TrackObject> my_data) {
        super(context, layoutResourceId, my_data);
        this.context = context;
        this.my_data = my_data;


        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        ViewHolder holder = null;


            if (itemView == null) {
                holder = new ViewHolder();

                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.songs_row, parent, false);

                holder.name_song = itemView.findViewById(R.id.tvsongname);
                holder.singer_name = itemView.findViewById(R.id.tvsingername);
                holder.photo_song = itemView.findViewById(R.id.item_image);
                holder.rlcontainer = itemView.findViewById(R.id.rlcontainer);
                itemView.setTag(holder);
            }
        holder = (ViewHolder) itemView.getTag();
        if (position >= my_data.size()) {
            position = position % my_data.size();
            //  position=0;

        } else {


        }
        final String language = LanguageHelper.getCurrentLanguage(context);

                if (language.equalsIgnoreCase("ar")) {
                holder.name_song.setText(my_data.get(position).getTrackArName());
                holder.singer_name.setText(my_data.get(position).getSingerArName());

            }
            if (language.equalsIgnoreCase("en")) {
                holder.name_song.setText(my_data.get(position).getTrackEnName());
                holder.singer_name.setText(my_data.get(position).getSingerEnName());

            }
            if (my_data.get(position).getTrackImage() != null || my_data.get(position).getTrackImage() != "") {

                Picasso.with(context)
                        .load((ServerConfig.Image_path + my_data.get(position).getTrackImage()))
                        .error(R.mipmap.defualt_img)
                        .placeholder(R.mipmap.defualt_img).into(holder.photo_song);
            } else {
                Picasso.with(context).load(R.mipmap.defualt_img)
                        .error(R.mipmap.defualt_img)
                        .placeholder(R.mipmap.defualt_img).into(holder.photo_song);
            }

        ViewHolder finalHolder = holder;
        int finalPosition = position;
        holder.rlcontainer.setOnClickListener(view1 -> {
                Drawable mipmap = finalHolder.photo_song.getDrawable();
                Bitmap bitmap = ((BitmapDrawable) mipmap).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();

                Intent intent = new Intent(context, KareokePlayerActivity.class);
                intent.putExtra("picture", b);
                if (language.equalsIgnoreCase("ar")) {
                    intent.putExtra("trackname", my_data.get(finalPosition).getTrackArName());

                    intent.putExtra("singername", my_data.get(finalPosition).getSingerArName());
                    intent.putExtra("trackid", my_data.get(finalPosition).getTrackId());
                    intent.putExtra("trackpath", my_data.get(finalPosition).getTrackPath());
                    intent.putExtra("trackLyricFile", my_data.get(finalPosition).getLyricFile());


                }
                if (language.equalsIgnoreCase("en")) {
                    intent.putExtra("trackname", my_data.get(finalPosition).getTrackEnName());

                    intent.putExtra("singername", my_data.get(finalPosition).getSingerEnName());
                    intent.putExtra("trackid", my_data.get(finalPosition).getTrackId());
                    intent.putExtra("trackpath", my_data.get(finalPosition).getTrackPath());
                    intent.putExtra("trackLyricFile", my_data.get(finalPosition).getLyricFile());

                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                KaraokeTabActivity.getActivity().finish();
                ((Activity) context).finish();
                context.startActivity(intent);


            });


        return itemView;
    }


    @Override
    public int getCount() {
        return my_data.size();
    }

    public class ViewHolder {

        public TextView singer_name;
        public TextView name_song;
        public ImageView photo_song;
        public RelativeLayout rlcontainer;

    }
}





