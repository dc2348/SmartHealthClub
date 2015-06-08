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

public class SubPage01Activity extends Activity {
	
	private final String SERVER_ADDRESS = "http://192.168.43.28/icebreaking/dumbbell_tab";
	
	LinearLayout mainLayout;
    Resources res;
    Animation growAnim;
    String phoneNum="";
    
    int getWeight1=0;
    int getWeight2=0;
    int getWeight3=0;
    int getWeight4=0;
    int getWeight5=0;
    int getWeight6=0;
    int getWeight7=0;
    int getCounting1=0;
    int getCounting2=0;
    int getCounting3=0;
    int getCounting4=0;
    int getCounting5=0;
    int getCounting6=0;
    int getCounting7=0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subpage01);
        
        getPhoneNum();
        searchData();
        
        res = getResources();
        growAnim = AnimationUtils.loadAnimation(this, R.anim.grow);
        mainLayout = (LinearLayout)findViewById(R.id.sub01Layout);

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
				
							String weight1 = getXmlData("searchresult.xml", "weight1"); 
							String counting1 = getXmlData("searchresult.xml", "counting1");	
							String weight2 = getXmlData("searchresult.xml", "weight2"); 
							String counting2 = getXmlData("searchresult.xml", "counting2");	
							String weight3 = getXmlData("searchresult.xml", "weight3"); 
							String counting3 = getXmlData("searchresult.xml", "counting3");	
							String weight4 = getXmlData("searchresult.xml", "weight4"); 
							String counting4 = getXmlData("searchresult.xml", "counting4");	
							String weight5 = getXmlData("searchresult.xml", "weight5"); 
							String counting5 = getXmlData("searchresult.xml", "counting5");	
							String weight6 = getXmlData("searchresult.xml", "weight6"); 
							String counting6 = getXmlData("searchresult.xml", "counting6");	
							String weight7 = getXmlData("searchresult.xml", "weight7"); 
							String counting7 = getXmlData("searchresult.xml", "counting7");	
							
							String[] cdata1 = counting1.split("\n"); 
							String[] cdata2 = counting2.split("\n"); 
							String[] cdata3 = counting3.split("\n"); 
							String[] cdata4 = counting4.split("\n"); 
							String[] cdata5 = counting5.split("\n"); 
							String[] cdata6 = counting6.split("\n"); 
							String[] cdata7 = counting7.split("\n"); 
							
							getWeight1 = Integer.parseInt(weight1);
							getCounting1 = Integer.parseInt(cdata1[0]);
							getWeight2 = Integer.parseInt(weight2);
							getCounting2 = Integer.parseInt(cdata2[0]);
							getWeight3 = Integer.parseInt(weight3);
							getCounting3 = Integer.parseInt(cdata3[0]);
							getWeight4 = Integer.parseInt(weight4);
							getCounting4 = Integer.parseInt(cdata4[0]);
							getWeight5 = Integer.parseInt(weight5);
							getCounting5 = Integer.parseInt(cdata5[0]);
							getWeight6 = Integer.parseInt(weight6);
							getCounting6 = Integer.parseInt(cdata6[0]);
							getWeight7 = Integer.parseInt(weight7);
							getCounting7 = Integer.parseInt(cdata7[0]);
							
							// 아이템 추가
					        addItem("2013/12/12  "+weight1+"kg", getCounting1);
					        addItem("2013/12/13  "+weight2+"kg", getCounting2);
					        addItem("2013/12/14  "+weight3+"kg", getCounting3);
					        addItem("2013/12/15  "+weight4+"kg", getCounting4);
					        addItem("2013/12/16  "+weight5+"kg", getCounting5);
					        addItem("2013/12/17  "+weight6+"kg", getCounting6);
					        addItem("2013/12/18  "+weight7+"kg", getCounting7);
							
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