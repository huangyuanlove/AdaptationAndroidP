package com.huangyuanlove.adaptationhighversion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.file_operation).setOnClickListener(this);
        findViewById(R.id.photo).setOnClickListener(this);
        findViewById(R.id.saf).setOnClickListener(this);
        findViewById(R.id.notification).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.file_operation:
                startActivity(new Intent(MainActivity.this,FileOperationActivity.class));
                break;
            case R.id.photo:
                startActivity(new Intent(MainActivity.this,TakePhotoOrChoosePhotoActivity.class));
                break;
            case R.id.saf:
                startActivity(new Intent(MainActivity.this,SAFActivity.class));
                break;
            case R.id.notification:
                startActivity(new Intent(MainActivity.this,NotificationActivity.class));
                break;
            default:

                break;
        }
    }
}
