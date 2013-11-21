package lucks.com.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import lucks.com.data.LanguageTable.LanguageColumns;
import lucks.com.data.LuckTable.LuckColumns;
import lucks.com.model.Language;
import lucks.com.model.Luck;

public class LuckDao {

	private static final String INSERT =
            "insert into " + LuckTable.TABLE_NAME + "(" + LuckColumns.LUCK_TEXT + ", "
            		+ LuckColumns.AUTHOR + ", " + LuckColumns.LANGUAGE_ID + ") values (?, ?, ?)";
	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;
	
	public LuckDao(SQLiteDatabase db) {
	      this.db = db;
	      insertStatement = db.compileStatement(LuckDao.INSERT);
	}
	
	//this function will add new lucks (after last id)
	public long addNewLuck(Luck luck){
		if(luck.getLuckText().equals("")){
			return -1;
		}
		insertStatement.clearBindings();
		insertStatement.bindString(1, luck.getLuckText());
		insertStatement.bindString(2, luck.getLuckAuthor());
		insertStatement.bindLong(3, luck.getluckLanguageId());
		
	    return insertStatement.executeInsert();
	}
	
	public void updateLucks(List<Luck> lucks){
		ContentValues values = new ContentValues();
		for(int i = 0; i < lucks.size(); i++){
			if(lucks.get(i).getId() <= getIdCount()){
				values.put(BaseColumns._ID, lucks.get(i).getId());
				values.put(LuckColumns.LUCK_TEXT, lucks.get(i).getLuckText());
				values.put(LuckColumns.AUTHOR, lucks.get(i).getLuckAuthor());
				values.put(LuckColumns.LANGUAGE_ID, lucks.get(i).getluckLanguageId());
				db.update(LuckTable.TABLE_NAME, values, BaseColumns._ID + "=" + lucks.get(i).getId(), null);
			}else{
				addNewLuck(lucks.get(i));
			}
		}
	}
	
	public Luck getRandomLuck(){
		Luck luck = new Luck();
		String GET_LUCK_QUERY = "SELECT "
				+ " * " + ", " 
				+ LanguageTable.TABLE_NAME + "." + LanguageColumns.IS_CHECKED
				+ " FROM " + LuckTable.TABLE_NAME + ", " + LanguageTable.TABLE_NAME
				+ " WHERE " 
				+ LanguageTable.TABLE_NAME + "." + LanguageColumns.IS_CHECKED
				+ " = 1 AND "
				+ LuckColumns.LANGUAGE_ID + " = " + LanguageColumns._ID
				+ " ORDER BY RANDOM() LIMIT 1";
		Cursor cursor = db.rawQuery(GET_LUCK_QUERY, null);
		if(cursor.moveToFirst()){
			luck.setId(cursor.getInt(cursor.getColumnIndex(LuckColumns.LUCK_ID)));
			luck.setLuckText(cursor.getString(cursor.getColumnIndex(LuckColumns.LUCK_TEXT)));
			luck.setLuckAuthor(cursor.getString(cursor.getColumnIndex(LuckColumns.AUTHOR)));
			luck.setLuckLanguageId(cursor.getInt(cursor.getColumnIndex(LuckColumns.LANGUAGE_ID)));
		}
		return luck;
	}
	
	private int getIdCount(){
		int rowCount = 0;
		Cursor c = db.query(LuckTable.TABLE_NAME, new String[] { BaseColumns._ID }, null, null, null, null, null);
		rowCount = c.getCount();
		if(!c.isClosed()){
			c.close();
		}
		return rowCount;
	}
	
	public List<Language> getAvailableLanguages(){
		List <Language> langs = new ArrayList<Language>();
		String AVAILABLE_LANGS_QUERY = "SELECT "
				+ LuckTable.TABLE_NAME + "." + LuckColumns.LANGUAGE_ID + ", "
				+ LanguageTable.TABLE_NAME + "." + LanguageColumns._ID + ", "
				+ LanguageTable.TABLE_NAME + "." + LanguageColumns.NAME + ", "
				+ LanguageTable.TABLE_NAME + "." + LanguageColumns.IS_CHECKED
				+ " FROM " + LuckTable.TABLE_NAME + ", " + LanguageTable.TABLE_NAME
				+ " WHERE " + LuckTable.TABLE_NAME + "." + LuckColumns.LANGUAGE_ID + " = " + LanguageTable.TABLE_NAME + "." + LanguageColumns._ID
				+ " ORDER BY " + LanguageColumns._ID;
		
		Cursor c = db.rawQuery(AVAILABLE_LANGS_QUERY, null);
		
		if(c.moveToFirst()){
			do{
				Language lang = new Language();
				lang.setId(c.getInt(c.getColumnIndex(LanguageColumns._ID)));
				lang.setLang(c.getString(c.getColumnIndex(LanguageColumns.NAME)));
				if(c.getInt(c.getColumnIndex(LanguageColumns.IS_CHECKED)) == 1){
					lang.setIsChecked(true);
				}else{
					lang.setIsChecked(false);
				}
				langs.add(lang);
			}while(c.moveToNext());
		}
		return langs;
	}
}
