package com.mediamonks.teatimer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by stephan on 10/04/2017.
 */

public class TeaCup extends View {
    private static final String TAG = TeaCup.class.getSimpleName();

    private Bitmap _bitmap;
    private final Paint _paint = new Paint();
    private final Matrix _matrix = new Matrix();
    private final Camera _camera = new Camera();

    public TeaCup(Context context) {
        super(context);

        init();
    }

    public TeaCup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public TeaCup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        _bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.tea_cup);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(_bitmap, 0, 0, _paint);
    }
}
