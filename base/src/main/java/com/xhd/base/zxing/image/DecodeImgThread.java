package com.xhd.base.zxing.image;

import android.text.TextUtils;
import android.util.Log;

import com.google.zxing.Result;
import com.xhd.base.zxing.parse.DecodeFormatManager;
import com.xhd.base.zxing.parse.QRUtils;

/**
 * Content:
 * Create by wk on 2021/7/23
 */
public class DecodeImgThread extends Thread {

    /*图片路径*/
    private String imgPath;
    /*回调*/
    private DecodeImgCallback callback;


    public DecodeImgThread(String imgPath, DecodeImgCallback callback) {
        this.imgPath = imgPath;
        this.callback = callback;
    }

    @Override
    public void run() {
        super.run();

        if (TextUtils.isEmpty(imgPath) || callback == null) {
            return;
        }

        Result result = null;
        try {
            result = QRUtils.parseCodeResult(imgPath, DecodeFormatManager.ALL_HINTS);
            Log.i("解析结果", result.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result != null) {
            callback.onImageDecodeSuccess(result);
        } else {
            callback.onImageDecodeFailed();
        }
    }

    public interface DecodeImgCallback {
        void onImageDecodeSuccess(Result result);

        void onImageDecodeFailed();
    }

}