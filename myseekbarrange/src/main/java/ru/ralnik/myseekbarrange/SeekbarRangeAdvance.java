package ru.ralnik.myseekbarrange;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ru.ralnik.myseekbarrange.enums.Formats;
import ru.ralnik.myseekbarrange.interfaces.OnSeekbarRangeChangeListener;

public class SeekbarRangeAdvance extends LinearLayout {
    private EditText editMin;
    private EditText editMax;
    private RelativeLayout layoutForEdits;
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
    private int sbra_heightEdits;
    private int sbra_widthEdits;
    private boolean sbra_fontBoldEdits;
    private OnSeekbarRangeChangeListener scl;


    public SeekbarRangeAdvance(Context context) {
        super(context);
        init(context);
    }

    public SeekbarRangeAdvance(Context context, AttributeSet attrs) throws Exception {
        super(context, attrs);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SeekbarRangeAdvance);
        try {
            this.sbra_textSizeOfEdits = typedArray.getInt(R.styleable.SeekbarRangeAdvance_sbra_textSizeOfEdits, 8);
            this.sbra_textColorOfEdits = typedArray.getColor(R.styleable.SeekbarRangeAdvance_sbra_textColorOfEdits, Color.BLACK);
            if (typedArray.getFloat(R.styleable.SeekbarRangeAdvance_sbra_absoluteMinValue, 1F) != 0) {
                this.sbra_absoluteMinValue = typedArray.getFloat(R.styleable.SeekbarRangeAdvance_sbra_absoluteMinValue, 1F);
            } else {
                throw new Exception("Минимальный показатель не должен быть равен '0'");
            }
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
            this.sbra_widthEdits = typedArray.getInt(R.styleable.SeekbarRangeAdvance_sbra_widthEdits, LayoutParams.WRAP_CONTENT);
            this.sbra_heightEdits = typedArray.getInt(R.styleable.SeekbarRangeAdvance_sbra_heightEdits, LayoutParams.WRAP_CONTENT);
            this.sbra_fontBoldEdits = typedArray.getBoolean(R.styleable.SeekbarRangeAdvance_sbra_fontBoldEdits, false);
        } finally {
            typedArray.recycle();
        }

        init(context);
    }

    private void init(Context context) {

        root = mInflater.from(context).inflate(R.layout.activity_seekbarrangeadvance, this, false);

        setOrientation(VERTICAL);
        seekbar = (SeekbarRange) root.findViewById(R.id.seekbar);

        layoutForEdits = (RelativeLayout) root.findViewById(R.id.layoutForEdits);

        editMin = (EditText) root.findViewById(R.id.editMin);
        editMax = (EditText) root.findViewById(R.id.editMax);

        setupEdit(editMin);
        setupEdit(editMax);
        setupSeekBar(seekbar);

        //отображение едитов
        setShowEdits(sbra_show_edits);



        //thumb
        seekbar.setThumbLeftBitmap(sbra_left_thumb_image);
        seekbar.setThumbRightBitmap(sbra_right_thumb_image);

        //thumb default position
        seekbar.setAbsoluteMinValue(sbra_absoluteMinValue);
        seekbar.setAbsoluteMaxValue(sbra_absoluteMaxValue);
        setDefaultValue();
        editMin.setText(String.valueOf(getAbsoluteMinValue()));
        editMax.setText(String.valueOf(getAbsoluteMaxValue()));

        seekbar.setSeekBarChangeListener(new OnSeekbarRangeChangeListener() {
            @Override
            public void SeekbarRangeValueChanged(Double Thumb1Value, Double Thumb2Value) {

                String resultMin = null;
                if (Thumb1Value < sbra_absoluteMinValue) {
                    resultMin = String.valueOf(formatValue(Double.valueOf(Float.toString(sbra_absoluteMinValue))));
                } else resultMin = String.valueOf(formatValue(Thumb1Value)) ;
                editMin.setText(resultMin);
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

    private void setupEdit(EditText edit){
        if (sbra_fontBoldEdits){
            edit.setTypeface(Typeface.DEFAULT_BOLD);
        }
        if(sbra_heightEdits > 0){
            edit.getLayoutParams().height = sbra_heightEdits;
        }
        if(sbra_widthEdits > 0){
            edit.getLayoutParams().width = sbra_widthEdits;
        }
        edit.setTextSize(sbra_textSizeOfEdits);
        edit.setTextColor(sbra_textColorOfEdits);

        edit.setBackground(sbra_bgEditText);

        edit.setEms(sbra_edits_ems);

        edit.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
    }

    private void setupSeekBar(SeekbarRange seekbarRange){
        seekbarRange.setBackground(sbra_bgSeekbarRange);
        seekbarRange.setBarHighlightColor(sbra_barHighlightColor);
        seekbarRange.setBarColor(sbra_barColor);
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
            sbra_absoluteMinValue = min.floatValue();
            seekbar.setAbsoluteMinValue(sbra_absoluteMinValue);
            editMin.setText(String.valueOf(formatValue(Double.parseDouble(sbra_absoluteMinValue+""))));
        }

        public void setAbsoluteMaxValue(Number max) {
        sbra_absoluteMaxValue = max.floatValue();
            seekbar.setAbsoluteMaxValue(sbra_absoluteMaxValue);
            editMax.setText(String.valueOf(max));
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
            editMin.setText(String.valueOf(formatValue(Double.parseDouble(seekbar.getAbsoluteMinValue()+""))));
            setMaxValue(seekbar.getAbsoluteMaxValue());
            editMax.setText(String.valueOf(seekbar.getAbsoluteMaxValue()));
        }

    public void setSeekBarChangeListener(OnSeekbarRangeChangeListener scl){
        this.scl = scl;
    }
}