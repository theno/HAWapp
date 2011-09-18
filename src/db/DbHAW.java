package db;

import java.util.ArrayList;
import java.util.List;

import parser.Veranstaltung;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DbHAW {

	private SQLiteDatabase db;
	private static DbHAW self = null;
	
	public static DbHAW getInstance(Context context) {
		return self == null ? self = new DbHAW(context) : self;
	}
	
	// Constructor for DbHAW
	private DbHAW (Context context){
		DbOpenHelper dbOpenHelper = new DbOpenHelper(context);
		this.db = dbOpenHelper.getWritableDatabase();	
	}
	
	public void addTerminplanAttribute(String attrName, String attrValue) {
	
		ContentValues cv = new ContentValues();
		cv.put(DbOpenHelper.NAME, attrName);
		cv.put(DbOpenHelper.VALUE, attrValue);
		db.insert(DbOpenHelper.TERMINPLAN_ATTR_TABLE_NAME, null, cv);
	}
	
	
	// Adds Veranstaltung object to database
	public void addVeranstaltung(Veranstaltung v) {
		
		//List<Veranstaltung> termine = new ArrayList<Veranstaltung>();
		//termine = v.getTermine();
		
		ContentValues cv = new ContentValues();
		//for (Veranstaltung obj : termine){
			cv.put(DbOpenHelper.NAME, v.getName());
			cv.put(DbOpenHelper.DOZENT, v.getDozent());
			cv.put(DbOpenHelper.RAUM, v.getRaum());
			cv.put(DbOpenHelper.WOCHENTAG, v.getWochentag());
			cv.put(DbOpenHelper.ANFANG, v.getAnfangsZeit());
			cv.put(DbOpenHelper.ENDE, v.getEndeZeit());
			cv.put(DbOpenHelper.KW, v.getKWs());
			cv.put(DbOpenHelper.NOTITZ, v.getNotitz());
			
			db.insert(DbOpenHelper.VERANSTALTUNGEN_TABLE_NAME, null, cv);
		//}
	}
	
	
	public void addVeranstaltungOneKW(Veranstaltung v) {
		
		ContentValues cv = new ContentValues();
		
		cv.put(DbOpenHelper.NAME, v.getName());
		cv.put(DbOpenHelper.DOZENT, v.getDozent());
		cv.put(DbOpenHelper.RAUM, v.getRaum());
		cv.put(DbOpenHelper.WOCHENTAG, v.getWochentag());
		cv.put(DbOpenHelper.ANFANG, v.getAnfangsZeit());
		cv.put(DbOpenHelper.ENDE, v.getEndeZeit());
		cv.put(DbOpenHelper.KW, v.getKWs());
		cv.put(DbOpenHelper.NOTITZ, v.getNotitz());
		
		db.insert(DbOpenHelper.VERANSTALTUNGEN_TABLE_NAME, null, cv);

	}
	
	// Closes database
	public void closeDB() {
		this.db.close();
	}
	
	private Cursor getVeranstaltungCursor(int kw) {
		return db.query(DbOpenHelper.VERANSTALTUNGEN_TABLE_NAME, null, "kw = " + kw, null, null, null, null);
	}
	
	public Cursor getAllVeranstaltungCursor() {
		return db.query(DbOpenHelper.VERANSTALTUNGEN_TABLE_NAME, null, null, null, null, null, null);
	}
	
	public Cursor getTerminplanAttributeCursor(String attrName) {
		
		Cursor c = null;
		try {
			c = db.query(DbOpenHelper.TERMINPLAN_ATTR_TABLE_NAME, null, "name = \"" + attrName + "\"", null, null, null, null);
		} catch (Exception e) {
			Log.e("HAWApp", e.getMessage());
		}
		return c;
	}
	
	public List<Veranstaltung> getVeranstaltungList (int kw) {
		Cursor c = this.getVeranstaltungCursor(kw);
		
		List <Veranstaltung> vList = new ArrayList<Veranstaltung> ();
		
		if (true == c.moveToFirst()) {		
			do {
				vList.add( new Veranstaltung (
						c.getString(c.getColumnIndex(DbOpenHelper.NAME)),
						c.getString(c.getColumnIndex(DbOpenHelper.DOZENT)),
						c.getString(c.getColumnIndex(DbOpenHelper.RAUM)),
						c.getString(c.getColumnIndex(DbOpenHelper.WOCHENTAG)),
						c.getString(c.getColumnIndex(DbOpenHelper.ANFANG)),
						c.getString(c.getColumnIndex(DbOpenHelper.ENDE)),
						c.getString(c.getColumnIndex(DbOpenHelper.KW)),
						c.getString(c.getColumnIndex(DbOpenHelper.NOTITZ))
					));
			} while (true == c.moveToNext());
		}
		c.close();
		
		return vList;
	}
	
	public List<Veranstaltung> getAllVeranstaltungList () {
		Cursor c = this.getAllVeranstaltungCursor();
		
		List <Veranstaltung> vList = new ArrayList<Veranstaltung> ();
		
		if (true == c.moveToFirst()) {		
			do {
				vList.add( new Veranstaltung (
					c.getString(c.getColumnIndex(DbOpenHelper.NAME)),
					c.getString(c.getColumnIndex(DbOpenHelper.DOZENT)),
					c.getString(c.getColumnIndex(DbOpenHelper.RAUM)),
					c.getString(c.getColumnIndex(DbOpenHelper.WOCHENTAG)),
					c.getString(c.getColumnIndex(DbOpenHelper.ANFANG)),
					c.getString(c.getColumnIndex(DbOpenHelper.ENDE)),
					c.getString(c.getColumnIndex(DbOpenHelper.KW)),
					c.getString(c.getColumnIndex(DbOpenHelper.NOTITZ))
				));
			} while (true == c.moveToNext());
		}
		c.close();
		
		return vList;
	}
	
	public String getTerminplanAttribute(String attrName) {
		
		Cursor c = this.getTerminplanAttributeCursor(attrName);
		String attr = "";
		if (null != c && true == c.moveToFirst()) {
			attr = c.getString(c.getColumnIndex(DbOpenHelper.VALUE));
			c.close();
		}
		return attr;
	}
	
	public void deleteAllVeranstaltungen() {
		db.delete(DbOpenHelper.VERANSTALTUNGEN_TABLE_NAME, null, null);
		db.delete(DbOpenHelper.TERMINPLAN_ATTR_TABLE_NAME, null, null);
	}
	
}// Class
