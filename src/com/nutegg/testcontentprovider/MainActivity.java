package com.nutegg.testcontentprovider;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nutegg.testcontentprovider.domain.Person;

public class MainActivity extends Activity {
	
	TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView)this.findViewById(R.id.tv);
	}

	public void getOtherAppInfo(View view) {
		//通过ContentResolver获得内容提供者
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri.parse("content://com.nutegg.personprovider/query");
		Cursor cursor = resolver.query(uri, null, null, null, null);//查询所以信息
		List<Person> persons = new ArrayList<Person>();
		String pInfo = null;
		while(cursor.moveToNext()){
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			int account = cursor.getInt(cursor.getColumnIndex("account"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String number = cursor.getString(cursor.getColumnIndex("number"));
			Person p = new Person(id,name,number,account);
			persons.add(p);
			pInfo = pInfo+"姓名:"+name+",电话号码:"+number+"\n";
		}
		tv.setText(pInfo);
	}

}
