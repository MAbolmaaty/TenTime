package com.swipe.componenet;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public interface CustomCircularProgressListener extends OnSeekBarChangeListener {
    //private CustomCircularProgressListener mcustomListener;
    @Override
    void onProgressChanged(SeekBar seekBar, int progress,
                           boolean fromUser);
    /*
public void setCustomCircularProgressListener(CustomCircularProgressListener listener){
	
	mcustomListener=listener;
}*/


}
