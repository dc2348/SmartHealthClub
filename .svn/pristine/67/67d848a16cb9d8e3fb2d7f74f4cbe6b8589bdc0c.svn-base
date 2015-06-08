package com.example.test2;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.google.common.base.Charsets;
import com.google.common.primitives.Bytes;

import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.widget.TabHost;


public class MainActivity extends Activity {
	
	public static final String TAG = "MainActivity";

    public static final int REQ_CODE_PUSH = 1001;
    public static final int SHOW_PUSH_CONFIRM = 2001;

	private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private String mText;

	public static final int TYPE_TEXT = 1;
	public static final int TYPE_URI = 2;
	
	public String phoneNum = "";	
	String todaydate = "";
	String time = "";
	
	Calendar c;
	int Year;
	int Month;
	int Day;
	int DayOfWeek;
	int Hour;
	int Minute;
	int Second;
	int AmPm;
	
	String newMachine = "";
	int check = 0;//출입하면1, 퇴장하면0
	
	private final String SERVER_ADDRESS = "http://192.168.43.28/icebreaking/time";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate() called.");


        
        if (mAdapter == null) {
        	mText="사용하기 전에 NFC를 활성화하세요.";
        } else {
        	mText="출입문에 부착된 NFC에 스마트폰을 태그해 주세요";
        }

