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

public class KitchenActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public static String[] sensorString=new String[]{"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
    public static String tag="KitchenActivity";
    TextView yanwu,ranqi,name,time,chaxun;
    ToggleButton baojingdeng,fengshan,shedeng,anfangmoshi;
    Button backup;
    int i;
    boolean isCha=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);
        yanwu=findViewById(R.id.kitchen_yanwu);
        ranqi=findViewById(R.id.kitchen_ranqi);
        baojingdeng=findViewById(R.id.kitchen_baojingdeng);
        name=findViewById(R.id.tv_name);
        time=findViewById(R.id.tv_time);
        fengshan=findViewById(R.id.kitchen_fengshan);
        shedeng=findViewById(R.id.kitchen_shedeng);
        chaxun=findViewById(R.id.btn_chaxun);
        anfangmoshi=findViewById(R.id.anfangmoshi);
        backup=findViewById(R.id.backup);
        baojingdeng.setOnCheckedChangeListener(this);
        fengshan.setOnCheckedChangeListener(this);
        shedeng.setOnCheckedChangeListener(this);
        anfangmoshi.setOnCheckedChangeListener(this);
        backup.setOnClickListener(this);
        chaxun.setOnClickListener(this);

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
                                 if (list.get(i).getBoardId().equals("1")){
                                     if (list.get(i).getSensorType().equals(ConstantUtil.Temperature)){
                                         sensorString[0]=zhi;
                                     }else {
                                         sensorString[1]=zhi;
                                     }
                                 }//温度湿度 1
                                 if (list.get(i).getBoardId().equals("2")&&list.get(i).getSensorType().equals(ConstantUtil.Illumination)){
                                     sensorString[2]=zhi;
                                 }//光照度（1）
                                 if (list.get(i).getBoardId().equals("9")&&list.get(i).getSensorType().equals(ConstantUtil.Illumination)){
                                     sensorString[3]=zhi;
                                 }//光照度（2）
                                 if (list.get(i).getBoardId().equals("5")&&list.get(i).getSensorType().equals(ConstantUtil.PM25)){
                                     sensorString[4]=zhi;
                                 }//PM25（1）
                                 if (list.get(i).getBoardId().equals("20")&&list.get(i).getSensorType().equals(ConstantUtil.PM25)){
                                     sensorString[5]=zhi;
                                 }//PM25(2)
                                 if (list.get(i).getBoardId().equals("6")&&list.get(i).getSensorType().equals(ConstantUtil.CO2)){
                                     sensorString[6]=zhi;
                                 }//Co2
                                 if (list.get(i).getBoardId().equals("4")&&list.get(i).getSensorType().equals(ConstantUtil.Gas)){
                                     sensorString[7]=zhi;
                                 }//燃气(1)
                                 if (list.get(i).getBoardId().equals("19")&&list.get(i).getSensorType().equals(ConstantUtil.Gas)){
                                     sensorString[8]=zhi;
                                 }//燃气（2）
                                 if (list.get(i).getBoardId().equals("3")&&list.get(i).getSensorType().equals(ConstantUtil.Smoke)){
                                     sensorString[9]=zhi;
                                 }//烟雾（1）
                                 if (list.get(i).getBoardId().equals("18")&&list.get(i).getSensorType().equals(ConstantUtil.Smoke)){
                                     sensorString[10]=zhi;
                                 }//烟雾（2）
                                 if (list.get(i).getBoardId().equals("8")&&list.get(i).getSensorType().equals(ConstantUtil.HumanInfrared)){
                                     sensorString[11]=zhi;
                                 }//人体红外
                                 if (list.get(i).getBoardId().equals("7")&&list.get(i).getSensorType().equals(ConstantUtil.AirPressure)){
                                     sensorString[12]=zhi;
                                 }//气压
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
                moshi();
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
        if (i==1){
            Log.i(tag,"模式开");
            if (Double.parseDouble(KitchenActivity.sensorString[3])<10){
                ControlUtils.control(ConstantUtil.Relay,"11",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Open);
                ControlUtils.control(ConstantUtil.Relay,"13",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Open);

            }else {
                ControlUtils.control(ConstantUtil.Relay,"11",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Close);
                ControlUtils.control(ConstantUtil.Relay,"13",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Close);
                Log.i(tag,"模式关");}

        }
        }


    private void caiji() {
        yanwu.setText(KitchenActivity.sensorString[3]);//设置的是光照值
        ranqi.setText(KitchenActivity.sensorString[8]);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backup:
                i=0;
                startActivity(new Intent(KitchenActivity.this,MainActivity.class));
//                SocketClient.getInstance().release();
                finish();
                break;
            case R.id.btn_chaxun:
                Log.i(tag,"厨房人脸查询");
                ControlUtils.getFaceData();
                isCha=true;
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.anfangmoshi:
                if (anfangmoshi.isChecked()) {
                    i = 1;
                    Log.i(tag, "moshi开");
                }else {
                    i=0;
                    Log.i(tag,"moshi关");
                }

                break;
            case R.id.kitchen_shedeng:
                if(shedeng.isChecked()){
                    ControlUtils.control(ConstantUtil.Relay,"22",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Open);
                    Log.i(tag,"厨房单射灯开");//单射灯
                }else {
                    ControlUtils.control(ConstantUtil.Relay,"22",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Close);
                    Log.i(tag,"厨房单射灯关");
                }
                break;
            case R.id.kitchen_baojingdeng:
                if (baojingdeng.isChecked()){
                    ControlUtils.control(ConstantUtil.Relay,"11",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Open);
                    Log.i(tag,"餐厅报警灯开");
                }else {
                    ControlUtils.control(ConstantUtil.Relay,"11",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Close);
                    Log.i(tag,"餐厅报警灯关");
                }
                break;
            case R.id.kitchen_fengshan:
                if (fengshan.isChecked()){
                    ControlUtils.control(ConstantUtil.Relay,"13",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Open);
                    Log.i(tag,"风扇开");
                }else {
                    ControlUtils.control(ConstantUtil.Relay,"13",ConstantUtil.CmdCode_1,ConstantUtil.Channel_ALL,ConstantUtil.Close);
                    Log.i(tag,"风扇关");
                }
                break;

        }
    }
}
