package com.tutorial.sqliteopenhelper;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {	
	private DBHelper mDBHelper = new DBHelper(this);
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);               
                        
        final EditText editText = (EditText)findViewById(R.id.output_edittext);
        
        Button insertButton = (Button)findViewById(R.id.insert_button);
        insertButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		SQLiteDatabase db = mDBHelper.getWritableDatabase();
        		db.execSQL("INSERT INTO forecast VALUES (null, 'Seoul', 200, 29);");
        		db.execSQL("INSERT INTO forecast VALUES (null, 'Daegu', 100, 34);");
        		db.execSQL("INSERT INTO forecast VALUES (null, 'Pusan', 300, 32);");
        		db.execSQL("INSERT INTO forecast VALUES (null, 'Daejeon', 400, 28);");
        		mDBHelper.close();
        		editText.setText("Insert success");
        	}
        });
        
        Button deleteButton = (Button)findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		SQLiteDatabase db = mDBHelper.getWritableDatabase();
        		db.execSQL("DELETE FROM forecast;");
        		mDBHelper.close();
        		editText.setText("Delet success");
        	}
        });
        
        Button updateButton = (Button)findViewById(R.id.update_button);
        updateButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		SQLiteDatabase db = mDBHelper.getWritableDatabase();
        		db.execSQL("UPDATE forecast SET code = '500' WHERE location = 'Daegu';");
        		mDBHelper.close();
        		editText.setText("Update success");
        	}
        });
        
        Button selectButton = (Button)findViewById(R.id.select_button);
        selectButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		SQLiteDatabase db = mDBHelper.getReadableDatabase();
        		Cursor cursor = db.rawQuery("SELECT * from forecast;", null);
        		
        		String results = new String();
        		while (cursor.moveToNext()) {
        			int id = cursor.getInt(0);
        			String location = cursor.getString(1);
        			int code = cursor.getInt(2);
        			int temperature = cursor.getInt(3);
        			
        			results += "ID: " + Integer.toString(id) 
        					+ " Location: " + location 
        					+ " Code: " + Integer.toString(code)
        					+ " Temperature: " + Integer.toString(temperature)
        					+ "\n";
        		}
        		
        		if (results.length() == 0) {
        			editText.setText("Empty set");
        		} else {
        			editText.setText(results);
        		}
        		
        		cursor.close();
        		mDBHelper.close();
        	}
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    class DBHelper extends SQLiteOpenHelper {
    	public DBHelper(Context context) {
    		super(context, "tutorial.db", null, 1);
    	}
    	
    	public void onCreate(SQLiteDatabase db) {
    		db.execSQL("CREATE TABLE forecast (id INTEGER PRIMARY KEY AUTOINCREMENT, location TEXT, code INTEGER, temperature INTEGER);");
    	}
    	
    	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    		db.execSQL("DROP TABLE IF EXISTS forecast");
    		onCreate(db);
    	}
    	
    }
}
