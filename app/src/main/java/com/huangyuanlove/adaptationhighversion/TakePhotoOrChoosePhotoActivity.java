package com.huangyuanlove.adaptationhighversion;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class TakePhotoOrChoosePhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView showImage;


    private File tempImageFile;

    private final int TAKE_PHOTO_CODE = 10001;
    private final int CHOOSE_PHOTO_CODE = 10002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo_or_choose_photo);
        showImage = findViewById(R.id.show_image);

        findViewById(R.id.take_photo).setOnClickListener(this);
        findViewById(R.id.choose_photo).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo:
                try {

                    //指定Uri，拍照返回的data为空
                    String tempImagePath = getFilesDir().getPath();
                    tempImageFile = File.createTempFile("image_", ".jpg", new File(tempImagePath));
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (Build.VERSION.SDK_INT >= 24) {
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                FileProvider.getUriForFile(TakePhotoOrChoosePhotoActivity.this,
                                        "com.huangyuanlove.adaptationhighversion.fileprovider", tempImageFile));
                    } else {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempImageFile));
                    }
                    startActivityForResult(intent, TAKE_PHOTO_CODE);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case R.id.choose_photo:
                Intent albumIntent = new Intent(Intent.ACTION_PICK);
                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(albumIntent, CHOOSE_PHOTO_CODE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO_CODE:
                    bitmap = BitmapFactory.decodeFile(tempImageFile.getPath());
                    showImage.setImageBitmap(bitmap);
                    break;
                case CHOOSE_PHOTO_CODE:
                    if (data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        bitmap = BitmapFactory.decodeFile(picturePath);
                        showImage.setImageBitmap(bitmap);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
