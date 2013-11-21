package lucks.com.data;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public final class LanguageTable {

	public static final String TABLE_NAME = "languages";

	public static class LanguageColumns implements BaseColumns {
		public static final String NAME = "language";
		public static final String IS_CHECKED = "is_checked";
	}

	public static void onCreate(SQLiteDatabase db) {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("CREATE TABLE " + LanguageTable.TABLE_NAME + " (");
	    stringBuilder.append(BaseColumns._ID + " INTEGER PRIMARY KEY, ");
	    stringBuilder.append(LanguageColumns.NAME + " TEXT UNIQUE NOT NULL,");
	    stringBuilder.append(LanguageColumns.IS_CHECKED + " SHORT NOT NULL DEFAULT 0");
	    stringBuilder.append(");");
	    
	    db.execSQL(stringBuilder.toString());
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
	}


}
