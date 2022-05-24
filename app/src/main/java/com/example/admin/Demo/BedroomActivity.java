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

public class BedroomActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    public static String tag="BedroomFragment";
    public static  String[] sensorString=new String[]{ "0","0","0","0","0","0"};
    TimerTask task;
    Timer timer;
    TextView guangzhaodu,shidu,wendu,pm25,co2,name,time,chaxun;
    ToggleButton menjin,jiashiqi,kongtiao,shedeng,wendumoshi;
    Button backup;
    int a;
    boolean isCha=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedroom);
        guangzhaodu=findViewById(R.id.bedroom_guangzhao);
        shidu=findViewById(R.id.bedroom_shidu);
        name=findViewById(R.id.tv_name);
        time=findViewById(R.id.tv_time);
        wendu=findViewById(R.id.bedroom_wendu);
        pm25=findViewById(R.id.bedroom_pm25);
        co2=findViewById(R.id.bedroom_co2);
        menjin=findViewById(R.id.bedroom_menjin);
        jiashiqi=findViewById(R.id.bedroom_jiashiqi);
        kongtiao=findViewById(R.id.bedroom_kongtiao);
        shedeng=findViewById(R.id.bedroom_shedeng);
        chaxun=findViewById(R.id.btn_chaxun);
        backup=findViewById(R.id.backup);
        wendumoshi=findViewById(R.id.wendumoshi);
        menjin.setOnCheckedChangeListener(this);
        backup.setOnClickListener(this);
        jiashiqi.setOnCheckedChangeListener(this);
        kongtiao.setOnClickListener(this);
        shedeng.setOnCheckedChangeListener(this);
        wendumoshi.setOnCheckedChangeListener(this);
        chaxun.setOnClickListener(this);

        ControlUtils.getData();
        SocketClient.getInstance().getData(new DataCallback<DeviceBean>() {

            @Override
            public void onResult(final DeviceBean deviceBean) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if ( isCha && !TextUtils.isEmpty(deviceBean.getName()))
                                name.setText(deviceBean.getName());
                            if ( isCha && !TextUtils.isEmpty(deviceBean.getTime()))
                                time.setText(deviceBean.getTime());
                            ArrayList<DeviceBean.Devices>list=deviceBean.getDevice();
                            if (list.size()>0){
                                for (int i=0;i<list.size(); i++){
                                    String zhi=list.get(i).getValue();
                                    if (list.get(i).getBoardId().equals("17")){
                                        if (list.get(i).getSensorType().equals(ConstantUtil.Temperature)){
                                            sensorString[0]=zhi;
                                        }else {
                                            sensorString[1]=zhi;
                                        }
                                    }
                                    if (list.get(i).getBoardId().equals("9")&&list.get(i).getSensorType().equals(ConstantUtil.Illumination)){
                                        sensorString[2]=zhi;
                                    }
                                    if (list.get(i).getBoardId().equals("2")&&list.get(i).getSensorType().equals(ConstantUtil.Illumination)){
                                        sensorString[3]=zhi;//烟雾
                                    }
                                    if (list.get(i).getBoardId().equals("6")&&list.get(i).getSensorType().equals(ConstantUtil.PM25)){
                                        sensorString[4]=zhi;
                                    }

                                }
                            }
                        }catch (Exception e){}


                    }
                });
            }
        });
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                caiji();
//                moshi();
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

    }

    private void moshi() {

        if (a==1){
            if (Double.parseDouble(BedroomActivity.sensorString[0])>30||Double.parseDouble(BedroomActivity.sensorString[0])<18){
                if (!shedeng.isChecked()){
                    Log.i(tag,"模式开");
                    ControlUtils.control(ConstantUtil.Relay,"10",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Open);
                }
            }else {
                if (shedeng.isChecked())
                Log.i(tag,"模式关");
                ControlUtils.control(ConstantUtil.Relay,"10",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Close);
            }
        }

    }

    private void caiji() {

        guangzhaodu.setText(BedroomActivity.sensorString[2]);
        pm25.setText(BedroomActivity.sensorString[4]);
        wendu.setText(BedroomActivity.sensorString[0]);
        shidu.setText(BedroomActivity.sensorString[1]);
        co2.setText(BedroomActivity.sensorString[3]);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){

            case R.id.bedroom_jiashiqi:
                if (jiashiqi.isChecked()){
                    ControlUtils.control(ConstantUtil.Infrared,"14",ConstantUtil.CmdCode_1,"9",ConstantUtil.Open);
                }else {
                    ControlUtils.control(ConstantUtil.Infrared,"14",ConstantUtil.CmdCode_1,"9",ConstantUtil.Open);
                }
                break;
            case R.id.bedroom_shedeng:
                if(shedeng.isChecked()){
                    ControlUtils.control(ConstantUtil.Relay,"10",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Open);
                    Log.i(tag,"卧室双射灯关");//双射灯
                }else {
                    ControlUtils.control(ConstantUtil.Relay,"10",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Close);
                    Log.i(tag,"卧室双射灯开");
                }
                break;
            case R.id.wendumoshi:
                if (wendumoshi.isChecked()){
                    Log.i(tag,"moshi开");
                    a=1;
                }else {
                    Log.i(tag,"moshi关");
                    a=0;
                }
                break;
            case R.id.bedroom_menjin:
                ControlUtils.control( ConstantUtil.RFID_Door, "15", ConstantUtil.CmdCode_2, ConstantUtil.Channel_1, ConstantUtil.Open );
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backup:
                ControlUtils.control(ConstantUtil.Relay,"10",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Close);
                startActivity(new Intent(BedroomActivity.this,MainActivity.class));
                finish();
                break;
            case R.id.bedroom_kongtiao:
                if (kongtiao.isChecked()) {
                    ControlUtils.control( ConstantUtil.Infrared, "14", ConstantUtil.CmdCode_1,"1", ConstantUtil.Open );
                } else {
                    ControlUtils.control( ConstantUtil.Infrared, "14", ConstantUtil.CmdCode_1, "1", ConstantUtil.Open );
                }
                break;
            case R.id.btn_chaxun:
                Log.i(tag,"卧室人脸查询");
                ControlUtils.getFaceData();
                isCha=true;
                break;


        }
    }
}