        Intent targetIntent = new Intent(this, MainActivity.class);
        targetIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = PendingIntent.getActivity(this, 0, targetIntent, 0);


        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }

        mFilters = new IntentFilter[] {
                ndef,
        };

        mTechLists = new String[][] { new String[] { NfcF.class.getName() } };


        Intent passedIntent = getIntent();
        if (passedIntent != null) {
        	String action = passedIntent.getAction();
            if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            	processTag(passedIntent);
            }
        }

        Button StartNowButton = (Button)findViewById(R.id.button1);
        Button VideoButton = (Button)findViewById(R.id.button2);
        Button TodayExpButton = (Button)findViewById(R.id.button3);
        Button TodayAimButton = (Button)findViewById(R.id.button4);
        Button PastMyButton = (Button)findViewById(R.id.button5);
        
        StartNowButton.setOnClickListener(new OnClickListener() {

    		public void onClick(View v) {

    			Intent intent = new Intent(MainActivity.this,NFCScanForegroundActivity.class);
    			startActivity(intent);
    		}
    	});
        
        VideoButton.setOnClickListener(new OnClickListener() {

    		public void onClick(View v) {

    			Intent intent = new Intent(MainActivity.this,videoPlayerActivity.class);
    			startActivity(intent);
    		}
    	});

        TodayExpButton.setOnClickListener(new OnClickListener() {

    		public void onClick(View v) {

    			Intent intent = new Intent(MainActivity.this,secondActivity.class);
    			startActivity(intent);
    		}
    	});
	
        TodayAimButton.setOnClickListener(new OnClickListener() {

 		public void onClick(View v) {

 			Intent intent = new Intent(MainActivity.this,thirdActivity.class);
 			startActivity(intent);
 		}
 	});
	
        PastMyButton.setOnClickListener(new OnClickListener() {
        	
 		public void onClick(View v) {

 	 			Intent intent = new Intent(MainActivity.this,PastActivity.class);
 	 			startActivity(intent);
 		}
	});
        
	}
	
	public void onResume() {
        super.onResume();

        if (mAdapter != null) {
        	mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        }
    }

    public void onPause() {
        super.onPause();

        if (mAdapter != null) {
        	mAdapter.disableForegroundDispatch(this);
        }
    }

    private void processTag(Intent passedIntent) {
    	
    	getPhoneNum();
    	getTime();
    	
    	//Toast.makeText(getApplicationContext(), "NFC tag is detected.", Toast.LENGTH_LONG).show();
    	Log.d(TAG, "processTag() called.");

        Parcelable[] rawMsgs = passedIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs == null) {
        	Log.d(TAG, "NDEF is null.");
        	return;
        }

        NdefMessage[] msgs;
        if (rawMsgs != null) {
            msgs = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                msgs[i] = (NdefMessage) rawMsgs[i];
                showTag(msgs[i]);
            }
        }   
    }


    //태그 정보 보여주기     
    private int showTag(NdefMessage mMessage) {
        List<ParsedRecord> records = NdefMessageParser.parse(mMessage);
        final int size = records.size();
        mText="\n";
        for (int i = 0; i < size; i++) {
            ParsedRecord record = records.get(i);

            int recordType = record.getType();
            String recordStr = "";
            if (recordType == ParsedRecord.TYPE_TEXT) {
            	recordStr = "Text : " + ((TextRecord) record).getText() + "\n";
            } else if (recordType == ParsedRecord.TYPE_URI) {
            	recordStr = "URI : " + ((UriRecord) record).getUri().toString() + "\n";
            }

            Log.d(TAG, "record string : " + recordStr);
            
            //지금 태그된 카드에 저장된 운동기구 이름
            newMachine = (((TextRecord) record).getText());
            
            if(newMachine.equals("entrance")&&check==0){
            	Toast.makeText(getApplicationContext(), "헬스장 입장\n"+todaydate +time+"" +
            			"\n사용자정보 : " + phoneNum, Toast.LENGTH_LONG).show();
            	check=1;
            	insertData();
            }else if(newMachine.equals("exit")&&check==1){
            	Toast.makeText(getApplicationContext(), "헬스장 퇴장\n"+time+"" +
            			"\n사용자정보 : " + phoneNum, Toast.LENGTH_LONG).show();
            	check=0;
            	insertData();
            }else{
            	Toast.makeText(getApplicationContext(), "이미 처리되었습니다", Toast.LENGTH_LONG).show();
            }
        }

        return size;
    }

    public void onNewIntent(Intent passedIntent) {
    	Log.d(TAG, "onNewIntent() called.");

    	if (passedIntent != null) {
        	processTag(passedIntent);
        }
    }

    private NdefMessage createTagMessage(String msg, int type) {
    	NdefRecord[] records = new NdefRecord[1];

    	if (type == TYPE_TEXT) {
    		records[0] = createTextRecord(msg, Locale.KOREAN, true);
    	} else if (type == TYPE_URI){
    		records[0] = createUriRecord(msg.getBytes());
    	}

    	NdefMessage mMessage = new NdefMessage(records);

    	return mMessage;
    }

    private NdefRecord createTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        final byte[] langBytes = locale.getLanguage().getBytes(Charsets.US_ASCII);
        final Charset utfEncoding = encodeInUtf8 ? Charsets.UTF_8 : Charset.forName("UTF-16");
        final byte[] textBytes = text.getBytes(utfEncoding);
        final int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        final char status = (char) (utfBit + langBytes.length);
        final byte[] data = Bytes.concat(new byte[] {(byte) status}, langBytes, textBytes);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    private NdefRecord createUriRecord(byte[] data) {
        return new NdefRecord(NdefRecord.TNF_ABSOLUTE_URI, NdefRecord.RTD_URI, new byte[0], data);
    }
    
    public void getPhoneNum() {
    	TelephonyManager mng = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		phoneNum = mng.getLine1Number();		
    }
    
    public void getTime(){
    	c = Calendar.getInstance();
		Year = c.get(Calendar.YEAR);
		Month = c.get(Calendar.MONTH) + 1; // 1월(0), 2월(1), ..., 12월(11)
		Day = c.get(Calendar.DAY_OF_MONTH);
		DayOfWeek = c.get(Calendar.DAY_OF_WEEK); // 일요일(1), 월요일(2), ..., 토요일(7)
		Hour = c.get(Calendar.HOUR_OF_DAY); // HOUR는 12시간, HOUR_OF_DAY는 24시간
		Minute = c.get(Calendar.MINUTE);
		Second = c.get(Calendar.SECOND);
		AmPm = c.get(Calendar.AM_PM); // AM(0), PM(1)

		String stringDayOfWeek[] = { "", "일", "월", "화", "수", "목", "금", "토" };
		// 요일은 1부터 시작이므로 stringDayOfWeek[0]은 해당 요일 없음

		String stringAmPm[] = { "오전", "오후" };
		String stringDayAndTimeFormat1 = String.format("%4d년 %d월 %d일 "
				+ stringDayOfWeek[DayOfWeek] + "요일 \n", Year, Month,Day);
		String stringDayAndTimeFormat2 = String.format(stringAmPm[AmPm] + " %d시 %02d분 %02d초", Hour, Minute, Second);
		
		todaydate = stringDayAndTimeFormat1;
		time = stringDayAndTimeFormat2;
    }    
    
	public void insertData() {
	
		runOnUiThread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				String phonenum = phoneNum;
				String entrancetime = time;
				String date = todaydate;
				String exittime = time;
				String result = "";
				
				try {
					if(check==1){
						URL url = new URL(SERVER_ADDRESS + "/insert.php?"
								+ "phonenum=" + URLEncoder.encode(phonenum, "UTF-8")
								+ "&entrancetime=" + URLEncoder.encode(entrancetime, "UTF-8")
								+ "&date=" + URLEncoder.encode(date, "UTF-8")); //변수값을 UTF-8로 인코딩하기 위해 URLEncoder를 이용하여 인코딩함
						url.openStream(); //서버의 DB에 입력하기 위해 웹서버의 insert.php파일에 입력된 이름과 가격을 넘김
						
						result = getXmlData("insertresult.xml", "result"); //입력 성공여부
					}else if(check==0){
						URL url = new URL(SERVER_ADDRESS + "/update.php?"
								+ "phonenum=" + URLEncoder.encode(phonenum, "UTF-8")
								+ "&exittime=" + URLEncoder.encode(exittime, "UTF-8")); //변수값을 UTF-8로 인코딩하기 위해 URLEncoder를 이용하여 인코딩함
						url.openStream(); //서버의 DB에 입력하기 위해 웹서버의 insert.php파일에 입력된 이름과 가격을 넘김
						
						result = getXmlData("updateresult.xml", "result"); //입력 성공여부
					}
					
					if(result.equals("1")) { //result 태그값이 1일때 성공
						Toast.makeText(MainActivity.this,
								"DB insert 성공", Toast.LENGTH_SHORT).show();
						
					}
					else //result 태그값이 1이 아닐때 실패
						Toast.makeText(MainActivity.this,
								"DB insert 실패", Toast.LENGTH_SHORT).show();
				} catch(Exception e) {
					Log.e("Error", e.getMessage());
				}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}