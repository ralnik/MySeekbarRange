package ru.ralnik.myseekbarrange;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import ru.ralnik.myseekbarrange.enums.Formats;
import ru.ralnik.myseekbarrange.interfaces.OnSeekbarRangeChangeListener;

public class SeekbarRangeAdvance extends LinearLayout {
    private EditText editMin;
    private EditText editMax;
    private LinearLayout layoutForEdits;
    private ru.ralnik.myseekbarrange.SeekbarRange seekbar;
    private LayoutInflater mInflater = null;
    private View root;

    //   ******* ATTRIBUTES ***************
    private float sbra_absoluteMinValue, sbra_absoluteMaxValue;
    private float sbra_textSizeOfEdits;
    private int sbra_textColorOfEdits;
    private Drawable sbra_bgSeekbarRange;
    private Drawable sbra_bgEditText;
    private int sbra_barColor;
    private int sbra_barHighlightColor;
    private Drawable sbra_left_thumb_image;
    private Drawable sbra_right_thumb_image;
    private int sbra_edits_ems;
    private boolean sbra_show_edits;
    private int sbra_dataType;
    private OnSeekbarRangeChangeListener scl;


    public SeekbarRangeAdvance(Context context) {
        super(context);
        init(context);
    }

    public SeekbarRangeAdvance(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SeekbarRangeAdvance);
        try {
            this.sbra_textSizeOfEdits = typedArray.getInt(R.styleable.SeekbarRangeAdvance_sbra_textSizeOfEdits, 8);
            this.sbra_textColorOfEdits = typedArray.getColor(R.styleable.SeekbarRangeAdvance_sbra_textColorOfEdits, Color.BLACK);
            this.sbra_absoluteMinValue = typedArray.getFloat(R.styleable.SeekbarRangeAdvance_sbra_absoluteMinValue, 0F);
            this.sbra_absoluteMaxValue = typedArray.getFloat(R.styleable.SeekbarRangeAdvance_sbra_absoluteMaxValue, 100F);
            this.sbra_bgEditText = typedArray.getDrawable(R.styleable.SeekbarRangeAdvance_sbra_bgEdits);
            this.sbra_bgSeekbarRange = typedArray.getDrawable(R.styleable.SeekbarRangeAdvance_sbra_bgSeekbarRange);
            this.sbra_barColor = typedArray.getColor(R.styleable.SeekbarRangeAdvance_sbra_barColor, Color.GRAY);
            this.sbra_barHighlightColor = typedArray.getColor(R.styleable.SeekbarRangeAdvance_sbra_barHighlightColor,Color.BLACK);
            this.sbra_left_thumb_image = typedArray.getDrawable(R.styleable.SeekbarRangeAdvance_sbra_left_thumb_image);
            this.sbra_right_thumb_image = typedArray.getDrawable(R.styleable.SeekbarRangeAdvance_sbra_right_thumb_image);
            this.sbra_edits_ems = typedArray.getInt(R.styleable.SeekbarRangeAdvance_sbra_edits_ems, 4);
            this.sbra_show_edits = typedArray.getBoolean(R.styleable.SeekbarRangeAdvance_sbra_show_edits, true);
            this.sbra_dataType = typedArray.getInt(R.styleable.SeekbarRangeAdvance_sbra_data_type, 0);
        } finally {
            typedArray.recycle();
        }

