package com.example.admin.Demo;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.List;

import dao.User;
import dao.UserDao;
import dao.UserHelper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Button login,register;
    EditText et_user,et_password,et_again;
    UserDao dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        login=findViewById(R.id.register_login);
        register=findViewById(R.id.register_register);
        et_user=findViewById(R.id.register_user);
        et_password=findViewById(R.id.register_password);
        et_again=findViewById(R.id.again_password);
        login.setOnClickListener(this);
        dao=new UserDao(this);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_login:
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
                break;
            case R.id.register_register:
                String User=et_user.getText().toString();
                String Password=et_password.getText().toString();
                User user=new User(User,Password);
                if (TextUtils.isEmpty(et_user.getText().toString())){
                    Toast.makeText(RegisterActivity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(et_password.getText().toString())){
                    Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!et_password.getText().toString().equals(et_again.getText().toString())){
                    Toast.makeText(RegisterActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_password.getText().toString().length()<6){
                    Toast.makeText(RegisterActivity.this,"密码不能小于六位",Toast.LENGTH_SHORT).show();
                    return;
                }
                List<User>list=dao.find(user);
                if (list.size()>=1){
                    Toast.makeText(RegisterActivity.this,"用户名已存在",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    dao.insert(user);
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

                }
                break;
        }
    }

}