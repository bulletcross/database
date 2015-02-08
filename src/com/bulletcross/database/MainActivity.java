package com.bulletcross.database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.opencsv.CSVWriter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

public class MainActivity extends Activity implements LocationListener{
	
	Button start,export;
	DataHandler handler;
	Cursor C;
	boolean toggle;
	private LocationManager locationManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		start = (Button) findViewById(R.id.start);
		export = (Button) findViewById(R.id.export);
		
		handler = new DataHandler(getBaseContext());
		handler.open();
		C = handler.returnData();
		
		toggle = false;
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,10, this);
		
	start.setOnClickListener(new OnClickListener(){
			
		@Override
		public void onClick(View v) {
			if(toggle){
				toggle = false;
				Toast.makeText(getBaseContext(), "Toggled off", Toast.LENGTH_LONG).show();
			}
			else{
				toggle = true;
				Toast.makeText(getBaseContext(), "Toggled on", Toast.LENGTH_LONG).show();
			}
			
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
		        	Toast.makeText(getBaseContext(), "File is created! at "+ file.getAbsolutePath(),Toast.LENGTH_LONG).show();
		          }else{
		        	  Toast.makeText(getBaseContext(), "File already exists",Toast.LENGTH_LONG).show();
		          }
		        CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
		        Cursor curCSV=handler.db.rawQuery("select * from " + handler.TABLE_NAME,null);
		        while(curCSV.moveToNext()){
		            String arrStr[] ={curCSV.getString(0),curCSV.getString(1)};
		            csvWrite.writeNext(arrStr);
		        }
		        csvWrite.close();
		        curCSV.close();
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
	@Override
	public void onLocationChanged(Location location) {
		String str = "Latitude: "+location.getLatitude()+"Longitude: "+location.getLongitude();
		Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG).show();
		if(toggle){
			long id = handler.insertData(location.getLatitude(), location.getLongitude());
		}
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
		
	}
}
