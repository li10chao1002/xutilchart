package com.example.xutilchart;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import com.example.xutilchart.model.User;
import com.example.xutilchart.util.PropertyDescriptor;
import com.example.xutilchart.util.PropertyDescriptorHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ChartActivity extends Activity {

    public static final String APP_DATA = "XUtils_MPAndroidChart";

    private LineChart chart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        chart = (LineChart) super.findViewById(R.id.chart);

        initUser();
        constructChart(User.class, "score");
    }

    private void constructChart(Class<?> clazz, String propertyName) {
        try {
            DbUtils db = DbUtils.create(this);
            List<User> userList = db.findAll(User.class);

            ArrayList<String> xVals = new ArrayList<String>();
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            Random random = new Random();
            int index = 0;
            for (User user : userList) {
                xVals.add(String.valueOf(index));
                PropertyDescriptor pd = PropertyDescriptorHelper.getPropertyDescriptor(propertyName, clazz);
                float value = Float.parseFloat(String.valueOf(pd.getReadMethod().invoke(user)));
                yVals.add(new Entry(value * random.nextInt(10), index++));
            }
            LineDataSet dataSet = new LineDataSet(yVals, propertyName);
            dataSet.setLineWidth(2l);
            dataSet.setHighLightColor(Color.BLUE);
            dataSet.setCircleColor(Color.rgb(255, 211, 140));
            dataSet.setCircleSize(2l);
            dataSet.setColor(Color.GREEN);

            ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
            dataSets.add(dataSet);
            LineData lineData = new LineData(xVals, dataSets);

            chart.setBackgroundColor(Color.WHITE);
            chart.setData(lineData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUser() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(APP_DATA, MODE_PRIVATE);
        boolean initUserData = sharedPreferences.getBoolean("InitUserData", false);
        System.out.println("InitUserData: " + initUserData);
        if (initUserData)
            return;
        else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("InitUserData", true);
            editor.commit();
        }

        List<User> userList = new ArrayList<User>();
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setGender(i % 2 == 0 ? true : false);
            user.setNickname("nickname" + i);
            user.setName("name" + i);
            user.setBirthday(new Date());
            user.setScore(i);
            userList.add(user);
        }

        DbUtils db = DbUtils.create(this);
        try {
            db.saveAll(userList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
