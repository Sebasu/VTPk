package com.vagoscorp.virtualterminalprokey;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends Activity {

	TextView stat;
    Button enPro;
	
	boolean SDread = false;
	boolean SDwrite = false;
	File path;
//	File[] fileList;
//	String[] fileNames;
//	int nfil = 0;
	static String config = "config";
	static String ext = ".vtconfig";
//	String bSel;
//	String nPro;
	String baseVer = "1\n1\n1\n1\n1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		stat = (TextView)findViewById(R.id.state);
        enPro = (Button)findViewById(R.id.enPro);
		path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.vagoscorp.vcvt");
		path.mkdirs();
		setupActionBar();
        write(config, baseVer);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar aB = getActionBar();
			if(aB != null)
				aB.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onPro(View view) {
		write(config, baseVer);
	}
	
	void checkSD() {
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)) {
			SDread = true;
			SDwrite = true;
		}else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			SDread = true;
			SDwrite = false;
		}else {
//			oops
			SDread = false;
			SDwrite = false;
		}
	}
	
	void write(String name, String data) {
		checkSD();
		if(SDread && SDwrite) {
			byte[] buff = data.getBytes();
			File file = new File(path, name + ext);
			OutputStream os;
			try {
				os = new FileOutputStream(file);
				os.write(buff);
				os.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			stat.setText(R.string.prOK);
            enPro.setVisibility(View.GONE);
		}else {
            stat.setText(R.string.noSD);
            enPro.setVisibility(View.VISIBLE);
        }
	}

	@Override
	protected void onDestroy() {
		Intent intent;
		PackageManager manager = getPackageManager();
		intent = manager.getLaunchIntentForPackage("com.vagoscorp.virtualterminal");
		if(intent != null) {
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			startActivity(intent);
		}else {
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id=com.vagoscorp.virtualterminal"));
			startActivity(intent);
		}
		super.onDestroy();
	}
	
	
}
