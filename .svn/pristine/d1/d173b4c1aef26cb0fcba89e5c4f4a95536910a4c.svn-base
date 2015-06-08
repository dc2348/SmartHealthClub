package com.example.test2;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import com.google.common.base.Charsets;
import com.google.common.primitives.Bytes;

public class NFCScanForegroundActivity extends Activity {
	public static final String TAG = "NFCScanForegroundActivity";

    public static final int REQ_CODE_PUSH = 1001;
    public static final int SHOW_PUSH_CONFIRM = 2001;

	private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private TextView mText;

    private TextView broadcastBtn;

	public static final int TYPE_TEXT = 1;
	public static final int TYPE_URI = 2;
	
	public String phoneNum="";	
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
	int machineCount=0;
	String oldMachine="";
	String newMachine="";

	int oldHour;
	int oldMinute;
	int newHour;
	int newMinute;
	String timeChange="";
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // NFC 어댑터 객체 참조
        mAdapter = NfcAdapter.getDefaultAdapter(this);

        setContentView(R.layout.scan);

        Log.d(TAG, "onCreate() called.");


        mText = (TextView) findViewById(R.id.text);
        if (mAdapter == null) {
        	mText.setText("사용하기 전에 NFC를 활성화하세요.");
        } else {
        	mText.setText("운동기구에 부착된 NFC에 스마트폰을 태그해 주세요");
        }

        Intent targetIntent = new Intent(this, NFCScanForegroundActivity.class);
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
        mText.append("\n");
        for (int i = 0; i < size; i++) {
            ParsedRecord record = records.get(i);

            int recordType = record.getType();
            String recordStr = "";
            if (recordType == ParsedRecord.TYPE_TEXT) {
            	recordStr = "운동기구 : " + ((TextRecord) record).getText() + "\n";
            } else if (recordType == ParsedRecord.TYPE_URI) {
            	recordStr = "URI : " + ((UriRecord) record).getUri().toString() + "\n";
            }

            Log.d(TAG, "record string : " + recordStr);
            
            //지금 태그된 카드에 저장된 운동기구 이름
            newMachine = (((TextRecord) record).getText());
            
            if(newMachine.equals("treadmill")){
            	Intent intent = new Intent(getApplicationContext(), Treadmill.class);
	            startActivity(intent);
            }else if(newMachine.equals("butterfly")){
            	Intent intent = new Intent(getApplicationContext(), Butterfly.class);
	            startActivity(intent);
            }else if(newMachine.equals("dumbbell_5kg")){
            	Intent intent = new Intent(getApplicationContext(), Dumbbell.class);
	            startActivity(intent);
            }
            	
            		
            /*
            if(newMachine.equals("treadmill")||newMachine.equals("butterfly")
            		||newMachine.equals("dumbbell_5kg")){
            
            if((newMachine.equals(oldMachine)==true) && (machineCount==1)){
            	newHour=Hour;
            	newMinute = Minute;
            	if(oldMinute<=newMinute)
            		timeChange="운동시간 : "+ (newHour-oldHour)+"시간 "+(newMinute -oldMinute)+"분";
            	else{
            		timeChange="운동시간 : "+ (newHour-oldHour-1)+"시간 "+(60-oldMinute+newMinute)+"분";
            	}
            	Toast.makeText(getApplicationContext(), "종료", Toast.LENGTH_LONG).show();
            	machineCount=0;
            }else{
            	oldHour = Hour;
            	oldMinute = Minute;
            	timeChange="";
            	Toast.makeText(getApplicationContext(), "시작", Toast.LENGTH_LONG).show();
            	machineCount=1;
            	oldMachine=newMachine;
            }
            }
            

            mText.setText(recordStr);
            mText.append("전화번호 : "+ phoneNum);
            mText.append("\n");
            mText.append("시간 : " + time);
            mText.append("\n");
            mText.append(timeChange);
            mText.invalidate();
            */
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
		String stringDayAndTimeFormat = String.format("%4d년 %d월 %d일 "
				+ stringDayOfWeek[DayOfWeek] + "요일 \n"
				+ stringAmPm[AmPm] + " %d시 %02d분 %02d초", Year, Month,
				Day, Hour, Minute, Second);
		time = stringDayAndTimeFormat;		
    }    
}

