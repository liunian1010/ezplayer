package com.lmly.ezplayerlib.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextUtils;

import com.videogo.exception.InnerException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

public class EZUtils {


    /**
     * 保存录像抓图到本地
     */
    public static void saveCapturePictrue(String filePath, String thumbnailFilePath, Bitmap bitmap) throws InnerException {
        FileOutputStream out = null;
        FileOutputStream thumbnailOut = null;
        try {
            // 保存原图
            
            if (!TextUtils.isEmpty(filePath)) {
                File file = new File(filePath);
                out = new FileOutputStream(file);
                bitmap.compress(CompressFormat.JPEG, 100, out);
                //out.write(tempBuf, 0, size);
                out.flush();
                out.close();
                out = null;
            }

            // 保存缩略图
            if (!TextUtils.isEmpty(thumbnailFilePath)) {
                File file = new File(thumbnailFilePath);
                thumbnailOut = new FileOutputStream(file);
                boolean decodeRet = decodeThumbnail(bitmap, bitmap.getWidth(), bitmap.getHeight(), thumbnailOut);
                thumbnailOut.flush();
                thumbnailOut.close();
                thumbnailOut = null;
                if (!decodeRet) {
                    throw new InnerException("decode thumbnail picture fail");
                }
            }

        } catch (FileNotFoundException e) {
            throw new InnerException(e.getLocalizedMessage());
        } catch (IOException e) {
            throw new InnerException(e.getLocalizedMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (thumbnailOut != null) {
                try {
                    thumbnailOut.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


    private static boolean decodeThumbnail(Bitmap bitmap, int width, int height, FileOutputStream fos) {
        if (bitmap == null || width < 0 || height < 0 || fos == null) {
            return false;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        int scale = 0;
        int width_tmp = width;
        int height_tmp = height;
        int MIN_WIDTH = 120;
        int MIN_HEIGHT = 90;
        while (true) {
            if (width_tmp <= MIN_WIDTH && height_tmp <= MIN_HEIGHT) {
                break;
            }

            width_tmp = width_tmp / 2;
            height_tmp = height_tmp / 2;
            scale += 2;
        }

        options.inSampleSize = scale;
        
        Bitmap newBmp = Bitmap.createBitmap(width_tmp, height_tmp, Config.ARGB_8888);
        
        Canvas cv = new Canvas(newBmp);

        // 在 0, 0坐标开始画入src
//        cv.drawBitmap(bitmap, 0, 0, null);
        Rect src = new Rect(0,0,bitmap.getWidth(), bitmap.getHeight());
        Rect dst = new Rect(0,0, newBmp.getWidth(), newBmp.getHeight());
        cv.drawBitmap(bitmap, src, dst, null);

        // 保存
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();

        newBmp.compress(CompressFormat.JPEG, 100, fos);

        return true;
    }


}
