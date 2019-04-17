package com.huangyuanlove.adaptationhighversion;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private final int REQUEST_PERMISSION_CAMERA = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.file_operation).setOnClickListener(this);
        findViewById(R.id.photo).setOnClickListener(this);
        findViewById(R.id.saf).setOnClickListener(this);
        findViewById(R.id.notification).setOnClickListener(this);
        findViewById(R.id.request_permission).setOnClickListener(this);

    }

    private boolean checkPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.file_operation:
                startActivity(new Intent(MainActivity.this, FileOperationActivity.class));
                break;
            case R.id.photo:

                if (checkPermission(Manifest.permission.CAMERA) || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                    startActivity(new Intent(MainActivity.this, TakePhotoOrChoosePhotoActivity.class));
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
                }

                break;
            case R.id.saf:
                startActivity(new Intent(MainActivity.this, SAFActivity.class));
                break;
            case R.id.notification:
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                break;
            case R.id.request_permission:


                String[] permissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};

               requestPermissions(permissions,REQUEST_PERMISSION_CAMERA);



//                if (checkPermission(Manifest.permission.CAMERA) || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                    Toast.makeText(MainActivity.this, "相机权限", Toast.LENGTH_SHORT).show();
//                } else {
//                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
//                }

                break;
            default:

                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(permissions.length <= 0 || grantResults.length<=0){
            return ;
        }


        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERA:
                final String permission = permissions[0];

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "授予拍照权限成功", Toast.LENGTH_SHORT).show();
                } else {
                    if (shouldShowRequestPermissionRationale(permissions[0])) {
                        //用户只点击了禁止，没有选择不再提示
                        Toast.makeText(MainActivity.this, "用户禁止了" + permission + "权限", Toast.LENGTH_SHORT).show();


                    } else {

                        //解释原因，并且引导用户至设置页手动授权
                        new AlertDialog.Builder(this)
                                .setMessage("【用户选择了不在提示按钮，或者系统默认不在提示（如MIUI）。" +
                                        "引导用户到应用设置页去手动授权,注意提示用户具体需要哪些权限】\r\n" +
                                        "获取相关权限失败:xxxxxx,将导致部分功能无法正常使用，需要到设置页面手动授权")
                                .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //引导用户至设置页手动授权
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //引导用户手动授权，权限请求失败
                                        Toast.makeText(MainActivity.this, "引导授权失败:" + permission, Toast.LENGTH_LONG).show();
                                    }
                                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                //引导用户手动授权，权限请求失败
                                Toast.makeText(MainActivity.this, "引导授权失败:" + permission, Toast.LENGTH_LONG).show();
                            }
                        }).show();
                    }
                }

                break;
            default:
                break;
        }


    }
}
