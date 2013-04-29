package com.oranz.dao;

import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter 
{ 
    private static final String TAG = "DBAdapter";    
    
    private static final int DATABASE_VERSION = 1;
    
    private static final String DATABASE_NAME = "gol_a_gol";
    private static final String DATABASE_TABLE_REGISTER = "register";
    private static final String DATABASE_TABLE_ARENA = "arena";
    private static final String DATABASE_TABLE_SCORE = "score";
    private static final String DATABASE_TABLE_FRIENDS = "friends";
    private static final String DATABASE_TABLE_PASSWORD = "user_password";
	
    public static final String TABLE_REGISTER_KEY_ROWID = "_id";
    public static final String TABLE_REGISTER_KEY_NICKNAME = "nickname";
    public static final String TABLE_REGISTER_KEY_EMAIL = "fullname"; // email!!
    public static final String TABLE_REGISTER_KEY_DATE_CREATED = "date_created";
    public static final String TABLE_REGISTER_KEY_USER_ID = "user_id";

    public static final String TABLE_ARENA_KEY_ROWID = "_id";
    public static final String TABLE_ARENA_KEY_NM_ARENA = "nm_arena";
    
    public static final String TABLE_SCORE_KEY_ROWID = "_id";
    public static final String TABLE_SCORE_KEY_ARENA_ID = "arena_id";
    public static final String TABLE_SCORE_KEY_SCORE_QUANTITY = "score_quantity";
    public static final String TABLE_SCORE_KEY_SCORE_DATE = "score_date";
    public static final String TABLE_SCORE_KEY_USER_ID = "user_id";
    
    public static final String TABLE_PASSOWRD_KEY_USER_EMAIL = "email";
    public static final String TABLE_PASSWORD_KEY_PASSWORD = "passwd";

    private static final String DATABASE_CREATE =
        "create table if not exists " + DATABASE_TABLE_REGISTER + " (_id integer primary key autoincrement, "
        + "nickname text not null," 
        + "fullname text not null," // email !! 
        + "date_created int not null,"
        + "user_id integer);";
    
    private static final String DATABASE_CREATE2 =
        "create table if not exists " + DATABASE_TABLE_ARENA + " (_id integer primary key autoincrement, "
        + "nm_arena text not null);";
    
    private static final String DATABASE_CREATE3 =
            "create table if not exists " + DATABASE_TABLE_SCORE + "(_id integer primary key autoincrement, "
            + "arena_id integer not null," 
            + "score_quantity integer not null,"
            + "score_date int not null,"
            + "user_id integer);";

    private static final String DATABASE_CREATE4 =
            "create table if not exists " + DATABASE_TABLE_FRIENDS + "(_id integer primary key autoincrement, "
            + "friend_id integer not null,"
            + "friend_score_quantity integer not null,"
            + "date_created int not null);";

    private static final String DATABASE_CREATE5 =
            "create table if not exists " + DATABASE_TABLE_PASSWORD + "(email text not null, "
            + "passwd text not null);";
    
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
            db.execSQL(DATABASE_CREATE5);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                  + " to "
                  + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_REGISTER);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ARENA);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SCORE);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_FRIENDS);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_PASSWORD);
            onCreate(db);
        }
    }    
    
    public void dropTables(){
    	this.open();
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_REGISTER);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ARENA);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SCORE);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_FRIENDS);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_PASSWORD);
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
	        initialValues.put(TABLE_ARENA_KEY_NM_ARENA, nm_arena);
	        retorno = db.insert(DATABASE_TABLE_ARENA, null, initialValues);
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
        return db.delete(DATABASE_TABLE_ARENA, TABLE_ARENA_KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public boolean deleteArena(String nm_arena) 
    {
        return db.delete(DATABASE_TABLE_ARENA, TABLE_ARENA_KEY_NM_ARENA + "='" + nm_arena + "'", null) > 0;
    }
        
    public int deleteAllArenas(){
    	return db.delete(DATABASE_TABLE_ARENA, "_id > 0", null);
    }
    
    public Cursor getAllArenas() 
    {
        return db.query(DATABASE_TABLE_ARENA, new String[] {
        		TABLE_ARENA_KEY_ROWID, 
        		TABLE_ARENA_KEY_NM_ARENA}, 
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

	public long insertNickname(String nickname, String email){
    	long retorno = 0;
    	try{
    		Date date_created = new Date();
    		long timeMilliseconds = date_created.getTime();
    		db.execSQL("INSERT INTO " + DATABASE_TABLE_REGISTER + " (nickname, fullname, date_created) VALUES ('"+ nickname +"','"+ email +"'," + timeMilliseconds + ") ");
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	this.close();
        } 
        return retorno;
    }
    
    public Cursor getRegister() 
    {
        return db.query(DATABASE_TABLE_REGISTER, new String[] {
        		TABLE_REGISTER_KEY_ROWID, 
        		TABLE_REGISTER_KEY_NICKNAME,
        		TABLE_REGISTER_KEY_EMAIL, // email !!
        		TABLE_REGISTER_KEY_USER_ID}, 
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
    		Date date_created = date.getTime();
    		long timeMilliseconds = date_created.getTime();
    		this.open();
    		db.execSQL("INSERT INTO "+DATABASE_TABLE_SCORE+" (arena_id, score_quantity, score_date, user_id) VALUES ("+ id_arena +"," + quantity + ", " + timeMilliseconds+ ", " + user_id +") ");
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	this.close();
        } 
        return retorno;
    }
	
	public long updateScore(int quantity, int cd_score){
    	long retorno = 0;
    	try{
    		this.open();
    		String query = "UPDATE "+DATABASE_TABLE_SCORE+" SET score_quantity = " + quantity + " WHERE _id = '" + cd_score + "'";  
    		db.execSQL(query);
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	this.close();
        } 
        return retorno;
    }
    
    public boolean updateRegister(String nickname, String email) 
    {
        ContentValues args = new ContentValues();
        args.put(TABLE_REGISTER_KEY_NICKNAME, nickname);
        args.put(TABLE_REGISTER_KEY_EMAIL, email);
        return db.update(DATABASE_TABLE_REGISTER, args, TABLE_REGISTER_KEY_ROWID + ">" + 0, null) > 0;
    }    

    public String getPassword(){
    	String passwd = "";
    	try{
    		this.open();
	    	Cursor cursor = db.rawQuery("SELECT * FROM user_password", null);
	    	cursor.moveToFirst();
	    	passwd = cursor.getString(cursor.getColumnIndex(TABLE_PASSWORD_KEY_PASSWORD));
	    	cursor.close();
    	}catch(Exception e){
    		e.printStackTrace();
    		this.close();
    	}
    	return passwd;
    }
    
    public boolean updatePassword(String email, String passwd){
    	try{
    		String query = "UPDATE " + DATABASE_TABLE_PASSWORD + " SET " + TABLE_PASSWORD_KEY_PASSWORD + " = " + passwd + " WHERE " + TABLE_PASSOWRD_KEY_USER_EMAIL + " = '" + email + "'";
    		db.execSQL(query);
    		return true;
        }catch(Exception e){
        	e.printStackTrace();
        	return false;
        }finally{
        	this.close();
        }
    }

	public boolean insertPassword(String email, String passwd){
    	try{
    		String query = "INSERT INTO " + DATABASE_TABLE_PASSWORD + " (" + TABLE_PASSOWRD_KEY_USER_EMAIL +"," + TABLE_PASSWORD_KEY_PASSWORD + ") VALUES ('" + email + "','" + passwd + "')";
    		db.execSQL(query);
    		return true;
        }catch(Exception e){
        	e.printStackTrace();
        	return false;
        }finally{
        	this.close();
        } 
    }
    
	public boolean deleteScore(String cd_score) 
    {
        return db.delete(DATABASE_TABLE_SCORE, TABLE_SCORE_KEY_ROWID + "=" + cd_score, null) > 0;
    }
    
    public Cursor getAllScores() 
    {
        return db.query(DATABASE_TABLE_SCORE, new String[] {
        		TABLE_SCORE_KEY_ROWID,
                TABLE_SCORE_KEY_ARENA_ID,
                TABLE_SCORE_KEY_SCORE_QUANTITY,
                TABLE_SCORE_KEY_SCORE_DATE,
                TABLE_SCORE_KEY_USER_ID}, 
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
        		Date date_created = new Date();
        		long timeMilliseconds = date_created.getTime();
        		timeMilliseconds = timeMilliseconds - (7 * (24 * (3600 * 1000))); // 7 dias
    			String query = "SELECT SUM(score_quantity) FROM score WHERE score_date >= " + timeMilliseconds;
    			Cursor cursor = db.rawQuery(query, null);
    			cursor.moveToFirst();
        		while (cursor.isAfterLast() == false){ 
    			    total = cursor.getInt(0);
            		cursor.moveToNext();
            	}
    	    	cursor.close();
        	}else if(qtd == 15){
        		Date date_created = new Date();
        		long timeMilliseconds = date_created.getTime();
        		timeMilliseconds = timeMilliseconds - (15 * (24 * (3600 * 1000))); // 15 dias
    			String query = "SELECT SUM(score_quantity) FROM score WHERE score_date >= " + timeMilliseconds;
    			Cursor cursor = db.rawQuery(query, null);
    			cursor.moveToFirst();
        		while (cursor.isAfterLast() == false){ 
    			    total = cursor.getInt(0);
            		cursor.moveToNext();
            	}
    	    	cursor.close();
        	}else if(qtd == 30){
    			//Calendar dateAux = Calendar.getInstance();
    			//dateAux.add(Calendar.DATE, -30);
    			//String query = "SELECT SUM(score_quantity) FROM score WHERE score_date >= date('now','-30 days')";
    			//String query = "SELECT SUM(score_quantity) FROM score WHERE score_date >= '" + dateAux.get(Calendar.DAY_OF_MONTH) + "-" + (dateAux.get(Calendar.MONTH)+1) + "-" + dateAux.get(Calendar.YEAR) + "'";
        		//String query = "SELECT SUM(score_quantity) FROM score WHERE score_date >= '" + dateAux.get(Calendar.DAY_OF_MONTH) + "/" + (dateAux.get(Calendar.MONTH)+1) + "/" + dateAux.get(Calendar.YEAR) + "'";
        		Date date_created = new Date();
        		long timeMilliseconds = date_created.getTime();
        		timeMilliseconds = timeMilliseconds - (30 * (24 * (3600 * 1000))); // 30 dias
    			String query = "SELECT SUM(score_quantity) FROM score WHERE score_date >= " + timeMilliseconds;
    			Cursor cursor = db.rawQuery(query, null);
    			cursor.moveToFirst();
        		while (cursor.isAfterLast() == false){ 
    			    total = cursor.getInt(0);
            		cursor.moveToNext();
            	}
    	    	cursor.close();
        	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		this.close();
    	}
    	return total;
    }
    
    public Cursor getScoresByArena(String nm_arena){
    	Cursor cursor = null;
    	try{
    		String query = "SELECT scr._id, case when arn.nm_arena is null then 'NÃO IDENTIFICADA' else arn.nm_arena end nm_arena, scr.score_quantity, scr.score_date FROM score scr LEFT OUTER JOIN arena arn ON arn._id = scr.arena_id";
    		if (!(nm_arena == null || nm_arena.trim().equals(""))){
    			query = "SELECT scr._id, arn.nm_arena, scr.score_quantity, scr.score_date FROM score scr INNER JOIN arena arn ON arn._id = scr.arena_id where arn.nm_arena = '" + nm_arena + "'";
    		}
	    	cursor = db.rawQuery(query, null);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return cursor;
    }
}