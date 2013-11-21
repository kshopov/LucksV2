package lucks.com;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import lucks.com.data.DataManagerImpl;
import lucks.com.model.Luck;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class LucksApp extends Application {
	
	private ConnectivityManager cMgr;
	private DataManagerImpl dataManager;
	
	public DataManagerImpl getDataManager() {
	      return this.dataManager;
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		this.cMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		this.dataManager = new DataManagerImpl(this);
	}
	
	 @Override
	 public void onTerminate() {
		 // not guaranteed to be called
	     super.onTerminate();
	 }
	
	public boolean isOnline(){
	    NetworkInfo networkInfo = cMgr.getActiveNetworkInfo();
	    
	    return (networkInfo != null && networkInfo.isConnected());
	}
	
	public void sendDataToServer(Luck luck){
		try {
			HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httpPost = new HttpPost("http://10.0.2.2:8888/lucksserverv2");
		    
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
	        nameValuePairs.add(new BasicNameValuePair("id", "" + luck.getId()));
	        nameValuePairs.add(new BasicNameValuePair("languageId", "" + luck.getluckLanguageId()));
	        nameValuePairs.add(new BasicNameValuePair("luckText", luck.getLuckText()));
	        nameValuePairs.add(new BasicNameValuePair("luckAuthor", luck.getLuckAuthor()));
	        
	        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
	        
	        httpPost.setEntity(ent);
	        HttpResponse responsePost = httpclient.execute(httpPost);
	        HttpEntity resEntity = responsePost.getEntity();
	        
	        if(resEntity != null){
	        	Log.i("RESPONSE", EntityUtils.toString(resEntity));
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}