package com.example.test2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.primitives.Bytes;

public class Treadmill extends BT_Preference implements TextToSpeech.OnInitListener {
	
	private TextToSpeech tts;

	BluetoothAdapter mBluetoothAdapter;
	BluetoothSocket mmSocket;
	BluetoothDevice mmDevice;
	OutputStream mmOutputStream;
	InputStream mmInputStream;

	boolean is_connected = false;
	String devicename;

	Thread workerThread;
	byte[] readBuffer;
	int readBufferPosition;
	int counter;
	volatile boolean stopWorker;

	private TextView mSpeed;//운동속도
	private TextView mText;//운동기구이름
    private TextView mTime;//운동한시간
	int treadmillSpeed=5;//초기운동속도
	
	//NFC
	public static final String TAG = "Treadmill";

    public static final int REQ_CODE_PUSH = 1001;
    public static final int SHOW_PUSH_CONFIRM = 2001;

	private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

	public static final int TYPE_TEXT = 1;
	public static final int TYPE_URI = 2;

	Calendar c;
	int Year;
	int Month;
	int Day;
	String todaydate = "";
	
	public String phoneNum="";
	String newMachine="";
	String tag = null;
	String finalTime="";
	
	int Second=-1;
	int Minute=0;
	int Hour=0;
	int treadmillCount=0;
	int treadmillcalorie=0;
	int userWeight=0;
	
	int speedCheck=0;//속도가변했는지체크
	int timerLock=1;
	
	TimerTask minute;
	
	private final String SERVER_ADDRESS = "http://192.168.43.28/icebreaking/treadmill_user";

	private final Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		
		setContentView(R.layout.treadmill);

		String strText = "MPUCAFE";
		SharedPreferences mPairedSettings;
		mPairedSettings = getSharedPreferences(BT_PREFERENCES,
				Context.MODE_PRIVATE);

		Editor editor = mPairedSettings.edit();
		editor.putString(BP_PREFERENCES_PAIRED_DEVICE, strText);
		editor.commit();

		if (!mPairedSettings.contains(BP_PREFERENCES_PAIRED_DEVICE)) {
		}
		devicename = mPairedSettings
				.getString(BP_PREFERENCES_PAIRED_DEVICE, "");

		final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		final BroadcastReceiver mReceiver = new ScreenReceiver();
		registerReceiver(mReceiver, filter);

		tts = new TextToSpeech(this, this);
		
		Log.d(TAG, "onCreate() called.");

        mText = (TextView) findViewById(R.id.name);
		mTime = (TextView) findViewById(R.id.time);
		mSpeed = (TextView) findViewById(R.id.speed);
		mSpeed.setText(Integer.toString(treadmillSpeed)+" km/h");

		if (mAdapter == null) {
        	mText.setText("사용하기 전에 NFC를 활성화하세요.");
        } else {
        	mText.setText("Treadmill");
        }
		
		searchData();
		Test();
		getDate();
		
		Intent targetIntent = new Intent(this, Treadmill.class);
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

	@Override
	public void onBackPressed() {
		try {
			String msg = "stop";
			msg += "\n";
			mmOutputStream.write(msg.getBytes());
			try {

				Thread.sleep(500);

			} catch (InterruptedException e) {
			}
			closeBT();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}

		} catch (IOException ex) {
		}

