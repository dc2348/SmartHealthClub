package com.example.test2;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

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
import android.os.Handler;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.primitives.Bytes;

public class Butterfly extends Activity {
	public static final String TAG = "NFCScanForegroundActivity";

    public static final int REQ_CODE_PUSH = 1001;
    public static final int SHOW_PUSH_CONFIRM = 2001;

	private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    
    private TextView mText;//��ⱸ�̸�
    private TextView mTime;//��ѽð�
    private TextView mCount;//�Ƚ��
    private TextView mWeight;//�Ƚ��

	public static final int TYPE_TEXT = 1;
	public static final int TYPE_URI = 2;
	
	Calendar c;
	int Year;
	int Month;
	int Day;
	String todaydate = "";
	
	public String phoneNum="";	
	String finalTime="";
	String newMachine="";
	String tag = null;
	
	int Second=-1;
	int Minute=0;
	int Hour=0;
	
	String butterflyCount="0";
	String butterflyWeight="0";
	
	TimerTask minute;
	String result="";
	int check=0;;

	private final String SERVER_ADDRESS = "http://192.168.43.28/icebreaking/butterfly_user";
	
	private final Handler handler = new Handler();
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // NFC ����� ��ü ����
        mAdapter = NfcAdapter.getDefaultAdapter(this);

        setContentView(R.layout.butterfly);

        Log.d(TAG, "onCreate() called.");
        

        mText = (TextView) findViewById(R.id.name);
		mTime = (TextView) findViewById(R.id.time);
		mCount = (TextView) findViewById(R.id.count);
		mWeight = (TextView) findViewById(R.id.weight);
        
        if (mAdapter == null) {
        	mText.setText("����ϱ� ���� NFC�� Ȱ��ȭ�ϼ���.");
        } else {
        	mText.setText("Butterfly Machine");
        }
        
        getPhoneNum();
        Test();
        getDate();
        check=1;
        insertData();

        Intent targetIntent = new Intent(this, Butterfly.class);
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

