package com.oranz.golagol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreReport extends Activity {
	private Button btClose;
	private TextView tvTotal;
	private TextView tvLast7days;
	private TextView tvLast15days;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_report);
        
        btClose = (Button) findViewById(R.id.btCloseReport);
        tvTotal = (TextView) findViewById(R.id.tvTotalResult);
        tvLast7days = (TextView) findViewById(R.id.tvLast7DaysResult);
        tvLast15days = (TextView) findViewById(R.id.tvLast15DaysResult);
        
        int total = SaveScore.getTotal(0);
        tvTotal.setText("" + total);
        
        int last7days = SaveScore.getTotal(7);
        tvLast7days.setText("" + last7days);
        
        int last15days = SaveScore.getTotal(15);
        tvLast15days.setText("" + last15days);
        
        btClose.setOnClickListener(new View.OnClickListener() {public void onClick(View arg0) {finish();}});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_score_report, menu);
        return true;
    }
    
    public void openConfig(View view){
        switch (view.getId()) {
        case R.id.ivConfig:	    	
	    	Intent nextScreen = new Intent(getApplicationContext(), Config.class);	    	
	    	startActivity(nextScreen);
        }
    }
    
	public void postOnFacebook(View view){
        switch (view.getId()) {
        case R.id.ivFacebookLogo:
        	Toast.makeText(this, "Breve", Toast.LENGTH_LONG).show();
        }
	}
	
	public void listScores(View view){
        switch (view.getId()) {
        case R.id.btListScores:	    	
	    	Intent nextScreen = new Intent(getApplicationContext(), ListScores.class);	    	
	    	startActivity(nextScreen);
        }
	}
}
