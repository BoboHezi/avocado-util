package eli.avocado.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import eli.avocado.utils.R;

/**
 * 圆角图片
 *
 * @author Eli Chang
 * @email eliflichang@gmail.com
 */
public class CircleImageView extends AppCompatImageView {

    private Paint mPaint;
    //圆角半径
    private int mRadius;
    //边框颜色
    private int mBorderColor;
    //边框宽度
    private int mBorderWidth;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        mRadius = (int) ta.getDimension(R.styleable.CircleImageView_radius, 0);
        mBorderColor = ta.getColor(R.styleable.CircleImageView_borderColor, 0);
        mBorderWidth = (int) ta.getDimension(R.styleable.CircleImageView_borderWidth, 0);
        ta.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (width <= 0 || height <= 0) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getRoundBitmapByShader(Bitmap bitmap, int outWidth, int outHeight,
                                                int radius, int boarder, int borderColor) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float widthScale = outWidth * 1f / width;
        float heightScale = outHeight * 1f / height;

        Matrix matrix = new Matrix();
        matrix.setScale(widthScale, heightScale);
        Bitmap desBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(desBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        RectF rect = new RectF(boarder, boarder, outWidth - boarder, outHeight - boarder);
        canvas.drawRoundRect(rect, radius, radius, paint);

        if (boarder > 0) {
            Paint boarderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            boarderPaint.setColor(borderColor);
            boarderPaint.setStyle(Paint.Style.STROKE);
            boarderPaint.setStrokeWidth(boarder);
            canvas.drawRoundRect(rect, radius, radius, boarderPaint);
        }
        return desBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (null != drawable) {
            Bitmap bitmap = getBitmapFromDrawable(drawable);
            if (bitmap == null) {
                super.onDraw(canvas);
                return;
            }
            Bitmap b = getRoundBitmapByShader(bitmap, getWidth(), getHeight(), mRadius, mBorderWidth, mBorderColor);
            final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
            final Rect rectDest = new Rect(0, 0, getWidth(), getHeight());
            mPaint.reset();
            canvas.drawBitmap(b, rectSrc, rectDest, mPaint);
        } else {
            super.onDraw(canvas);
        }
    }

    public void setRadius(int mRadius) {
        this.mRadius = mRadius;
        postInvalidate();
    }

    public void setBorderColor(int mBorderColor) {
        this.mBorderColor = mBorderColor;
        postInvalidate();
    }

    public void setBorderWidth(int mBorderWidth) {
        this.mBorderWidth = mBorderWidth;
        postInvalidate();
    }
}