        init(context);
    }

    private void init(Context context) {

        root = mInflater.from(context).inflate(R.layout.activity_seekbarrangeadvance, this, false);

        setOrientation(VERTICAL);
        seekbar = (SeekbarRange) root.findViewById(R.id.seekbar);

        layoutForEdits = (LinearLayout) root.findViewById(R.id.layoutForEdits);

        editMin = (EditText) root.findViewById(R.id.editMin);
        editMax = (EditText) root.findViewById(R.id.editMax);


        //Устанавливаем размер шрифта
        editMin.setTextSize(sbra_textSizeOfEdits);
        editMax.setTextSize(sbra_textSizeOfEdits);

        //Устанавливаем цвет шрифта
        editMin.setTextColor(sbra_textColorOfEdits);
        editMax.setTextColor(sbra_textColorOfEdits);

        //задний фон
        editMin.setBackground(sbra_bgEditText);
        editMax.setBackground(sbra_bgEditText);

        editMin.setEms(sbra_edits_ems);
        editMax.setEms(sbra_edits_ems);


        editMin.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        editMax.setInputType(EditorInfo.TYPE_CLASS_NUMBER);

        //отображение едитов
        setShowEdits(sbra_show_edits);

        //Задний фон
        seekbar.setBackground(sbra_bgSeekbarRange);

        //Цвет выделенного
        seekbar.setBarHighlightColor(sbra_barHighlightColor);

        //thumb
        seekbar.setThumbLeftBitmap(sbra_left_thumb_image);
        seekbar.setThumbRightBitmap(sbra_right_thumb_image);

        //thumb default position
        seekbar.setAbsoluteMinValue(sbra_absoluteMinValue);
        seekbar.setAbsoluteMaxValue(sbra_absoluteMaxValue);
        setDefaultValue();
        editMin.setText(getAbsoluteMinValue().toString());
        editMax.setText(getAbsoluteMaxValue().toString());

        seekbar.setSeekBarChangeListener(new OnSeekbarRangeChangeListener() {
            @Override
            public void SeekbarRangeValueChanged(Double Thumb1Value, Double Thumb2Value) {

                editMin.setText(formatValue(Thumb1Value).toString());
                editMax.setText(formatValue(Thumb2Value).toString());

                if(scl !=null){
                    scl.SeekbarRangeValueChanged(Thumb1Value, Thumb2Value );
                }

            }
        });

        editMin.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    Double valueMin = (Double) Double.parseDouble(editMin.getText().toString());
                    seekbar.setMin(valueMin.floatValue());
                }
                return true ;
            }
        });


        editMax.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //проверка нажатие enter
                if (keyCode == 66) {
                    Double valueMax = (Double) Double.parseDouble(editMax.getText().toString());
                    seekbar.setMax(valueMax.floatValue());
                }
                return true;
            }
        });
        addView(root);
    }


    private Number formatValue(Number value){
        final Double v = (Double) value;

        if(sbra_dataType == Formats.INTEGER){
            return Math.round(v);
        }
        if(sbra_dataType == Formats.FLOAT){
            return v.floatValue();
        }
        if(sbra_dataType == Formats.DOUBLE){
            return v;
        }
        return Math.round(v);
    }

        public Number getMinValue() {
            return formatValue(seekbar.getMin());
        }

        public Number getMaxValue() {
            return formatValue(seekbar.getMax());
        }

        public Number getAbsoluteMinValue() {

            return formatValue(Double.parseDouble(seekbar.getAbsoluteMinValue() + ""));
        }

        public Number getAbsoluteMaxValue() {
            return formatValue(Double.parseDouble(seekbar.getAbsoluteMaxValue() + ""));
        }

        public void setAbsoluteMinValue(Number min) {
            seekbar.setAbsoluteMinValue(min.floatValue());
        }

        public void setAbsoluteMaxValue(Number max) {
            seekbar.setAbsoluteMaxValue(max.floatValue());
        }

        public void setMinValue(Number min) {
            seekbar.setMin(min.floatValue());
        }

        public void setMaxValue(Number max) {
           seekbar.setMax(max.floatValue());
        }

        public void setShowEdits(Boolean flag) {
            if (flag == true) {
                layoutForEdits.setVisibility(VISIBLE);
            } else {
                layoutForEdits.setVisibility(GONE);
            }
        }

        public void setDefaultValue(){
            setMinValue(seekbar.getAbsoluteMinValue());
            setMaxValue(seekbar.getAbsoluteMaxValue());
        }

    public void setSeekBarChangeListener(OnSeekbarRangeChangeListener scl){
        this.scl = scl;
    }
}