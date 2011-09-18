package activities;

import informatik.haw.app.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import parser.E_Studiengang;
import parser.Veranstaltung;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import db.DbHAW;

public class WochenAnsicht extends Activity {
	
	public static boolean changesMade = false;
	private boolean verlassen = false;
	
	private Uri calendarURI;
	private int chosenCalenar;
	private Map<String, String> calendars = new TreeMap<String, String>();

	private AlertDialog speichernAlert;
	private AlertDialog deleteAllAlert;
	private AlertDialog optionenAlert;
	
	//Es wird zwar ein RelativeLayout benutzt, wird aber wie ein AbsolutLayout verwendet
	//TODO: entweder ein TableLayout einsetzen, oder die Auflösung dynamisch feststellen und
	//die ganze Koefizienten zum Veranstaltungen Plazieren anhand dessen ausrechnen.
	//Sonst sieht das ganze scheisse auf handys mit andere Auflösungen!
	private RelativeLayout rl;

	private List<VeranstaltungButton> buttons;
	public static VeranstaltungButton gewaehlteVeranstaltung;
	List<Veranstaltung> veranstaltungen;
	

	//public static final int VERLASSEN_ID = 1;
	public static final int MENU_ID			= 0;
	public static final int SPEICHERN_ID	= 1;
	public static final int LEEREN_ID		= 2;
	public static final int BEARBEITEN_ID	= 3;
	public static final int EXPORTIEREN_ID	= 4;

	private static final int DAY_PIXELS	= 59;
	private static final float MINUTE_PIXELS = 0.7F;
	
	private OnClickListener veranstaltungOnClickListner = new OnClickListener() {
		public void onClick(View v) {
			gewaehlteVeranstaltung = (VeranstaltungButton)v;
			startActivity(new Intent(WochenAnsicht.this, TerminDetails.class));
		}
	};
	
	private OnLongClickListener veranstaltungOnLongClickListner = new OnLongClickListener() {
		public boolean onLongClick(View v) {
			gewaehlteVeranstaltung = (VeranstaltungButton)v;
			optionenAlert.show();
			return true;
		}
	};
	
	public class VeranstaltungButton extends Button {

		public RelativeLayout.LayoutParams params;
		public Veranstaltung veranstaltung;
		
		public void onDraw(Canvas canvas) {
			
			Paint paint = new Paint();
			paint.setTextSize(10);
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Align.CENTER);
			paint.setAntiAlias(true);

			canvas.drawText(veranstaltung.getAnfangsZeit(), params.width/2, 12, paint);
			canvas.drawText(veranstaltung.getEndeZeit(),    params.width/2, params.height-10, paint);
			
			String text = "";
			String[] s = veranstaltung.getName().split("-");
			if(s.length > 1) {
				for(int i = 1; i < s.length; i++)
				text += s[i];
			}
			else text = s[0];

			canvas.drawText(text, params.width/2, params.height/2-5, paint);
			canvas.drawText(veranstaltung.getRaum(), params.width/2, params.height/2+5, paint);
		}
		
