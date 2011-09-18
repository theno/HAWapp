package helper;

import android.util.Log;

public class Time {

	public int hours;
	public int minutes;
	
	public Time(String s) {
		String[] time = s.split(":");
		try {	
			hours	= Integer.parseInt(time[0]);
			minutes = Integer.parseInt(time[1]);
			
		} catch (NumberFormatException e) {
			Log.e("HAWapp", "Could create Time object from " + s);
		}
	}
}
