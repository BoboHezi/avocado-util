package eli.avocado.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;

import eli.avocado.utils.R;

/**
 *
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class HollowTextView extends AppCompatTextView {

    private final String TAG = this.getClass().getSimpleName();

    private Paint mPaint;

    // wrapper color
    private int mWrapperColor;

    public HollowTextView(Context context) {
        this(context, null);
    }

    public HollowTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HollowTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HollowTextView, 0, 0);
        mWrapperColor = ta.getColor(R.styleable.HollowTextView_wrapper_color, Color.BLACK);
        ta.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(getTextSize());
        mPaint.setTypeface(getTypeface());

        mPaint.setXfermode(new PorterDuffXfermode(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ?
                PorterDuff.Mode.DST_OUT : PorterDuff.Mode.CLEAR));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setXfermode(new PorterDuffXfermode(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ?
                PorterDuff.Mode.DST_OUT : PorterDuff.Mode.CLEAR));
        Bitmap result = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(result);
        // draw background (totally dark)
        c.drawColor(mWrapperColor);
        // draw text
        c.drawText(getText().toString(), 0, getTextSize(), mPaint);
        Log.i(TAG, "draw text: " + getText());

        //drawing to view
        float textWidth = mPaint.measureText(getText().toString());
        int leftOffset = (int) ((getMeasuredWidth() - textWidth) / 2);
        leftOffset = leftOffset < 0 ? 0 : leftOffset;
        mPaint.setXfermode(null);
        if (leftOffset > 0) {
            Bitmap fillBitmap = Bitmap.createBitmap(leftOffset, getHeight(), Bitmap.Config.ARGB_8888);
            Canvas fillCanvas = new Canvas(fillBitmap);
            fillCanvas.drawColor(mWrapperColor);
            canvas.drawBitmap(fillBitmap, 0, 0, mPaint);
        }
        canvas.drawBitmap(result, leftOffset, 0, mPaint);
    }

    /**
     * 获取包裹色
     *
     * @return
     */
    public int getWrapperColor() {
        return mWrapperColor;
    }

    /**
     * 设置包裹色
     *
     * @param mBaseColor
     */
    public void setWrapperColor(int mBaseColor) {
        this.mWrapperColor = mBaseColor;
        postInvalidate();
    }
}
