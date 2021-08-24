package com.binarywaves.ghaneely.ghannelyactivites;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.binarywaves.ghaneely.R;

import java.io.IOException;
import java.io.InputStream;

public class AboutGhaneelyActivity extends Activity {
    private ImageView back;
    private WebView mTextStatus;
    private ScrollView mScrollView;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutlayout);
        back = findViewById(R.id.navigation_up);
        back.setOnClickListener(v -> finish());

        mTextStatus = findViewById(R.id.TEXT_STATUS_ID);
        mTextStatus.setBackgroundColor(Color.TRANSPARENT);
        mTextStatus.setBackgroundResource(R.mipmap.webviewbackground);
        mScrollView = findViewById(R.id.SCROLLER_ID);


        try {
            InputStream is = getAssets().open("license.txt");

            // We guarantee that the available method returns the total
            // size of the asset...  of course, this does mean that a single
            // asset can't be more than 2 gigs.
            int size = is.available();

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Convert the buffer into a string.
            String text = new String(buffer);
            mTextStatus.loadData(text, "text/html; charset=UTF-8", null);

            // Finally stick the string into the text view.
            //   mTextStatus.setText(Html.fromHtml(text));
        } catch (IOException e) {
            // Should never happen!
            throw new RuntimeException(e);
        }
    }


}
