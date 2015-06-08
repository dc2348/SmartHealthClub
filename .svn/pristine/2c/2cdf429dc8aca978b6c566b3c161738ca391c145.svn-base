package com.example.test2;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.MediaController;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.VideoView;
import android.widget.Button;

public class videoPlayerActivity extends MainActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.video);
		
		 Button ath1Button = (Button)findViewById(R.id.ath1);
	     Button ath2Button = (Button)findViewById(R.id.ath2);
	     Button ath3Button = (Button)findViewById(R.id.ath3);

	     ath1Button.setOnClickListener(new OnClickListener() {

	    		public void onClick(View v) {
	    						
	    			
	    			VideoView videoView = (VideoView) findViewById(R.id.videoView1);


	    			  // 비디오뷰를 커스텀하기 위해서 미디어컨트롤러 객체 생성
	    			  MediaController mediaController = new MediaController(videoPlayerActivity.this);

	    			  // 비디오뷰에 연결
	    			  mediaController.setAnchorView(videoView);
	    			  // 안드로이드 res폴더에 raw폴더를 생성 후 재생할 동영상파일을 넣습니다.
	    			  Uri video = Uri.parse("android.resource://" + getPackageName()
	    			    + "/raw/dumbbell");
	    			  
	    			  //비디오뷰의 컨트롤러를 미디어컨트롤로러 사용
	    			  videoView.setMediaController(mediaController);
	    			  
	    			  //비디오뷰에 재생할 동영상주소를 연결
	    			  videoView.setVideoURI(video);

	    			  //비디오뷰를 포커스하도록 지정
	    			  videoView.requestFocus();

	    			  //동영상 재생
	    			  videoView.start();
	    		}

	    	});
		
	     ath2Button.setOnClickListener(new OnClickListener() {

	 		public void onClick(View v) {


	 			VideoView videoView = (VideoView) findViewById(R.id.videoView1);

	 			  // 비디오뷰를 커스텀하기 위해서 미디어컨트롤러 객체 생성
	 			  MediaController mediaController = new MediaController(videoPlayerActivity.this);

	 			  mediaController.setAnchorView(videoView);
	 			  // 안드로이드 res폴더에 raw폴더를 생성 후 재생할 동영상파일을 넣습니다.
	 			  Uri video = Uri.parse("android.resource://" + getPackageName()
	 			    + "/raw/treadmill");
	 			  
	 			  //비디오뷰의 컨트롤러를 미디어컨트롤로러 사용
	 			  videoView.setMediaController(mediaController);
	 			  
	 			  //비디오뷰에 재생할 동영상주소를 연결
	 			  videoView.setVideoURI(video);

	 			  //비디오뷰를 포커스하도록 지정
	 			  videoView.requestFocus();

	 			  //동영상 재생
	 			  videoView.start();
	 		}

	 	});
		
	     ath3Button.setOnClickListener(new OnClickListener() {

	 		public void onClick(View v) {

	 			VideoView videoView = (VideoView) findViewById(R.id.videoView1);

	 			  // 비디오뷰를 커스텀하기 위해서 미디어컨트롤러 객체 생성
	 			  MediaController mediaController = new MediaController(videoPlayerActivity.this);

	 			  // 비디오뷰에 연결
	 			  mediaController.setAnchorView(videoView);
	 			  // 안드로이드 res폴더에 raw폴더를 생성 후 재생할 동영상파일을 넣습니다.
	 			  Uri video = Uri.parse("android.resource://" + getPackageName()
	 			    + "/raw/butterfly");
	 			  
	 			  //비디오뷰의 컨트롤러를 미디어컨트롤로러 사용
	 			  videoView.setMediaController(mediaController);
	 			  
	 			  //비디오뷰에 재생할 동영상주소를 연결
	 			  videoView.setVideoURI(video);

	 			  //비디오뷰를 포커스하도록 지정
	 			  videoView.requestFocus();

	 			  //동영상 재생
	 			  videoView.start();
	 		}
		});
		
		}
}
