package com.ihongqiqu.Identify.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.ihongqiqu.Identify.BuildConfig;
import com.ihongqiqu.Identify.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 微信分享
 *
 * Created by zhenguo on 9/9/15.
 */
public class ShareUtil {

    public static final String WX_APP_ID = "wxe8a6cdca7a4c943c";
    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
    private static final int THUMB_SIZE = 120;

    public static boolean shareTxt2WeiXin(Context context, boolean isTimeline, String text) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, WX_APP_ID);
        iwxapi.registerApp(WX_APP_ID);
        int wxSdkVersion = iwxapi.getWXAppSupportAPI();
        boolean isInstalled = iwxapi.isWXAppInstalled();
        if (!isInstalled) {
            return false;
        }
        if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {

            // ³õÊ¼»¯Ò»¸öWXTextObject¶ÔÏó
            WXTextObject textObj = new WXTextObject();
            textObj.text = text;

            // ÓÃWXTextObject¶ÔÏó³õÊ¼»¯Ò»¸öWXMediaMessage¶ÔÏó
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = textObj;
            // ·¢ËÍÎÄ±¾ÀàÐÍµÄÏûÏ¢Ê±£¬title×Ö¶Î²»Æð×÷ÓÃ
            // msg.title = "Will be ignored";
            msg.description = text;

            // ¹¹ÔìÒ»¸öReq
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("text"); // transaction×Ö¶ÎÓÃÓÚÎ¨Ò»±êÊ¶Ò»¸öÇëÇó
            req.message = msg;
            req.scene = isTimeline  ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

            // µ÷ÓÃapi½Ó¿Ú·¢ËÍÊý¾Ýµ½Î¢ÐÅ
            iwxapi.sendReq(req);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 分享图片到微信
     *
     * @param context
     * @param isTimeline
     * @param bitmap
     *
     * @return
     */
    public static boolean shareImg2WeiXin(Context context, boolean isTimeline, Bitmap bitmap) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, WX_APP_ID);
        iwxapi.registerApp(WX_APP_ID);
        int wxSdkVersion = iwxapi.getWXAppSupportAPI();
        boolean isInstalled = iwxapi.isWXAppInstalled();
        if (!isInstalled) {
            return false;
        }
        if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
            Bitmap bmp = bitmap; // compressImage(bitmap);
            WXImageObject imgObj = new WXImageObject(bmp);

            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObj;

            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, false);
            // Bitmap thumbBmp = compressImage(bmp);
            bmp.recycle();
            msg.thumbData = bmpToByteArray(thumbBmp, true);

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = isTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            boolean isSendSuccess = iwxapi.sendReq(req);
            // if (BuildConfig.DEBUG)
                Log.d("ShareUtil", "isSendSuccess : " + isSendSuccess);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 压缩图片大小
     *
     * @param image 源Bitmap
     * @return 压缩后的Bitmap
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 64) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
