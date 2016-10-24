package com.codepath.newyorktimes.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.codepath.newyorktimes.activities.R;

/**
 * Created by sdevired on 10/23/16.
 */
public class ChromeShareProvider {
    Bitmap bitmap;

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

    public void setPendingIntent(PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    PendingIntent pendingIntent;

    public ChromeShareProvider(Activity activity){
        //prepare intent
       Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "http://www.codepath.com");

        //pending intent
        int requestCode = 100;

         pendingIntent = PendingIntent.getActivity(activity,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

          bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_sharable);
    }

}