    //�±� ���� �����ֱ�     
    private int showTag(NdefMessage mMessage) {
        List<ParsedRecord> records = NdefMessageParser.parse(mMessage);
        final int size = records.size();
        for (int i = 0; i < size; i++) {
            ParsedRecord record = records.get(i);

            int recordType = record.getType();
            String recordStr = "";
            if (recordType == ParsedRecord.TYPE_TEXT) {
            	recordStr = "��ⱸ : " + ((TextRecord) record).getText() + "\n";
            } else if (recordType == ParsedRecord.TYPE_URI) {
            	recordStr = "URI : " + ((UriRecord) record).getUri().toString() + "\n";
            }

            Log.d(TAG, "record string : " + recordStr);
            
            //���� �±׵� ī�忡 ����� ��ⱸ �̸�
            newMachine = (((TextRecord) record).getText());
            
            if(newMachine.equals("treadmill")){
            	Toast.makeText(getApplicationContext(), "���� Butterfly Machine ��� �������ּ���", Toast.LENGTH_LONG).show();
            }else if(newMachine.equals("butterfly")){
            	finalTime= Hour + ":" + Minute + ":" + Second;
            	Toast.makeText(getApplicationContext(), "��� �����մϴ�\n" + "��ѽð� : " + finalTime +"\n��������� : " + phoneNum, Toast.LENGTH_LONG).show();
            	check=0;
            	insertData();
            	Intent intent = new Intent(getApplicationContext(), NFCScanForegroundActivity.class);
            	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	startActivity(intent);
            }else if(newMachine.equals("dumbbell_5kg")){
            	Toast.makeText(getApplicationContext(), "���� Butterfly Machine ��� �������ּ���", Toast.LENGTH_LONG).show();
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
    
    private void Test() {

		minute = new TimerTask() {
			private String tag;

			@Override
			public void run() {
				Log.d(tag, Minute + "�� " + Second +"��");
				Update();
				searchData();
			}
		};
		Timer timer = new Timer();
		timer.schedule(minute, 0, 1000);
	}

	protected void Update() {
		Second++;
		if(Second==60){
			Minute++;
			Second=Second%60;
			if(Minute==60){
				Minute=Minute%60;
				Hour++;
			}
		}

		Runnable updater = new Runnable() {
			public void run() {
				mTime.setText(Hour + "�ð� "+ Minute + "�� " + Second +"��");
				mCount.setText(""+butterflyCount);
				mWeight.setText(""+butterflyWeight+" kg");
			}
		};
		handler.post(updater);
	}
	
	private String getXmlData(String filename, String str) { //�±װ� �ϳ��� �޾ƿ������� String�� �Լ�
		String rss = SERVER_ADDRESS + "/";
		String ret = "";
		
		try { //XML �Ľ��� ���� ����
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			URL server = new URL(rss + filename);
			InputStream is = server.openStream();
			xpp.setInput(is, "UTF-8");
			
			int eventType = xpp.getEventType();
			
			while(eventType != XmlPullParser.END_DOCUMENT) {
				if(eventType == XmlPullParser.START_TAG) {
					if(xpp.getName().equals(str)) { //�±� �̸��� str ���ڰ��� ���� ���
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
	
	public void getDate(){
    	c = Calendar.getInstance();
		Year = c.get(Calendar.YEAR);
		Month = c.get(Calendar.MONTH) + 1; // 1��(0), 2��(1), ..., 12��(11)
		Day = c.get(Calendar.DAY_OF_MONTH);

		String stringDayAndTimeFormat1 = String.format("%4d/%d/%d", Year, Month,Day);
		
		todaydate = stringDayAndTimeFormat1;
    }    
	
	public void insertData() {
	
		runOnUiThread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				String phonenum = phoneNum;
				String time = finalTime;
				String date = todaydate;
				
					try {
						if(check==1){
						URL url = new URL(SERVER_ADDRESS + "/insert.php?"
								+ "phonenum=" + URLEncoder.encode(phonenum, "UTF-8")
								+ "&date=" + URLEncoder.encode(date, "UTF-8")); //�������� UTF-8�� ���ڵ��ϱ� ���� URLEncoder�� �̿��Ͽ� ���ڵ���
						url.openStream(); //������ DB�� �Է��ϱ� ���� �������� insert.php���Ͽ� �Էµ� �̸��� ������ �ѱ�
						
						result = getXmlData("insertresult.xml", "result"); //�Է� ��������
					}else if(check==0){
						URL url = new URL(SERVER_ADDRESS + "/update.php?"
								+ "phonenum=" + URLEncoder.encode(phonenum, "UTF-8")
								+ "&time=" + URLEncoder.encode(time, "UTF-8")
								+ "&date=" + URLEncoder.encode(date, "UTF-8")); //�������� UTF-8�� ���ڵ��ϱ� ���� URLEncoder�� �̿��Ͽ� ���ڵ���
						url.openStream(); //������ DB�� �Է��ϱ� ���� �������� insert.php���Ͽ� �Էµ� �̸��� ������ �ѱ�
						
						result = getXmlData("updateresult.xml", "result"); //�Է� ��������
					}
					
					if(result.equals("1")) { //result �±װ��� 1�϶� ����
						Toast.makeText(Butterfly.this,
								"DB insert ����", Toast.LENGTH_SHORT).show();
						
					}
					else //result �±װ��� 1�� �ƴҶ� ����
						Toast.makeText(Butterfly.this,
								"DB insert ����", Toast.LENGTH_SHORT).show();
				} catch(Exception e) {
					Log.e("Error", e.getMessage());
				}
			}
		});
	}
	
	public void searchData(){

		// TODO Auto-generated method stub
		try {
			URL url = new URL(SERVER_ADDRESS + "/search.php");
			url.openStream(); // ������ serarch.php������ ������

			String counting = getXmlData("searchresult.xml","counting");// name �±װ��� �о� namelist ����Ʈ�� ����
			String weight = getXmlData("searchresult.xml", "weight");

			butterflyCount = "" + counting;
			butterflyWeight = "" + weight;

		} catch (Exception e) {
			Log.e("Error", e.getMessage());

		}
	}
}

