package helper;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

public class Tools {

	public static final int WARNING = R.drawable.ic_dialog_alert;
	public static final int ALERT = R.drawable.ic_dialog_info;
	public static final int INFO = R.drawable.ic_menu_info_details;

	/**
	 * @author me
	 * @param Activity
	 *            witch using this dialog
	 * @param Title
	 *            text
	 * @param Message
	 *            text to show in the dialog
	 * @param Allert
	 *            dialog icon type. USE STATIC INTEGER FROM THIS CLASS!!!
	 */
	public static void showAllertDialog(final Activity context, String title,
			String message, int type) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setIcon(type);

		builder.setCancelable(false).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Log.i("HAWApp", context.getClass().getSimpleName()
								+ ": has created an a new window - ");
					}
				});
		builder.create().show();
	}
}
