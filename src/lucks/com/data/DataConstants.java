package lucks.com.data;

import android.os.Environment;

public class DataConstants {
	public static final String DATABASE_NAME = "lucks.db";
	
	private static final String APP_PACKAGE_NAME = "com.lucks";
	
	public static final String DATABASE_PATH =
            Environment.getDataDirectory() + "/data/" + DataConstants.APP_PACKAGE_NAME + "/databases/"
                     + DataConstants.DATABASE_NAME;
}
