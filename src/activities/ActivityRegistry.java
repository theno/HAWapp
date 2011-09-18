package activities;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;

public class ActivityRegistry {
	private static List<Activity> activities;

	public static List<Activity> getActivities() {
		return activities;
	}

	public static void register(Activity activity) {
		if (activities == null) {
			activities = new ArrayList<Activity>();
		}
		activities.add(activity);
	}

	public static void finishAll() {
		for (Activity activity : activities) {
			activity.finish();
		}
	}
	
	public static void finishAllExeptMain() {
		for (Activity activity : activities) {
			if(activity.getClass().getSimpleName().equals("HAWmain") == false) {
				activity.finish();
			}
		}
	}
	
	public static boolean isRegistered(String className) {
		for (Activity activity : activities) {
			if(className.equals(activity.getClass().getName())) return true;
		}
		return false;
	}
}
