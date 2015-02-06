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
		//ͨ��ContentResolver��������ṩ��
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri.parse("content://com.nutegg.personprovider/query");
		Cursor cursor = resolver.query(uri, null, null, null, null);//��ѯ������Ϣ
		List<Person> persons = new ArrayList<Person>();
		String pInfo = null;
		while(cursor.moveToNext()){
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			int account = cursor.getInt(cursor.getColumnIndex("account"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String number = cursor.getString(cursor.getColumnIndex("number"));
			Person p = new Person(id,name,number,account);
			persons.add(p);
			pInfo = pInfo+"����:"+name+",�绰����:"+number+"\n";
		}
		tv.setText(pInfo);
	}
	
	public void getSms(View view) {
		//ͨ��ContentResolver��������ṩ��
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri.parse("content://sms/");
		Cursor cursor = resolver.query(uri,new String[]{"address","date","type","body"}, null, null, null);//��ѯ������Ϣ
		String pInfo = null;
		String addressType = null;
		while(cursor.moveToNext()){
			String address = cursor.getString(cursor.getColumnIndex("address"));
			String date = cursor.getString(cursor.getColumnIndex("date"));
			String type = cursor.getString(cursor.getColumnIndex("type"));
			if("1".equals(type)){
				addressType ="�ռ��˺���";
			}else if("2".equals(type)){
				addressType ="�����˺���";
			}
			String body = cursor.getString(cursor.getColumnIndex("body"));

			pInfo = pInfo+"����:"+date+","+addressType+":"+address+",��������"+body+"\n";
		}
		tv.setText(pInfo);
	}

}
