package activities;

import informatik.haw.app.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class AlarmClock extends Activity {

	public static final int VERLASSEN_ID = Menu.FIRST;
	public static final int INFORMATIONEN_ID = Menu.CATEGORY_SECONDARY;
	public static final int MENU_ID = Menu.CATEGORY_ALTERNATIVE;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.alarmclock);
		ActivityRegistry.register(this);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	public void onAlarmClick(View view) {
		Intent AlarmClockIntent;
		if (view.getId() == R.id.bt_alarm_festlegen || view.getId() == R.id.AnalogClock02 ) {
			AlarmClockIntent = new Intent(Intent.ACTION_MAIN).addCategory(
					Intent.CATEGORY_LAUNCHER).setComponent(
					new ComponentName("com.android.alarmclock",
							"com.android.alarmclock.AlarmClock"));
			startActivity(AlarmClockIntent);
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_ID, 0, "Menu");
		menu.add(0, INFORMATIONEN_ID, 0, "Informationen");
		menu.add(0, VERLASSEN_ID, 0, "Verlassen");
		return result;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case VERLASSEN_ID:
			ActivityRegistry.finishAll();
			return true;
		case INFORMATIONEN_ID:
			Intent intent = new Intent(this, InformationenAnzeigen.class);
			startActivity(intent);
			break;
		case MENU_ID:
			Intent intent02 = new Intent(this, HAWmain.class);
			startActivity(intent02);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
