package com.example.test2;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;

/**
 * ���� ����� ���� ����� ����� ���� �� �� �ֽ��ϴ�.
 * ��Ƽ��Ƽ�� TabActivity�� ����ؾ� �մϴ�.
 * 
 * @author Mike
 */
public class PastActivity extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.past);

        // �� ������ ���� ���� �޼ҵ� ȣ��
       setupTabs();
    }

    /**
     * �� ������ ó���ϴ� �޼ҵ�
     */
    private void setupTabs() {
    	TabHost tabs = getTabHost();
 	    
 	    // TAB 01 
 	    TabHost.TabSpec spec = null;
 	    Intent intent = null;
        
 	    spec = tabs.newTabSpec("tab01");
 	    intent = new Intent(this, SubPage01Activity.class);
 	    intent.putExtra("mode", "new");
	   	intent.putExtra("initialize", true);
	   	intent.putExtra("request", true);
	   	intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
   	
 	    spec.setContent(intent);

 	    spec.setIndicator("����");
 	    tabs.addTab(spec);
 	    
 	    // TAB 02 
 	    spec = tabs.newTabSpec("tab02");
 	    intent = new Intent(this, SubPage02Activity.class);
 	    intent.putExtra("mode", "new");
	   	intent.putExtra("initialize", true);
	   	intent.putExtra("request", true);
	   	intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
 	    spec.setContent(intent);

 	    spec.setIndicator("�����ö��̸ӽ�");
 	    tabs.addTab(spec);
 	    
 	    // TAB 03 
 	    spec = tabs.newTabSpec("tab03");
 	    intent = new Intent(this, SubPage03Activity.class);
 	    intent.putExtra("mode", "new");
	   	intent.putExtra("initialize", true);
	   	intent.putExtra("request", true);
	   	intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
 	    spec.setContent(intent);

 	    spec.setIndicator("Ʈ�����");
 	    tabs.addTab(spec);
 	    
 	    // set current tab
 	    tabs.setCurrentTab(0);
 	
    }
    
    }

