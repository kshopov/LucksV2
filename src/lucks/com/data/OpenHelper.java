package lucks.com.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import lucks.com.R;
import lucks.com.model.Language;
import lucks.com.model.Luck;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OpenHelper extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 1;
	private Context context;
	
	public OpenHelper(final Context context) {
		super(context, DataConstants.DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}
	
	@Override
	public void onOpen(final SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {       
	         db.execSQL("PRAGMA foreign_keys=ON;");

	         Cursor c = db.rawQuery("PRAGMA foreign_keys", null);
	         if (c.moveToFirst()) {
	            int result = c.getInt(0);
	            Log.i("MyLucks", "SQLite foreign key support (1 is on, 0 is off): " + result);
	         } else {
	            Log.i("MyLucks", "SQLite foreign key support NOT AVAILABLE");
	         }
	         if (!c.isClosed()) {
	            c.close();
	         }
	      }
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		LanguageTable.onCreate(db);
		LanguageDao languagesDao = new LanguageDao(db);
		String[] languages = context.getResources().getStringArray(R.array.languages);
		
		for(String lang : languages){
			languagesDao.addLanguage(new Language(lang));
		}
		
		LuckTable.onCreate(db);
		LuckDao luckDao = new LuckDao(db);

		AssetManager am = context.getAssets();
			try {
				InputStream instream = am.open("lucks.txt");
				if (instream != null) {
					InputStreamReader inputeader = new InputStreamReader(instream);
					BufferedReader buffReader = new BufferedReader(inputeader);
					String line;

					while ((line = buffReader.readLine()) != null) {
						String[] separated = line.split(";");
						if(separated.length == 2){
							luckDao.addNewLuck(new Luck(0, 2, separated[0], separated[1]));
						}else{
							luckDao.addNewLuck(new Luck(0, 2, separated[0], ""));
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated
				// catch block e.printStackTrace();
			}
			
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	
}