		public VeranstaltungButton(Veranstaltung v) {
			super(WochenAnsicht.this);
			
			this.veranstaltung = v;
			
			int anfangMinutes =  v.getAnfangsZeitMinutes();
			int endeMinutes   =  v.getEndeZeitMinutes();
			
			//TODO: "+5" is fitting. Bad...very BAD!!!
			int height = (int) ((endeMinutes - anfangMinutes) * MINUTE_PIXELS + 5);
			int width  = 64;
				
			int y = (int) ((anfangMinutes - 480) * MINUTE_PIXELS + 27);			
			int x = 23 + (v.getWochentagAsInt() - 1) * DAY_PIXELS;
			
			
			VeranstaltungButton vb2 = checkForCrossings(v);
			if (vb2 != null) {
				width = 35;
				x += 29;
				vb2.setWidth(width);
			}
			
			params = new RelativeLayout.LayoutParams(width, height);
			params.leftMargin = x;
			params.topMargin  = y;
			
			getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFF0000AA));
					
			//set onclick listners
			setOnClickListener(veranstaltungOnClickListner);
			setOnLongClickListener(veranstaltungOnLongClickListner);
		}
		
		public void setWidth(int width) {
			params.width = width;
		}
		public void setHeight(int height) {
			params.height = height;
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i("HAWApp", "WochenAnsicht -> Wochenansicht gestartet");
		
		ActivityRegistry.register(this);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
								  WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.termin_list);
		
		drawVeranstaltungButtons();
		createBackBtnAlertDialog();
		createDeleteAllAlertDialog();		
		
		
		//Hier wird ein AlertDialog für das onLongClick auf Veranstaltungen initialisiert
		final CharSequence[] items = {"Editieren", "Löschen", "Exportieren"};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Optionen:");
		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        switch (item) {
		        case 0: startActivity(new Intent(WochenAnsicht.this, TerminDetails.class));
		        		break;
		        case 1: VeranstaltungenWaehlen.gewaehlteVeranstaltungen.remove(
		        			gewaehlteVeranstaltung.veranstaltung);		        		
		        		drawVeranstaltungButtons();
		        		changesMade = true;
		        		break;
		        case 2: //Toast.makeText(WochenAnsicht.this, "Kommt in der nächste Version!", Toast.LENGTH_SHORT).show();
		        		if(calendars.isEmpty() == true) {
		        			Toast.makeText(WochenAnsicht.this, "Du hast kein Kalender man!", Toast.LENGTH_SHORT).show();
		        		}
		        		else {
		        			if(calendars.size() > 1) {
		        				
		        				dialog.dismiss();
		        				
		        				AlertDialog.Builder builder = new AlertDialog.Builder(WochenAnsicht.this);
		        				builder.setTitle("Wohin willst du exportieren:");
		        				CharSequence[] c = calendars.keySet().toArray(new CharSequence[0]); 
		        				builder.setItems(c, new DialogInterface.OnClickListener() {
		        				    public void onClick(DialogInterface dialog, int item) {
		        				    	chosenCalenar = item;
		        				    	dialog.dismiss();
		        				    	exportEvent(gewaehlteVeranstaltung.veranstaltung);
		        				    }
		        				});
		        				builder.setCancelable(true);
		        				optionenAlert = builder.create();
		        				optionenAlert.show();
		        			}
		        			else exportEvent(gewaehlteVeranstaltung.veranstaltung);
		        		}
		        		break;
		        }
		    }
		});
		builder.setCancelable(true);
		optionenAlert = builder.create();
		
		
		if (null != getCalendarURI()) {
			getAllCalendars();
		}
	}
	
	public static long pickDate(int year, int month, int day, int hour, int minute) 
	{
	    	Calendar rightNow = Calendar.getInstance();
	    	int timeShift = rightNow.get(Calendar.ZONE_OFFSET);    	
	    	return Date.UTC(year - 1900, month, day, hour - (timeShift / 3600000), minute, 0);
	}

	
	private void exportEvent(Veranstaltung v) {
		
		String cal = ((calendars.keySet().toArray(new String[0])))[chosenCalenar];
		String calID = calendars.get(cal);
		
		
		
		ContentValues event = new ContentValues();
//		event.put("calendar_id", calID);
//		event.put("title", "name");//v.getName());
//		event.put("description", "descr");//v.getNotitz());
//		event.put("eventLocation", "loc");//v.getRaum());
////		int i = v.getKWs().indexOf(',');
////		int kw = Integer.parseInt(v.getKWs().substring(0, i));
////		Termin t = new Termin(v, kw);
////		event.put("dtstart", t.getAnfang().getTimeInMillis());
////		event.put("dtend", t.getEnde().getTimeInMillis());
////		event.put("dtstart", pickDate(t.getAnfang().YEAR, t.getAnfang().MONTH, t.getAnfang().DAY_OF_MONTH, t.getAnfang().HOUR, t.getAnfang().MINUTE));
////		event.put("dtend", pickDate(t.getEnde().YEAR, t.getEnde().MONTH, t.getEnde().DAY_OF_MONTH, t.getEnde().HOUR, t.getEnde().MINUTE));
//		event.put("dtstart", pickDate(2011, 2, 21, 8, 30));
//		event.put("dtend", pickDate(2011, 2, 21, 9, 30));
		
        event.put("calendar_id", calID);
        event.put("title", "Today's Event [TEST]");
        event.put("description", "2 Hour Presentation");
        event.put("eventLocation", "Online");

        long startTime = System.currentTimeMillis() + 1000 * 60 * 60;
        long endTime = System.currentTimeMillis() + 1000 * 60 * 60 * 2;

        event.put("dtstart", startTime);
        event.put("dtend", endTime);

        event.put("allDay", 0); // 0 for false, 1 for true
        event.put("eventStatus", 1);
        event.put("visibility", 0);
        event.put("transparency", 0);
        event.put("hasAlarm", 0); // 0 for false, 1 for true

		
		
		Uri eventsURI=Uri.parse(calendarURI + "/events");
		this.getContentResolver().insert(eventsURI, event);

		String bla = "";
		bla = "bla";
	}
	
	private Uri getCalendarURI() {
		
		calendarURI = Uri.parse("content://calendar/calendars");
		Cursor managedCursor = this.managedQuery(calendarURI, new String[] { "_id", "name" }, null, null, null);
		if(managedCursor == null || managedCursor.getCount() == 0)
		{
			// must be 2.2/2.3
			calendarURI = Uri.parse("content://com.android.calendar/calendars");
			managedCursor = this.managedQuery(calendarURI, new String[] { "_id", "name" }, null, null, null);
			if(managedCursor == null || managedCursor.getCount() == 0) 
			{
				Toast.makeText( WochenAnsicht.this, "Könnte kein Kalender finden! Export wird nicht funktionieren.. =( ",
								Toast.LENGTH_LONG).show();
				calendarURI = null;
			}
		}
		else
		{
			// < 2.2
		}
		return calendarURI;
	}

	private void getAllCalendars() {
		
		String[] projection = new String[] { "_id", "name" };
		Cursor managedCursor = this.managedQuery(calendarURI, projection, null, null, null);
		
		if (managedCursor != null && managedCursor.moveToFirst()) 
        {
        	String calName;
        	String calID;        	
        	int nameColumn = managedCursor.getColumnIndex("name");
        	int idColumn = managedCursor.getColumnIndex("_id");
        	do 
        	{
        		calName = managedCursor.getString(nameColumn);
        		calID = managedCursor.getString(idColumn);
        		if (calName != null) calendars.put(calName, calID); 			 
           	} while (managedCursor.moveToNext());
            managedCursor.close();
        }

	}
	
	
