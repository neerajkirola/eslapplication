package com.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.util.Log;

public class JsonParser {
	
	 static JSONObject jObj = null;
/*	public JSONObject getJSONFromUrl(String url,List<NameValuePair> data) {
		// TODO Auto-generated method stub
		Log.d("test", "test1");
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 50000);
		HttpConnectionParams.setSoTimeout(httpParameters, 50000);
		HttpConnectionParams.setTcpNoDelay(httpParameters, true);
		HttpClient httpClient=new DefaultHttpClient(httpParameters);
		Log.d("test", "test2");
		HttpHost httpHost = new HttpHost("dotsoftech.com", 80); 
		HttpPost post=new HttpPost(url);
		
		 
	             try {
	            	 Log.d("test", "test3");
					post.setEntity(new UrlEncodedFormEntity(data));
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
	             Log.d("test", "test4");
		post.addHeader("Content-Type", "application/json; charset=utf-8");
																																
		
		HttpResponse response;
		
		try {
			String result=null;
			response = httpClient.execute(httpHost,post);
			Log.d("test", "test5");
			System.out.println("response:"+response);
			//String responseData = EntityUtils.toString(response.getEntity(), "utf-8");
			//System.out.println("responseData:"+responseData);
			
			
			HttpEntity entity = response.getEntity();
			System.out.println("entity:"+entity);
//Toast.makeText(getApplicationContext(), "entity"+entity, Toast.LENGTH_LONG).show();			
			if(entity!=null)
			{
				InputStream instream = entity.getContent();
				BufferedReader reader = new BufferedReader(
				         new InputStreamReader(instream ,"utf-8"));
				    StringBuilder sb = new StringBuilder();


				    String line = null;
				    while ((line = reader.readLine()) != null)
				        sb.append(line + "\n");
				    result = sb.toString();
				    Log.d("test", "result:"+result);
				    jObj = new JSONObject(result);
			}
		
		
	}catch(Exception e)
	{
		
		e.printStackTrace();
		
	}
		return jObj;
	
	
	
	}
	
	*/
	
	
	public  static JSONObject callWebService(String url,JSONStringer data) {

		String result;
        try {

               // make web service connection
               HttpPost request = new HttpPost(url);
               request.setHeader("Accept", "application/json");
               request.setHeader("Content-type", "application/json");
               // Build JSON string
               JSONStringer TestApp=data;
               StringEntity entity = new StringEntity(TestApp.toString());

               Log.d("****Parameter Input****", "Testing:" + TestApp);
               request.setEntity(entity);
               // Send request to WCF service
               DefaultHttpClient httpClient = new DefaultHttpClient();
               HttpResponse response = httpClient.execute(request);

               Log.d("WebInvoke", "Saving: " + response.getStatusLine().toString());
               // Get the status of web service
               BufferedReader reader = new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));
               StringBuilder sb = new StringBuilder();


			    String line = null;
			    while ((line = reader.readLine()) != null)
			        sb.append(line + "\n");
			    result = sb.toString();
			    Log.d("test", "result:"+result);
			    jObj = new JSONObject(result);

        } catch (Exception e) {
               e.printStackTrace();
        }
return jObj;
 }


//Read more: http://www.androidhub4you.com/2012/09/consuming-rest-web-services-in-android.html#ixzz3CEbXHqPr

}
