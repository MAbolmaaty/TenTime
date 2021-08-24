package com.binarywaves.ghaneely.ghannelyadaptors;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannelyactivites.DrawerActivity;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;

public class DrawerItemCustomAdapter extends ArrayAdapter<ObjectDrawerItem> {
    private Context mContext;
    private int layoutResourceId;
    private ObjectDrawerItem[] data = null;
    private int[] pics = {R.mipmap.iconsho, R.mipmap.iconsse,
            R.mipmap.iconsli, R.mipmap.iconspl, R.mipmap.iconsin, R.mipmap.iconsfr,
            R.mipmap.iconsra, R.mipmap.iconska, R.mipmap.downloadsmenu, R.mipmap.settingsmenuicon};

    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId,
                                   ObjectDrawerItem[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = null;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);


        ImageView imageViewIcon = listItem
                .findViewById(R.id.imageViewIcon);
        TextView textViewName = listItem
                .findViewById(R.id.textViewName);

/*
        if (position == 0) {

			//setting.setVisibility(View.VISIBLE);
			profilepic.setVisibility(View.VISIBLE);
			if (prefs.getString(Constants.UserImage, "").equalsIgnoreCase("")
					&& prefs.getString(Constants.isFaceReg, "")
							.equalsIgnoreCase("false")) {
				profilepic.setImageResource(R.mipmap.app_icon);

			} else {
				String  isfbRegisterd = prefs.getString(Constants.isFaceReg,
						"");
				if (isfbRegisterd.equalsIgnoreCase(
						"true")) {
					SharedPreferences prefsfacebook = mContext.getSharedPreferences("GhaniliPreffacebook",
							Context.MODE_PRIVATE);
					profilePictureView.setProfileId(prefsfacebook.getString(
							Constants.facebookId, ""));

					imageViewIcon.setVisibility(View.GONE);
					profilepic.setVisibility(View.GONE);
				}

				else {
					 
						imageViewIcon.setVisibility(View.GONE);
						profilePictureView.setVisibility(View.GONE);
						profilepic.setVisibility(View.VISIBLE);

					
						String imgpath = ServerConfig.Image_profile
								+ prefs.getString(Constants.UserImage, "");
						String final_imgpath = imgpath.replaceAll(" ", "%20");
						Picasso.with(mContext).load(final_imgpath).error(R.mipmap.defualt_img)
						.placeholder(R.mipmap.defualt_img).into(profilepic);
						DrawerActivity.adapter.notifyDataSetChanged();
						DrawerActivity.mDrawerList.invalidateViews();
						
					}
					imageViewIcon.setVisibility(View.GONE);

				
			}
		} else {
		//	setting.setVisibility(View.VISIBLE);
		//	setting.setVisibility(View.GONE);
			profilepic.setVisibility(View.VISIBLE);
			profilepic.setVisibility(View.GONE);
			profilePictureView.setVisibility(View.VISIBLE);
			profilePictureView.setVisibility(View.GONE);
			imageViewIcon.setVisibility(View.VISIBLE);

		}

		/*
		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//DrawerActivity.mDrawerLayout.closeDrawer(DrawerActivity.list_layout);
				Intent player = new Intent(getContext(), SeetingActivity.class);

				getContext().startActivity(player);

			}
		});
		*/
        String language = LanguageHelper.getCurrentLanguage(mContext);
        ObjectDrawerItem folder = data[position];


        if (language.equalsIgnoreCase("ar")) {
            if (position == DrawerActivity.selectedListItem) {
                listItem.setBackgroundResource(R.drawable.menuselectorright);
                imageViewIcon.setImageResource(pics[position]);

            } else {
                listItem.setBackgroundResource(R.color.black);
                imageViewIcon.setImageResource(folder.icon);

            }


        } else {
            if (position == DrawerActivity.selectedListItem) {
                listItem.setBackgroundResource(R.drawable.menuselector);
                imageViewIcon.setImageResource(pics[position]);

            } else {
                listItem.setBackgroundResource(R.color.black);
                imageViewIcon.setImageResource(folder.icon);

            }
        }

        textViewName.setText(folder.name);

        return listItem;
    }

}
