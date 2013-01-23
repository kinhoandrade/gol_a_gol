package com.oranz.golagol;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Config extends Activity {
	private Button btClose;
	private EditText etCreateArena;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        
        btClose = (Button) findViewById(R.id.btCloseConfig);
        etCreateArena = (EditText) findViewById(R.id.etArenaConfig);
        
        btClose.setOnClickListener(new View.OnClickListener() {public void onClick(View arg0) {finish();}});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_config, menu);
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
    
    public void removeArena(View view){
        switch (view.getId()) {
        case R.id.btRemoveArena:	        
        	Toast.makeText(this, "Arena excluída", Toast.LENGTH_LONG).show();
        }
    }
    
    public void createArena(View view){
        switch (view.getId()) {
        case R.id.btCreateArena:
        	String arena = etCreateArena.getText().toString();
        	
        	SaveScore.createArena(arena);
     	           	
        	Toast.makeText(this, "Arena " + arena + " criada", Toast.LENGTH_LONG).show();
        }
    }
    
}