package com.example.xx.zxing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.xx.zxing.utils.ImageUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.SettingService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_SCAN = 102;
    private static final int REQUEST_SELECT = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
    }

    /**
     * 检查权限
     */
    private void checkPermission() {
        AndPermission.with(this)
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        new MaterialDialog.Builder(MainActivity.this)
                                .cancelable(false)
                                .title("必要权限申请")
                                .content("请务必授权该权限！")
                                .positiveText("去授权")
                                .negativeText("退出")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        SettingService settingService = AndPermission.permissionSetting(MainActivity.this);
                                        settingService.execute();
                                    }
                                })
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        finish();
                                    }
                                })
                                .show();
                    }
                })
                .start();
    }

    public void scanClick(View view) {
        startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class), REQUEST_SCAN);
    }

    public void selectClick(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_SELECT);
    }

    public void cusScanClick(View view) {
        startActivityForResult(new Intent(MainActivity.this, CustomScanActivity.class), REQUEST_SCAN);
    }

    public void generateQRClick(View view) {
        startActivity(new Intent(MainActivity.this, GenerateQRActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }

        switch (requestCode) {
            case REQUEST_SCAN:
                if (data == null) {
                    return;
                }

                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }

                int type = bundle.getInt(CodeUtils.RESULT_TYPE);
                if (CodeUtils.RESULT_SUCCESS == type) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    showToast(result);
                } else if (CodeUtils.RESULT_FAILED == type) {
                    showToast("解析二维码失败");
                }
                break;
            case REQUEST_SELECT:
                if (data == null) {
                    return;
                }

                Uri uri = data.getData();
                if (uri == null) {
                    return;
                }

                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            showToast(result);
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            showToast("解析二维码失败");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
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