//	@Override
//	protected void onPostCreate(Bundle savedInstanceState) {
//		changesMade = true;
//	}
	
	private void drawVeranstaltungButtons() {	
		
		if (VeranstaltungenWaehlen.gewaehlteVeranstaltungen == null && 
			HAWmain.terminPlanVorhanden == true) {			
			veranstaltungen = loadVeranstaltungenDB();
			VeranstaltungenWaehlen.gewaehlteVeranstaltungen = veranstaltungen;
		}
		else
		if (VeranstaltungenWaehlen.gewaehlteVeranstaltungen != null) {
			veranstaltungen = VeranstaltungenWaehlen.gewaehlteVeranstaltungen;
		}
		else return;
		
		buttons = new ArrayList<VeranstaltungButton>();

		rl = (RelativeLayout) findViewById(R.id.waRelativeLayout);	
		rl.removeAllViews();
		
		for (Veranstaltung v : veranstaltungen) {

			VeranstaltungButton btn = new VeranstaltungButton(v);			
			buttons.add(btn);			
			//add the view on our relative layout
			rl.addView(btn, btn.params);
		}
		
		VeranstaltungenWaehlen.changesMade = false;
	}
	
	public VeranstaltungButton checkForCrossings(Veranstaltung v) {
			
		int anfangV1 = v.getAnfangsZeitMinutes();
		int endeV1   = v.getEndeZeitMinutes();
		
		for (VeranstaltungButton vb : buttons) {
			
			Veranstaltung v2 = vb.veranstaltung;
			
			if (true == v.equals(v2)) continue;
			// uberprüfe ob die Veranstaltungen am gleichen Tag stattfinden			
			if (true == v.getWochentag().equals(v2.getWochentag())) {

				int anfangV2 = v2.getAnfangsZeitMinutes();
				int endeV2   = v2.getEndeZeitMinutes();
				// .. ob die sich zeitlich überkreuzen
				if (   // einmal von oben
					((anfangV2 <= anfangV1) && (endeV2 > anfangV1))
					|| // und einmal von unten
					((anfangV2 >= anfangV1) && (anfangV2 < endeV1))) {
					return vb;
				}
			}
		}
		return null;
	}

	/**
	 * Im Normalfall(else Zweig) beenden wir alle Activitaeten auser HAWmain
	 *	und kehren somit insHauptmenu zurueck.
	 *	Falls der benutzer "Verlassen" übers Menu gedrueckt hat, werden alle 
	 *	Aktivitaeten geschlossen.
	 **/
	private void exitWochenAnsicht() {
		if(verlassen == true) {
			ActivityRegistry.finishAll();
		}
		else {
			ActivityRegistry.finishAllExeptMain();
		}	
	}
	
	private void createBackBtnAlertDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true)
				.setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Log.i("HAWApp", "WochenAnsicht -> Terminplan speichern geklickt ");
						
						if (veranstaltungen.isEmpty() == true) {
							HAWmain.terminPlanVorhanden = false;
							deleteAll();
						} else {
							saveAll();
						}		
						exitWochenAnsicht();
					}
				})
				.setNegativeButton("Verwerfen", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Log.i("HAWApp", "WochenAnsicht -> Terminplan verwerfen geklickt ");
						
						VeranstaltungenWaehlen.gewaehlteVeranstaltungen = null;
						
						
						HAWmain.terminPlanVorhanden = false;
						changesMade = false;
						veranstaltungen.clear();
						StudiengangWaehlen.checkedCourses.clear();
						deleteAll();
						
						
						exitWochenAnsicht();
					}
				})
				.setNeutralButton("Bearbeiten",	new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Log.i("HAWApp", "WochenAnsicht -> Terminplan bearbeiten geklickt ");
						
						VeranstaltungenWaehlen.changesMade = false;
						Intent intent = new Intent(WochenAnsicht.this, VeranstaltungenWaehlen.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
					}
				});
		builder.setTitle("Wollen Sie Ihr Terminplan");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		speichernAlert = builder.create();
	}
	
	private void createDeleteAllAlertDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true)
				.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Log.i("HAWApp", "WochenAnsicht -> DeleteAllAllertDialog -> Ja");
						
						HAWmain.terminPlanVorhanden = false;
						changesMade = false;
						veranstaltungen.clear();
						StudiengangWaehlen.checkedCourses.clear();
						deleteAll();						
						exitWochenAnsicht();
					}
				})
				.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Log.i("HAWApp", "WochenAnsicht -> DeleteAllAllertDialog -> Nein");
					}
				});
		builder.setTitle("Wirklich alles Löschen?");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		deleteAllAlert = builder.create();
	}

	private void saveAll() {		
		HAWmain.terminPlanVorhanden = true;
		changesMade = false;
		
		DbHAW db = DbHAW.getInstance(this);
		
		
		//TODO: Update only modified Veranstaltungen
		db.deleteAllVeranstaltungen();
		
		for(Veranstaltung v: veranstaltungen) {
			db.addVeranstaltung(v);
		}
		
		
		String studiengaenge = "";
		for(E_Studiengang studiengang: StudiengangWaehlen.checkedCourses) {
			studiengaenge += studiengang.name();
		}
		
		db.addTerminplanAttribute("studiengaenge", studiengaenge);
		db.addTerminplanAttribute("studoderprof", StudentOderProfWaehlen.studoderprof);
	}
	
	private List<Veranstaltung> loadVeranstaltungenDB() {
		DbHAW db = DbHAW.getInstance(this);		
		return db.getAllVeranstaltungList();
	}
	
	private void deleteAll() {
		DbHAW db = DbHAW.getInstance(this);
		db.deleteAllVeranstaltungen();
	}
	
	/*
	 * VORSICHT!!!
	 * Falls Sie die Reihenfolge von Menüitems ändern wollen, ändern Sie bitte auch 
	 * die entsprechende ID constant oben weil menu.getItem() Methode in onPrepareOptionsMenu()
	 * nach index und nicht nach ID sucht.
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);

		menu.add(0, EXPORTIEREN_ID, 0, "Alles in Google Kalender Exportiren").setIcon(
				android.R.drawable.ic_menu_upload);
		menu.findItem(EXPORTIEREN_ID).setEnabled(false);
		
		menu.add(0, MENU_ID, 0, "Hauptmenu").setIcon(
				android.R.drawable.ic_menu_revert);
		menu.add(0, SPEICHERN_ID, 0, "Terminplan speichern").setIcon(
				android.R.drawable.ic_menu_save);
		menu.add(0, LEEREN_ID, 0, "Terminplan leeren").setIcon(
				android.R.drawable.ic_menu_delete);
		menu.add(0, BEARBEITEN_ID, 0, "Terminplan bearbeiten").setIcon(
				android.R.drawable.ic_menu_edit);
//		menu.add(0, VERLASSEN_ID, 0, "Verlassen").setIcon(
//				android.R.drawable.ic_menu_close_clear_cancel);
		
		return result;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
//		case VERLASSEN_ID:
//			Log.i("HAWApp", "WochenAnsicht -> Verlassen in WochenAnsicht ueber Menu ");
//			if (changesMade == true) {
//				verlassen = true;
//				speicherAlert.show();
//			}
//			return true;
		case MENU_ID:
			Log.i("HAWApp", "WochenAnsicht -> zum Hauptmenu in WochenAnsicht ueber Menu ");
			if (changesMade == true)
				speichernAlert.show();
			else {
				Intent intent = new Intent(this, HAWmain.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			break;
		case SPEICHERN_ID:
			Log.i("HAWApp", "WochenAnsicht -> Terminplan speichern in WochenAnsicht ueber Menu ");
			saveAll();
			break;
		case LEEREN_ID:
			Log.i("HAWApp", "WochenAnsicht -> Terminplan leeren in WochenAnsicht ueber Menu ");
			deleteAllAlert.show();
			break;
		case BEARBEITEN_ID:
			Log.i("HAWApp",	"WochenAnsicht -> Terminplan bearbeiten in WochenAnsicht ueber Menu ");
			changesMade = true;
			Intent intent = new Intent(this, VeranstaltungenWaehlen.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public boolean onPrepareOptionsMenu (Menu menu) {
		if (changesMade == false)
			menu.findItem(SPEICHERN_ID).setEnabled(false);
		else 
			menu.findItem(SPEICHERN_ID).setEnabled(true);
		return true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i("HAWApp", "WochenAnsicht -> Ruecktaste in WochenAnsicht gedrueckt");
			if (changesMade == true) {
				speichernAlert.show();
			}
			else {
				changesMade = false;
				exitWochenAnsicht();
			}
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			Log.i("HAWApp", "WochenAnsicht -> Menu in WochenAnsicht aktiviert");
		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
	
		Log.i("HAWApp", "WochenAnsicht -> onResume()");
		
		//changesMade = false;
		
		if(TerminDetails.changesMade == true) {
			TerminDetails.changesMade = false;
			drawVeranstaltungButtons();			
			changesMade = true;
		}
		
		if(VeranstaltungenWaehlen.changesMade == true) {
			VeranstaltungenWaehlen.changesMade = false;
			//TODO: I think you dont need this clear...
			veranstaltungen.clear();
			drawVeranstaltungButtons();
		}
	}
}
