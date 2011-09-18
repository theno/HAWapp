package activities;

import parser.Veranstaltung;
import informatik.haw.app.R;
import helper.Time;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class TerminDetails extends Activity{

	public static boolean changesMade = false;
	
	private EditText veranstaltung_et;
	private EditText raum_et;
	private EditText dozent_et;
	private EditText kws_et;
	private EditText notitz_et;
	
	private TimePicker anfang_tp;
	private TimePicker ende_tp;
	
	private Button speichern;
	
	private Veranstaltung v;
	
	private TextWatcher watcher = new TextWatcher() {
		
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			speichern.setEnabled(true);
		}		
		public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}		
		public void afterTextChanged(Editable s) {}
	};
	
	private OnTimeChangedListener onTimeChangedListener = new OnTimeChangedListener() {
		
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			speichern.setEnabled(true);
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		setContentView(R.layout.termin_details);
		
		v = WochenAnsicht.gewaehlteVeranstaltung.veranstaltung;
		
		Log.i( "HAWApp", "TerminDetails -> TerminDetails fuer " + v.getName() + " gestartet" );
		
		ActivityRegistry.register(this);
		
		veranstaltung_et = (EditText) findViewById(R.id.Veranstaltung_et);
		veranstaltung_et.setText(v.getName());
		veranstaltung_et.addTextChangedListener(watcher);
		
		raum_et = (EditText) findViewById(R.id.Raum_et);
		raum_et.setText(v.getRaum());
		raum_et.addTextChangedListener(watcher);
		
		dozent_et = (EditText) findViewById(R.id.Dozent_et);
		dozent_et.setText(v.getDozent());
		dozent_et.addTextChangedListener(watcher);
		
		kws_et = (EditText) findViewById(R.id.KWs_et);
		kws_et.setText(v.getKWs());
		kws_et.addTextChangedListener(watcher);
		
		notitz_et = (EditText) findViewById(R.id.Notitz_et);
		notitz_et.setText(v.getNotitz());
		notitz_et.addTextChangedListener(watcher);
		
		
		anfang_tp = (TimePicker) findViewById(R.id.Anfang_tp);
		anfang_tp.setIs24HourView(true);
		
		Time anfang = new Time(v.getAnfangsZeit());
		anfang_tp.setCurrentHour(anfang.hours);
		anfang_tp.setCurrentMinute(anfang.minutes);
		
		anfang_tp.setOnTimeChangedListener(onTimeChangedListener);
		
		ende_tp = (TimePicker) findViewById(R.id.Ende_tp);
		ende_tp.setIs24HourView(true);
		
		Time ende = new Time(v.getEndeZeit());
		ende_tp.setCurrentHour(ende.hours);
		ende_tp.setCurrentMinute(ende.minutes);
		
		ende_tp.setOnTimeChangedListener(onTimeChangedListener);
		
		speichern = (Button) findViewById(R.id.SpeichernButton);
		speichern.setEnabled(false);
	}
	
	public void onZurueckButton(View view) {
		this.finish();
	}
	
	// convert 0 to "00" or return t as String otherwise
	private String correctZero(int t) {
		return t == 0 ? "00" : "" + t;
	}
	
	public void onSpeichernButton(View view) {
		
		v.setName(veranstaltung_et.getText().toString());
		v.setRaum(raum_et.getText().toString());
		v.setDozent(dozent_et.getText().toString());
		v.setKws(kws_et.getText().toString());v.setName(veranstaltung_et.getText().toString());
		v.setNotitz(notitz_et.getText().toString());
		
		String anfang = ""  + correctZero(anfang_tp.getCurrentHour()) + 
						":" + correctZero(anfang_tp.getCurrentMinute());
		v.setAnfang(anfang);
		String ende =   ""  + correctZero(ende_tp.getCurrentHour()) + 
						":" + correctZero(ende_tp.getCurrentMinute());
		v.setEnde(ende);
		
		speichern.setEnabled(false);		
		changesMade = true;
	}

}
