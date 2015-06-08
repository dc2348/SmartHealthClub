package com.example.test2;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class secondActivity extends Activity {
	private final String SERVER_ADDRESS = "http://192.168.43.28/icebreaking/goal"; //서버 주소(php파일이 저장되어있는 경로까지, 절대로 127.0.0.1이나 localhost를 쓰면 안된다!!)
	
	TextView treadmillTime;
	TextView treadmillSpeed;
	TextView dumbbellWeight;
	TextView dumbbellCount;
	TextView butterflyWeight;
	TextView butterflyCount;
	
	String str="";
	String phoneNum="";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        
        treadmillTime = (TextView)findViewById(R.id.treadmillTime); 
        treadmillSpeed = (TextView)findViewById(R.id.treadmillSpeed);  
        dumbbellWeight = (TextView)findViewById(R.id.dumbbellWeight);  
        dumbbellCount = (TextView)findViewById(R.id.dumbbellCount);  
        butterflyWeight = (TextView)findViewById(R.id.butterflyWeight);  
        butterflyCount = (TextView)findViewById(R.id.butterflyCount);  
        
        getPhoneNum();
        searchGoal();
       
    }
    
    public void searchGoal(){
		
		// TODO Auto-generated method stub
		final Handler handler = new Handler();
		runOnUiThread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				
				handler.post(new Runnable() {
					
					public void run() {
						
						String phonenum =phoneNum;
						
						// TODO Auto-generated method stub
						try {
							URL url = new URL(SERVER_ADDRESS + "/search.php?"+ "phonenum=" + URLEncoder.encode(phonenum, "UTF-8"));
							url.openStream(); //서버의 serarch.php파일을 실행함
				
							String dweight = getXmlData("searchresult.xml", "dweight");
							String dcounting = getXmlData("searchresult.xml", "dcounting"); 
							String bweight = getXmlData("searchresult.xml", "bweight");
							String bcounting = getXmlData("searchresult.xml", "bcounting");
							String tspeed = getXmlData("searchresult.xml", "tspeed");
							String ttime = getXmlData("searchresult.xml", "ttime");
							
							
							treadmillTime.setText(ttime); 
					        treadmillSpeed.setText(tspeed); 
					        dumbbellWeight.setText(dweight);  
					        dumbbellCount.setText(dcounting);
					        butterflyWeight.setText(bweight);
					        butterflyCount.setText(bcounting);
								
							
						} catch(Exception e) {
							Log.e("Error", e.getMessage());
						}
					}
				});
			}
		});
		
	}
    
	private String getXmlData(String filename, String str) { //태그값 하나를 받아오기위한 String형 함수
		String rss = SERVER_ADDRESS + "/";
		String ret = "";
		
		try { //XML 파싱을 위한 과정
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			URL server = new URL(rss + filename);
			InputStream is = server.openStream();
			xpp.setInput(is, "UTF-8");
			
			int eventType = xpp.getEventType();
			
			while(eventType != XmlPullParser.END_DOCUMENT) {
				if(eventType == XmlPullParser.START_TAG) {
					if(xpp.getName().equals(str)) { //태그 이름이 str 인자값과 같은 경우
						ret = xpp.nextText();
					}
				}
				eventType = xpp.next();
			}
		} catch(Exception e) {
			Log.e("Error", e.getMessage());
		}
		
		return ret;
	}
	
	public void getPhoneNum() {
    	TelephonyManager mng = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		phoneNum = mng.getLine1Number();		
    }
    
}