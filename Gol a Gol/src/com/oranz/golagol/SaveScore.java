package com.oranz.golagol;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.oranz.dao.DBAdapter;
import com.oranz.golagol.entidades.Score;

@SuppressWarnings("unused")
public class SaveScore extends Activity {

	private static DBAdapter db;
	
	private static Spinner spArena;
	private EditText etQuantidade;
	private EditText etNickname;
	private EditText etFullname;
	private DatePicker dpDataScore;
	private Button btRegisterScore;
		
    private static String array_spinner[];
    
	private static String arena;
	private static int quantity;
	private static Calendar date;
	private static List<String> listArenas;
	private static String nickname;
	private static String email;
	private static String message;
	
	private static Context appContext;
	private static String fbId = "318684344914920"; 
	
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_score);
        
        boolean drop = false;
        
        if (drop == true){
	       	 AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
	       	 myAlertDialog.setTitle("Apagar dados");
	       	 myAlertDialog.setMessage("Todos os dados podem ser apagados?");
	       	 myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	       	  public void onClick(DialogInterface arg0, int arg1) {
	       		  db = new DBAdapter(getApplicationContext());
	       		  db.dropTables();
	       		  Toast.makeText(getApplicationContext(), "Dados apagados.", Toast.LENGTH_LONG).show();
	       		  db = new DBAdapter(getApplicationContext());    
	       		  db.close();
	       	  }});
	       	 myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {       	       
	       	  public void onClick(DialogInterface arg0, int arg1) {
	       		  Toast.makeText(getApplicationContext(), "Dados mantidos.", Toast.LENGTH_LONG).show();        	  	
	       	  }});
	       	 myAlertDialog.show();
        }       
        
        try{
        	db = new DBAdapter(getApplicationContext());    
        	db.close();
        }catch(Exception e){
        	Toast.makeText(this, "Ocorreu um erro no sistema.", Toast.LENGTH_LONG).show();
        }
        
        spArena = (Spinner) findViewById(R.id.spArena);
        etQuantidade = (EditText) findViewById(R.id.etQuantity);
        btRegisterScore = (Button) findViewById(R.id.btRegisterScore);
        
        appContext = getApplicationContext();
        
        try{
	        spArena = getArenasSpinner(spArena);   
        }catch(Exception e){
    		e.printStackTrace();
        }
        
        if (drop == false){
        	refreshNickname();
        	refreshEmail();

	        if(nickname == null || nickname.equalsIgnoreCase("")){
	        	createRegister();
	        }
	        
	        if(email == null || email.equalsIgnoreCase("")){
	        	createRegister();
	        }        
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, 1, Menu.NONE, R.string.info );
        menu.add(0, 2, Menu.NONE, R.string.reports );
        return result;
    }
    
    public void openMenu(View view){
        switch (view.getId()) {
        case R.id.ivMenu:
        	openOptionsMenu();
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
          case 1:   	
              Toast.makeText(this, "Gol a Gol v1.5\nDesenvolvido por Oranz", Toast.LENGTH_LONG).show();
              return super.onOptionsItemSelected(item);
          case 2:	    	
  	    	Intent nextScreen = new Intent(getApplicationContext(), ScoreReport.class);	    	
  	    	startActivity(nextScreen);
  	        return super.onOptionsItemSelected(item);        	  
        }
        return super.onOptionsItemSelected(item);
    }
    
    public static Spinner getSpArena(){
    	return spArena;
    }
    
    public static void refreshArenaSpinner(Spinner auxSpinner) throws Exception{
    	spArena = getArenasSpinner(auxSpinner);
    }
    
    public static Spinner getArenasSpinner(Spinner auxSpinner) throws Exception{
    	try {
	    	db.open();
		    Cursor cursorArenas = db.getAllArenas();
		    
		    listArenas = new ArrayList<String>();
		    cursorArenas.moveToFirst();
			while (cursorArenas.isAfterLast() == false){
				String arena = cursorArenas.getString(cursorArenas.getColumnIndex("nm_arena"));
				if(!listArenas.contains(arena))
					listArenas.add(arena);
				cursorArenas.moveToNext();
			}
			
			db.close();
			cursorArenas.close();		    
		    
		    array_spinner=new String[listArenas.size()+1];
		    array_spinner[0]="NÃO IDENTIFICADA";
		    for(int i = 0; i < listArenas.size(); i++){
		    	array_spinner[i+1] = listArenas.get(i);
		    }
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(appContext, android.R.layout.simple_spinner_item, array_spinner);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    auxSpinner.setAdapter(adapter);
		    auxSpinner.setSelection(array_spinner.length - 1);
    	}catch(Exception e){
    		Toast.makeText(appContext, "Ocorreu um erro ao carregar as arenas", Toast.LENGTH_LONG).show();
    	}
    	
    	return auxSpinner;
    }
    
    public void refreshNickname(){
    	nickname = "";
    	db.open();
        Cursor cursor = db.getRegister();
        cursor.moveToFirst();
    	while (cursor.isAfterLast() == false){ 
    		nickname = cursor.getString(cursor.getColumnIndex("nickname"));
    		cursor.moveToNext();
    	}
        db.close();
        cursor.close();
    }
    
    public void refreshEmail(){
    	email = "";
    	db.open();
        Cursor cursor = db.getRegister();
        cursor.moveToFirst();
    	while (cursor.isAfterLast() == false){ 
    		email = cursor.getString(cursor.getColumnIndex("fullname"));
    		cursor.moveToNext();
    	}
        db.close();
        cursor.close();
    }
        
	public void createRegister(){
    	LayoutInflater factory = LayoutInflater.from(this);

        final View textEntryView = factory.inflate(R.layout.layout_dialog_register, null);

        final EditText etNickname = (EditText) textEntryView.findViewById(R.id.etNickname);
        final EditText etEmail = (EditText) textEntryView.findViewById(R.id.etEmail);
        
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setIcon(R.drawable.icon).setTitle("Identificação").setView(textEntryView).setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int whichButton) {
            
        	   nickname = etNickname.getText().toString();
               email = etEmail.getText().toString();
               
               if(nickname == "" || nickname.equalsIgnoreCase("")){
            	   String mensagem = "Você precisa escolher um apelido";
            	   Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
            	   createRegister();
               }

               if(email == "" || email.equalsIgnoreCase("") || !(email.contains("@")) || !(email.contains("."))){
            	   String mensagem = "Você precisa inserir um email válido";
            	   Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
            	   createRegister();
               }               
               
               db.open();
               db.insertNickname(nickname, email);
               db.close();
        	   
        	   String mensagem = "Olá, " + nickname;
        	   Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
           }
          }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog,
             int whichButton) {
        	   String mensagem = "Você precisa se identificar";
        	   Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
        	   createRegister();
           }
          });
        alert.show();
    }    
    
    public void registerScore(View view){
        switch (view.getId()) {
        case R.id.btRegisterScore:
        	refreshNickname();
        	if (nickname == null || nickname.equalsIgnoreCase("")){
        		createRegister();
        		return;
        	}
        	
	        if (etQuantidade.getText().length() == 0 || Float.parseFloat(etQuantidade.getText().toString()) <= 0) {
	        	String mensagem = "";
	        	mensagem = "Só faltou falar quantos gols!";
	            Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
	            return;
	        }

	        dpDataScore = (DatePicker) findViewById(R.id.dpScoreDate);
	    	
	    	Intent nextScreen = new Intent(getApplicationContext(), ConfirmScore.class);
	    	
	    	arena = spArena.getSelectedItem().toString();
	    	quantity = Integer.parseInt(etQuantidade.getText().toString());
	    	date = Calendar.getInstance();
	    	date.set(dpDataScore.getYear(), dpDataScore.getMonth(), dpDataScore.getDayOfMonth());
	    	
	    	startActivity(nextScreen);
        }
    }
    
    public void registerScoreSmall(View view){
        switch (view.getId()) {
        case R.id.btRegisterScoreSmall:
        	refreshNickname();
        	if (nickname == null || nickname.equalsIgnoreCase("")){
        		createRegister();
        		return;
        	}
        	
	        if (etQuantidade.getText().length() == 0 || Float.parseFloat(etQuantidade.getText().toString()) <= 0) {
	        	String mensagem = "";
	        	mensagem = "Só faltou falar quantos gols!";
	            Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
	            return;
	        }	        
	    	
	    	Intent nextScreen = new Intent(getApplicationContext(), ConfirmScore.class);
	    	
	    	arena = spArena.getSelectedItem().toString();
	    	quantity = Integer.parseInt(etQuantidade.getText().toString());
	    	date = Calendar.getInstance();
	    	
	    	startActivity(nextScreen);
        }
    }

    public static int getTotal(int qtd) {
    	int total= 0;
    	try{
    		if(qtd == 0){
    			total = db.getTotalScore(0);
    		}else if(qtd == 7){
    			total = db.getTotalScore(7);    			
    		}else if(qtd == 15){
    			total = db.getTotalScore(15);    			
    		}else if(qtd == 30){
    			total = db.getTotalScore(30);    			
    		}    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}        
        return total;
	}
    
    public static void printAllScores(){
    	db.open();
        Cursor cursor = db.getAllScores();
        cursor.moveToFirst();
    	while (cursor.isAfterLast() == false){ 
    		System.out.println(cursor.getString(cursor.getColumnIndex("arena_id")) + " >> "+ cursor.getString(cursor.getColumnIndex("score_quantity")) + " >> " + cursor.getString(cursor.getColumnIndex("score_date")));
    		cursor.moveToNext();
    	}
        db.close();
        cursor.close();
    }
    
    public static void registerScoreConfirmed(String arena, int quantity, Calendar date){
    	db.open();
        db.registerScore(arena, quantity, date);
        db.close();
        
		Toast.makeText(appContext,"Gols gravados com sucesso!", Toast.LENGTH_LONG).show();
    }
    
    public static List<Score> getAllScores(){
    	List<Score> listScores = new ArrayList<Score>();
    	
    	db.open();
        Cursor cursor = db.getScoresByArena("");
        cursor.moveToFirst();
    	while (cursor.isAfterLast() == false){
    		Score scoreAux = new Score();
    		scoreAux.setArena(cursor.getString(cursor.getColumnIndex("nm_arena")));
    		scoreAux.setQuantity(cursor.getInt(cursor.getColumnIndex("score_quantity")));
    		long longAux= cursor.getLong(cursor.getColumnIndex("score_date"));
    		Date myDateNew = new Date(longAux);
    		scoreAux.setDate(myDateNew);
    		listScores.add(scoreAux);
    		cursor.moveToNext();
    	}
        db.close();
        cursor.close();
    	
    	return listScores;
    }
    
    public static List<String> getAllArenas(){
    	List<String> listArenas = new ArrayList<String>();
    	
    	db.open();
        Cursor cursor = db.getAllArenas();
        cursor.moveToFirst();
    	while (cursor.isAfterLast() == false){
    		String arena = cursor.getString(cursor.getColumnIndex("nm_arena"));
    		cursor.moveToNext();
    	}
        db.close();
        cursor.close();
    	
    	return listArenas;
    }
    
    public static void createArena(String arena){
    	db.open();
    	if(!listArenas.contains(arena))
    		db.insertArena(arena);
        db.close();
    }
    
    public static boolean removeArena(String nm_arena){
    	db.open();
    	boolean hasScore = false;
    	Cursor cursor = db.getScoresByArena(nm_arena);
        cursor.moveToFirst();
    	while (cursor.isAfterLast() == false){ 
    		hasScore = true;
    		String arenaTemp = cursor.getString(cursor.getColumnIndex("nm_arena"));
    		cursor.moveToNext();
    	}
        cursor.close();
        
    	if(hasScore == false){
        	db.deleteArena(nm_arena);
        	db.close();
        	return true;
    	}
    	    	
        db.close();
        return false;
    }
    
    public void openConfig(View view){
        switch (view.getId()) {
        case R.id.ivConfig:	    	
	    	Intent nextScreen = new Intent(getApplicationContext(), Config.class);	    	
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
	
	public static void setMessage(String msg){
		message = msg;
	}
	
	public static String getMessage(){
		return message;
	}

	public static Context getAppContext() {
		return appContext;
	}

	public static void setAppContext(Context appContext) {
		SaveScore.appContext = appContext;
	}	

	public static String getNickname() {
		return nickname;
	}

	public static void setNickname(String nickname) {
		SaveScore.nickname = nickname;

		try{
	    	db.open();
			db.updateRegister(nickname, email);
			db.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static String getEmail() {
		return email;
	}

	public static void setEmail(String email) {
		SaveScore.email = email;

		try{
	    	db.open();
			db.updateRegister(nickname, email);
			db.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
}