package com.example.alucard.p1_acceleration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener {

    Button btnChoiceTraining, btnShowStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChoiceTraining = (Button) findViewById(R.id.btnChoiceTrainig);
        btnChoiceTraining.setOnClickListener(this);

        btnShowStatistics = (Button) findViewById(R.id.btnShowStatistics);
        btnShowStatistics.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnChoiceTrainig:{
                Intent activity = new Intent(this, ActivityChoiceTraining.class);
                startActivity(activity);
                break;
            }
            case R.id.btnShowStatistics: {
                Intent activity = new Intent(this, ActivityShowStatistics.class);
                startActivity(activity);
                break;
            }
            default:
                break;
        }
    }
}
