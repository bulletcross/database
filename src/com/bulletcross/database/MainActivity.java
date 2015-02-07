package com.bulletcross.database;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	Button save,show;
	EditText name,email;
	TextView Name,Email;
	DataHandler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		save = (Button) findViewById(R.id.save);
		show = (Button) findViewById(R.id.show);
		name = (EditText) findViewById(R.id.nameText);
		email = (EditText) findViewById(R.id.emailText);
		Name = (TextView) findViewById(R.id.nameView);
		Email = (TextView) findViewById(R.id.emailView);
		
		
	save.setOnClickListener(new OnClickListener(){
			
		@Override
		public void onClick(View v) {
			String getName = name.getText().toString();
			String getEmail = email.getText().toString();
			handler = new DataHandler(getBaseContext());
			handler.open();
			long id = handler.insertData(getName, getEmail);
			Toast.makeText(getBaseContext(), "Inserted", Toast.LENGTH_LONG).show();
			handler.close();
			
		}
	});
	
	show.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			String getName,getEmail;
			getName = "";
			getEmail = "";
			handler = new DataHandler(getBaseContext());
			handler.open();
			Cursor C = handler.returnData();
			if(C.moveToFirst()){
				do{
					getName = C.getString(0);
					getEmail = C.getString(1);
				}while(C.moveToNext());
			}
			handler.close();
			Toast.makeText(getBaseContext(), "Name:" + getName + ", Email: "+getEmail,Toast.LENGTH_LONG).show();
			
		}	
	});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
