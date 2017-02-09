package com.fifed.architecture.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Fedir on 16.09.2016.
 */
public class BitmapNumerator {
    public static Bitmap getNumberedBitmap(int gResId, int position, Context context) {
        String num = String.valueOf(position) ;
        float scale = context.getResources().getDisplayMetrics().density;
        Drawable drawable = ResourceHelper.getResources(gResId, context);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Bitmap.Config bitmapConfig = bitmap.getConfig();
        if(bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setTextSize((int) (14 * scale));
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        Rect bounds = new Rect();
        paint.getTextBounds(num, 0, num.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())/2;
        int y = ((bitmap.getHeight() + bounds.height())/2) - ((bitmap.getHeight() + bounds.height()) / 8) ;
        canvas.drawText(num, x, y, paint);
        return bitmap;
    }
}
