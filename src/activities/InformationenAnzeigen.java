package activities;

import com.haw.app.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class InformationenAnzeigen extends Activity {
	
	public static final int VERLASSEN_ID = Menu.FIRST;
	public static final int MENU_ID = Menu.CATEGORY_ALTERNATIVE;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.informationen);
		Log.i("HAWApp", "InformationenAnzeigen -> Informationen zum HAWApp gestartet");

		ActivityRegistry.register(this);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i("HAWApp", "InformationenAnzeigen -> zum Hauptmenu uber Rucktaste");
			Intent intent = new Intent(this, HAWmain.class);
   			startActivity(intent);			
		}
		if (keyCode == KeyEvent.KEYCODE_MENU){
			Log.i("HAWApp", "InformationenAnzeigen -> Menu in den Informationen aktiviert");
		}
		if (keyCode == KeyEvent.KEYCODE_HOME){
			Log.i("HAWApp", "InformationenAnzeigen -> Home über Hometaste");
		}
		return false;
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_ID, 0, "Menu");
		menu.add(0, VERLASSEN_ID, 0, "Verlassen");
		return result;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i("HAWApp", "InformationenAnzeigen -> Menu in den Informationen aktiviert");
		switch (item.getItemId()) {
		case VERLASSEN_ID:
			Log.i("HAWApp", "InformationenAnzeigen -> Verlassen Informationen uber Menu");
			ActivityRegistry.finishAll();
			return true;
		case MENU_ID:
			Log.i("HAWApp", "InformationenAnzeigen -> zum Hauptmenu uber Menu");
			Intent intent02 = new Intent(this, HAWmain.class);
			startActivity(intent02);
			break;
		}		
		return super.onOptionsItemSelected(item);
	}

}
