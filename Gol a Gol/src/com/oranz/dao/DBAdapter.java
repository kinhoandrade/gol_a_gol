package com.oranz.dao;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter 
{ 
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NICKNAME = "nickname";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_DATE_CREATED = "date_created";
    public static final String KEY_USER_ID = "user_id";

    public static final String KEY_ROWID2 = "_id";
    public static final String KEY_NM_ARENA = "nm_arena";
    
    public static final String KEY_ROWID3 = "_id";
    public static final String KEY_ARENA_ID = "arena_id";
    public static final String KEY_SCORE_QUANTITY = "score_quantity";
    public static final String KEY_SCORE_DATE = "score_date";
    public static final String KEY_USER_ID2 = "user_id";
    
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "gol_a_gol";
    private static final String DATABASE_TABLE = "register";
    private static final String DATABASE_TABLE2 = "arena";
    private static final String DATABASE_TABLE3 = "score";
    private static final String DATABASE_TABLE4 = "friends";
    
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
        "create table if not exists register(_id integer primary key autoincrement, "
        + "nickname text not null," 
        + "fullname text," 
        + "date_created date not null,"
        + "user_id integer);";
    
    private static final String DATABASE_CREATE2 =
        "create table if not exists arena (_id integer primary key autoincrement, "
        + "nm_arena text not null);";
    
    private static final String DATABASE_CREATE3 =
            "create table if not exists score(_id integer primary key autoincrement, "
            + "arena_id integer not null," 
            + "score_quantity integer not null,"
            + "score_date datetime not null,"
            + "user_id integer);";

    private static final String DATABASE_CREATE4 =
            "create table if not exists friends(_id integer primary key autoincrement, "
            + "friend_id integer not null,"
            + "friend_score_quantity integer not null,"
            + "date_created date not null);";

    private final Context context;  
    
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.onCreate(getWritableDatabase());
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {        	
            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE2);
            db.execSQL(DATABASE_CREATE3);
            db.execSQL(DATABASE_CREATE4);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                  + " to "
                  + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE2);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE3);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE4);
            onCreate(db);
        }
    }    
    
    public void dropTables(){
    	this.open();
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE2);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE3);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE4);
        this.close();
    }
    
    //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    
    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
	public long insertArena(String nm_arena){
    	long retorno = 0;
    	try{
	        ContentValues initialValues = new ContentValues();
	        initialValues.put(KEY_NM_ARENA, nm_arena);
	        retorno = db.insert(DATABASE_TABLE2, null, initialValues);
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	this.close();
        } 
        return retorno;
    }
    
    //---deletes a particular title---
    public boolean deleteArena(long rowId) 
    {
        return db.delete(DATABASE_TABLE2, KEY_ROWID2 + "=" + rowId, null) > 0;
    }
    
    public boolean deleteArena(String nm_arena) 
    {
        return db.delete(DATABASE_TABLE2, KEY_NM_ARENA + "='" + nm_arena + "'", null) > 0;
    }
        
    public int deleteAllArenas(){
    	return db.delete(DATABASE_TABLE2, "_id > 0", null);
    }
    
    public Cursor getAllArenas() 
    {
        return db.query(DATABASE_TABLE2, new String[] {
        		KEY_ROWID2, 
        		KEY_NM_ARENA}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }
    
    public int getArenaId(String nm_arena){
    	Cursor cursor = null;
    	try{
    		int id_arena = 0;
    		this.open();
	    	cursor = db.rawQuery("SELECT * FROM arena WHERE TRIM(nm_arena) = '" + nm_arena.trim() + "'", null);
	    	cursor.moveToFirst();
	    	while (cursor.isAfterLast() == false){ 
		    	id_arena = cursor.getInt(cursor.getColumnIndex("_id"));
	    		cursor.moveToNext();
	    	}
	    	cursor.close();
	    	return id_arena;
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		cursor.close();
    		this.close();
    	}
    	return 0;
    }

	public long insertNickname(String nickname, String fullname){
    	long retorno = 0;
    	try{
    		db.execSQL("INSERT INTO "+DATABASE_TABLE+" (nickname, fullname, date_created) VALUES ('"+ nickname +"','"+ fullname +"', datetime()) ");
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	this.close();
        } 
        return retorno;
    }
    
    public Cursor getNickname() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		KEY_ROWID, 
        		KEY_NICKNAME,
        		KEY_FULLNAME,
        		KEY_USER_ID}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }
    
    public int getUserId(){
    	try{
    		this.open();
	    	Cursor cursor = db.rawQuery("SELECT * FROM register", null);
	    	cursor.moveToFirst();
	    	int user_id = cursor.getInt(cursor.getColumnIndex("_id"));
	    	cursor.close();
	    	return user_id;
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		this.close();
    	}
    	return 0;
    }
    
	public long registerScore(String nm_arena, int quantity, Calendar date){
    	long retorno = 0;
    	try{
    		int id_arena = getArenaId(nm_arena);
    		int user_id = getUserId();
    		this.open();
    		db.execSQL("INSERT INTO "+DATABASE_TABLE3+" (arena_id, score_quantity, score_date, user_id) VALUES ("+ id_arena +"," + quantity + ", '" + date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.MONTH) + 1)  + "/" + date.get(Calendar.YEAR) + "', " + user_id +") ");
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	this.close();
        } 
        return retorno;
    }
    
    public Cursor getAllScores() 
    {
        return db.query(DATABASE_TABLE3, new String[] {
        		KEY_ROWID3,
                KEY_ARENA_ID,
                KEY_SCORE_QUANTITY,
                KEY_SCORE_DATE,
                KEY_USER_ID2}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }
    
    public int getTotalScore(int qtd){
    	int total = 0;
    	try{
    		this.open();
    		if(qtd == 0){
    			String query = "SELECT SUM(score_quantity) FROM score";
	    		Cursor cursor = db.rawQuery(query, null);
    			if(cursor.moveToFirst()) {
    			    total = cursor.getInt(0);
    			}
		    	cursor.close();
    		}else if(qtd == 7){
    			Calendar dateAux = Calendar.getInstance();
    			dateAux.add(Calendar.DATE, -7);
    			String query = "SELECT SUM(score_quantity) FROM score WHERE score_date >= '" + dateAux.get(Calendar.DAY_OF_MONTH) + "/" + (dateAux.get(Calendar.MONTH)+1) + "/" + dateAux.get(Calendar.YEAR) + "'";
    			Cursor cursor = db.rawQuery(query, null);
    			cursor.moveToFirst();
        		while (cursor.isAfterLast() == false){ 
    			    total = cursor.getInt(0);
            		cursor.moveToNext();
            	}
        		System.out.println(query + " - " + total);
    	    	cursor.close();
        	}else if(qtd == 15){
    			Calendar dateAux = Calendar.getInstance();
    			dateAux.add(Calendar.DATE, -15);
    			//String query = "SELECT SUM(score_quantity) FROM score WHERE score_date >= '" + dateAux.get(Calendar.DAY_OF_MONTH) + "-" + (dateAux.get(Calendar.MONTH)+1) + "-" + dateAux.get(Calendar.YEAR) + "'";
    			String query = "SELECT SUM(score_quantity) FROM score WHERE score_date >= '" + dateAux.get(Calendar.DAY_OF_MONTH) + "/" + (dateAux.get(Calendar.MONTH)+1) + "/" + dateAux.get(Calendar.YEAR) + "'";
    			Cursor cursor = db.rawQuery(query, null);
    			cursor.moveToFirst();
        		while (cursor.isAfterLast() == false){ 
    			    total = cursor.getInt(0);
            		cursor.moveToNext();
            	}
        		System.out.println(query + " - " + total);
    	    	cursor.close();
        	}else if(qtd == 30){
    			Calendar dateAux = Calendar.getInstance();
    			dateAux.add(Calendar.DATE, -30);
    			//String query = "SELECT SUM(score_quantity) FROM score WHERE score_date >= date('now','-30 days')";
    			//String query = "SELECT SUM(score_quantity) FROM score WHERE score_date >= '" + dateAux.get(Calendar.DAY_OF_MONTH) + "-" + (dateAux.get(Calendar.MONTH)+1) + "-" + dateAux.get(Calendar.YEAR) + "'";
    			String query = "SELECT SUM(score_quantity) FROM score WHERE score_date >= '" + dateAux.get(Calendar.DAY_OF_MONTH) + "/" + (dateAux.get(Calendar.MONTH)+1) + "/" + dateAux.get(Calendar.YEAR) + "'";
    			Cursor cursor = db.rawQuery(query, null);
    			cursor.moveToFirst();
        		while (cursor.isAfterLast() == false){ 
    			    total = cursor.getInt(0);
            		cursor.moveToNext();
            	}
        		System.out.println(query + " - " + total);
    	    	cursor.close();
        	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		this.close();
    	}
    	return total;
    }
       
    //---updates a title---
    public boolean updateRegister(long rowId, String nickname, int user_id) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NICKNAME, nickname);
        args.put(KEY_USER_ID, user_id);
        return db.update(DATABASE_TABLE2, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}