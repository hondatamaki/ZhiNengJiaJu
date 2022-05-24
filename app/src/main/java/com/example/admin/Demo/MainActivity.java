package com.example.admin.Demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button hall,kitchen,bedroom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hall=findViewById(R.id.hall);
        kitchen=findViewById(R.id.kitchen);
        bedroom=findViewById(R.id.bedroom);
        hall.setOnClickListener(this);
        kitchen.setOnClickListener(this);
        bedroom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hall:
                startActivity(new Intent(MainActivity.this,HallActivity.class));
                break;
            case R.id.kitchen:
                startActivity(new Intent(MainActivity.this,KitchenActivity.class));
                break;
            case R.id.bedroom:
                startActivity(new Intent(MainActivity.this,BedroomActivity.class));
                break;
        }
    }
}
