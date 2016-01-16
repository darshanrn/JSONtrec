package com.dash.supportlibs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

public class ImageCache {
	private static final String TAG = "ImageCache";
	static int memClass = 0;	
    private static LruCache<String, Bitmap> cache = null;
    private long size=0;/*current allocated size*/
    private static long limit=1000000;/*max memory in bytes*/
    private static ImageCache instance;

    public static ImageCache getInstance(int memoryclass){
    	if(instance == null){
    		/*use only 1/4th of available heap size*/
            setLimit(Runtime.getRuntime().maxMemory()/4);
            memClass = memoryclass;
            final int cacheSize = 800 * 800 * memClass / 8;
            cache = new LruCache<String, Bitmap>(cacheSize);
            instance = new ImageCache();
    	}
    	return instance;
    }
    
    public static void setLimit(long new_limit){
        limit = new_limit;
        Log.i(TAG, "ImageCache will use up to "+limit/800./800.+"MB"); /*most of the image sizes used in our app is of 800X800*/
    }

    public Bitmap get(String id){
        try{            
            return cache.get(id);
        }catch(NullPointerException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public void put(String id, Bitmap bitmap){        
    	cache.put(id, bitmap);
    	size += getSizeInBytes(bitmap);
    }
    
    public void clear() {
        try{
            size = 0;
        }catch(NullPointerException ex){
            ex.printStackTrace();
        }
    }

    long getSizeInBytes(Bitmap bitmap) {
        if(bitmap==null){
            return 0;
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
