package com.oranz.golagol;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.oranz.golagol.entidades.Score;

public class ListScores extends ListActivity {
	private Button btClose;
	private TextView totalView; 
	ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    private SimpleAdapter notes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_scores);
        btClose = (Button) findViewById(R.id.btCloseList);
        totalView = (TextView) findViewById(R.id.tvTotalList);        

        notes = new SimpleAdapter( 
 				this, 
 				list,
 				R.layout.main_item_two_line_row,
 				new String[] { "line1","line2" },
 				new int[] { R.id.text1, R.id.text2 }  );
        setListAdapter( notes );
        
        refreshList();
        
        btClose.setOnClickListener(new View.OnClickListener() {public void onClick(View arg0) {finish();}});
    }
    
    public void refreshList(){
        try {
           	filteredList();
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "ERROR: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void addItem(String first, String second) {
  	  HashMap<String,String> item = new HashMap<String,String>();
  	  item.put( "line1", first );
  	  item.put( "line2", second);
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
			addItem(("[" + myCal.get(Calendar.DAY_OF_MONTH) + "/" + (myCal.get(Calendar.MONTH) + 1) + "/" + myCal.get(Calendar.YEAR) + "] Arena: " + scoreAux.getArena().toString()),("Gols: " + scoreAux.getQuantity()));
			total += scoreAux.getQuantity();
		}

        totalView.setVisibility(1);
        totalView.setText("\nTotal: " + total);   
    }
}