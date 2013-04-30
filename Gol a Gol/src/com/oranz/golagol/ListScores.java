package com.oranz.golagol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.oranz.golagol.entidades.Score;

public class ListScores extends Activity {
	private Button btClose;
	private TextView totalView; 
	ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    private SimpleAdapter notes;
    private ListView listView;
    private List<String> scoresText;
    private String email;
	private String fileCompletePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_scores);
        btClose = (Button) findViewById(R.id.btCloseList);
        totalView = (TextView) findViewById(R.id.tvTotalList);
        listView = (ListView) findViewById(R.id.listScores);
        
        scoresText = new ArrayList<String>();
        scoresText.add("Email;Data;Arena;Quantity");
        email = SaveScore.getEmail();
        
        notes = new SimpleAdapter( 
 				this, 
 				list,
 				R.layout.main_item_two_line_row,
 				new String[] { "date","arena", "quantity","id" },
 				new int[] { R.id.text1, R.id.text2, R.id.text3, R.id.text4 }  );
        
        listView.setAdapter(notes);
        
        refreshList();
        
        btClose.setOnClickListener(new View.OnClickListener() {public void onClick(View arg0) {finish();}});
        
        listView.setOnItemClickListener(new OnItemClickListener() {
			
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				final HashMap<String, String> score = (HashMap<String, String>) arg0.getItemAtPosition(arg2);				

		       	AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(ListScores.this);
		       	myAlertDialog.setTitle("Apagar registro");
		       	myAlertDialog.setMessage("Deseja apagar este Gol?\n" + score.get("arena") + " - " + score.get("quantity") + "\nData: " + score.get("date") );
		       	myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		       		public void onClick(DialogInterface arg0, int arg1) {
		       			SaveScore.removeRegister(score.get("id"));
		       			Toast.makeText(getApplicationContext(), "Gol apagado.", Toast.LENGTH_LONG).show();				       	
				       	refreshList();
		       	}});
		       	myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {       	       
		       		public void onClick(DialogInterface arg0, int arg1) { }});
		       	myAlertDialog.show();
			}
		});
        
        listView.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {	        
				Toast.makeText(getApplicationContext(),"Teste", Toast.LENGTH_LONG).show();
				return false;
			}
		});
    }
    
    public void refreshList(){
        try {
           	filteredList();
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "ERROR: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void addItem(String date, String arena, String quantity, String id) {
  	  HashMap<String,String> item = new HashMap<String,String>();
  	  item.put( "date", "[" + date +"]" );
  	  item.put( "arena", "Arena: " + arena.trim());
  	  item.put( "quantity", "Gols: " + quantity);
  	  item.put( "id", id);
  	  list.add( item );
      notes.notifyDataSetChanged();
      scoresText.add(email.trim() + ";" + date + ";" + arena.replace(";", ".").replace(":","-").trim() + ";" + quantity);
  	}
    
	public void filteredList(){
    	list.clear();
    	List<Score> listaCompras = new ArrayList<Score>();
    	
    	List<Score> listaComprasList = new ArrayList<Score>();
    	
        int total = 0;
    	
        listaCompras = SaveScore.getAllScores();
    	for (Score scoreAux : listaCompras) {
    		listaComprasList.add(scoreAux);
		}

    	int listaSize = listaComprasList.size();
        for (int i = 0; i < listaSize; i++) {
        	Score scoreAux = listaComprasList.get(i);
        	Date dateAux = scoreAux.getDate();
        	Calendar myCal = Calendar.getInstance();
        	myCal.setTime(dateAux);
			addItem((myCal.get(Calendar.DAY_OF_MONTH) + "/" + (myCal.get(Calendar.MONTH) + 1) + "/" + myCal.get(Calendar.YEAR)), (scoreAux.getArena().toString()),("" + scoreAux.getQuantity()), "" + scoreAux.getCd_score());
			total += scoreAux.getQuantity();
		}

        totalView.setVisibility(1);
        totalView.setText("\nTotal: " + total);   
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, 1, Menu.NONE, R.string.export );
        menu.add(0, 2, Menu.NONE, R.string.importfile );
        return result;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String directory = Environment.getExternalStorageDirectory().toString();
        String filename = "golagol-data.csv";
    	
        switch ( item.getItemId() ) {
          case 1: 
              Toast.makeText(this, "Exportando...", Toast.LENGTH_LONG).show();
              fileCompletePath = export(directory, filename);
          	  Toast.makeText(this, "Exportado:\n" +  fileCompletePath, Toast.LENGTH_LONG).show();
              return super.onOptionsItemSelected(item);
          case 2:
              importFile(directory + "/" + filename);
              return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
    
    public String export(String directory, String filename){
    	File file = new File(directory, filename);
    	FileOutputStream fos;
    	String fullText = "";
    	for(int i = 0; i < scoresText.size(); i++){
    		fullText = fullText + (scoresText.get(i)) + "\n";
    	}
    	byte[] data = new String(fullText).getBytes();
    	try {
    	    fos = new FileOutputStream(file);
    	    fos.write(data);
    	    fos.flush();
    	    fos.close();
    	} catch (FileNotFoundException e) {
    	    e.printStackTrace();
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
    	return Environment.getExternalStorageDirectory().toString() + "/" + filename;
    }
    
    @SuppressWarnings("resource")
	public boolean importFile(final String fileCompletePath){
    	AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(ListScores.this);
       	myAlertDialog.setTitle("Importar registros");
       	myAlertDialog.setMessage("Deseja importar os registros do arquivo " + fileCompletePath + "?");
       	myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
       		public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "Importando\n" + fileCompletePath, Toast.LENGTH_LONG).show();
		
		    	File file = new File(fileCompletePath);

		    	try {
		    	    BufferedReader br = new BufferedReader(new FileReader(file));
		    	    String line;
		    	    line = br.readLine();
		    	    while ((line = br.readLine()) != null) {
		    	        String[] scoreText = line.split(";");
		    	        
		    	        String date = scoreText[1];
		    	        String arena = scoreText[2];
		    	        String quantity = scoreText[3];
		    	        Date formattedDate = formatStringToDate(date);
		    	        Calendar cal=Calendar.getInstance();
		    	        cal.setTime(formattedDate);
		    	        //SaveScore.insertScore(arena[1], Integer.parseInt(quantity[1]), cal);
		    	        SaveScore.registerScoreConfirmed(arena, Integer.parseInt(quantity), cal);
		    	    }
		    	}
		    	catch (IOException e) {
		    	    e.printStackTrace();
		    	}
		    	
       			Toast.makeText(getApplicationContext(), "Arquivo importado.", Toast.LENGTH_LONG).show();
       			refreshList();
       	}});
       	myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {       	       
       		public void onClick(DialogInterface arg0, int arg1) { }});
       	myAlertDialog.show();
    	
    	    	
    	return false;
    }    
    
    public Date formatStringToDate(String date){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date formattedDate = new Date();
        try {
        	formattedDate = df.parse(date);
            String newDateString = df.format(formattedDate);
            System.out.println(newDateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
}