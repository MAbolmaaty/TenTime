package com.binarywaves.ghaneely.ghannelyactivites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;

public class HelpActivity extends DrawerActivity {
    private Button send;
    private TextView email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = findViewById(R.id.content_frame);
        // inflate the custom activity layout
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup nullparent = null;

        View activityView = layoutInflater.inflate(R.layout.help, nullparent, false);
        send = activityView.findViewById(R.id.register_btn);
        email = activityView.findViewById(R.id.emailtext);

        frameLayout.addView(activityView);


        send.setOnClickListener(v -> {


            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email.getText().toString()});
            intent.putExtra(Intent.EXTRA_SUBJECT, "");

            startActivity(Intent.createChooser(intent, " Send E-mail via..."));
        });
    }
}
