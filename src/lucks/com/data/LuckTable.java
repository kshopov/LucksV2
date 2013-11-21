package lucks.com.data;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class LuckTable {
	public static final String TABLE_NAME = "luck";
	
	public class LuckColumns implements BaseColumns {
		public static final String LUCK_ID = "luck_id";
		public static final String LUCK_TEXT = "luck_text";
		public static final String AUTHOR = "author";
		public static final String LANGUAGE_ID = "language_id";
	}
	
	public static void onCreate(SQLiteDatabase db){
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("CREATE TABLE " + LuckTable.TABLE_NAME + " (");
		stringBuilder.append(LuckColumns.LUCK_ID + " INTEGER PRIMARY KEY, ");
		stringBuilder.append(LuckColumns.LUCK_TEXT + " TEXT NOT NULL, ");
		stringBuilder.append(LuckColumns.AUTHOR + " TEXT NOT NULL, ");
		stringBuilder.append(LuckColumns.LANGUAGE_ID + " SHORT NOT NULL, ");
		stringBuilder.append("FOREIGN KEY(" + LuckColumns.LANGUAGE_ID + ") REFERENCES " + LanguageTable.TABLE_NAME + "("
	               + BaseColumns._ID + ") ");
		stringBuilder.append(");");
		
		db.execSQL(stringBuilder.toString());
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}