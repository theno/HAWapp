package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper{

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "dbHAW";

    public static final String VERANSTALTUNGEN_TABLE_NAME = "Veranstaltungen";
    public static final String NAME = "name";
    public static final String DOZENT = "dozent";
    public static final String RAUM = "raum";
    public static final String WOCHENTAG = "wochentag";
    public static final String ANFANG = "anfang";
    public static final String ENDE = "ende";
    public static final String KW = "kw";
    public static final String NOTITZ = "notitz";
    
    public static final String TERMINPLAN_ATTR_TABLE_NAME = "TerminplanAttributes";
    public static final String VALUE = "value";
    
    public static final String TERMINE_TABLE_NAME = "Termine";
    
    private static final String CREATE_VERANSTALTUNGEN_TABLE = "create table " 
    	+ VERANSTALTUNGEN_TABLE_NAME + " ( _id integer primary key autoincrement, "
        + NAME + " TEXT, "
        + DOZENT + " TEXT, "
        + RAUM + " TEXT, "
        + WOCHENTAG + " TEXT, "
        + ANFANG + " TEXT, "
        + ENDE + " TEXT, "
        + KW + " TEXT, "
        + NOTITZ + " TEXT)";
    
    private static final String CREATE_TERMINE_TABLE = "create table " 
    	+ TERMINE_TABLE_NAME + " ( _id integer primary key autoincrement, "
        + NAME + " TEXT, "
        + DOZENT + " TEXT, "
        + RAUM + " TEXT, "
        + WOCHENTAG + " TEXT, "
        + ANFANG + " TEXT, "
        + ENDE + " TEXT, "
        + KW + " TEXT, "
        + NOTITZ + " TEXT)";
    
    private static final String CREATE_TERMINPLAN_ATTR_TABLE = "create table " 
    	+ TERMINPLAN_ATTR_TABLE_NAME + " ( _id integer primary key autoincrement, "
        + NAME + " TEXT, "
        + VALUE + " TEXT)";
 
    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_VERANSTALTUNGEN_TABLE);
        sqLiteDatabase.execSQL(CREATE_TERMINE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TERMINPLAN_ATTR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

}// Class
