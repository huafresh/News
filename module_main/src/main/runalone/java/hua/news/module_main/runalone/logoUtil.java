package hua.news.module_main.runalone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Environment;
import android.widget.Toast;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import hua.news.module_main.R;

/**
 * Created by hua on 2017/6/9.
 * 生成logo
 */
public class logoUtil {

    //生成logo，供参考
    public static void createLogo(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pixels = new int[w * h];
        bitmap.getPixels(pixels, 0, w, 0, 0, w, h);

        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] != 0) {
                pixels[i] = 0xFFD21112;
            }
        }
        Bitmap bitmap1 = Bitmap.createBitmap(pixels, w, h, bitmap.getConfig());
        Bitmap bitmap2 = bitmap1.copy(bitmap.getConfig(), true);
        Canvas canvas = new Canvas(bitmap2);
        Paint paint = new Paint();
        Paint paint1 = new Paint();
        paint.setColor(0xffffffff);
        paint1.setColor(0xffffffff);
        paint.setTextSize(48);
        paint1.setTextSize(13);
        String text = "华创";
        Rect bounds = new Rect();
        Rect bounds2 = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        paint1.getTextBounds(text, 0, text.length(), bounds2);
        paint.setTextAlign(Paint.Align.CENTER);
        paint1.setTextAlign(Paint.Align.CENTER);

        int x1 = w / 2;
        int y1 = h / 2 + (bounds.height() / 2 - bounds.bottom) - bounds2.height() / 2 - 5;

        int x2 = w / 2;
        int y2 = h / 2 + bounds2.height() / 2 - bounds2.bottom + bounds.height() / 2 + 5;

        paint1.setAntiAlias(true);
        paint1.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText("n   e   w   s", x2, y2, paint1);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/STXINGKA.TTF");
        paint.setTypeface(typeface);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("华创", x1, y1, paint);

        String path = Environment.getExternalStorageDirectory() + "/logo.png";
        File file = new File(path);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();

    }

}
