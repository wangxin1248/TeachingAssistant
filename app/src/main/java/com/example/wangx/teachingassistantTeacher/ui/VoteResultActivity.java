package com.example.wangx.teachingassistantTeacher.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Select;
import com.example.wangx.teachingassistantTeacher.bean.Student;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.bean.Vote;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
/**
 * 投票结果展示
 */
public class VoteResultActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    private PieChart pieChart;
    private Vote vote;
    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    private String[] mParties;
    private String[] vote_selects;// 选项内容
    private HashMap<String,Integer> results;
    private double[] mSelects;
    private String name;
    private LinearLayout layout_view;

    private TextView tv_A;
    private TextView tv_B;
    private TextView tv_C;
    private TextView tv_D;
    private TextView tv_E;
    private TextView tv_F;
    private TextView tv_G;
    private TextView tv_H;
    ContextUtil app;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_result);
        // 查看当前登陆用户
        app = (ContextUtil) getApplication();
        user = app.getUser();
        layout_view = (LinearLayout) findViewById(R.id.layout_view);
        tv_A = (TextView) findViewById(R.id.tv_A);
        tv_B = (TextView) findViewById(R.id.tv_B);
        tv_C = (TextView) findViewById(R.id.tv_C);
        tv_D = (TextView) findViewById(R.id.tv_D);
        tv_E = (TextView) findViewById(R.id.tv_E);
        tv_F = (TextView) findViewById(R.id.tv_F);
        tv_G = (TextView) findViewById(R.id.tv_G);
        tv_H = (TextView) findViewById(R.id.tv_H);

        vote = (Vote) getIntent().getSerializableExtra("vote");
        name = vote.getName();
        vote_selects = vote.getSelects().split("&&-&&");
        pieChart = (PieChart) findViewById(R.id.pieChart);
        // 图形数据处理
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setCenterTextTypeface(mTfLight);
        // 设置投票题目
        pieChart.setCenterText(name);

        pieChart.setDrawHoleEnabled(true);
        // 饼状图中间表盘颜色
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        // add a selection listener
        pieChart.setOnChartValueSelectedListener(this);

        // 设置选项
        BmobQuery<Select> query = new BmobQuery<>();
        query.addWhereEqualTo("id",vote.getObjectId());
        query.findObjects(VoteResultActivity.this, new FindListener<Select>() {
            @Override
            public void onSuccess(List<Select> list) {
                // 使用hashMap来存对应选项的选择人数
                results = new HashMap<String, Integer>();
                // 记录所有的结果字符串
                final String[] str = {""};
                String ss = "";
                final String[] strA = {"A: "};
                final String[] strB = {"B: "};
                final String[] strC = {"C: "};
                final String[] strD = {"D: "};
                final String[] strE = {"E: "};
                final String[] strF = {"F: "};
                final String[] strG = {"G: "};
                final String[] strH = {"H: "};
                // 添加所以的选项到字符串中
                for (Select select:list){
                    ss = select.getSelect();
                    if (1==user.type){
                        for (int i=0;i<ss.length();i++){
                            String s = ss.substring(i,i+1);
                            switch (s){
                                case "A":
                                    BmobQuery<Student> query1 = new BmobQuery<Student>();
                                    query1.addWhereEqualTo("studentId",select.getUserId());
                                    query1.findObjects(VoteResultActivity.this, new FindListener<Student>() {
                                        @Override
                                        public void onSuccess(List<Student> list) {
                                            if (list.size()>0){
                                                strA[0] += list.get(0).getName()+" ";
                                                tv_A.setText(strA[0]);
                                            }
                                        }

                                        @Override
                                        public void onError(int i, String s) {
                                            Toast.makeText(VoteResultActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                case "B":
                                    BmobQuery<Student> query2 = new BmobQuery<Student>();
                                    query2.addWhereEqualTo("studentId",select.getUserId());
                                    query2.findObjects(VoteResultActivity.this, new FindListener<Student>() {
                                        @Override
                                        public void onSuccess(List<Student> list) {
                                            if (list.size()>0){
                                                strB[0] += list.get(0).getName()+" ";
                                                tv_B.setText(strB[0]);
                                            }
                                        }

                                        @Override
                                        public void onError(int i, String s) {
                                            Toast.makeText(VoteResultActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                case "C":
                                    BmobQuery<Student> query3 = new BmobQuery<Student>();
                                    query3.addWhereEqualTo("studentId",select.getUserId());
                                    query3.findObjects(VoteResultActivity.this, new FindListener<Student>() {
                                        @Override
                                        public void onSuccess(List<Student> list) {
                                            if (list.size()>0){
                                                strC[0] += list.get(0).getName()+" ";
                                                tv_C.setText(strC[0]);
                                            }
                                        }

                                        @Override
                                        public void onError(int i, String s) {
                                            Toast.makeText(VoteResultActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                case "D":
                                    BmobQuery<Student> query4 = new BmobQuery<Student>();
                                    query4.addWhereEqualTo("studentId",select.getUserId());
                                    query4.findObjects(VoteResultActivity.this, new FindListener<Student>() {
                                        @Override
                                        public void onSuccess(List<Student> list) {
                                            if (list.size()>0){
                                                strD[0] += list.get(0).getName()+" ";
                                                tv_D.setText(strD[0]);
                                            }
                                        }

                                        @Override
                                        public void onError(int i, String s) {
                                            Toast.makeText(VoteResultActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                case "E":
                                    BmobQuery<Student> query5 = new BmobQuery<Student>();
                                    query5.addWhereEqualTo("studentId",select.getUserId());
                                    query5.findObjects(VoteResultActivity.this, new FindListener<Student>() {
                                        @Override
                                        public void onSuccess(List<Student> list) {
                                            if (list.size()>0){
                                                strE[0] += list.get(0).getName()+" ";
                                                tv_E.setText(strE[0]);
                                            }
                                        }

                                        @Override
                                        public void onError(int i, String s) {
                                            Toast.makeText(VoteResultActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                case "F":
                                    BmobQuery<Student> query6 = new BmobQuery<Student>();
                                    query6.addWhereEqualTo("studentId",select.getUserId());
                                    query6.findObjects(VoteResultActivity.this, new FindListener<Student>() {
                                        @Override
                                        public void onSuccess(List<Student> list) {
                                            if (list.size()>0){
                                                strF[0] += list.get(0).getName()+" ";
                                                tv_F.setText(strF[0]);
                                            }
                                        }

                                        @Override
                                        public void onError(int i, String s) {
                                            Toast.makeText(VoteResultActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                case "G":
                                    BmobQuery<Student> query7 = new BmobQuery<Student>();
                                    query7.addWhereEqualTo("studentId",select.getUserId());
                                    query7.findObjects(VoteResultActivity.this, new FindListener<Student>() {
                                        @Override
                                        public void onSuccess(List<Student> list) {
                                            if (list.size()>0){
                                                strG[0] += list.get(0).getName()+" ";
                                                tv_G.setText(strG[0]);
                                            }
                                        }

                                        @Override
                                        public void onError(int i, String s) {
                                            Toast.makeText(VoteResultActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                case "H":
                                    BmobQuery<Student> query8 = new BmobQuery<Student>();
                                    query8.addWhereEqualTo("studentId",select.getUserId());
                                    query8.findObjects(VoteResultActivity.this, new FindListener<Student>() {
                                        @Override
                                        public void onSuccess(List<Student> list) {
                                            if (list.size()>0){
                                                strH[0] += list.get(0).getName()+" ";
                                                tv_H.setText(strH[0]);
                                            }
                                        }

                                        @Override
                                        public void onError(int i, String s) {
                                            Toast.makeText(VoteResultActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                            }
                        }
                    }
                    str[0] +=select.getSelect();
                }
                // 将选项以及对应的选择次数添加到map当中
                for (int i = 0; i< str[0].length(); i++){
                    int num = 0;
                    String s = str[0].substring(i,i+1);
                    if (results.containsKey(s)){
                        num = results.get(s);
                    }
                    results.put(s,++num);
                }
                /**
                 * 将选项出现的频率添加到数组中
                 */
                // 所有人选的选项数量
                double length = str[0].length();
                mParties = new String[results.size()];
                // 记录选择出现的频率
                mSelects = new double[mParties.length];
                int i=0;
                // 遍历HashMap
                for (Map.Entry<String ,Integer> entry:results.entrySet()){
                    mParties[i] = entry.getKey();
                    mSelects[i] = entry.getValue()/length;
                    i++;
                }
                // 保存选项的内容
                for (int j = 0;j<mParties.length;j++){
                    String s = mParties[j];
                    switch (s){
                        case "A":
                            mParties[j] = "A."+vote_selects[0];
                            break;
                        case "B":
                            mParties[j] = "B."+vote_selects[1];
                            break;
                        case "C":
                            mParties[j] = "C."+vote_selects[2];
                            break;
                        case "D":
                            mParties[j] = "D."+vote_selects[3];
                            break;
                        case "E":
                            mParties[j] = "E."+vote_selects[4];
                            break;
                        case "F":
                            mParties[j] = "F."+vote_selects[5];
                            break;
                        case "G":
                            mParties[j] = "G."+vote_selects[6];
                            break;
                        case "H":
                            mParties[j] = "H."+vote_selects[7];
                            break;
                        default:
                            break;
                    }
                }
                setData(mParties.length, 100,mSelects);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(VoteResultActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
            }
        });

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        // 选项颜色和字体大小
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTypeface(mTfRegular);
        pieChart.setEntryLabelTextSize(14f);
    }

    /**
     * 数据设置
     * @param count
     * @param range
     * @param mSelects
     */
    private void setData(int count, float range,double[] mSelects) {
        float mult = range;
        // 选项集合
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // 设置选项的值
        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry(((float) ((mSelects[i]* mult) + mult / 5)),
                    mParties[i],
                    getResources().getDrawable(R.drawable.star)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        // 选择颜色和大小
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(mTfLight);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }

    // 选项点击
    @Override
    public void onValueSelected(Entry e, Highlight h) {
//        if (e == null)
//            return;
//        Log.i("VAL SELECTED",
//                "Value: " + e.getY() + ", index: " + h.getX()
//                        + ", DataSet index: " + h.getDataSetIndex());
//        Toast.makeText(VoteResultActivity.this,"点击了"+"Value: " + e.getY() + ", index: " + h.getX()+ ", DataSet index: " + h.getDataSetIndex(),Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onNothingSelected() {

    }
}
