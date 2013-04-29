package com.oranz.golagol;

import android.app.Activity;
import android.content.Intent;
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
	private EditText etNickname;
	private EditText etEmail;
	private EditText etPassword;
	private Spinner spArenasConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        
        btClose = (Button) findViewById(R.id.btCloseConfig);
        etCreateArena = (EditText) findViewById(R.id.etArenaConfig);
        etNickname = (EditText) findViewById(R.id.etNickname);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        spArenasConfig = (Spinner) findViewById(R.id.spArenasConfig);
        
        try{
        	spArenasConfig = SaveScore.getArenasSpinner(spArenasConfig);
        }catch(Exception e){
        	e.printStackTrace();
        }
        
        etNickname.setText(SaveScore.getNickname());
        etEmail.setText(SaveScore.getEmail());
        
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
        	
        	if(arena.equals("NÃO IDENTIFICADA")){	        
            	Toast.makeText(this, "Esta arena não pode ser apagada.", Toast.LENGTH_LONG).show();
            	return;        		
        	}        	
        	
        	boolean result = SaveScore.removeArena(arena);
        	if (result == true){
        		Toast.makeText(this, "Arena excluída", Toast.LENGTH_LONG).show();
        	}else {
        		Toast.makeText(this, "Arena com gol não pode ser removida.", Toast.LENGTH_LONG).show();
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
    
    public void updateNickname(View view){
    	switch (view.getId()) {
        case R.id.btUpdateNickName:        	
        	String nickname = etNickname.getText().toString();
        	
        	if(nickname.trim().equals("")){	        
            	Toast.makeText(this, "Necessário informar o apelido", Toast.LENGTH_LONG).show();
            	return;
        	}
        	
        	SaveScore.setNickname(nickname);
     	           	
        	Toast.makeText(this, "Apelido atualizado para: " + nickname, Toast.LENGTH_LONG).show();
        }
    }    
    
    public void updateEmail(View view){
    	switch (view.getId()) {
        case R.id.btUpdateEmail:        	
        	String email = etEmail.getText().toString();
        	
        	if(email == "" || email.equalsIgnoreCase("") || !(email.contains("@")) || !(email.contains("."))){	        
        		String mensagem = "Você precisa inserir um email válido";
         	   	Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
            	return;
        	}
        	
        	SaveScore.setEmail(email);

        	Toast.makeText(this, "Email atualizado para: " + email, Toast.LENGTH_LONG).show();
        }
    }    
    
    public void updatePassword(View view){
    	switch (view.getId()) {
        case R.id.btUpdatePassword:        	
        	String password = etPassword.getText().toString().trim();
        	
        	if(password.equalsIgnoreCase("")){
        		String mensagem = "Você precisa inserir uma nova senha";
         	   	Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
            	return;
        	} else if (password.length() < 4){
        		String mensagem = "A senha precisa ter ao menos 4 caracteres";
         	   	Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
            	return;        		
        	}
        	String current_password = SaveScore.getPassword();
        	
        	SaveScore.setPassword(password);
        	
        	if(current_password == null){
        		Toast.makeText(this, "Senha atualizada para: " + password, Toast.LENGTH_LONG).show();
        	} else {
        		Toast.makeText(this, "Senha atualizada de " + current_password + " para: " + password, Toast.LENGTH_LONG).show();
        	}
        }
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