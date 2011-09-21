package activities;

import com.haw.app.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.Toast;

public class StudentOderProfWaehlen extends Activity{
	
	private RadioButton studentRadioButton;
	private RadioButton profRadioButton;
	
	public static String studoderprof = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setTitle("Bist du ein Prof oder Student?");
		setContentView(R.layout.student_prof_waehlen);
		this.initView();
		Log.i("HAWApp", "StudentOderProfWaehlen -> StudentOderProfWaehlen gestartet");
		
		ActivityRegistry.register(this);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Log.i("HAWApp", "StudentOderProfWaehlen -> zum Hauptmenu uber Rucktaste");
			Intent intent = new Intent(this, HAWmain.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
   			startActivity(intent);
		}
		if (keyCode == KeyEvent.KEYCODE_MENU){
			Log.i("HAWApp", "StudentOderProfWaehlen -> Menu in StudiengangWaehlen aktiviert");
		}
		if (keyCode == KeyEvent.KEYCODE_HOME){
			Log.i("HAWApp", "StudentOderProfWaehlen -> Home über Hometaste");
		}
		return false;
	}
	
	private void setOnCheckedChangeListener(RadioButton r, final String s) {
		r.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					Log.i("HAWApp", "StudentOderProfWaehlen -> " + s + " gecheckt");
				} else {
					Log.i("HAWApp", "StudentOderProfWaehlen -> " + s + " entcheckt");
				}
			}
		});
	}
	
	private void initView(){
		//set Orientation to portrait mode
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		studentRadioButton  = (RadioButton) findViewById(R.id.StudentRadioButton);
		profRadioButton  = (RadioButton) findViewById(R.id.ProfRadioButton);
		
		setOnCheckedChangeListener(studentRadioButton, "Student");
		setOnCheckedChangeListener(profRadioButton, "Prof");
	}
	
	//perform action when Button is clicked
	public void onSelectionButtonClick(View view){
		
		Log.i("HAWApp", "StudentOderProfWaehlen -> Auf Auswaehlen geklickt");
		
		if(studentRadioButton.isChecked() == true) {
			
			studoderprof = "stud";
			Intent intent = new Intent(this, StudiengangWaehlen.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else
		if(profRadioButton.isChecked() == true) {	
		
			studoderprof = "prof";
			Toast.makeText(	StudentOderProfWaehlen.this, 
							"Profs sind derzeit leider nicht unterstützt", 
							Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(	StudentOderProfWaehlen.this, 
							"Wähle doch irgendwas aus! =)", 
							Toast.LENGTH_SHORT).show();
		}
	}

}
