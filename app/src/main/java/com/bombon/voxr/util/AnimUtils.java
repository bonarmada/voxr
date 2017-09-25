package com.bombon.voxr.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by Vaughn on 8/17/17.
 */

public class AnimUtils {
    public static void ImageViewAnimatedChange(Context c, final ImageView v, final Drawable new_image) {
        final Animation fadeOut = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        fadeOut.setDuration(150);

        final Animation fadeIn  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        fadeIn.setDuration(200);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageDrawable(new_image);
                fadeIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(fadeIn);
            }
        });
        v.startAnimation(fadeOut);
    }
}
