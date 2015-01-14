package com.example.xutilchart;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import com.example.xutilchart.model.User;
import com.example.xutilchart.util.PropertyDescriptor;
import com.example.xutilchart.util.PropertyDescriptorHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ChartActivity extends Activity {

    public static final String APP_DATA = "XUtils_MPAndroidChart";

    private LineChart chart;
    private Typeface mTf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        chart = (LineChart) super.findViewById(R.id.chart);
        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf");

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

            dataSet.setLineWidth(1.75f);
            dataSet.setCircleSize(3f);
            dataSet.setColor(Color.WHITE);
            dataSet.setCircleColor(Color.WHITE);
            dataSet.setHighLightColor(Color.WHITE);

            ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
            dataSets.add(dataSet);
            LineData lineData = new LineData(xVals, dataSets);

            setupChart(chart, lineData, mColors[random.nextInt(4)]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int[] mColors = new int[]{
            Color.rgb(137, 230, 81),
            Color.rgb(240, 240, 30),
            Color.rgb(89, 199, 250),
            Color.rgb(250, 104, 104)
    };

    private void setupChart(LineChart chart, LineData data, int color) {

        // if enabled, the chart will always start at zero on the y-axis
        chart.setStartAtZero(true);

        // disable the drawing of values into the chart
        chart.setDrawYValues(false);

        chart.setDrawBorder(false);

        // no description text
        chart.setDescription("");
        chart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable / disable grid lines
        chart.setDrawVerticalGrid(false);
        // mChart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background
        chart.setDrawGridBackground(false);
        chart.setGridColor(Color.WHITE & 0x70FFFFFF);
        chart.setGridWidth(1.25f);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setBackgroundColor(color);

        chart.setValueTypeface(mTf);

        // add data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setFormSize(6f);
        l.setTextColor(Color.WHITE);
        l.setTypeface(mTf);

        YLabels y = chart.getYLabels();
        y.setTextColor(Color.WHITE);
        y.setTypeface(mTf);
        y.setLabelCount(4);

        XLabels x = chart.getXLabels();
        x.setTextColor(Color.WHITE);
        x.setTypeface(mTf);

        // animate calls invalidate()...
        chart.animateX(2500);
    }

    private void setchartStyle() {
        chart.setBackgroundColor(Color.rgb(110, 222, 13));
        chart.setGridColor(Color.rgb(219, 235, 1));
        chart.setGridWidth(1.25f);
        chart.setDrawBorder(false);
        chart.setDescription("");
        chart.setValueTextColor(Color.WHITE);
        chart.setValueTextSize(12f);
        chart.setDrawVerticalGrid(false);
        chart.setDrawGridBackground(false);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setTextColor(Color.WHITE);
        l.setFormSize(12f);
        YLabels y = chart.getYLabels();
        y.setTextColor(Color.WHITE);
        y.setTextSize(12f);
        XLabels x = chart.getXLabels();
        x.setTextColor(Color.WHITE);
        x.setTextSize(12f);
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
            user.setScore(Math.round(20));
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
