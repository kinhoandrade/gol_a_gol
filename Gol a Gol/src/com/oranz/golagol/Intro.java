package com.oranz.golagol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class Intro extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        //display the logo during X seconds,
        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long millisUntilFinished){} 

            @Override
            public void onFinish(){
                //set the new Content of your activity
                //Intro.this.setContentView(R.layout.activity_save_score);
       	    	Intent nextScreen = new Intent(getApplicationContext(), SaveScore.class);	    	
    	    	startActivity(nextScreen);
            }
       }.start();
    } 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_intro, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
