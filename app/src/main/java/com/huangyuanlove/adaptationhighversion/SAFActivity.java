package com.huangyuanlove.adaptationhighversion;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class SAFActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView showInfo;

    private final int OPEN_DOCUMENT_CODE = 10001;
    private final int OPEN_TREE_CODE = 10002;
    private final int CREATE_DOCUMENT_CODE = 10003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saf);

        showInfo = findViewById(R.id.show_info);

        findViewById(R.id.open_document).setOnClickListener(this);
        findViewById(R.id.open_document_tree).setOnClickListener(this);
        findViewById(R.id.create_document).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.open_document:
                 intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                //文档需要是可以打开的
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                //指定文档的minitype为text类型
                intent.setType("*/*");
                //是否支持多选，默认不支持
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
                startActivityForResult(intent, OPEN_DOCUMENT_CODE);
                break;
            case R.id.open_document_tree:
                 intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                startActivityForResult(intent, OPEN_TREE_CODE);
                break;
            case R.id.create_document:
                 intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                //设置创建的文件是可打开的
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                //设置创建的文件的minitype为文本类型
                intent.setType("text/*");
                //设置创建文件的名称，注意SAF中使用minitype而不是文件的后缀名来判断文件类型。
                intent.putExtra(Intent.EXTRA_TITLE, "123.txt");
                startActivityForResult(intent,CREATE_DOCUMENT_CODE);
                break;
            default:
                break;

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case OPEN_DOCUMENT_CODE:
                    //根据request_code处理打开文档的结果
                    handleOpenDocumentAction(data);
                    break;
                case OPEN_TREE_CODE:
                    handleTreeAction( data);
                    break;
                case CREATE_DOCUMENT_CODE:
                    handleCreateDocumentAction(data);
                    break;
                    default:break;
            }
        }
    }


    private void handleOpenDocumentAction(Intent data){
        if (data == null) {
            return;
        }
        //获取文档指向的uri,注意这里是指单个文件。
        Uri uri = data.getData();
        //根据该Uri可以获取该Document的信息，其数据列的名称和解释可以在DocumentsContact类的内部类Document中找到
        //我们在此查询的信息仅仅只是演示作用
        Cursor cursor = getContentResolver().query(uri,null,
                null,null,null,null);
        StringBuilder sb = new StringBuilder(" open document Uri ");
        sb.append(uri.toString());
        sb.append("\n");
        if(cursor!=null && cursor.moveToFirst()){
            String documentId = cursor.getString(cursor.getColumnIndex(
                    DocumentsContract.Document.COLUMN_DOCUMENT_ID));
            String name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
            String size = null;
            if (!cursor.isNull(sizeIndex)) {
                // Technically the column stores an int, but cursor.getString()
                // will do the conversion automatically.
                size = cursor.getString(sizeIndex);
            } else {
                size = "Unknown";
            }
            sb.append(" name ").append(name).append(" size ").append(size);
            sb.append("\n");
        }
        //以下为直接从该uri中获取InputSteam，并读取出文本的内容的操作，这个是纯粹的java流操作，大家应该已经很熟悉了
        //我就不多解释了。另外这里也可以直接使用OutputSteam，向文档中写入数据。
        BufferedReader br = null;
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            sb.append("\r\n content : ");
            while((line = br.readLine())!=null){
                sb.append(line);
            }
            sb.append("\n");
            showInfo.setText(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        }
    }


    private void handleTreeAction(Intent data){
        Uri treeUri = data.getData();
        //授予打开的文档树永久性的读写权限
//        final int takeFlags = intent.getFlags()
//                & (Intent.FLAG_GRANT_READ_URI_PERMISSION
//                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        getContentResolver().takePersistableUriPermission(uri, takeFlags);
        //使用DocumentFile构建一个根文档，之后的操作可以在该文档上进行
       DocumentFile  mRoot = DocumentFile.fromTreeUri(this, treeUri);
        //显示结果toast


        showInfo.setText(" open tree uri "+treeUri);
    }


    private void handleCreateDocumentAction(Intent data){
        if(data==null){
            return;
        }
        try {
            String text = "创建文件的内容";
            Uri path = data.getData();
            //获取该Document的输入流，并写入数据
            OutputStream  os = getContentResolver().openOutputStream(path);
            os.write(text.getBytes());
            showInfo.setText(" create document succeed "+path);
        }catch (Exception e){
            e.printStackTrace();
            showInfo.setText(" create document fail "+e.toString());
        }finally {

        }

    }


}
