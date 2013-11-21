package lucks.com.data;

import lucks.com.data.LanguageTable.LanguageColumns;
import lucks.com.model.Language;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;


public class LanguageDao {
	
	private static final String INSERT =
            "insert into " + LanguageTable.TABLE_NAME + "(" + LanguageColumns.NAME + ") values (?)";
	
	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;
	
	public LanguageDao(SQLiteDatabase db) {
	      this.db = db;
	      insertStatement = db.compileStatement(LanguageDao.INSERT);
	}
	
	public long addLanguage(Language lang){
		if(lang.getLang().equals("")){
			return -1;
		}
		insertStatement.clearBindings();
	    insertStatement.bindString(1, lang.getLang());

	    return insertStatement.executeInsert();
	}
	
	public void updateCheckedLanguages(List<Language> langs){
		ContentValues values = new ContentValues();
		for(int i = 0; i < langs.size(); i++){
			if(langs.get(i).getIsChecked()){
				values.put("is_checked", 1);
			}else{
				values.put("is_checked", 0);
			}
			db.update(LanguageTable.TABLE_NAME, values, BaseColumns._ID + "=" + langs.get(i).getId(), null);
		}
	}
	
	public void deleteLanguage(int id){
		db.delete(LanguageTable.TABLE_NAME, BaseColumns._ID + "=" + id, null);
	}
	
	public List<Language> getAllLanguages(){
		List<Language> list = new ArrayList<Language>();
		Cursor c = 
				db.query(LanguageTable.TABLE_NAME, new String[] { BaseColumns._ID, LanguageColumns.NAME }, null, null, null, null, null);
		if(c.moveToFirst()){
			do{
				Language language = new Language();
				language.setId(c.getInt(0));
				language.setLang(c.getString(1));
				list.add(language);
			}while(c.moveToNext());
		}
		if(!c.isClosed()){
			c.close();
		}
		return list;
	}
}
