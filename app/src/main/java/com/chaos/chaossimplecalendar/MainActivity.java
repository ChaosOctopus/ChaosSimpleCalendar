package com.chaos.chaossimplecalendar;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements BackYear {
    @BindView(R.id.iv_years)
    ImageView ivYears;
    @BindView(R.id.btn_one)
    Button btnOne;
    @BindView(R.id.btn_two)
    Button btnTwo;
    @BindView(R.id.btn_three)
    Button btnThree;
    private int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    private GestureDetector gestureDetector = null;
    private CalendarAdapter calV = null;
    private NoScrollGridView gridView = null;
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";
    private int gvFlag = 0;
    @BindView(R.id.prevMonth)
    ImageView prevMonth;
    @BindView(R.id.currentMonth)
    TextView currentMonth;
    @BindView(R.id.nextMonth)
    ImageView nextMonth;
    @BindView(R.id.flipper)
    ViewFlipper flipper;
    private String type="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initCalendarView();
    }


    private void initCalendarView() {
        gestureDetector = new GestureDetector(this, new MyGestureListener());
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.removeAllViews();
        calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, this);
        addGridView();
        gridView.setAdapter(calV);
        flipper.addView(gridView, 0);
        addTextToTopTextView(currentMonth);
    }

    private void addTextToTopTextView(TextView currentMonth) {
        StringBuffer textDate = new StringBuffer();
        textDate.append(calV.getShowYear()).append("年").append(calV.getShowMonth()).append("月").append("\t");
        currentMonth.setText(textDate);
    }

    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        gridView = new NoScrollGridView(this);
        gridView.setNumColumns(7);
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setOnTouchListener(new View.OnTouchListener() {
            // 将gridview中的触摸事件回传给gestureDetector
            public boolean onTouch(View v, MotionEvent event) {
                return MainActivity.this.gestureDetector.onTouchEvent(event);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String scheduleDay = calV.getDateByClickItem(i).split("\\.")[0]; // 天
                String scheduleYear = calV.getShowYear(); //年
                String scheduleMonth = calV.getShowMonth(); //月
                Toast.makeText(MainActivity.this,scheduleYear+"年"+scheduleMonth+"月"+scheduleDay+"日",Toast.LENGTH_SHORT).show();
            }
        });
        gridView.setLayoutParams(params);
    }

    public MainActivity() {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date); // 当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);

    }

    @OnClick({R.id.prevMonth, R.id.nextMonth,R.id.btn_one,R.id.btn_two,R.id.btn_three})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prevMonth:
                enterPrevMonth(gvFlag);
                break;
            case R.id.nextMonth:
                enterNextMonth(gvFlag);
                break;
            case R.id.btn_one:
                type="1";
                calV.setType(type);
                calV.notifyDataSetChanged();
                break;
            case R.id.btn_two:
                type="2";
                calV.setType(type);
                calV.notifyDataSetChanged();
                break;
            case R.id.btn_three:
                type="0";
                calV.setType(type);
                calV.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void getYear(String year) {
        if (year.equals("鼠")) {
            ivYears.setImageResource(R.mipmap.rat);
        } else if (year.equals("牛")) {
            ivYears.setImageResource(R.mipmap.ox);
        } else if (year.equals("虎")) {
            ivYears.setImageResource(R.mipmap.tiger);
        } else if (year.equals("兔")) {
            ivYears.setImageResource(R.mipmap.rabbit);
        } else if (year.equals("龙")) {
            ivYears.setImageResource(R.mipmap.dragon);
        } else if (year.equals("蛇")) {
            ivYears.setImageResource(R.mipmap.snake);
        } else if (year.equals("马")) {
            ivYears.setImageResource(R.mipmap.horse);
        } else if (year.equals("羊")) {
            ivYears.setImageResource(R.mipmap.goat);
        } else if (year.equals("猴")) {
            ivYears.setImageResource(R.mipmap.monkey);
        } else if (year.equals("鸡")) {
            ivYears.setImageResource(R.mipmap.rooster);
        } else if (year.equals("狗")) {
            ivYears.setImageResource(R.mipmap.dog);
        } else {
            ivYears.setImageResource(R.mipmap.pig);
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
            if (e1.getX() - e2.getX() > 120) {
                // 像左滑动 上一个月
                enterNextMonth(gvFlag);
                return true;
            } else if (e1.getX() - e2.getX() < -120) {
                // 向右滑动 下一个月
                enterPrevMonth(gvFlag);
                return true;
            }
            return false;
        }
    }

    /**
     * 移动到下一个月
     *
     * @param gvFlag
     */
    private void enterNextMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月
        calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, this);
        gridView.setAdapter(calV);
        addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
        gvFlag++;

        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
        flipper.showNext();
        flipper.removeViewAt(0);
    }

    /**
     * 移动到上一个月
     *
     * @param gvFlag
     */
    private void enterPrevMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth--; // 上一个月
        calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, this);
        gridView.setAdapter(calV);
        gvFlag++;
        addTextToTopTextView(currentMonth); // 移动到上一月后，将当月显示在头标题中
        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
        flipper.showPrevious();
        flipper.removeViewAt(0);
    }
}
