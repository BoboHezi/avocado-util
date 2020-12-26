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
 * 镂空TextView，字体部分镂空效果，显示底部颜色,其他部分显示固定颜色
 *
 * @author Eli Chang
 * @email eliflichang@gmail.com
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();

        Log.d(TAG, "HollowTextView: " + getText());

        // draw text bitmap
        Bitmap textBmp = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(textBmp);
        c.drawColor(mWrapperColor);
        getPaint().setXfermode(new PorterDuffXfermode(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
                ? PorterDuff.Mode.DST_OUT : PorterDuff.Mode.CLEAR));
        super.onDraw(c);
        getPaint().setXfermode(null);

        // draw to view
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(textBmp, 0, 0, mPaint);
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
