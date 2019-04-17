package com.huangyuanlove.adaptationhighversion;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class FileOperationActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {

    private TextView showInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_operation);


        showInfoTextView = findViewById(R.id.show_info);

        findViewById(R.id.show_info).setOnLongClickListener(this);
        findViewById(R.id.private_file).setOnClickListener(this);
        findViewById(R.id.public_file).setOnClickListener(this);
        findViewById(R.id.public_index_file).setOnClickListener(this);
        findViewById(R.id.external_private_file).setOnClickListener(this);

        Log.e("filePath",getFilesDir().getPath());
        Log.e("filePath",getCacheDir().getPath());
        Log.e("filePath",getExternalCacheDir().getPath());
        Log.e("filePath",getExternalFilesDir(null).getPath());
        Log.e("filePath",Environment.getExternalStorageDirectory().getPath());
        Log.e("filePath",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());


        try{
            File privateCachedFile = new File(getCacheDir().getPath() +"/privateCachedFile.txt");
            if(!privateCachedFile.exists()){
                privateCachedFile.createNewFile();
            }

            Log.e("privateCachedFile",privateCachedFile.getPath());


            File externalCacheFile =  new File(  getExternalCacheDir().getPath() +"/externalCacheFile.txt" );
            if(!externalCacheFile.exists()){
                externalCacheFile.createNewFile();
            }

            Log.e("externalCacheFile",externalCacheFile.getPath());

        }catch (Exception e){
            e.printStackTrace();
        }




    }

    @Override
    public boolean onLongClick(View v) {
        showInfoTextView.setText("");
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.private_file:
                try {
                    //不需要申请权限
                    String privateFilePath = getFilesDir().getPath();
                    File privateFile = new File(privateFilePath + "/privateFile.txt");
                    if (!privateFile.exists()) {
                        privateFile.createNewFile();
                    }
                    showInfoTextView.append(privateFile.getPath());
                    showInfoTextView.append("\n");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.public_file:
                try {
                    //需要申请权限
                    String externalStorageFilePath = Environment.getExternalStorageDirectory().getPath();
                    File externalFile = new File(externalStorageFilePath + "/externalFile.txt");
                    if (!externalFile.exists()) {
                        externalFile.createNewFile();
                    }
                    showInfoTextView.append(externalFile.getPath());
                    showInfoTextView.append("\n");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.public_index_file:
                try {
                    //不需要申请权限
                    String externalPublicFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File externalPublicFile = new File(externalPublicFilePath + "/externalPublicFile.txt");
                    if (!externalPublicFile.exists()) {
                        externalPublicFile.createNewFile();
                    }
                    showInfoTextView.append(externalPublicFile.getPath());
                    showInfoTextView.append("\n");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.external_private_file:
                try {
                    //不需要申请权限
                    String externalPrivateFilePath = getExternalFilesDir(null).getPath();
                    File externalPrivateFile = new File(externalPrivateFilePath + "/externalPrivateFile.txt");
                    if (!externalPrivateFile.exists()) {
                        externalPrivateFile.createNewFile();
                    }
                    showInfoTextView.append(externalPrivateFile.getPath());
                    showInfoTextView.append("\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

    }
}
