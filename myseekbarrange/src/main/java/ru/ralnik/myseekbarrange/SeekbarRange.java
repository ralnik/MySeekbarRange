package ru.ralnik.myseekbarrange;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import ru.ralnik.myseekbarrange.interfaces.OnSeekbarRangeChangeListener;


public class SeekbarRange extends View {
    Context context;
    private Bitmap thumbLeft = getBitmap(getResources().getDrawable(R.drawable.thumb));
    private Bitmap thumbRight = getBitmap(getResources().getDrawable(R.drawable.thumb));
    private Paint paint;
    private float thumbLeft_x, thumbRight_x;
    private float xStart, xEnd, downX = 0;
    private int selectedThumb;
    private float offset, offsetLeft, offsetRight;
    private OnSeekbarRangeChangeListener scl;
    private Double thumb1Value, thumb2Value;


    //   ******* ATTRIBUTES ***************
    private float sbr_absoluteMinValue = 1F;
    private float sbr_absoluteMaxValue = 100F;
    private Drawable sbr_bgSeekbarRange;
    private int sbr_barColor;
    private int sbr_barHighlightColor;
    private Drawable sbr_left_thumb_image;
    private Drawable sbr_right_thumb_image;
    private float sbr_cornerRadius;

    public SeekbarRange(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public SeekbarRange(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SeekbarRange);
            try {
                if (typedArray.getFloat(R.styleable.SeekbarRange_sbr_absoluteMinValue, 1F) != 0) {
                    this.sbr_absoluteMinValue = typedArray.getFloat(R.styleable.SeekbarRange_sbr_absoluteMinValue, 1F);
                } else {
                    new Exception("Минимальный показатель не должен быть равен '0'");
                }
                this.sbr_absoluteMaxValue = typedArray.getFloat(R.styleable.SeekbarRange_sbr_absoluteMaxValue, 100F);
                this.sbr_bgSeekbarRange = typedArray.getDrawable(R.styleable.SeekbarRange_sbr_bgSeekbarRange);
                this.sbr_barColor = typedArray.getColor(R.styleable.SeekbarRange_sbr_barColor, Color.GRAY);
                this.sbr_barHighlightColor = typedArray.getColor(R.styleable.SeekbarRange_sbr_barHighlightColor, Color.BLACK);
                this.sbr_left_thumb_image = getLeftThumb(typedArray);
                this.sbr_right_thumb_image = getRightThumb(typedArray);
                this.sbr_cornerRadius = typedArray.getFloat(R.styleable.SeekbarRange_sbr_cornerRaduis, 0F);
            } finally {
                typedArray.recycle();
            }
            thumbLeft = getBitmap(sbr_left_thumb_image);
            thumbRight = getBitmap(sbr_right_thumb_image);

        }
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        this.xStart = 0;
        this.xEnd = width;

        getLayoutParams().height = thumbLeft.getHeight();

