package com.oranz.golagol;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmScore extends Activity {
	
	private TextView tvArenaConfirm;
	private TextView tvQuantityConfirm;
	private TextView tvDateConfirm;
	private Button btClose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_score);
        
        tvArenaConfirm = (TextView) findViewById(R.id.tvArenaConfirm);
        tvQuantityConfirm = (TextView) findViewById(R.id.tvQuantityConfirm);
        tvDateConfirm = (TextView) findViewById(R.id.tvDateConfirm);
        btClose = (Button) findViewById(R.id.btClose);
        
        tvArenaConfirm.setText(SaveScore.getArena());
        int quantity = SaveScore.getQuantity();
        tvQuantityConfirm.setText("" + quantity);
        Calendar date = SaveScore.getDate();
        tvDateConfirm.setText(date.get(Calendar.DAY_OF_MONTH) + "/" + date.get(Calendar.MONTH) + "/" + date.get(Calendar.YEAR));
        
    	if(quantity <= 2){
    		Toast.makeText(this, "Só " + quantity + "? Que fase hein!", Toast.LENGTH_LONG).show();
    	}else if(quantity == 3){
    		Toast.makeText(this, "Aqui é assim, marcou 3. Pode pedir a música.", Toast.LENGTH_LONG).show();
    	}else if(quantity > 9){
    		Toast.makeText(this, "Se isso é estar na pior. Póhan. Que é que é estar bem então!?", Toast.LENGTH_LONG).show();
    	}
        
        btClose.setOnClickListener(new View.OnClickListener() {public void onClick(View arg0) {finish();}});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_confirm_score, menu);
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
