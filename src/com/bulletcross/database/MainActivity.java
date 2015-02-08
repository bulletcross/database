package com.bulletcross.database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.opencsv.CSVWriter;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	Button save,show,export;
	EditText name,email;
	TextView Name,Email;
	DataHandler handler;
	Cursor C;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		save = (Button) findViewById(R.id.save);
		show = (Button) findViewById(R.id.show);
		export = (Button) findViewById(R.id.export);
		name = (EditText) findViewById(R.id.nameText);
		email = (EditText) findViewById(R.id.emailText);
		Name = (TextView) findViewById(R.id.nameView);
		Email = (TextView) findViewById(R.id.emailView);
		
		handler = new DataHandler(getBaseContext());
		handler.open();
		C = handler.returnData();
		
	save.setOnClickListener(new OnClickListener(){
			
		@Override
		public void onClick(View v) {
			String getName = name.getText().toString();
			String getEmail = email.getText().toString();
			//handler = new DataHandler(getBaseContext());
			//handler.open();
			long id = handler.insertData(getName, getEmail);
			handler = new DataHandler(getBaseContext());
			handler.open();
			C = handler.returnData();
			Toast.makeText(getBaseContext(), "Inserted", Toast.LENGTH_LONG).show();
			//handler.close();
			
		}
	});
	
	show.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			String getName,getEmail;
			getName = "";
			getEmail = "";
			//handler = new DataHandler(getBaseContext());
			//handler.open();
			//Cursor C = handler.returnData();
			/*if(C.moveToFirst()){
				do{
					getName = C.getString(0);
					getEmail = C.getString(1);
				}while(C.moveToNext());
			}
			//handler.close();
			Toast.makeText(getBaseContext(), "Name:" + getName + ", Email: "+getEmail,Toast.LENGTH_LONG).show();*/
			if(!C.moveToNext()){
				C.moveToFirst();
			}
			getName = C.getString(0);
			getEmail = C.getString(1);
			Name.setText(getName);
			Email.setText(getEmail);
			
		}	
	});
	
	export.setOnClickListener(new OnClickListener(){
		
		@Override
		public void onClick(View v) {
			File dbFile=getDatabasePath(handler.DATABASE_NAME);
			System.out.println(dbFile);
			File exportDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "");        
		    if (!exportDir.exists()){
		        exportDir.mkdirs();
		    }
		    File file = new File(exportDir, "excerDB.csv");
		    try{
		        if (exportDir.createNewFile()){
		            //System.out.println("File is created!");
		        	Toast.makeText(getBaseContext(), "File is created! at "+ file.getAbsolutePath(),Toast.LENGTH_LONG).show();
		            //System.out.println("myfile.csv "+file.getAbsolutePath());
		          }else{
		            //System.out.println("File already exists.");
		        	  Toast.makeText(getBaseContext(), "File already exists",Toast.LENGTH_LONG).show();
		          }
		        CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
		      //SQLiteDatabase db = dbhelper.getWritableDatabase();
		        Cursor curCSV=handler.db.rawQuery("select * from " + handler.TABLE_NAME,null);
		        //csvWrite.writeNext(curCSV.getColumnNames());
		        while(curCSV.moveToNext()){
		            String arrStr[] ={curCSV.getString(0),curCSV.getString(1)};
		         /*curCSV.getString(3),curCSV.getString(4)};*/
		            csvWrite.writeNext(arrStr);
		        }
		        csvWrite.close();
		        curCSV.close();
		        /*String data="";
		        data=readSavedData();
		        data= data.replace(",", ";");
		        writeData(data);*/
		    }
		    catch(SQLException sqlEx){
		        //Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
		    	Toast.makeText(getBaseContext(), "Exception 1",Toast.LENGTH_LONG).show();
		    }
		    catch (IOException e){
		        //Log.e("MainActivity", e.getMessage(), e);
		    	Toast.makeText(getBaseContext(), "Exception 2",Toast.LENGTH_LONG).show();
		    }			
		}
	});		
	}
	protected void onPause(){
		
	}
	
	
	protected void onStop(){
		handler.close();
	}
	protected void onDestroy(){
		
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
