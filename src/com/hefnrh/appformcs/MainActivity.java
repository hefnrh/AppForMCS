package com.hefnrh.appformcs;

import java.util.concurrent.CountDownLatch;

import tools.Counter;
import tools.MultiThreadTest;
import abstractmath.Field28;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView status;
	private TextView result;
	private Button run;
	private Spinner multiChoice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setTitle("IS MATH 2 DEMO");

		status = (TextView) findViewById(R.id.textView1);
		result = (TextView) findViewById(R.id.textView2);
		run = (Button) findViewById(R.id.button1);

		String[] items = { "1 thread", "2 threads", "4 threads", "6 threads",
				"8 threads" };
		MyAdapter adapter = new MyAdapter(this, items);

		multiChoice = (Spinner) findViewById(R.id.spinner1);
		multiChoice.setAdapter(adapter);
		multiChoice
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		TabHost.TabSpec spec = tabHost.newTabSpec("multiply");
		spec.setContent(R.id.multiplylayout);
		spec.setIndicator("10W multiply");
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("mcs");
		spec.setContent(R.id.MCSLayout);
		spec.setIndicator("MCS");
		tabHost.addTab(spec);
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
		case R.id.item1:
			finish();
			break;
		default:
			break;
		}
		return true;
	}

	public void exit(View v) {
		finish();
	}

	public void run(View v) {
		run.setEnabled(false);
		int n = multiChoice.getSelectedItemPosition();
		status.setText("status: running");
		long res;
		if (n == 0)
			n = 1;
		else
			n <<= 1;
		res = multiThreadTest(n);
		if (res >= 0)
			result.setText("time used: " + res + " ms with " + n + " threads");
		status.setText("status: run over");
		run.setEnabled(true);
	}

	private long multiThreadTest(int n) {
		byte p = 13; // 00001101
		p |= -128; // 10001101 x^8 + x^4 + x^3 + x + 1
		Field28 field = new Field28(p);
		Counter c = new Counter();
		CountDownLatch cdl = new CountDownLatch(n);
		MultiThreadTest[] ms = new MultiThreadTest[n];
		for (int i = 0; i < n; ++i)
			ms[i] = new MultiThreadTest(c, 100000 / n, field, cdl);
		c.setStart();
		for (int i = 0; i < n; ++i)
			ms[i].start();
		try {
			cdl.await();
		} catch (InterruptedException e) {
			result.setText("an error occurred when running");
			return -1;
		}
		return c.getTime();
	}
	
	public void viewSrc(View v) {
		Uri uri = Uri.parse("https://github.com/hefnrh/AppForMCS");
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(it);
	}
}
