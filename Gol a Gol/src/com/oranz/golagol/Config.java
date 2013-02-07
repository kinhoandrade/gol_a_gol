package com.oranz.golagol;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Config extends Activity {
	private Button btClose;
	private EditText etCreateArena;
	private Spinner spArenasConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        
        btClose = (Button) findViewById(R.id.btCloseConfig);
        etCreateArena = (EditText) findViewById(R.id.etArenaConfig);
        spArenasConfig = (Spinner) findViewById(R.id.spArenasConfig);
        
        try{
        	spArenasConfig = SaveScore.getArenasSpinner(spArenasConfig);
        }catch(Exception e){
        	e.printStackTrace();
        }
        
        btClose.setOnClickListener(new View.OnClickListener() {public void onClick(View arg0) {
	        	try {
				SaveScore.refreshArenaSpinner(SaveScore.getSpArena());
			} catch (Exception e) {
				e.printStackTrace();
			} finish();
		}});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_config, menu);
        return true;
    }
    
    public void removeArena(View view){
        switch (view.getId()) {
        case R.id.btRemoveArena:
        	String arena = spArenasConfig.getSelectedItem().toString();
        	
        	if(arena.equals("PADRÃO")){	        
            	Toast.makeText(this, "A arena Padrão não apaga.", Toast.LENGTH_LONG).show();
            	return;        		
        	}        	
        	
        	boolean result = SaveScore.removeArena(arena);
        	if (result == true){
        		Toast.makeText(this, "Arena excluída", Toast.LENGTH_LONG).show();
        	}else {
        		Toast.makeText(this, "Arena com gols não pode ser excluída.", Toast.LENGTH_LONG).show();
        	}

            try{
            	spArenasConfig = SaveScore.getArenasSpinner(spArenasConfig);
            }catch(Exception e){
            	e.printStackTrace();
            }
        }
    }
    
    public void createArena(View view){
        switch (view.getId()) {
        case R.id.btCreateArena:
        	
        	String arena = etCreateArena.getText().toString();
        	
        	if(arena.trim().equals("")){	        
            	Toast.makeText(this, "Falta informar a arena!", Toast.LENGTH_LONG).show();
            	return;
        	}
        	
        	SaveScore.createArena(arena);
     	           	
        	Toast.makeText(this, "Arena " + arena + " criada", Toast.LENGTH_LONG).show();
        }
    }
    
}
