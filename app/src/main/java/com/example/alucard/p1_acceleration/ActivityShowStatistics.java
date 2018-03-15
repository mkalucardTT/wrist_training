package com.example.alucard.p1_acceleration;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityShowStatistics extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_statistics);

        ListView listView = (ListView)findViewById(R.id.listView);

        SharedPreferences keyValues = this.getSharedPreferences("statistics", this.MODE_PRIVATE);

        int CountRecords = keyValues.getInt("CountRecords", 0);
        CountRecords = keyValues.getInt("CountRecords", 0);

        ArrayList<UserStatistic> list = new ArrayList<UserStatistic>();

        for (int i = CountRecords; i > 0; i--){
            list.add(new UserStatistic(
                    keyValues.getString("start"+i, ""),
                    keyValues.getString("finish"+i, ""),
                    keyValues.getString("count"+i, ""),
                    keyValues.getString("time"+i, ""),
                    keyValues.getString("comment"+i, "")));
        }

        ListAdapter adapter = new SimpleAdapter(this, list, R.layout.record_list, new String[] {
                UserStatistic.START, UserStatistic.FINISH, UserStatistic.COUNT, UserStatistic.TIME, UserStatistic.COMMENT },
                new int[] { R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5});

        listView.addHeaderView(View.inflate(this, R.layout.header_record_list, null));
        listView.setAdapter(adapter);
    }

    public class UserStatistic extends HashMap<String, String>{
        public static final String START = "string";
        public static final String FINISH = "finish";
        public static final String COUNT = "count";
        public static final String TIME = "time";
        public static final String COMMENT = "comment";

        public UserStatistic(String start, String finish, String count, String time, String comment){
            super();
            super.put(START, start);
            super.put(FINISH, finish);
            super.put(COUNT, count);
            super.put(TIME, time);
            super.put(COMMENT, comment);

        }
    }
}
