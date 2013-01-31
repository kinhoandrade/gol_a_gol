package com.oranz.golagol;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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

@SuppressWarnings("unused")
public class SaveScore extends Activity {

	private static DBAdapter db;
	
	private Spinner spArena;
	private EditText etQuantidade;
	private EditText etNickname;
	private EditText etFullname;
	private DatePicker dpDataScore;
	private Button btRegisterScore;
	
	private List<String> arenasList;
	
    private String array_spinner[];
    
	private static String arena;
	private static int quantity;
	private static Calendar date;
	private static List<String> listArenas;
	private static String nickname;
	private static String fullname;
	
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
        dpDataScore = (DatePicker) findViewById(R.id.dpScoreDate);
        btRegisterScore = (Button) findViewById(R.id.btRegisterScore);
        
        
        try{
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
	        array_spinner[0]="PADRÃO";
	        for(int i = 0; i < listArenas.size(); i++){
	        	array_spinner[i+1] = listArenas.get(i);
	        }
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array_spinner);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spArena.setAdapter(adapter);
	        spArena.setSelection(0);   
        }catch(Exception e){
    		e.printStackTrace();
        }
        
        if (drop == false){
        	refreshNickname();

	        if(nickname == null || nickname.equalsIgnoreCase("")){
	        	createNickname2();
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
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
          case 1:   	
              Toast.makeText(this, "Gol a Gol v1.0\nDesenvolvido por Oranz", Toast.LENGTH_LONG).show();
          case 2:	    	
  	    	Intent nextScreen = new Intent(getApplicationContext(), ScoreReport.class);	    	
  	    	startActivity(nextScreen);
        	  
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void refreshNickname(){
    	nickname = "";
    	db.open();
        Cursor cursor = db.getNickname();
        cursor.moveToFirst();
    	while (cursor.isAfterLast() == false){ 
    		nickname = cursor.getString(cursor.getColumnIndex("nickname"));
    		cursor.moveToNext();
    	}
        db.close();
        cursor.close();
    }
    
	public void createNickname(){
        AlertDialog.Builder editalert = new AlertDialog.Builder(this);

        editalert.setTitle("Apelido");
        editalert.setMessage("Identifique-se");

        etNickname = new EditText(this);
        etFullname = new EditText(this);
        @SuppressWarnings("deprecation")
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        etNickname.setLayoutParams(lp);
        etNickname.setHeight(100);
        editalert.setView(etNickname);
        etFullname.setLayoutParams(lp);
        etFullname.setHeight(100);
        editalert.setView(etFullname);

        editalert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                nickname = etNickname.getText().toString();
                fullname = etFullname.getText().toString();
                
                db.open();
                db.insertNickname(nickname, fullname);
                db.close();
            }
        });

        editalert.show();    	
    }
    
	public void createNickname2(){
    	LayoutInflater factory = LayoutInflater.from(this);

        final View textEntryView = factory.inflate(R.layout.layout_dialog_register, null);
           //text_entry is an Layout XML file containing two text field to display in alert dialog

        final EditText etNickname = (EditText) textEntryView.findViewById(R.id.etNickname);
        final EditText etFullname = (EditText) textEntryView.findViewById(R.id.etFullname);
        
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setIcon(R.drawable.icon).setTitle("Identificação").setView(textEntryView).setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog,
             int whichButton) {
            
        	   nickname = etNickname.getText().toString();
               fullname = etFullname.getText().toString();
               
               if(nickname == "" || nickname.equalsIgnoreCase("")){
            	   String mensagem = "Você precisa se identificar";
            	   Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
            	   createNickname2();
               }
               
               db.open();
               db.insertNickname(nickname, fullname);
               db.close();
        	   
        	   String mensagem = "Olá, " + nickname;
        	   Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
           }
          }).setNegativeButton("Cancelar",
          new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog,
             int whichButton) {
        	   String mensagem = "Você precisa se identificar";
        	   Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
        	   createNickname2();
           }
          });
        alert.show();
    }    
    
    public void registerScore(View view){
        switch (view.getId()) {
        case R.id.btRegisterScore:
        	refreshNickname();
        	if (nickname == null || nickname.equalsIgnoreCase("")){
        		createNickname2();
        		return;
        	}
        	
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
	    	date.set(dpDataScore.getYear(), dpDataScore.getMonth(), dpDataScore.getDayOfMonth());
	    	
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
    		System.out.println(cursor.getString(cursor.getColumnIndex("score_quantity")) + " >> " + cursor.getString(cursor.getColumnIndex("score_date")));
    		cursor.moveToNext();
    	}
        db.close();
        cursor.close();
    }
    
    public static void registerScoreConfirmed(String arena, int quantity, Calendar date){
    	db.open();
        db.registerScore(arena, quantity, date);
        db.close();
    }
    
    public static void createArena(String arena){
    	db.open();
        db.insertArena(arena);
        db.close();
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
	
	public static String getNickname(){
		return nickname;
	}
}