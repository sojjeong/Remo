package com.imaginarywings.capstonedesign.remo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.support.media.ExifInterface;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by rimi on 2017. 5. 31..
 * Copyright (c) 2017 UserInsight Corp.
 */

public class ExifUtil {

    public static Bitmap decodeBitmapWithRotation(byte[] picture) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        Matrix matrix = getBitmapRotation(picture);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Matrix getBitmapRotation(byte[] picture) {
        int orientation = 0;
        try {
            orientation = getExifOrientation(new ByteArrayInputStream(picture));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Matrix matrix = new Matrix();
        switch (orientation) {
            case 2:
                matrix.setScale(-1, 1);
                break;
            case 3:
                matrix.setRotate(180);
                break;
            case 4:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case 5:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case 6:
                matrix.setRotate(90);
                break;
            case 7:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case 8:
                matrix.setRotate(-90);
                break;
        }
        return matrix;
    }

    private static int getExifOrientation(InputStream inputStream) throws IOException {
        int orientation = 1;

        try {
            /**
             * if your are targeting only api level >= 5
             * ExifInterface exif = new ExifInterface(src);
             * orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
             */
            if (Build.VERSION.SDK_INT >= 5) {
//                orientation = new ImageHeaderParser(inputStream).getOrientation();

                ExifInterface exifInterface = new ExifInterface(inputStream);
                orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Log.i("ori", "getExifOrientation: " + orientation);
//                Class<?> exifClass = Class.forName("android.media.ExifInterface");
//                Constructor<?> exifConstructor = exifClass.getConstructor(new Class[]{InputStream.class});
//                Object exifInstance = exifConstructor.newInstance(new Object[]{inputStream});
//                Method getAttributeInt = exifClass.getMethod("getAttributeInt", new Class[]{String.class, int.class});
//                Field tagOrientationField = exifClass.getField("TAG_ORIENTATION");
//                String tagOrientation = (String) tagOrientationField.get(null);
//                orientation = (Integer) getAttributeInt.invoke(exifInstance, new Object[]{tagOrientation, 1});
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return orientation;
    }
}