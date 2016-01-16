package com.dash.jsontrec;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.dash.supportlibs.ImageCache;

public class DetailedActivity extends Activity {
	ImageCache imgCache;
	String imageURL = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailed);
		
		Intent intent = getIntent();
		String title = intent.getStringExtra("title");
		String subtitle = intent.getStringExtra("subtitle");
		imageURL = intent.getStringExtra("imageURL");
		
		TextView titleTxt = (TextView)findViewById(R.id.title);
		TextView subtitleTxt = (TextView)findViewById(R.id.subtitle);
		
		
		titleTxt.setText(title);
		subtitleTxt.setText(subtitle);
				
		Shader shader = new LinearGradient(
				0, 0, 0, titleTxt.getTextSize(),
				Color.RED, Color.BLUE,
				Shader.TileMode.CLAMP);
		titleTxt.getPaint().setShader(shader);
		titleTxt.setTextSize(30f);
		subtitleTxt.setTextSize(20f);
		titleTxt.setPadding(10, 10, 10, 10);
		subtitleTxt.setPadding(10, 10, 10, 10);
		
		final int memClass = ((ActivityManager)getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		imgCache = ImageCache.getInstance(memClass);
		Bitmap bitmap = imgCache.get(imageURL);
        if(bitmap!=null){
        	ImageView imageView = (ImageView)findViewById(R.id.image);
            imageView.setImageBitmap(bitmap);
        }
        else
        {
			/*load image asynchronously */
			new LoadImage().execute(imageURL);
        }
	}
	
	private class LoadImage extends AsyncTask<String, String, Bitmap> {
		ProgressDialog pDialog;
		
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailedActivity.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();
 
        }
         protected Bitmap doInBackground(String... args) {
        	 Bitmap bitmap = null;
     		
        	 try {
                   bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent()); 
            } catch (Throwable ex) {
                  ex.printStackTrace();
                  if(ex instanceof OutOfMemoryError){
                      imgCache.clear();
                  }
            }
            return bitmap;
         }
 
         protected void onPostExecute(Bitmap image) {
        	 pDialog.dismiss();
        	 ImageView imageView = (ImageView)findViewById(R.id.image);
        	 if(image != null){
        		 imageView.setImageBitmap(image);
        		 imgCache.put(imageURL, image);
        	 }else{
        		 Log.e("ERROR", "Image Does Not exist or Network Error");
        	 }
         }
     }
}
