package com.oranz.golagol;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.R.integer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressWarnings("unused")
public class SaveScore extends Activity {
	private Spinner spArena;
	private EditText etQuantidade;
	private DatePicker dpDataScore;
	private Button btRegisterScore;
	
	private List<String> arenasList;
	
    private String array_spinner[];
    
	protected static String arena;
	protected static int quantity;
	protected static Calendar date;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_score);
 
        spArena = (Spinner) findViewById(R.id.spArena);
        etQuantidade = (EditText) findViewById(R.id.etQuantity);
        dpDataScore = (DatePicker) findViewById(R.id.dpScoreDate);
        btRegisterScore = (Button) findViewById(R.id.btRegisterScore);
        
        array_spinner=new String[10];
        array_spinner[0]="PADRÃO";
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array_spinner);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spArena.setAdapter(adapter);
        spArena.setSelection(0);		
        
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_save_score, menu);
        return true;
    }
*/    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, 1, Menu.NONE, R.string.info );
        return result;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
          case 1:   	
              Toast.makeText(this, "Gol a Gol v1.0\nDesenvolvido por Oranz", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void registerScore(View view){
        switch (view.getId()) {
        case R.id.btRegisterScore:
	        if (etQuantidade.getText().length() == 0 || Float.parseFloat(etQuantidade.getText().toString()) <= 0) {
	        	String mensagem = "";
	        	//mensagem = findViewById(R.string.msgQuantityNull).toString();
	        	mensagem = "Só faltou falar quantos gols!";
	            Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
	            return;
	        }
	    	
	    	Intent nextScreen = new Intent(getApplicationContext(), ConfirmScore.class);
	    	
	    	arena = spArena.getSelectedItem().toString();
	    	quantity = Integer.parseInt(etQuantidade.getText().toString());
	    	date = Calendar.getInstance();
	    	date.set(dpDataScore.getYear(), dpDataScore.getMonth()+1, dpDataScore.getDayOfMonth());
	    	
	    	startActivity(nextScreen);
        }
    }

	public static String getArena() {
		return arena;
	}

	public static int getQuantity() {
		return quantity;
	}

	public static Calendar getDate() {
		return date;
	}
}