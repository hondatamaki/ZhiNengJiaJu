package com.example.admin.Demo;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.bizideal.smarthome.socket.ConstantUtil;
import com.bizideal.smarthome.socket.ControlUtils;
import com.bizideal.smarthome.socket.LoginCallback;
import com.bizideal.smarthome.socket.SocketClient;

import java.util.List;

import dao.User;
import dao.UserDao;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_user,et_password,et_ip;
    Button btn_login,btn_register;
    CheckBox remember;
    UserDao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_ip=findViewById(R.id.login_ip);
        et_password=findViewById(R.id.login_password);
        et_user=findViewById(R.id.login_user);
        btn_login=findViewById(R.id.login_login);
        btn_register=findViewById(R.id.login_register);
        remember=findViewById(R.id.remember);
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
//        remember.setChecked(this);
        dao=new UserDao(this);
//        boolean isRemember=loginPreference.getBoolean()
//        loginPreference=getSharedPreferences("login",MODE_PRIVATE);
//        SharedPreferences.Editor editor=loginPreference.edit();
//        editor.putBoolean("checked",remember.isChecked());
//            if ()
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_login:
                String username=et_user.getText().toString();
                String password=et_password.getText().toString();
                String ip=et_ip.getText().toString();
                User user=new User(username,password);
                List<User>list=dao.query(user);
                if (list.size()==0){
                    Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    ControlUtils.setUser("bizideal", "123456", ip);
                    SocketClient.getInstance().creatConnect();
                    SocketClient.getInstance().login(new LoginCallback() {
                        @Override
                        public void onEvent(final String s) {
                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            if (s.equals(ConstantUtil.Success)) {
                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                                SocketClient.getInstance().release();
                                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    });
                }
                break;
            case R.id.login_register:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
                break;
        }
    }
}
