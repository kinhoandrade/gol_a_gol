package com.oranz.golagol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_scores);
        btClose = (Button) findViewById(R.id.btCloseList);
        totalView = (TextView) findViewById(R.id.tvTotalList);
        listView = (ListView) findViewById(R.id.listScores);
        
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
  	  item.put( "arena", arena);
  	  item.put( "quantity", quantity);
  	  item.put( "id", id);
  	  list.add( item );
      notes.notifyDataSetChanged();
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
			addItem((myCal.get(Calendar.DAY_OF_MONTH) + "/" + (myCal.get(Calendar.MONTH) + 1) + "/" + myCal.get(Calendar.YEAR)), ("Arena: " + scoreAux.getArena().toString()),("Gols: " + scoreAux.getQuantity()), "" + scoreAux.getCd_score());
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
        switch ( item.getItemId() ) {
          case 1:   	
              Toast.makeText(this, "Exportando...", Toast.LENGTH_LONG).show();
              export();
              return super.onOptionsItemSelected(item);
          case 2:  	
              Toast.makeText(this, "Importando...", Toast.LENGTH_LONG).show();
              importFile();
              return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
    
    public boolean export(){
    	String filename = "golagol-data.csv";
    	File file = new File(Environment.getExternalStorageDirectory(), filename);
    	FileOutputStream fos;
    	byte[] data = new String("Gol a Gol").getBytes();
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
    	return false;
    }
    
    @SuppressWarnings("resource")
	public boolean importFile(){
    	//Find the directory for the SD Card using the API
    	//*Don't* hardcode "/sdcard"
    	File sdcard = Environment.getExternalStorageDirectory();

    	File file = new File(sdcard,"golagol-data.csv");

    	//Read text from file
    	StringBuilder text = new StringBuilder();
    	try {
    	    BufferedReader br = new BufferedReader(new FileReader(file));
    	    String line;

    	    while ((line = br.readLine()) != null) {
    	        text.append(line);
    	        text.append('\n');
    	    }
    	}
    	catch (IOException e) {
    	    e.printStackTrace();
    	}

    	Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    	    	
    	return false;
    }    
}