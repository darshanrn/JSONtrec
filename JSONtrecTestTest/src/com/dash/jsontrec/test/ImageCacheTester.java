/**
 * 
 */
package com.dash.jsontrec.test;

import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Arrays;

import junit.framework.TestCase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dash.supportlibs.ImageCache;

/**
 * @author darshanrn
 *
 */
public class ImageCacheTester extends TestCase {

	public void testSingleTonInstance(){
		ImageCache instance1 = ImageCache.getInstance(123);
		ImageCache instance2 = ImageCache.getInstance(123);
		assertTrue("Singleton instance should be same", instance1 == instance2);
	}

	public void testImageCachingStoreAndRetrievePositiveScenario(){
		ImageCache instance = ImageCache.getInstance(123);
		 Bitmap bitmap1 = null, bitmap2 = null;
  		
    	 try {
               bitmap1 = BitmapFactory.decodeStream((InputStream)new URL("http://dummyimage.com/383x512/CA7043/0405C8").getContent()); 
        } catch (Exception ex) {
        	
        }
		instance.put("1111", bitmap1);
		bitmap2 = instance.get("1111");
		
		ByteBuffer buffer1 = ByteBuffer.allocate(bitmap1.getHeight() * bitmap1.getRowBytes());
	    bitmap1.copyPixelsToBuffer(buffer1);

	    ByteBuffer buffer2 = ByteBuffer.allocate(bitmap2.getHeight() * bitmap2.getRowBytes());
	    bitmap2.copyPixelsToBuffer(buffer2);
	    
		assertTrue("Image Cache was able to retireve the same image that was cached", Arrays.equals(buffer1.array(), buffer2.array()));
		
	}
	
	public void testImageCachingStoreAndRetrieveNegativeScenario(){
		ImageCache instance = ImageCache.getInstance(123);
		 Bitmap bitmap1 = null, bitmap2 = null;
  		
    	 try {
               bitmap1 = BitmapFactory.decodeStream((InputStream)new URL("http://dummyimage.com/383x512/CA7043/0405C8").getContent()); 
        } catch (Exception ex) {
        	
        }
		instance.put("1111", bitmap1);
		bitmap2 = instance.get("2222"); /* Try getting a bitmap for a different key */
		assertTrue("Image cache was NOT able to retireve an image that did not exist on the cache", bitmap2 == null);		
	}
}
