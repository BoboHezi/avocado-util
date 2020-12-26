package eli.avocado.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ArrayRes;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import eli.avocado.utils.R;

/**
 * 渐变色ViewGroup，只在子控件部分显示渐变色，其他部分显示固定颜色
 *
 * @author Eli Chang
 * @email eliflichang@gmail.com
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class GradientLayout extends LinearLayout {

    private final String TAG = this.getClass().getSimpleName();

    private Paint mPaint;

    // whether or not draw gradient colors
    private boolean mShowGradient;

    // color res for gradient color array
    private int mColorArrayRes;

    // angle of gradient
    private int mAngle;

    // color array for gradient
    private int[] mColorArray;

    // background color
    private int mBaseColor;

    private Integer[] childPositions;

    public GradientLayout(Context context) {
        this(context, null);
    }

    public GradientLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public GradientLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GradientLayout, 0, 0);
        mShowGradient = ta.getBoolean(R.styleable.GradientLayout_showGradient, true);
        mColorArrayRes = ta.getResourceId(R.styleable.GradientLayout_colors, R.array.gradient_colors);
        mAngle = ta.getInteger(R.styleable.GradientLayout_angle, 45);
        mBaseColor = ta.getColor(R.styleable.GradientLayout_base_color, Color.BLACK);
        ta.recycle();

        setBackgroundColor(mBaseColor);
        if (mShowGradient) {
            mColorArray = getResources().getIntArray(mColorArrayRes);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        List<Integer> posies = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child != null && child.getRight() > child.getLeft() && child.getBottom() > child.getTop()) {
                posies.add(child.getLeft());
                posies.add(child.getTop());
                posies.add(child.getRight());
                posies.add(child.getBottom());
            }
        }
        if (posies.size() != 0 && posies.size() % 4 == 0) {
            childPositions = posies.toArray(new Integer[0]);
            Log.i(TAG, "find children: " + posies);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mShowGradient) {
            int viewWidth = getMeasuredWidth();
            int viewHeight = getMeasuredHeight();

            // draw gradient bitmap
            Bitmap gd = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(gd);
            float[] positions = new float[4];
            calculatePosition(positions, viewWidth, viewHeight, mAngle);
            LinearGradient gradient =
                    new LinearGradient(positions[0], positions[1], positions[2], positions[3],
                            mColorArray, null, Shader.TileMode.CLAMP);
            mPaint.setShader(gradient);
            c.drawRect(0, 0, viewWidth, viewHeight, mPaint);
            canvas.drawBitmap(gd, 0, 0, mPaint);

            // draw child mask
            if (childPositions != null && childPositions.length >= 4 && childPositions.length % 4 == 0) {
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                Bitmap child = childMask(viewWidth, viewHeight, childPositions);
                canvas.drawBitmap(child, 0, 0, mPaint);
                mPaint.setXfermode(null);
            }

            // draw background bitmap
            Bitmap bg = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
            c = new Canvas(bg);
            c.drawColor(mBaseColor);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
            canvas.drawBitmap(bg, 0, 0, mPaint);
            mPaint.setXfermode(null);
        }
        super.onDraw(canvas);
    }

    private void calculatePosition(float[] positions, int width, int height, int angle) {
        int radius = Math.max(width, height) / 2;
        int circleX = width / 2;
        int circleY = height / 2;
        float radian = (float) (angle * Math.PI / 180);

        float p_H = (float) (radius * Math.sin(radian));
        float p_W = (float) (radius * Math.cos(radian));
        positions[0] = circleX + p_W;
        positions[1] = circleY - p_H;
        positions[2] = circleX - p_W;
        positions[3] = circleY + p_H;
    }

    private Bitmap childMask(int w, int h, Integer[] position) {
        Log.i(TAG, "draw a bitmap contains " + position.length / 4 + " children mask.");
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(Color.BLACK);
        for (int i = 0; i < position.length; i += 4) {
            c.drawRect(position[i], position[i + 1], position[i + 2], position[i + 3], p);
        }
        return bm;
    }

    /**
     * 是否显示渐变色
     *
     * @return
     */
    public boolean isShowGradient() {
        return mShowGradient;
    }

    /**
     * 设置是否显示渐变色
     *
     * @param mShowGradient
     */
    public void setShowGradient(boolean mShowGradient) {
        this.mShowGradient = mShowGradient;
        postInvalidate();
    }

    /**
     * 获取颜色队列
     *
     * @return
     */
    public int getColorArrayRes() {
        return mColorArrayRes;
    }

    /**
     * 设置颜色队列
     *
     * @param mColorArrayRes
     */
    public void setColorArrayRes(@ArrayRes int mColorArrayRes) {
        this.mColorArrayRes = mColorArrayRes;
        mColorArray = getResources().getIntArray(mColorArrayRes);
        postInvalidate();
    }

    /**
     * 获取角度
     *
     * @return
     */
    public int getAngle() {
        return mAngle;
    }

    /**
     * 设置角度
     *
     * @param mAngle
     */
    public void setAngle(int mAngle) {
        this.mAngle = mAngle;
        postInvalidate();
    }

    /**
     * 获取背景色
     *
     * @return
     */
    public int getBackgroundColor() {
        return mBaseColor;
    }

    /**
     * 设置背景色
     *
     * @param mBaseColor
     */
    public void setBaseColor(int mBaseColor) {
        this.mBaseColor = mBaseColor;
        postInvalidate();
    }
}
