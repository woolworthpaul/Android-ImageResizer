package com.svenkapudija.imageresizer.operations;

import java.io.File;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.svenkapudija.imageresizer.utils.ImageDecoder;

public class ImageCrop {

	private static final String TAG = ImageCrop.class.getName();
	
	public static Bitmap crop(Resources resources, int resId, int x, int y, int width, int height) {
		Bitmap sampledSrcBitmap = ImageDecoder.decodeResource(resources, resId, width, height);
		if(sampledSrcBitmap == null) {
			return null;
		}
		
		return cropBitmap(sampledSrcBitmap, x, y, width, height);
	}
	
	public static Bitmap crop(File original, int x, int y, int width, int height) {
		Bitmap sampledSrcBitmap = ImageDecoder.decodeFile(original, width, height);
		if(sampledSrcBitmap == null) {
			return null;
		}
		
		return cropBitmap(sampledSrcBitmap, x, y, width, height);
	}
	
	public static Bitmap crop(byte[] byteArray, int x, int y, int width, int height) {
		Bitmap sampledSrcBitmap = ImageDecoder.decodeByteArray(byteArray, width, height);
		if(sampledSrcBitmap == null) {
			return null;
		}
		
		return cropBitmap(sampledSrcBitmap, x, y, width, height);
	}
	
	private static Bitmap cropBitmap(Bitmap sampledSrcBitmap, int x, int y, int width, int height) {
		int sourceWidth = sampledSrcBitmap.getWidth();
		int sourceHeight = sampledSrcBitmap.getHeight();
		
		if(originalIsSmallerThanResult(sourceWidth, sourceHeight, width, height))
    		return sampledSrcBitmap;
		
		int newWidth, newHeight;
		int originalAspectRatio = sourceWidth / sourceHeight;
		int croppedAspectRatio = width / height;

		if (originalIsWiderThanCroppedImage(originalAspectRatio, croppedAspectRatio)) {
			newHeight = height;
			newWidth = sourceWidth / (sourceHeight / height);
		} else {
			newWidth = width;
			newHeight = sourceHeight / (sourceWidth / width);
		}
		
        Bitmap resizedBitmap = ImageResize.resize(sampledSrcBitmap, newWidth, newHeight, null);
        
        x = calculateX(x, resizedBitmap.getWidth(), width);
		y = calculateY(y, resizedBitmap.getHeight(), height);
		
        Bitmap croppedBitmap = Bitmap.createBitmap(resizedBitmap, x, y, width, height);
        resizedBitmap.recycle();
        
    	return croppedBitmap;
	}
	
	private static boolean originalIsWiderThanCroppedImage(int originalAspectRatio, int croppedAspectRatio) {
		return originalAspectRatio >= croppedAspectRatio;
	}

	private static boolean originalIsSmallerThanResult(int originalWidth, int originalHeight, int width, int height) {
		return originalWidth < width || originalHeight < height;
	}
	
	private static int calculateX(int x, int newWidth, int width) {
        if(x < 0) {
        	x = (newWidth - width) / 2;
        }
        
        return x;
	}
	
	private static int calculateY(int y, int newHeight, int height) {
        if(y < 0) {
        	y = (newHeight - height) / 2;
        }
        
        return y;
	}

}
