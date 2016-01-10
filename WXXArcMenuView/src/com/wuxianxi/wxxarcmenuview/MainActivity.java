package com.wuxianxi.wxxarcmenuview;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.wuxianxi.wxxarcmenuview.view.ArcMenu;
import com.wuxianxi.wxxarcmenuview.view.ArcMenu.onMenuItemClickListener;

public class MainActivity extends Activity {

	private ListView mListView;
	private List<String> mList;
	private ArcMenu mArcMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mListView = (ListView) findViewById(R.id.id_listView);
		mArcMenu = (ArcMenu) findViewById(R.id.id_arcmenu);
		initData();
		mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList));
		
		initListEvent();
	}

	private void initListEvent() {
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				if (mArcMenu.isOpen()) {
					mArcMenu.toggleMenu(400);
				}
			}
		});
		
		mArcMenu.setOnMenuItemClickListener(new onMenuItemClickListener() {
			
			@Override
			public void onClick(View view, int pos) {
				Toast.makeText(MainActivity.this, pos+":"+view.getTag(), Toast.LENGTH_LONG).show();
				
			}
		});
		
	}

	private void initData() {
		mList = new ArrayList<String>();
		for (int i = 'a'; i < 'z'; i++) {
			mList.add((char)i+"");
		}
		
	}


}
