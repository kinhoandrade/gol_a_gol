package com.oranz.golagol;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.Session;

public class ConfirmScore extends Activity {
	
	private TextView tvArenaConfirm;
	private TextView tvQuantityConfirm;
	private TextView tvDateConfirm;
	private Button btCancel;
	private Button btOk;
	private ToggleButton tbPostOnFb;
	//private GraphUser fbUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_score);
        
        tvArenaConfirm = (TextView) findViewById(R.id.tvArenaConfirm);
        tvQuantityConfirm = (TextView) findViewById(R.id.tvQuantityConfirm);
        tvDateConfirm = (TextView) findViewById(R.id.tvDateConfirm);
        btCancel = (Button) findViewById(R.id.btCancel);
        btOk = (Button) findViewById(R.id.btOk);
        tbPostOnFb = (ToggleButton) findViewById(R.id.tbPostFb);
        
        final String arena = SaveScore.getArena();
        tvArenaConfirm.setText(arena);
        final int quantity = SaveScore.getQuantity();
        tvQuantityConfirm.setText("" + quantity);
        final Calendar date = SaveScore.getDate();
        tvDateConfirm.setText(date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.YEAR));

        String nickname = SaveScore.getNickname();
        
    	if(quantity <= 2){
    		Toast.makeText(this, "Só " + quantity + ", " + nickname + "? Que fase hein!", Toast.LENGTH_LONG).show();
    	}else if(quantity == 3){
    		Toast.makeText(this, "Aqui é assim, marcou 3. Pede música.", Toast.LENGTH_LONG).show();
    	}else if(quantity > 9){
    		Toast.makeText(this, quantity + " gols? Póhan. Se isso é estar na pior. Que é que é estar bem então!?", Toast.LENGTH_LONG).show();
    	}
        
        btCancel.setOnClickListener(new View.OnClickListener() {public void onClick(View arg0) {finish();}});
        btOk.setOnClickListener(new View.OnClickListener() {public void onClick(View arg0) {
        	String msg = SaveScore.getNickname() + " tá marcando gol!";
        	if(tbPostOnFb.isChecked()){
        		if (quantity == 0){
        			msg = SaveScore.getNickname() + " não fez nenhum gol no estádio " + arena + " em " + (date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.MONTH) + 1));
        		}else if (quantity == 1){
            		msg = SaveScore.getNickname() + " fez " + quantity + " gol no estádio " + arena + " em " + (date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.MONTH) + 1));
        		}else{
        			msg = SaveScore.getNickname() + " fez " + quantity + " gols no estádio " + arena + " em " + (date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.MONTH) + 1));
        		}
        		SaveScore.setMessage(msg);
        		postOnFacebook();
        	}
        	SaveScore.registerScoreConfirmed(arena, quantity, date);
        	finish();
        	}
        });
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    	super.onActivityResult(requestCode, resultCode, data);
    	Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }
    
	public void postOnFacebook(){    	
    	Intent nextScreen = new Intent(getApplicationContext(), FacebookActivity.class);
    	
      	nextScreen.putExtra("origin", "main");
    	startActivity(nextScreen);
	}
    
    public void openMenu(View view){
        switch (view.getId()) {
        case R.id.ivMenu:
        	openOptionsMenu();
        }
    }
    
    public void openConfig(View view){
    	Intent nextScreen = new Intent(getApplicationContext(), Config.class);	    	
    	startActivity(nextScreen);
    }    
}
