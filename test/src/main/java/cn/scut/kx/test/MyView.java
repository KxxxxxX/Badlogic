package cn.scut.kx.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by DELL on 2016/12/19.
 */

public class MyView extends View {
    public MyView(Context context, AttributeSet set) {
        super(context, set);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        int viewWidth = this.getWidth();

        Path path1 = new Path();
        path1.moveTo(10, viewWidth * 9/10 + 60);
        path1.lineTo(viewWidth / 5 + 10, viewWidth * 9 / 10 + 60);
        path1.lineTo(viewWidth / 10 + 10, viewWidth * 7/10 +60);
        path1.close();
        canvas.drawPath(path1, paint);
    }
}
