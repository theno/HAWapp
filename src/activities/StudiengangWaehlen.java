package activities;


import java.util.ArrayList;
import java.util.List;

import com.haw.app.R;

import parser.E_Studiengang;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class StudiengangWaehlen extends Activity{
	
	private CheckBox checkBoxButton_BAI;
	private CheckBox checkBoxButton_BTI;
	private CheckBox checkBoxButton_MINF;
	
	private AlertDialog alert;
	private List<CheckBox> coursesList = new ArrayList<CheckBox>();	
	
	public static List<E_Studiengang> checkedCourses = new ArrayList<E_Studiengang>();
	public static ProgressDialog parserLoadingDialog = null;
	
	public static boolean changesMade = false;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setTitle("Wähle einen oder mehrere Studiengänge");
		setContentView(R.layout.studiengang_waehlen);

		this.initView();
		Log.i("HAWApp", "StudiengangWaehlen -> StudiengangWaehlen gestartet");
		
		ActivityRegistry.register(this);
		createAllertDialog();
	}
	
	
	private void createAllertDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false)
				.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Log.i("StudiengangWaehlen", "StudiengangWaehlen -> Änderungen verwerfen gewählt");
						
						WochenAnsicht.changesMade = false;
						Intent intent = new Intent(StudiengangWaehlen.this, HAWmain.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
					}
				})
				.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Log.i("HAWApp", "StudiengangWaehlen -> Änderungen nicht verwerfen gewählt");
					}
				});
				
		builder.setTitle("Deine Änderungen werden verworfen. Weiter?");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		alert = builder.create();

	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(WochenAnsicht.changesMade == true) {
				alert.show();
			}
			else {
				Log.i("HAWApp", "StudiengangWaehlen -> zum Hauptmenu uber Rucktaste");
				Intent intent = new Intent(this, StudentOderProfWaehlen.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	   			startActivity(intent);	
			}
		}
		if (keyCode == KeyEvent.KEYCODE_MENU){
			Log.i("HAWApp", "StudiengangWaehlen -> Menu in StudiengangWaehlen aktiviert");
		}
		if (keyCode == KeyEvent.KEYCODE_HOME){
			Log.i("HAWApp", "StudiengangWaehlen -> Home über Hometaste");
		}
		return false;
	}
	
	private void setOnCheckedChangeListener(CheckBox c, final String s) {
		c.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					Log.i("HAWApp", "StudiengangWaehlen -> " + s + " gecheckt");
					//Toast.makeText(StudiengangWaehlen.this, s + " markiert", Toast.LENGTH_SHORT).show();
				} else {
					Log.i("HAWApp", "StudiengangWaehlen -> " + s + " entcheckt");
					//Toast.makeText(StudiengangWaehlen.this, s + " Markierung rueckgaengig", Toast.LENGTH_SHORT).show();
				}
				changesMade = true;
			}
		});
	}
	
	private void initView(){
		//set Orientation to portrait mode
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		//get Views for every CheckBox
		checkBoxButton_BAI  = (CheckBox) findViewById(R.id.checkBoxButton_BAI);
		setOnCheckedChangeListener(checkBoxButton_BAI, "BAI");
		
		checkBoxButton_BTI  = (CheckBox) findViewById(R.id.checkBoxButton_BTI);
		setOnCheckedChangeListener(checkBoxButton_BTI, "BTI");
		
		checkBoxButton_MINF = (CheckBox) findViewById(R.id.checkBoxButton_MINF);
		setOnCheckedChangeListener(checkBoxButton_MINF, "MINF");
		
		//add CheckBoxes to List
		addCheckBoxToList(checkBoxButton_BAI);
		addCheckBoxToList(checkBoxButton_BTI);
		addCheckBoxToList(checkBoxButton_MINF);
		
		//setText for CHECKBOX
		checkBoxButton_BAI.setText(E_Studiengang.BAI.toString());
		checkBoxButton_BTI.setText(E_Studiengang.BTI.toString());
		checkBoxButton_MINF.setText(E_Studiengang.MINF.toString());
		
		if(checkedCourses.contains(E_Studiengang.BAI))	checkBoxButton_BAI.setChecked(true);
		if(checkedCourses.contains(E_Studiengang.BTI))	checkBoxButton_BTI.setChecked(true);
		if(checkedCourses.contains(E_Studiengang.MINF))	checkBoxButton_MINF.setChecked(true);
	}
	

	
	//add CheckBoxes to List 
	private void addCheckBoxToList(CheckBox cb){
		if(!coursesList.contains((CheckBox)cb)){
			coursesList.add(cb);
		}
	}
	
	//perform action when Button is clicked
	public void onSelectionButtonClick(View view){
		
		checkedCourses.clear();
		Log.i("HAWApp", "StudiengangWaehlen -> Auf Auswaehlen geklickt");
		for(int i = 0; i < coursesList.size(); i++) {
			if(coursesList.get(i).isChecked() == true) {
				
				switch(i) {
				case 0: 
					checkedCourses.add(E_Studiengang.BAI);
				    Log.i("HAWApp", "StudiengangWaehlen -> BAI ausgewaehlt");  
				    break;
				case 1: 
					checkedCourses.add(E_Studiengang.BTI);	
					Log.i("HAWApp", "StudiengangWaehlen -> BTI ausgewaehlt");
					break;
				case 2: 
					checkedCourses.add(E_Studiengang.MINF); 
					Log.i("HAWApp", "StudiengangWaehlen -> MINF ausgewaehlt");
					break;
				}
			}
		}
		
		//if size > 0 start new Activity
		if(checkedCourses.size() != 0) {			
			parserLoadingDialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
			Intent intent = new Intent(this, VeranstaltungenWaehlen.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else{
			Toast.makeText(	StudiengangWaehlen.this, 
							"Bitte mindestens einen Studiengang auswaehlen", 
							Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.i("HAWApp", "StudiengangWaehlen -> onResume()");
		
		changesMade = false;
		//ISUE: beim zweiten rückkehren feuert aus irgendwelchem Grund der parserLoadingDialog
		//TODO: diese ISUE Untersuchen. Dies ist ein temp FIX
		if(parserLoadingDialog != null) {
			parserLoadingDialog.dismiss();
		}
	}
	
//	@Override
//	protected void onSaveInstanceState (Bundle outState) {
//		outState.putBoolean("bai",  checkBoxButton_BAI.isChecked());
//		outState.putBoolean("bti",  checkBoxButton_BTI.isChecked());
//		outState.putBoolean("minf", checkBoxButton_MINF.isChecked());
//	}
	
//	@Override
//	protected void onRestoreInstanceState (Bundle outState) {
//		checkBoxButton_BAI.setChecked(outState.getBoolean("bai"));
//		checkBoxButton_BTI.setChecked(outState.getBoolean("bti"));
//		checkBoxButton_MINF.setChecked(outState.getBoolean("minf"));
//	}

}
