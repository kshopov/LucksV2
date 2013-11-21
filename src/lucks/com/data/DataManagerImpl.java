package lucks.com.data;

import java.util.ArrayList;
import java.util.List;

import lucks.com.model.Language;
import lucks.com.model.Luck;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;
import android.util.Log;

public class DataManagerImpl implements DataManager {
	
	private Context context;
	
	private SQLiteDatabase db;
	
	private LuckDao luckDao;
	private LanguageDao languageDao;
	
	public DataManagerImpl(Context context){
		this.context = context;
		
		SQLiteOpenHelper openHelper = new OpenHelper(this.context);
	    db = openHelper.getWritableDatabase();
	    Log.i("MY LUCKS", "DataManagerImpl created, db open status: " + db.isOpen());
	    
	    luckDao = new LuckDao(db);
	    languageDao = new LanguageDao(db);
	}
	
	public SQLiteDatabase db(){
		return db;
	}
	
	private void openDb(){
		if(!db.isOpen()){
			db = SQLiteDatabase.openDatabase(DataConstants.DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
			
			luckDao = new LuckDao(db);
			languageDao = new LanguageDao(db);
		}
	}
	
	private void closeDb(){
		if(db.isOpen()){
			db.close();
		}
	}
	
	@SuppressWarnings("unused")
	private void resetDb(){
		Log.i("MyLucks", "Resetting database connection (close and re-open).");
	    closeDb();
	    SystemClock.sleep(500);
	    openDb();
	}

	public long addLanguage(Language lang) {
		return languageDao.addLanguage(lang);
	}

	public void updateCheckedLanguages(List<Language> langs) {
		languageDao.updateCheckedLanguages(langs);
	}

	public void deleteLanguage(int id) {
		languageDao.deleteLanguage(id);
	}

	public List<Language> getAllLanguages() {
		return languageDao.getAllLanguages();
	}

	public long addNewLuck(Luck luck) {
		return luckDao.addNewLuck(luck);
	}

	public void updateLucks(List<Luck> lucks) {
		luckDao.updateLucks(lucks);
	}

	public Luck getRandomLuck() {
		return luckDao.getRandomLuck();
	}

	public List<Language> getAvailableLanguages() {
		int counter = 0;
		List<Language> availableLanguages = luckDao.getAvailableLanguages();
		List <Language> langs = new ArrayList<Language>();
		
		langs.add(availableLanguages.get(counter));
		for(int i = 1; i < availableLanguages.size(); i++){
			if(!langs.get(counter).getLang().equals(availableLanguages.get(i).getLang())){
				langs.add(availableLanguages.get(i));
				counter++;
			}
		}
		return langs; 
	}
}
