package com.hefnrh.appformcs;

import java.util.concurrent.CountDownLatch;

import tools.Counter;
import tools.MultiThreadTest;
import abstractmath.Field28;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
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
		
		status = (TextView) findViewById(R.id.textView1);
		result = (TextView) findViewById(R.id.textView2);
		run = (Button) findViewById(R.id.button1);
		
		String[] items = {"1 thread", "2 threads", "4 threads", "6 threads", "8 threads"};
		MyAdapter adapter = new MyAdapter(this, items);
		
		multiChoice = (Spinner) findViewById(R.id.spinner1);
		multiChoice.setAdapter(adapter);
		multiChoice.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
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
		switch (item.getItemId()) {
		case R.id.item1: finish(); break;
		default: break;
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
		if (n == 0) {
			res = singleThreadTest();
			n = 1;
		} else {
			n <<= 1;
			res = multiThreadTest(n);
		}
		result.setText("time used: " + res + " ms with " + n + " threads");
		status.setText("status: run over");
		run.setEnabled(true);
	}
	
	private long singleThreadTest() {
		byte b = 14; // 00001110
		b |= -128;	 // 10001110
		byte p = 14; //	00001110
		p |= -128;	 // 10001110 x^8 + x^4 + x^3 + x^2 + 1
		byte minus1 = -1;
		Field28 field = new Field28(p);
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; ++i) {
			b = field.multiply(b, minus1);
		}
		long end = System.currentTimeMillis();
		return end - start;
	}
	
	private long multiThreadTest(int n) {
		byte p = 14; // 00001110
		p |= -128;   // 10001110 x^8 + x^4 + x^3 + x^2 + 1
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
			status.setText("an error occurred when running");
			return 0;
		}
		return c.getTime();
	}
	
	public void change10W2MCS(View v) {
		// TODO
	}

}
