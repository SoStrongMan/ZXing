package com.example.xx.zxing;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CodeUtils;

public class GenerateQRActivity extends AppCompatActivity {

    private EditText etContent;
    private ImageView ivQr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);

        etContent = findViewById(R.id.et_content);
        ivQr = findViewById(R.id.iv_qr);
    }

    public void commonClick(View view) {
        String content = etContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            showToast("内容不能为空");
            return;
        }

        ivQr.setImageBitmap(CodeUtils.createImage(content, 400, 400, null));
        etContent.setText("");
    }

    public void logoClick(View view) {
        String content = etContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            showToast("内容不能为空");
            return;
        }

        ivQr.setImageBitmap(CodeUtils.createImage(content, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)));
        etContent.setText("");
    }

    /**
     * 吐司
     *
     * @param msg 内容
     */
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}