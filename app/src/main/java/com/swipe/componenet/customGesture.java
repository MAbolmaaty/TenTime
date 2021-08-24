package com.swipe.componenet;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;


class customGesture extends GestureDetector.SimpleOnGestureListener {

    private Context mcontext;


    public customGesture(Context mcontext) {
        super();
        this.mcontext = mcontext;
    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // TODO Auto-generated method stub
        float sensitivity = 50;
        if (e1.getY() - e2.getY() > sensitivity) {


            return true;
        }
        // Swipe Down Check
        else if (e2.getY() - e1.getY() > sensitivity) {
            // Setting Image Resource to Down_Arrow on Swipe Down
            //	Toast.makeText(mcontext, "subdown", Toast.LENGTH_SHORT).show();
            return true;
        }
        // Swipe Left Check
        else if (e1.getX() - e2.getX() > sensitivity) {
            // Setting Image Resource to Left_Arrow on Swipe Left
            //	 Toast.makeText(mcontext, "SubLeft", Toast.LENGTH_SHORT).show();

            return true;
        }
        // Swipe Right Check
        else if (e2.getX() - e1.getX() > sensitivity) {
            //	 Toast.makeText(mcontext, "SubRight", Toast.LENGTH_SHORT).show();


            return true;
        } else {
            // If some error occurrs, setting again to Default_Image (Actually
            // it will never happen in this case)
            //	Toast.makeText(mcontext, "Nothing", Toast.LENGTH_SHORT).show();
            return true;
        }

    }


}
