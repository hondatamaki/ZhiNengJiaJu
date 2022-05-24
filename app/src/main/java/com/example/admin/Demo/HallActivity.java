package com.example.admin.Demo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bizideal.smarthome.socket.ConstantUtil;
import com.bizideal.smarthome.socket.ControlUtils;
import com.bizideal.smarthome.socket.DataCallback;
import com.bizideal.smarthome.socket.DeviceBean;
import com.bizideal.smarthome.socket.SocketClient;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HallActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
//    public static String tag="HallActivity";
    public static String[] sensorString=new String[]{"0","0","0","0","0","0","0","0","0","0"};
    public static String sd;
    Timer timer;
    TimerTask task;
    TextView pm25,ranqi,rentihongwai,yanwu,guangzhaodu,qiya,name,time,chaxun;
    ToggleButton dianshi,dvd,shedeng,baojingdeng,shushimoshi,on,off,stop;
    Button backup;
    int b,c;
    boolean isCha=false;
    private String tag="HallActivity ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);
        pm25=findViewById(R.id.hall_pm25);
        ranqi=findViewById(R.id.hall_ranqi);
        rentihongwai=findViewById(R.id.hall_renti);
        yanwu=findViewById(R.id.hall_yanwu);
        name=findViewById(R.id.tv_name);
        time=findViewById(R.id.tv_time);
        guangzhaodu=findViewById(R.id.hall_guangzhaodu);
        qiya=findViewById(R.id.hall_qiya);
        dianshi=findViewById(R.id.hall_dianshi);
        dvd=findViewById(R.id.hall_dvd);
        shedeng=findViewById(R.id.hall_shedeng);
        baojingdeng=findViewById(R.id.hall_baojingdeng);
        shushimoshi=findViewById(R.id.shushimoshi);
        on=findViewById(R.id.curtainon);
        off=findViewById(R.id.curtainoff);
        stop=findViewById(R.id.curtainstop);
        backup=findViewById(R.id.backup);
        chaxun=findViewById(R.id.btn_chaxun);
        /*OnClickListener*/
        backup.setOnClickListener(this);
        shushimoshi.setOnCheckedChangeListener(this);
        on.setOnClickListener(this);
        off.setOnClickListener(this);
        stop.setOnClickListener(this);
        chaxun.setOnClickListener(this);
        /*OnCheckedChangeListener*/
        dianshi.setOnCheckedChangeListener(this);
        dvd.setOnCheckedChangeListener(this);
        shedeng.setOnCheckedChangeListener(this);
        baojingdeng.setOnCheckedChangeListener(this);
        ControlUtils.getData();
        SocketClient.getInstance().getData(new DataCallback<DeviceBean>() {

            @Override
            public void onResult(final DeviceBean deviceBean) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                                if (isCha && !TextUtils.isEmpty(deviceBean.getName()))
                                    name.setText(deviceBean.getName());
                                if (isCha && !TextUtils.isEmpty(deviceBean.getTime()))
                                    time.setText(deviceBean.getTime());
                            ArrayList<DeviceBean.Devices>list=deviceBean.getDevice();
                            if (list.size()>0){
                                for (int i=0;i<list.size();i++){
                                    String zhi=list.get(i).getValue();
                                    if (list.get(i).getBoardId().equals("5")&&list.get(i).getSensorType().equals(ConstantUtil.PM25)){
                                        sensorString[0]=zhi;
                                    }//PM25（1）
                                    if (list.get(i).getBoardId().equals("20")&&list.get(i).getSensorType().equals(ConstantUtil.PM25)){
                                        sensorString[1]=zhi;
                                    }//PM25(2)
                                    if (list.get(i).getBoardId().equals("4")&&list.get(i).getSensorType().equals(ConstantUtil.Gas)){
                                        sensorString[2]=zhi;
                                    }//燃气(1)
                                    if (list.get(i).getBoardId().equals("3")&&list.get(i).getSensorType().equals(ConstantUtil.Smoke)){
                                        sensorString[3]=zhi;
                                    }//烟雾（1）
                                    if (list.get(i).getBoardId().equals("8")&&list.get(i).getSensorType().equals(ConstantUtil.HumanInfrared)){
                                        sensorString[4]=zhi;
                                    }//人体红外
                                    if (list.get(i).getBoardId().equals("7")&&list.get(i).getSensorType().equals(ConstantUtil.AirPressure)){
                                        sensorString[5]=zhi;
                                    }//气压
                                    if (list.get(i).getBoardId().equals("9")&&list.get(i).getSensorType().equals(ConstantUtil.Illumination)){
                                        sensorString[6]=zhi;
                                    }
                                    if(list.get(i).getBoardId().equals("16")&&list.get(i).getSensorType().equals(ConstantUtil.Relay)){
                                        if (!TextUtils.isEmpty(list.get(i).getValue())&&list.get(i).getValue().equals(ConstantUtil.Close)){
                                            sd="1";
                                        }else {
                                            sd="0";
                                        }
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                caiji();
                moshi();
                control();
            }
        };
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                Message msg=new Message();
                handler.sendMessage(msg);
            }
        };
        Timer timer=new Timer();
        timer.schedule(task,0,1000);
//
    }

    private void control() {
        if (sd=="1"){
            shedeng.setChecked(false);
        }else {
            shedeng.setChecked(true);
        }
    }

    private void moshi() {
        if (b==1){
            if (Double.parseDouble(HallActivity.sensorString[6])<100){
                Log.i(tag,"模式射灯开");
                ControlUtils.control(ConstantUtil.Relay,"10",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Open);

            }
            else if (Double.parseDouble(HallActivity.sensorString[6])>300){
                Log.i(tag,"模式射灯关");
                ControlUtils.control(ConstantUtil.Relay,"10",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Close);
                ControlUtils.control(ConstantUtil.Relay,"12",ConstantUtil.CmdCode_3,"4",ConstantUtil.Open);
            }

        }else {
              ControlUtils.control(ConstantUtil.Relay,"10",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Close);
        }

    }

    private void caiji() {
        pm25.setText(HallActivity.sensorString[0]);//pm25(2)
        ranqi.setText(HallActivity.sensorString[2]);//ranqi(1)
        rentihongwai.setText(HallActivity.sensorString[4].equals("1")?"有人":"无人");//hongwai
        yanwu.setText(HallActivity.sensorString[3]);//yanwu(1)
        qiya.setText(HallActivity.sensorString[5]);
        guangzhaodu.setText(HallActivity.sensorString[6]);//guangzhao(2)
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.curtainon:
                ControlUtils.control(ConstantUtil.Relay,"12",ConstantUtil.CmdCode_3,"1",ConstantUtil.Open);
                Log.i(tag,"窗帘开");
                off.setChecked(false);
                stop.setChecked(false);
                break;
            case R.id.curtainoff:
                ControlUtils.control(ConstantUtil.Relay,"12",ConstantUtil.CmdCode_3,"4",ConstantUtil.Open);
                Log.i(tag,"窗帘关");
                on.setChecked(false);
                stop.setChecked(false);
                break;
            case R.id.curtainstop:
                ControlUtils.control(ConstantUtil.Relay,"12",ConstantUtil.CmdCode_3,"3",ConstantUtil.Open);
                Log.i(tag,"窗帘停");
                on.setChecked(false);
                off.setChecked(false);
                break;
            case R.id.backup:
                startActivity(new Intent(HallActivity.this,MainActivity.class));
                finish();
                break;
            case R.id.btn_chaxun:
                Log.i(tag,"大厅人脸查询");
                ControlUtils.getFaceData();
                isCha=true;
                break;



        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.shushimoshi:
                if (shushimoshi.isChecked()){
                    Log.i(tag,"moshi开");
                    b=1;
                }else {
                    Log.i(tag,"moshi关");
                    b=0;
                }
                break;
            case R.id.hall_dianshi:
                if (dianshi.isChecked()){
                    ControlUtils.control(ConstantUtil.Infrared,"14",ConstantUtil.CmdCode_3,"1",ConstantUtil.Open);
                }else {
                    ControlUtils.control(ConstantUtil.Infrared,"14",ConstantUtil.CmdCode_3,"1",ConstantUtil.Open);
                }
                break;
            case R.id.hall_dvd:
                if (dvd.isChecked()){
                    ControlUtils.control(ConstantUtil.Infrared,"14",ConstantUtil.CmdCode_3,"8",ConstantUtil.Open);
                }else {
                    ControlUtils.control(ConstantUtil.Infrared,"14",ConstantUtil.CmdCode_3,"8",ConstantUtil.Open);
                }
                break;
            case R.id.hall_shedeng:
                if(shedeng.isChecked()){
                    ControlUtils.control(ConstantUtil.Relay,"16",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Open);
                    Log.i(tag,"大厅双射灯开");//双射灯（卧室射灯）
                }else {
                    ControlUtils.control(ConstantUtil.Relay,"16",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Close);
                    Log.i(tag,"大厅双射灯关");
                }
                break;
            case R.id.hall_baojingdeng:
                if (baojingdeng.isChecked()){
                    ControlUtils.control(ConstantUtil.Relay,"18",ConstantUtil.CmdCode_3,ConstantUtil.Channel_ALL,ConstantUtil.Open);
                    Log.i(tag,"大厅报警灯开");
                }else {
                    
                }
                break;
        }
    }
}