package com.example.test2;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SubPage03Activity extends Activity {
	
	private final String SERVER_ADDRESS = "http://192.168.43.28/icebreaking/treadmill_tab";
	
	LinearLayout mainLayout;
    Resources res;
    Animation growAnim;
    String phoneNum="";
    
    int getTime1=0;
    int getTime2=0;
    int getTime3=0;
    int getTime4=0;
    int getTime5=0;
    int getTime6=0;
    int getTime7=0;
    
    int getSpeed1=0;
    int getSpeed2=0;
    int getSpeed3=0;
    int getSpeed4=0;
    int getSpeed5=0;
    int getSpeed6=0;
    int getSpeed7=0;
    
    //int getCalorie=0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subpage03);
        
        res = getResources();
        growAnim = AnimationUtils.loadAnimation(this, R.anim.grow);
        mainLayout = (LinearLayout)findViewById(R.id.sub03Layout);

        getPhoneNum();
        searchData();
    }
    
    private void addItem(String name, int value) {

        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.WRAP_CONTENT,
        		LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.WRAP_CONTENT,
        		LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.WRAP_CONTENT,
        		LinearLayout.LayoutParams.WRAP_CONTENT);

        // 텍스트뷰 추가
        TextView textView = new TextView(this);
        textView.setText(name);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        params.width = 290;
        params.setMargins(20, 20, 20, 20);
        itemLayout.addView(textView, params);

        // 프로그레스바 추가
        ProgressBar proBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        proBar.setIndeterminate(false);
        proBar.setMax(100);
        proBar.setProgress(100);
        proBar.setAnimation(growAnim);
        params2.height = 100;
        params2.width = value * 3;
        params2.gravity = Gravity.LEFT;
        itemLayout.addView(proBar, params2);

        mainLayout.addView(itemLayout, params3);

    }
    
public void searchData(){
		
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
				
							String time1 = getXmlData("searchresult.xml", "time1");
							String speed1 = getXmlData("searchresult.xml", "speed1"); 
							String time2 = getXmlData("searchresult.xml", "time2");
							String speed2 = getXmlData("searchresult.xml", "speed2"); 
							String time3 = getXmlData("searchresult.xml", "time3");
							String speed3 = getXmlData("searchresult.xml", "speed3"); 
							String time4 = getXmlData("searchresult.xml", "time4");
							String speed4 = getXmlData("searchresult.xml", "speed4"); 
							String time5 = getXmlData("searchresult.xml", "time5");
							String speed5 = getXmlData("searchresult.xml", "speed5"); 
							String time6 = getXmlData("searchresult.xml", "time6");
							String speed6 = getXmlData("searchresult.xml", "speed6"); 
							String time7 = getXmlData("searchresult.xml", "time7");
							String speed7 = getXmlData("searchresult.xml", "speed7"); 
							//String calorie = getXmlData("searchresult.xml", "calorie");							
							
							String[] data1 = time1.split(":"); 
							String[] data2 = time2.split(":"); 
							String[] data3 = time3.split(":"); 
							String[] data4 = time4.split(":"); 
							String[] data5 = time5.split(":"); 
							String[] data6 = time6.split(":"); 
							String[] data7 = time7.split(":"); 
				    		
							getTime1 = Integer.parseInt(data1[0])*60+Integer.parseInt(data1[1])+Integer.parseInt(data1[2])/60;
							getSpeed1 = Integer.parseInt(speed1);
							getTime2 = Integer.parseInt(data2[0])*60+Integer.parseInt(data2[1])+Integer.parseInt(data2[2])/60;
							getSpeed2 = Integer.parseInt(speed1);
							getTime3 = Integer.parseInt(data3[0])*60+Integer.parseInt(data3[1])+Integer.parseInt(data3[2])/60;
							getSpeed3 = Integer.parseInt(speed1);
							getTime4 = Integer.parseInt(data4[0])*60+Integer.parseInt(data4[1])+Integer.parseInt(data4[2])/60;
							getSpeed4 = Integer.parseInt(speed1);
							getTime5 = Integer.parseInt(data5[0])*60+Integer.parseInt(data5[1])+Integer.parseInt(data5[2])/60;
							getSpeed5 = Integer.parseInt(speed1);
							getTime6 = Integer.parseInt(data6[0])*60+Integer.parseInt(data6[1])+Integer.parseInt(data6[2])/60;
							getSpeed6 = Integer.parseInt(speed1);
							getTime7 = Integer.parseInt(data7[0])*60+Integer.parseInt(data7[1])+Integer.parseInt(data7[2])/60;
							getSpeed7 = Integer.parseInt(speed1);
							//getCalorie = Integer.parseInt(calorie);
							
							// 아이템 추가
					        addItem("2013/12/12  "+speed1+"m/s", getTime1);
					        addItem("2013/12/13  "+speed2+"m/s", getTime2);
					        addItem("2013/12/14  "+speed3+"m/s", getTime3);
					        addItem("2013/12/15  "+speed4+"m/s", getTime4);
					        addItem("2013/12/16  "+speed5+"m/s", getTime5);
					        addItem("2013/12/17  "+speed6+"m/s", getTime6);
					        addItem("2013/12/18  "+speed7+"m/s", getTime7);
					        
					        //addItem("calorie", getCalorie);
							
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