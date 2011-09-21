package activities;

import helper.Tools;
import informatik.haw.app.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import parser.E_Studiengang;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import db.DbHAW;

public class HAWmain extends Activity { 
	
	public final static String STUNDENPLAN_URL = 
	"http://www.informatik.haw-hamburg.de/fileadmin/Homepages/ProfPadberg/stundenplaene/Sem_I.txt";
	
	public static String  Sem_I = "";	
	public static boolean terminPlanVorhanden = false;

	public static final int VERLASSEN_ID = Menu.FIRST;
	public static final int INFORMATIONEN_ID = Menu.CATEGORY_SECONDARY;

	// private AnalogClock aClock;
	private AlertDialog alert;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("HAWApp", "HAWmain -> Hauptmenu gestartet");

		ActivityRegistry.register(this);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);
		
		DbHAW db = DbHAW.getInstance(this);
		//TODO: write a method that would check if the DB exists instead of doing this:
//		if(db.getAllVeranstaltungList().size() != 0) {
//			terminPlanVorhanden = true;
//		}
//		if(DbHAW.getInstance(this).doesDBexist() == true) {
		if(db.getAllVeranstaltungList().size() != 0) {
			terminPlanVorhanden = true;
			
			Button btn_Terminplan = (Button) findViewById(R.id.btn_Terminplan);
			btn_Terminplan.setText("zum Terminplan");

			//Restore the checkboxes if the user decides to edit his plan
			String studiengaenge = db.getTerminplanAttribute("studiengaenge");			
			if(studiengaenge.contains("BAI"))	StudiengangWaehlen.checkedCourses.add(E_Studiengang.BAI);
			if(studiengaenge.contains("BTI"))	StudiengangWaehlen.checkedCourses.add(E_Studiengang.BTI);
			if(studiengaenge.contains("MINF"))	StudiengangWaehlen.checkedCourses.add(E_Studiengang.MINF);
			
			//Do the same for "stud oder prof" radiobuttons	
			String studoderprof = db.getTerminplanAttribute("studoderprof");
			StudentOderProfWaehlen.studoderprof = studoderprof;
		}
		

		//you can change the color of the buttons like this:
		/*
		Button btn_Terminplan = (Button) findViewById(R.id.btn_Terminplan);
		btn_Terminplan.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFF0000AA));		
		Button btn_Mensa = (Button) findViewById(R.id.btn_Mensa);
		btn_Mensa.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFF0000AA));
		Button btn_Mailer = (Button) findViewById(R.id.btn_Mailer);
		btn_Mailer.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFF0000AA));
		Button btn_Bibliothek = (Button) findViewById(R.id.btn_Bibliothek);
		btn_Bibliothek.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFF0000AA));
		*/
		
		// aClock = (AnalogClock) findViewById(R.id.AnalogClock01);

		TextView text1 = (TextView) findViewById(R.id.tv_HAWapp);
		text1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("HAWApp", "HAWmain -> auf HAWApp geklickt");
				Toast.makeText( HAWmain.this, "Hallo, ich bin die HAWapp. Wie kann ich dir weiterhelfen?",
				Toast.LENGTH_LONG).show();				
			}
		});

		TextView text2 = (TextView) findViewById(R.id.tv_funktionen);
		text2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("HAWApp", "HAWmain -> auf Funktionen geklickt");
				Toast.makeText( HAWmain.this, "Hier sind alle meine Funktionen aufgelistet",
				Toast.LENGTH_SHORT).show();
			}
		});

		TextView text3 = (TextView) findViewById(R.id.tv_links);
		text3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("HAWApp", "HAWmain -> auf Web Links geklickt");
				Toast.makeText(HAWmain.this, "Hierrüber kannst du schnell auf die entsprechende Webseite springen",
				Toast.LENGTH_SHORT).show();
			}
		});

		//createAllertDialog();
		Sem_I = getActualTimeTable();
	}
	
	/**
	 * 
	 * @return
	 */
	private String getActualTimeTable() {
		
		InputStream is = null;
		String sem = "";
		try {
			//try to ream TimeTable from file
			is = openFileInput("Sem_I");
		} catch (FileNotFoundException e) {
			//read TimeTable from "raw"
			is = getResources().openRawResource(R.raw.sem);
		}
		// if file is available for reading
	    if (is != null) {

        	int charRead = 0;
            int BUFFER_SIZE = 2000;
            char[] inputBuffer = new char[BUFFER_SIZE]; 
            
        	InputStreamReader isr;
			try {
				isr = new InputStreamReader(is, "Cp1252");
        	
	            try {
					while ((charRead = isr.read(inputBuffer)) > 0) {                    
					    //---convert the chars to a String---
					    String readString = String.copyValueOf(inputBuffer, 0, charRead);                    
					    sem += readString;
					    inputBuffer = new char[BUFFER_SIZE];
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
		return sem;
	}

	private InputStream OpenHttpConnection(String urlString) 
    throws IOException
    {
        InputStream in = null;
        int response = -1;
               
        URL url = new URL(urlString); 
        URLConnection conn = url.openConnection();
        
        if (!(conn instanceof HttpURLConnection))                     
            throw new IOException("Not an HTTP connection");
        
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.setConnectTimeout(4500);
            httpConn.connect(); 

            response = httpConn.getResponseCode();                 
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();                                 
            }                     
        }
        catch (Exception ex)
        {
            throw new IOException("Error connecting");            
        }
        return in;     
    }
	
	
	/**
	 * Connects to the HAW Server (STUNDENPLAN_URL)
	 * @return InputStreamReader for reading data from the TimeTable
	 */
	private InputStreamReader connectToTimeTable()
	{
		InputStream in = null;
		InputStreamReader isr = null;
		
        try {
            in = OpenHttpConnection(STUNDENPLAN_URL);
            
            try {
    			isr = new InputStreamReader(in, "Cp1252");
    		} catch (UnsupportedEncodingException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return null;
        }
        
		return isr;
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean isUpdateAvailible()
	{
		int charRead;
		int BUFFER_SIZE = 46; //Stundenplan  WS 10/11 (Vers. 1.2 vom 27.09.10)
		char[] inputBuffer = new char[BUFFER_SIZE];  
		
		InputStreamReader isr = connectToTimeTable();
		
		try {
			if ((charRead = isr.read(inputBuffer)) > 0)	{
				String readString = String.copyValueOf(inputBuffer, 0, charRead);
				String readString2 = Sem_I.substring(0, 46);
				
				if(readString.equals(readString2)) return false;
			}
		} catch (IOException e) {
			Log.e("HAWApp", "HAWmain -> isUpdateAvailible(): " + e.getMessage());
			return false;
		}
		
		return true;
	}
	

	public void onClockClick(View view) {
		switch (view.getId()) {
		case R.id.AnalogClock01:
			Log.i("HAWApp", "HAWmain -> auf HAW-Uhr geklickt");
			Toast.makeText(	HAWmain.this,
							"In der nächste Version wird hier ein View mit allen deinen Reminders geöfnet ;-)",
							Toast.LENGTH_LONG).show();
			// Intent intent = new Intent(this, AlarmClock.class);
			// startActivity(intent);
		}
	}

	public void onTerminplanClick(View view) {
		Intent intent;
		
		if (true == isNetworkAvailable()) {
			if (true == isUpdateAvailible()) {
				updateStundenplan();
				Toast.makeText(	HAWmain.this, "Terminplan wurde Aktualisiert", Toast.LENGTH_LONG).show();
				//Tools.showAllertDialog(this, "Info", "Terminplan wurde Aktualisiert", Tools.WARNING);
				if (true == terminPlanVorhanden) {
					updateVeranstaltungen();
				}
			}
		}
				
		if (terminPlanVorhanden == true) {
			Log.i("HAWApp", "HAWmain -> zum vorhandenen Terminplan");
			WochenAnsicht.changesMade = false;
			intent = new Intent(this, WochenAnsicht.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else {
			Log.i("HAWApp", "HAWmain -> zum Terminplan erstellen");
			intent = new Intent(HAWmain.this, StudentOderProfWaehlen.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}
	
	
	private String downloadStundenplan()
    {
		int charRead;
        int BUFFER_SIZE = 2000;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];  
        InputStreamReader isr = connectToTimeTable();
	        
	        try {
	            while ((charRead = isr.read(inputBuffer)) > 0) {                    
	                //---convert the chars to a String---
	                String readString = String.copyValueOf(inputBuffer, 0, charRead);                    
	                str += readString;
	                inputBuffer = new char[BUFFER_SIZE];
	            }
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	            return "";
	        }    

        return str;        
    }
	
	private void updateStundenplan() {
		
		Sem_I = downloadStundenplan();
		try {
		  OutputStreamWriter out = new OutputStreamWriter(openFileOutput("Sem_I",0));
		  out.write(Sem_I);
		  out.close();
		} catch (java.io.IOException e) {
			Toast.makeText(	HAWmain.this,
				"Könnte den neuen Stundenplan nicht speichern",
				Toast.LENGTH_LONG).show();
		}
		
	}

	private void updateVeranstaltungen() {
		// TODO Auto-generated method stub
		
	}

	private void openURI(View view, String u) {
		Intent intent;
		Uri uri = Uri.parse(u);
		intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(HAWmain.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo.isConnected();
	}


	public void onMensaClick(View view) {
		Log.i("HAWApp", "HAWmain -> zur Mensa");
		openURI(view, "http://www.studierendenwerk-hamburg.de/essen/tag.php?haus=Berliner%20Tor");
	}

	public void onMailerClick(View view) {
		Log.i("HAWApp", "HAWmain -> zum Mailer");
		openURI(view, "https://haw-mailer.haw-hamburg.de/CookieAuth.dll?GetLogon?curl=Z2Fowa&reason=0&formdir=1");
	}

	public void onBiblClick(View view) {
		Log.i("HAWApp", "HAWmain -> zur Bibliothek");
		openURI(view, "http://www.haw-hamburg.de/8338.0.html");
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, INFORMATIONEN_ID, 0, "Informationen").setIcon(
				android.R.drawable.ic_menu_info_details);
		menu.add(0, VERLASSEN_ID, 0, "Verlassen").setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return result;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case VERLASSEN_ID:
			Log.i("HAWApp", "HAWmain -> Verlassen Hauptmenu uber Menu");
			ActivityRegistry.finishAll();
			return true;
		case INFORMATIONEN_ID:
			Log.i("HAWApp", "HAWmain -> zu den Informationen uber Menu");
			Intent intent = new Intent(this, InformationenAnzeigen.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i("HAWApp", "HAWmain -> Verlassen Hauptmenu uber Rucktaste");
			ActivityRegistry.finishAll();
		}
		if (keyCode == KeyEvent.KEYCODE_MENU){
			Log.i("HAWApp", "HAWmain -> Menu im Hauptmenu aktiviert");
		}
		return false;
	}

	/*
	private void createAllertDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("Terminplan ist leer");
		builder.setMessage("Willst du einen erstellen?");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		
		builder.setCancelable(false).setPositiveButton("Ja",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Log.i("HAWApp", "HAWmain -> TerminplanerstellenAllert -> Ja neuen Terminplan erstellen");
						
						Sem_I = downloadStundenplan();
						if(Sem_I == "") {
							dialog.cancel();							
							
							AlertDialog.Builder builder = new AlertDialog.Builder(HAWmain.this);
							builder.setTitle("Internetverbindung Problem")
								   .setMessage("Könnte auf Stundenplan nicht zugreifen..")
								   .setIcon(android.R.drawable.ic_dialog_alert)
							       .setCancelable(true)
//							       .setPositiveButton("Fortfahren", new DialogInterface.OnClickListener() {
//							           public void onClick(DialogInterface dialog, int id) {
//							        	   Intent intent = new Intent(HAWmain.this, StudentOderProfWaehlen.class);
//							        	   startActivity(intent);
//							        	   dialog.cancel();
//							           }
//							       })
							       .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
							           public void onClick(DialogInterface dialog, int id) {
							                dialog.cancel();
							           }
							       });
							AlertDialog alert = builder.create();
							alert.show();
						}
						else {
							Intent intent = new Intent(HAWmain.this, StudentOderProfWaehlen.class);
							startActivity(intent);
							dialog.cancel();
						}
					}

					private void updateVeranstaltungen() {
						// TODO Auto-generated method stub
						
					}

					private void updateStundenplan() {
						// TODO Auto-generated method stub
						
					}
				}).setNegativeButton("Nein",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Log.i("HAWApp", "HAWmain -> TerminplanerstellenAllert -> nein keinen Terminplan erstellen");
						dialog.cancel();
					}
				});

		alert = builder.create();
	}
	*/
}