		super.onBackPressed();
	}

	public void onInit(int status) {

		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} else {
				//showToast("start");

				try {
					findBT();
					openBT();

				} catch (IOException ex) {
					showToast("fail to open connection.");
				}
			}
		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}

	private void showToast(String txt) {
		Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
	}
	
	private void showToast(int txt) {
		Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause() {

		if (ScreenReceiver.wasScreenOn) {

			try {
				String msg = "stop";
				msg += "\n";
				mmOutputStream.write(msg.getBytes());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				closeBT();

			} catch (IOException ex) {
			}
			// this is the case when onPause() is called by the system due to a
			// screen state change
			Log.e("MYAPP", "SCREEN TURNED OFF");
		} else {
			// this is when onPause() is called when the screen state has not
			// changed
		}
		super.onPause();
		
		if (mAdapter != null) {
        	mAdapter.disableForegroundDispatch(this);
        }
	}

	void findBT() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			showToast("No bluetooth adapter available");
		}
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBluetooth = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBluetooth, 0);
		}
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				if (device.getName().equals(devicename)) {
					mmDevice = device;
					break;
				}
			}
		}
		// showToast("Bluetooth Device Found");
	}

	void openBT() throws IOException {
		UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // Standard SerialPortService ID
		mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
		mmSocket.connect();
		mmOutputStream = mmSocket.getOutputStream();
		mmInputStream = mmSocket.getInputStream();

		beginListenForData();

		showToast("Bluetooth Opened");
		is_connected = true;

		String msg = "start";
		msg += "\n";
		mmOutputStream.write(msg.getBytes());
	}

	void closeBT() throws IOException {
		String msg = "stop";
		msg += "\n";
		mmOutputStream.write(msg.getBytes());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

		stopWorker = true;
		mmOutputStream.close();
		mmInputStream.close();
		mmSocket.close();
		showToast("Connection Closed");

		is_connected = false;
	}

	void beginListenForData() {
		final Handler handler = new Handler();
		final byte delimiter = 10; // This is the ASCII code for a newline character
		stopWorker = false;
		readBufferPosition = 0;
		readBuffer = new byte[1024];
		workerThread = new Thread(new Runnable() {
			public void run() {
				while (!Thread.currentThread().isInterrupted() && !stopWorker) {

					if (!is_connected) {
						continue;
					}

					try {
						int bytesAvailable = mmInputStream.available();
						if (bytesAvailable > 0) {
							byte[] packetBytes = new byte[bytesAvailable];
							mmInputStream.read(packetBytes);
							for (int i = 0; i < bytesAvailable; i++) {
								byte b = packetBytes[i];
								if (b == delimiter) {
									byte[] encodedBytes = new byte[readBufferPosition];
									System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
									
									final String data = new String(encodedBytes, "EUC-KR");
									readBufferPosition = 0;

									handler.post(new Runnable() {
										public void run() {

											String tmp = (String) mSpeed.getText();
											
											if(data.length()>3){//Down
												if(treadmillSpeed<10)
												treadmillSpeed++;
												speedCheck=0;
												timerLock=0;
											}else{
												if(treadmillSpeed>4)
												treadmillSpeed--;//Up
												speedCheck=0;
												timerLock=0;
											}
											
											mSpeed.setText(Integer.toString(treadmillSpeed)+" km/h");
											
											if (tmp.indexOf("blocked") != -1) {
												showToast("blocked");

											} else if (tmp.indexOf("cleared") != -1) {
												showToast("cleared");
											}
										}
									});
								} else {
									readBuffer[readBufferPosition++] = b;
								}
							}
						}
					} catch (IOException ex) {
						stopWorker = true;
					}

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
		});
		workerThread.start();
	}
	
	//NFC시작
	public void onResume() {
        super.onResume();

        if (mAdapter != null) {
        	mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        }
    }

    private void processTag(Intent passedIntent) {
    	
    	getPhoneNum();
    	calorie();
    	
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
            	finalTime= Hour + ":" + Minute + ":" + Second;
            	Toast.makeText(getApplicationContext(), "운동을 종료합니다\n" +"운동한시간 : " +  finalTime +"\n사용자정보 : " + phoneNum, Toast.LENGTH_LONG).show();
            	Intent intent = new Intent(getApplicationContext(), NFCScanForegroundActivity.class);
            	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
            	insertData();
            	startActivity(intent);
            }else if(newMachine.equals("butterfly")){
            	Toast.makeText(getApplicationContext(), "먼저 Treadmill 운동을 종료해주세요", Toast.LENGTH_LONG).show();
            }else if(newMachine.equals("dumbbell_5kg")){
            	Toast.makeText(getApplicationContext(), "먼저 Treadmill 운동을 종료해주세요", Toast.LENGTH_LONG).show();
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
				Log.d(tag, "운동한 시간 : " +Minute + "분 " + Second +"초");
				Update();
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
		speedCheck++;
		if(speedCheck>4 && timerLock==0){//속도가변화되고 5초동안그대로이면 타이머재시작
			Second=0;
			Minute=0;
			Hour=0;
			timerLock=1;
		}

		Runnable updater = new Runnable() {
			public void run() {
				mTime.setText(Hour + "시간 "+ Minute + "분 " + Second +"초");
			}
		};
		handler.post(updater);
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
	
	public void getDate(){
    	c = Calendar.getInstance();
		Year = c.get(Calendar.YEAR);
		Month = c.get(Calendar.MONTH) + 1; // 1월(0), 2월(1), ..., 12월(11)
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
				String speed =""+treadmillSpeed;
				String calorie=""+treadmillcalorie;
				String date = todaydate;
				
				try {
					URL url = new URL(SERVER_ADDRESS + "/insert.php?"
							+ "phonenum=" + URLEncoder.encode(phonenum, "UTF-8")
							+ "&speed=" + URLEncoder.encode(speed, "UTF-8")
							+ "&time=" + URLEncoder.encode(time, "UTF-8")
							+ "&calorie=" + URLEncoder.encode(calorie, "UTF-8")
							+ "&date=" + URLEncoder.encode(date, "UTF-8")); //변수값을 UTF-8로 인코딩하기 위해 URLEncoder를 이용하여 인코딩함
					url.openStream(); //서버의 DB에 입력하기 위해 웹서버의 insert.php파일에 입력된 이름과 가격을 넘김
					
					String result = getXmlData("insertresult.xml", "result"); //입력 성공여부
					
					if(result.equals("1")) { //result 태그값이 1일때 성공
						Toast.makeText(Treadmill.this,
								"DB insert 성공", Toast.LENGTH_SHORT).show();
						
					}
					else //result 태그값이 1이 아닐때 실패
						Toast.makeText(Treadmill.this,
								"DB insert 실패", Toast.LENGTH_SHORT).show();
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
			url.openStream(); // 서버의 serarch.php파일을 실행함

			String weight = getXmlData("searchresult.xml","weight");

			userWeight = Integer.parseInt(weight);

		} catch (Exception e) {
			Log.e("Error", e.getMessage());
		}
	}
	
	public void calorie(){
		
		int Kcal;
		int weight = userWeight;
		int speed = treadmillSpeed;
		int time = Hour*360 + Minute*60 +Second;	
		/*
		int weight = userWeight;
		int speed = treadmillSpeed;
		int time = Hour*60 + Minute;		
		*/
		
		Kcal = (int)(0.0175*((0.1*speed+3.5)/3.5)*weight*time/60);
		
		treadmillcalorie = Kcal;
	}
	
}
