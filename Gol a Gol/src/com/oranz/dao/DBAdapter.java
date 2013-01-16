package com.oranz.dao;

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
    
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
        "create table if not exists register(_id integer primary key autoincrement, "
        + "nickname text not null," 
        + "date_created date not null,"
        + "user_id integer);";
    
    private static final String DATABASE_CREATE2 =
        "create table if not exists arena (_id integer primary key autoincrement, "
        + "nm_arena text not null);";
    
    private static final String DATABASE_CREATE3 =
            "create table if not exists score(_id integer primary key autoincrement, "
            + "arena_id integer not null," 
            + "score_quantity integer not null,"
            + "score_date date not null,"
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
            db.execSQL("DROP TABLE IF EXISTS register");
            db.execSQL("DROP TABLE IF EXISTS arena");
            db.execSQL("DROP TABLE IF EXISTS score");
            onCreate(db);
        }
    }    
    
    public void dropTables(){    	
        db.execSQL("DROP TABLE IF EXISTS register");
        db.execSQL("DROP TABLE IF EXISTS arena");
        db.execSQL("DROP TABLE IF EXISTS score");
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
    
	public long insertNickname(String nickname){
    	long retorno = 0;
    	try{
    		/*
	        ContentValues initialValues = new ContentValues();
	        initialValues.put(KEY_NICKNAME, nickname);
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			Date date = new Date();
	        initialValues.put(KEY_DATE_CREATED, dateFormat.format(date));
	        retorno = db.insert(DATABASE_TABLE, null, initialValues);
	        */
    		db.execSQL("INSERT INTO "+DATABASE_TABLE+" (nickname, date_created) VALUES ('"+ nickname +"', datetime()) ");
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	this.close();
        } 
        return retorno;
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

    public Cursor getNickname() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		KEY_ROWID, 
        		KEY_NICKNAME}, 
                null, 
                null, 
                null, 
                null, 
                null);
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
       
    //---updates a title---
    public boolean updateRegister(long rowId, String nickname, int user_id) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NICKNAME, nickname);
        args.put(KEY_USER_ID, user_id);
        return db.update(DATABASE_TABLE2, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}