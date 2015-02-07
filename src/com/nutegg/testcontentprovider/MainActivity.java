package com.nutegg.testcontentprovider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nutegg.testcontentprovider.domain.Person;
import com.nutegg.testcontentprovider.domain.SmsInfo;

public class MainActivity extends Activity {
	
	TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView)this.findViewById(R.id.tv);
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri.parse("content://sms/");
		resolver.registerContentObserver(uri, true, new MyObserver(new Handler()));
	}
	
	public class MyObserver extends ContentObserver{

		public MyObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			Toast.makeText(MainActivity.this, "ϵͳ�������ݷ����仯!", 1).show();
			//ͨ��ContentResolver��������ṩ��
			ContentResolver resolver = getContentResolver();
			Uri uri = Uri.parse("content://sms/");
			Cursor cursor = resolver.query(uri,new String[]{"address","date","type","body"}, null, null, null);//��ѯ������Ϣ
			String pInfo = null;
			String addressType = null;
			cursor.moveToFirst();
			String address = cursor.getString(cursor.getColumnIndex("address"));
			String date = cursor.getString(cursor.getColumnIndex("date"));
			String type = cursor.getString(cursor.getColumnIndex("type"));
			if("1".equals(type)){
				addressType ="�ռ��˺���";
			}else if("2".equals(type)){
				addressType ="�����˺���";
			}
			String body = cursor.getString(cursor.getColumnIndex("body"));
			
			
			pInfo = "����:"+date+","+addressType+":"+address+",��������"+body+"\n";
			System.out.println(pInfo);
			}
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
	
	public void addOtherAppInfo(View view) {
		//ͨ��ContentResolver��������ṩ��
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri.parse("content://com.nutegg.personprovider/insert");
		ContentValues values = new ContentValues();
		values.put("name", "̫����");
		values.put("number", "19900990990");
		values.put("account", 800000000);
		resolver.insert(uri,values );
		
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
	
	//���ݶ���
			public void saveSms(View view){
				//�����Ѿ���ȡ��ȫ���Ķ���
				//����2:ͨ��XML���л���ȥ����һ��XML�ļ�.
				ContentResolver resolver = getContentResolver();
				Uri uri = Uri.parse("content://sms/");
				Cursor cursor = resolver.query(uri,new String[]{"address","date","type","body"}, null, null, null);//��ѯ������Ϣ
				List<SmsInfo> smsInfoList = new ArrayList<SmsInfo>();
				while(cursor.moveToNext()){
					SmsInfo smsInfo = new SmsInfo();
					String address = cursor.getString(cursor.getColumnIndex("address"));
					String date = cursor.getString(cursor.getColumnIndex("date"));
					String type = cursor.getString(cursor.getColumnIndex("type"));

					String body = cursor.getString(cursor.getColumnIndex("body"));
					smsInfo.setAddress(address);
					smsInfo.setContent(body);
					smsInfo.setDate(Long.parseLong(date));
					smsInfo.setType(Integer.parseInt(type));
					smsInfoList.add(smsInfo);
				}
				
				XmlSerializer serializer = Xml.newSerializer();
				File file = new File(getFilesDir(),"SMSINFO.xml");
				//File file = new File("/data/data","SMSINFO.xml");
				try {
					FileOutputStream fos = new FileOutputStream(file);
			
					serializer.setOutput(fos,"utf-8");
				
					serializer.startDocument("utf-8", true);
					serializer.startTag(null, "smss");
					for(SmsInfo smsInfo : smsInfoList){
						serializer.startTag(null, "sms");
						serializer.attribute(null, "id", smsInfo.getId()+"");
						serializer.startTag(null, "date");
						serializer.text(smsInfo.getDate()+"");
						serializer.endTag(null, "date");
						serializer.startTag(null, "content");
						serializer.text(smsInfo.getContent());
						serializer.endTag(null, "content");
						serializer.startTag(null, "type");
						serializer.text(smsInfo.getType()+"");
						serializer.endTag(null, "type");
						serializer.startTag(null, "adress");
						serializer.text(smsInfo.getAddress());
						serializer.endTag(null, "adress");
						serializer.endTag(null, "sms");
					}				
					serializer.endDocument();
					fos.close();

					Toast.makeText(this, "���ű��ݳɹ�", Toast.LENGTH_SHORT).show();
				} catch (Exception e){
					e.printStackTrace();
					Toast.makeText(this, "���ű���ʧ��", Toast.LENGTH_SHORT).show();
				}
				
			}
			
			//��ȡ����
			public void readSms(View view){
				//�����Ѿ���ȡ��ȫ���Ķ���
				//����2:ͨ��XML���л���ȥ����һ��XML�ļ�.
				XmlPullParser parser = Xml.newPullParser();
				File file = new File(getFilesDir(),"SMSINFO.xml");
				//File file = new File("/data/data","SMSINFO.xml");
				try {
					FileInputStream fis = new FileInputStream(file);
					parser.setInput(fis,"utf-8");
					List<SmsInfo> smsInfoList = null;
					SmsInfo smsInfo = null;
					int type = parser.getEventType();
					while(type != XmlPullParser.END_DOCUMENT){
						switch(type){
						case XmlPullParser.START_TAG:
							if("smss".equals(parser.getName())){
								smsInfoList = new ArrayList<SmsInfo>();
							}else if("sms".equals(parser.getName())){
								smsInfo = new SmsInfo();
								String id = parser.getAttributeValue(0);
								smsInfo.setId(Integer.parseInt(id));
							}else if("date".equals(parser.getName())){						
								String date = parser.nextText();
								smsInfo.setDate(Long.parseLong(date));
							}else if("content".equals(parser.getName())){
								String content = parser.nextText();
								smsInfo.setContent(content);
							}else if("type".equals(parser.getName())){
								String type_ = parser.nextText();
								smsInfo.setType(Integer.parseInt(type_));
							}else if("adress".equals(parser.getName())){
								String adress = parser.nextText();
								smsInfo.setAddress(adress);
							}
							break;
						case XmlPullParser.END_TAG:
							if("sms".equals(parser.getName())){						
								smsInfoList.add(smsInfo);		
								smsInfo = null;
							}
							break;
						}
						type = parser.next();
					}
					
					StringBuffer strb= new StringBuffer();
					
					for(SmsInfo smsInfos : smsInfoList){
						String sms = smsInfos.toString();
						strb.append(sms);
						strb.append("\n");
					}
					TextView textView = (TextView)MainActivity.this.findViewById(R.id.tv);

					textView.setText(strb.toString());
				
				} catch (Exception e){
					e.printStackTrace();
					Toast.makeText(this, "���Ŷ�ȡʧ��", Toast.LENGTH_SHORT).show();
				}
				
			}

}
