package com.example.alucard.p1_acceleration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityChoiceTraining extends Activity implements View.OnClickListener, SensorEventListener {

    RadioButton rbTime, rbCount;
    EditText etCount, etComment;
    TextView etShowCount, etShowTime, tvText;
    Button btnTraining;

    LinearLayout container;
    SensorManager sensorManager;
    Sensor sensor;
    Vibrator vibrator;

    int count = -1, endcount = 0;
    int previousValue = 0;
    int time = 0, index = 1;

    Timer timer;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss");
    Date startDateandTime, finishDateAndTime;
    String comment = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_training);

        container = (LinearLayout)findViewById(R.id.container);
        btnTraining = (Button)findViewById(R.id.btnTraining);
        btnTraining.setOnClickListener(this);

        etCount = (EditText)findViewById(R.id.end_count);
        etShowCount = (TextView)findViewById(R.id.etShowCount);
        etShowTime = (TextView)findViewById(R.id.etShowTime);
        tvText = (TextView)findViewById(R.id.tvText);

        rbTime = (RadioButton)findViewById(R.id.rBtime);
        rbCount = (RadioButton)findViewById(R.id.rBcount);

        rbTime.setOnClickListener(this);
        rbCount.setOnClickListener(this);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rBcount:
            {
                rbTime.setChecked(false);
                etCount.setEnabled(true);
                index = 1;
                time = 0;
                break;
            }
            case R.id.rBtime:
            {
                rbCount.setChecked(false);
                etCount.setEnabled(false);
                index = -1;
                time = 60;
                break;
            }
            case R.id.btnTraining:
            {
                onClickTraining(view);
                break;
            }
            default:
                break;

        }
    }

    public void setEnableElements(boolean flag) {
        //как сделать весть layout недоступным
        rbCount.setEnabled(flag);//&& rbTime.isChecked());
        etCount.setEnabled(flag && rbCount.isChecked());
        rbTime.setEnabled(flag);// && rbTime.isChecked());
    }

    public  void onClickTraining(View view)
    {
        if (btnTraining.getText()== this.getString(R.string.start_training)){
            count = -1;
            onStartTraining();
        }
        else {


            onFinishTraining();
        }
    }

    public void onStartTraining()
    {
        endcount = Integer.valueOf(String.valueOf(etCount.getText()));

        etShowCount.setText("0");
        startDateandTime = new Date();
        setEnableElements(false);

        time = index == 1 ? 0: 60;

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        btnTraining.setText(R.string.finish_training);

        //падает с ошибкой, если повторно запускать таймер, временное решение
        timer = new Timer();
        timer.scheduleAtFixedRate( new TimeTraining(), 1000, 1000);

    }

    public void onFinishTraining()
    {
        finishDateAndTime = new Date();

        LayoutInflater inflater = LayoutInflater.from(this);
        View promptsView = inflater.inflate(R.layout.prompt, null);

        etComment = (EditText)promptsView.findViewById(R.id.etComment);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                comment = String.valueOf(etComment.getText());
                SaveStatistics();
            }
        });

        alertDialogBuilder.setNegativeButton("No comment", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                comment = "";
                SaveStatistics();
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        setEnableElements(true);

        btnTraining.setText(R.string.start_training);

        //падает с ошибкой, если повторно запускать таймер, временное решение
        if(timer!=null) {
            timer.cancel();
            timer = null;
        }
        sensorManager.unregisterListener(this);

    }

    public void SaveStatistics()
    {

        SharedPreferences keyValues = this.getSharedPreferences("statistics", this.MODE_PRIVATE);

        int CountRecords = keyValues.getInt("CountRecords", 0);
        ++CountRecords;

        SharedPreferences.Editor keyValuesEditor = keyValues.edit();

        keyValuesEditor.putString("start"+CountRecords, sdf.format(startDateandTime));
        keyValuesEditor.putString("finish"+CountRecords, sdf.format(finishDateAndTime));
        keyValuesEditor.putString("count"+CountRecords, String.valueOf(count));
        keyValuesEditor.putString("time"+CountRecords, String.valueOf(time));
        keyValuesEditor.putString("comment"+CountRecords, comment);
        keyValuesEditor.putInt("CountRecords", CountRecords);

        keyValuesEditor.apply();

        count = -1;
    }

    class TimeTraining extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    time = time +index;
                    tvText.setText(index == 1 ? " продолжительность тренировки в сек.": " до окончания тренеровки в сек.");
                    etShowTime.setText(String.valueOf(time));

                    if (index == -1 && time == 0){
                        onFinishTraining();
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float[] g = new float[3];
        g = sensorEvent.values.clone();
        double norm_Of_g = Math.sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2]);
//        // Normalize the accelerometer vector g[0] = g[0] / norm_Of_g g[1] = g[1] / norm_Of_g g[2] = g[2] / norm_Of_g
        int inclination = (int) Math.round(Math.toDegrees(Math.acos(g[2])));

        int rotation = (int) Math.round(Math.toDegrees(Math.atan2(g[0], g[1])));
        //t1.setText(String.valueOf(rotation));


        if (inclination < 25 || inclination > 155) {
            // bad date
        } else {
            if ((count == -1 && (rotation >= 90 || rotation <= -90)) || (previousValue >= 90 && rotation <= -90) || (previousValue <= -90 && rotation >= 90)){
                previousValue = rotation;
                ++count;
                etShowCount.setText(String.valueOf(count));

                vibrator.vibrate(100L);
            }

        }

        if (index == 1 && count == (endcount+1)){
            onFinishTraining();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