        this.thumbLeft_x = xStart;
        this.thumbRight_x = xEnd - thumbRight.getWidth();
    }

    public float getAbsoluteMinValue() {
        return sbr_absoluteMinValue;
    }

    public void setAbsoluteMinValue(float absoluteMinValue) {
        this.sbr_absoluteMinValue = absoluteMinValue;
    }

    public float getAbsoluteMaxValue() {
        return sbr_absoluteMaxValue;
    }

    public void setAbsoluteMaxValue(float absoluteMaxValue) {
        this.sbr_absoluteMaxValue = absoluteMaxValue;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        setupBar(canvas, paint);
        setupHighlightBar(canvas, paint);

        canvas.drawBitmap(thumbLeft, thumbLeft_x, 0, paint);
        canvas.drawBitmap(thumbRight, thumbRight_x, 0, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int mx = (int) event.getX();
        float deltaX = 0;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if ((mx >= thumbLeft_x && mx <= thumbLeft_x + thumbLeft.getWidth()) && (thumbLeft_x != thumbRight_x)) {
                    selectedThumb = 1;
                    offset = mx - thumbLeft_x;
                } else if (mx >= thumbRight_x && mx <= thumbRight_x + thumbLeft.getWidth() && (thumbLeft_x != thumbRight_x)) {
                    selectedThumb = 2;
                    offset = thumbRight_x - mx;
                } else if ((thumbLeft_x == thumbRight_x)) {
                    selectedThumb = 3;
                    downX = event.getX();
                    offsetLeft = mx - thumbLeft_x;
                    offsetRight = thumbRight_x - mx;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                deltaX = event.getX();
//проверяем если selectedThumb=1 значит двигается 1 ползунок(левый)
                if (selectedThumb == 1) {
                    thumbLeft_x = mx - offset;
//проверяем если selectedThumb=2 значит двигается 2 ползунок(правый)
                } else if (selectedThumb == 2) {
                    thumbRight_x = mx + offset;
//проверяем если selectedThumb=2 значит оба ползунка стоят друг на друге, ниже нужно определить если палец потянет в лево значит будет выбираться левый ползунок, если вправо то правый ползунок.
                } else if (selectedThumb == 3) {
                    //проверяем если координаты обоих thumb равны и равны 0(т.е. в самом начале) тогда двигать можно только правый thumb и только вправо
                    if (thumbRight_x == xStart && thumbLeft_x == xStart) {
                        selectedThumb = 2;
                    }
                    //проверяем если координаты обоих thumb равны и находятся в самой крайней правой точки(т.е. в самом конце) тогда двигать можно только левый thumb и только влево
                    if (thumbRight_x == xEnd - thumbRight.getWidth() && thumbLeft_x == xEnd - thumbLeft.getWidth()) {
                        selectedThumb = 1;
                    }
                    //Проверяем в какую сторону ползунок потянули с момента нажатия на ползунок и текущей координатой пальца при перемещении
                    //если не отпускать палец, то ползунок будет перепрагивать другой ползунок, поэтому в зависимости от выбранного ползунка делаем прерывание изменяя значение selectedThumb, отличное от 3х
                    //система такая, если палец не отпускается то событие на ACTION_MOVE не прекращеается, поэтому в зависимости от выбранного ползунка ссылаеся на него, и тогда быдет выполнятся под у которого selectedThumb= 1 или selectedThumb = 2, в этот разедел он не вернутся
                    if (deltaX < downX) {
                        thumbLeft_x = mx - offsetLeft;
                        if (thumbLeft_x < thumbRight_x) {
                            selectedThumb = 1;
                        }
                    } else {
                        thumbRight_x = mx + offsetRight;
                        if (thumbRight_x > thumbLeft_x) {
                            selectedThumb = 2;
                        }
                    }
                }
                break;
            //как только палец убираем, selectedThumb обнуляем
            case MotionEvent.ACTION_UP:
                selectedThumb = 0;
                break;
        }
        //дополнительная проверка, если выбран правый ползунок, то:
        if (selectedThumb == 2) {
            //проверяем, чтобы не вышел за правый края
            if (thumbRight_x > getWidth() - thumbRight.getWidth())
                thumbRight_x = getWidth() - thumbRight.getWidth();
            //проверяем, чтобы не вышел за левый край
            if (thumbRight_x <= xStart)
                thumbRight_x = xStart;
            //проверяем, чтобы не вышел за пределы левого ползунка
            if (thumbRight_x <= thumbLeft_x)
                thumbRight_x = thumbLeft_x;
        }
        //если выбран левый ползунок, то:
        else if (selectedThumb == 1) {
            //Проверяем, чтобы не вышел за левый край
            if (thumbLeft_x < xStart)
                thumbLeft_x = xStart;
            //Проверяем, чтобы не вышел за правый край
            if (thumbLeft_x >= getWidth() - (thumbRight.getWidth()))
                thumbLeft_x = getWidth() - (thumbRight.getWidth());
            //Проверяем, чтобы не вышел за пределы правого ползунка
            if (thumbLeft_x > thumbRight_x)
                thumbLeft_x = thumbRight_x;
        }
        invalidate();
        if (scl != null) {
            calculateThumbValue();
            scl.SeekbarRangeValueChanged(thumb1Value, thumb2Value);
        }
        return true;

    }

    protected void setupBar(final Canvas canvas, final Paint paint) {
        RectF rect = new RectF();
        float barHeight = (thumbLeft.getHeight() * 0.5f) * 0.3f;
        float barPadding = thumbLeft.getWidth() * 0.5f;
        rect.left = barPadding;
        rect.top = 0.5f * (getHeight() - barHeight);
        rect.right = getWidth() - barPadding;
        rect.bottom = 0.5f * (getHeight() + barHeight);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(this.sbr_barColor);
        paint.setAntiAlias(true);

        canvas.drawRoundRect(rect, this.sbr_cornerRadius, this.sbr_cornerRadius, paint);
    }

    protected void setupHighlightBar(final Canvas canvas, final Paint paint) {
        RectF rect = new RectF();
        float barHeight = (thumbLeft.getHeight() * 0.5f) * 0.3f;
        float barPadding = thumbLeft.getWidth() * 0.5f;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(sbr_barHighlightColor);

        rect.left = barPadding + thumbLeft_x;
        rect.right = (getWidth() - barPadding) - (getWidth() - thumbRight_x - thumbRight.getWidth());
        rect.top = 0.5f * (getHeight() - barHeight);
        rect.bottom = 0.5f * (getHeight() + barHeight);
        canvas.drawRoundRect(rect, this.sbr_cornerRadius, this.sbr_cornerRadius, paint);
    }

    private void calculateThumbValue() {
        //Вначале вычисляем процентное соотнощение от позиции thumb-ов
        Float minValue;
        Float maxValue;
        Float resultMinValue = null;
        Float resultMaxValue;
        minValue = (thumbLeft_x / (getWidth() - thumbLeft.getWidth())) * 100;
        maxValue = (thumbRight_x / (getWidth() - thumbRight.getWidth())) * 100;
        Log.d("myDebug", "minValue : " + minValue);
        Log.d("myDebug", "maxValue : " + maxValue);
        //Далее вычислием из полученого процент, процентное соотношение уже к указаными абсолютным значениям
        String result1 = String.format("%.2f", (this.sbr_absoluteMaxValue/100)*minValue).replace(",", ".");
        String result2 = String.format("%.2f", (this.sbr_absoluteMaxValue/100)*maxValue).replace(",", ".");
        thumb1Value = Double.parseDouble(result1);
        thumb2Value = Double.parseDouble(result2);
    }

    public Double getMin() {
        calculateThumbValue();
        return this.thumb1Value;
    }

    public void setMin(float min) {
        min = (min / this.sbr_absoluteMaxValue) * 100;
        thumbLeft_x = ((float) (getWidth() - thumbLeft.getWidth()) / 100) * min;
        invalidate();
    }

    public Double getMax() {
        calculateThumbValue();
        return this.thumb2Value;
    }

    public void setMax(float max) {
        max = (max / this.sbr_absoluteMaxValue) * 100;
        thumbRight_x = ((float)(getWidth() - thumbLeft.getWidth()) / 100) * max;
        invalidate();
    }

    /*
        thumb - идентификатор картинки из ресурсов "R.drawable.thumb"
     */
    public void setThumbLeftBitmap(Drawable thumb) {
        this.thumbLeft = getBitmap(thumb);
    }

    public void setThumbRightBitmap(Drawable thumb) {
        this.thumbRight = getBitmap(thumb);
    }

    public void setBarColor(int color) {
        this.sbr_barColor = color;
    }

    public void setBarHighlightColor(int color) {
        this.sbr_barHighlightColor = color;
    }


    public void setSeekBarChangeListener(OnSeekbarRangeChangeListener scl) {
        this.scl = scl;
    }

    //////////////////////////////////////////
    // PROTECTED METHODS
    //////////////////////////////////////////
    protected Bitmap getBitmap(Drawable drawable) {
        //return (drawable != null) ? ((BitmapDrawable) drawable).getBitmap() : null;
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }

    protected Drawable getLeftThumb(final TypedArray typedArray) {
        if (typedArray.getDrawable(R.styleable.SeekbarRange_sbr_left_thumb_image) != null) {
            return typedArray.getDrawable(R.styleable.SeekbarRange_sbr_left_thumb_image);
        } else {
            return getResources().getDrawable(R.drawable.thumb);
        }
    }

    protected Drawable getRightThumb(final TypedArray typedArray) {
        if (typedArray.getDrawable(R.styleable.SeekbarRange_sbr_right_thumb_image) != null) {
            return typedArray.getDrawable(R.styleable.SeekbarRange_sbr_right_thumb_image);
        } else {
            return getResources().getDrawable(R.drawable.thumb);
        }
    }

    protected int getMeasureSpecWith(int widthMeasureSpec) {
        int width = 200;
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        return width;
    }

    protected int getMeasureSpecHeight(int heightMeasureSpec) {
        int height = Math.round(thumbLeft.getHeight());
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
        }
        return height;
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getMeasureSpecWith(widthMeasureSpec), getMeasureSpecHeight(heightMeasureSpec));
    }
}