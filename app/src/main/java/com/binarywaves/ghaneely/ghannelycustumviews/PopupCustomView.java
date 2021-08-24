package com.binarywaves.ghaneely.ghannelycustumviews;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;

/**
 * Created by Amany on 01-Feb-17.
 */

class PopupCustomView {
    public void showDialog(Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialogpopup);

        TextView text = dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton = dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }
}