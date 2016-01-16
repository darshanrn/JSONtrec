package com.dash.jsontrec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dash.supportlibs.IAsyncResponse;

public class MainActivity extends Activity implements IAsyncResponse{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*
		 * call REST API and get the JSON response
		 */
		
		/*To do - get the url from config */ 
		String url = "https://gist.githubusercontent.com/maclir/f715d78b49c3b4b3b77f/raw/8854ab2fe4cbe2a5919cea97d71b714ae5a4838d/items.json";
		RetrieveJSONcontents asyncGet = new RetrieveJSONcontents();
		asyncGet.delegate = this;		
		asyncGet.execute(url);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void processFinish(String output) {
		final List<Item> items = new ArrayList<Item>();
		JSONArray jsonarray = null;
		
		try {
			jsonarray = new JSONArray(output);
		} catch (JSONException ex) {
			System.out.print(ex.getMessage());
		}
		
		for(int i=0; i < jsonarray.length() ; i++) {
		    JSONObject json_data;
			try {
				json_data = jsonarray.getJSONObject(i);
				String title = json_data.getString("title");
				String description = json_data.getString("description");
				String imageURL = json_data.getString("image");
				items.add(new Item(title, description, imageURL));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1, items) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text1 = (TextView) view.findViewById(android.R.id.text1);
				TextView text2 = (TextView) view.findViewById(android.R.id.text2);
				
				text1.setTextColor(Color.BLUE);
				text2.setTextColor(Color.BLACK);
				
				/*Ellipsizing the description*/
				text2.setEllipsize(TruncateAt.END);
				text2.setMaxLines(1); 
				
				text1.setText(items.get(position).getTitle());
				text2.setText(items.get(position).getSubtitle());
				
				return view;
			}
		};
		ListView lv = (ListView)findViewById(R.id.list);
		lv.setAdapter(adapter);
		
		/* handle the row item click event */
		lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
                Intent intent = new Intent(getApplicationContext(), DetailedActivity.class);
                intent.putExtra("title", items.get(position).getTitle());
                intent.putExtra("subtitle", items.get(position).getSubtitle());
                intent.putExtra("imageURL", items.get(position).getImageURL());
                startActivity(intent);
            }
        });
	}
	
	class RetrieveJSONcontents extends AsyncTask<String, Void, String>
	{
		public IAsyncResponse delegate = null;
		ProgressDialog asyncDialog = new ProgressDialog(MainActivity.this);
		
		@Override
        protected void onPreExecute() {
			/* show processing dialog */
            asyncDialog.setMessage(getString(R.string.loadingMessage));
            asyncDialog.show();
            super.onPreExecute();
        }

		protected String doInBackground(String... urls) {
			BufferedReader reader = null;
		    StringBuffer buffer = null;
		    try {
		    	URL url = new URL(urls[0]);
		        reader = new BufferedReader(new InputStreamReader(url.openStream()));
		        buffer = new StringBuffer();
		        int read;
		        char[] chars = new char[1024];
		        while ((read = reader.read(chars)) != -1)
		            buffer.append(chars, 0, read); 		        
		    } catch (Exception e) {
				e.printStackTrace();
			} finally {
		        if (reader != null)
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		    }
		    return buffer.toString();
		}
		
		 protected void onPostExecute(String jsondata) {
			 /*hide the processing dialog */
			 asyncDialog.hide();
			 delegate.processFinish(jsondata);
		 }
	}
}
