package ru.ralnik.demo;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.ralnik.myseekbarrange.SeekbarRangeAdvance;

public class MainActivity extends AppCompatActivity {
    SeekbarRangeAdvance seekbar;
    Button buttonClear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekbar = (SeekbarRangeAdvance) findViewById(R.id.seekbarRange);
        buttonClear = (Button) findViewById(R.id.buttonClear);
        seekbar.setAbsoluteMinValue(0D);
        seekbar.setAbsoluteMaxValue(115.2D);

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekbar.setDefaultValue();
            }
        });
    }
}
