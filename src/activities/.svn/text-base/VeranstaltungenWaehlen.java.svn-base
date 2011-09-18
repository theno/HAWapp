package activities;

import informatik.haw.app.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import parser.Semester;
import parser.Stundenplan;
import parser.StundenplanParser;
import parser.Termin;
import parser.Veranstaltung;
import activities.WochenAnsicht.VeranstaltungButton;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

public class VeranstaltungenWaehlen extends Activity {

	public static List<Veranstaltung> gewaehlteVeranstaltungen = null;
	public static boolean changesMade = false;
	private Stundenplan stundenplan = null;

	//Views
	private ArrayList<LinkedList<veranstaltungView>> veranstaltungenList;
	private ExpandableListAdapter mAdapter;
	private ExpandableListView elv;
	private AlertDialog alert;	
	
	//Some constants
	private static final int OK = 0;
	private static final int NOK = 1;

	private static final int WHITE  = 0xffffffff;
	private static final int DARK   = 0xff30587D;
	private static final int GREY   = 0x5fffffff;
	private static final int ORANGE = 0xffcc6600;
	
	private Comparator<veranstaltungView> c = new Comparator<veranstaltungView>() {
		  public int compare(veranstaltungView v1, veranstaltungView v2) {
			  return v1.veranstaltung.getName().compareTo(v2.veranstaltung.getName());
		  }
	};

	// Speichern die Zustände in eigene Struktur
	class veranstaltungView {
		public Veranstaltung veranstaltung;
		public boolean marked;

		public veranstaltungView(Veranstaltung v) {
			veranstaltung = v;
			marked = false;
		}
	}

	// Der Listener wird nicht auf einzelne Elemente gesetzt sondern auf die
	// ganze ExpandableListView
	private OnChildClickListener cl = new OnChildClickListener() {

		public boolean onChildClick(ExpandableListView parent, View view,
				int groupPosition, int childPosition, long id) {

			veranstaltungView v = veranstaltungenList.get(groupPosition).get(childPosition);
			
			if (v.marked == false) {

				v.marked = true;
				((TextView) view).setSelected(true);
				((TextView) view).setBackgroundColor(ORANGE);
				Log.i("HAWApp", "VeranstaltungenWaehlen -> Veranstaltung " + v.veranstaltung.getName() + " markiert");
			} else {
				v.marked = false;
				((TextView) view).setSelected(false);
				((TextView) view).setBackgroundColor(DARK);
				Log.i("HAWApp", "VeranstaltungenWaehlen -> Veranstaltung  " + v.veranstaltung.getName() + " Markierung entfernt");
			}
			
			changesMade = true;
			return false;
		}
	};
	
	private OnGroupClickListener gl = new OnGroupClickListener() {

		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			Log.i("HAWApp", "VeranstaltungenWaehlen -> Semester " + (groupPosition+1) + " ausgewaehlt");
			return false;
		}		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("Wähle die Veranstaltungen");
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.veranstaltungen_waehlen);
		
		Log.i("HAWApp", "VeranstaltungenWaehlen -> VeranstaltungenWaehlen gestartet");
		
		ActivityRegistry.register(this);		
		
		generateData();
		
		elv = (ExpandableListView) findViewById(R.id.ExpandableListView01);
		// Adapter erstellen und setzen
		mAdapter = new MyExpandableListAdapter();
		elv.setAdapter(mAdapter);

		elv.setItemsCanFocus(false);
		elv.setChoiceMode(ExpandableListView.CHOICE_MODE_MULTIPLE);
		elv.setCacheColorHint(0x00000000);
		elv.setVerticalFadingEdgeEnabled(true);
		elv.setFadingEdgeLength(30);
		elv.setDividerHeight(3);
		
		elv.setScrollbarFadingEnabled(false);

		elv.setOnChildClickListener(cl);
		elv.setOnGroupClickListener(gl);

		createAllertDialog();
	}
	
	private void generateData() {
		
		veranstaltungenList = new ArrayList<LinkedList<veranstaltungView>>();
		
		StundenplanParser parser = StundenplanParser.getInstance();
		
		stundenplan = parser.getVeranstaltungen();
		
		if (StudiengangWaehlen.parserLoadingDialog != null) {
			StudiengangWaehlen.parserLoadingDialog.dismiss();
		}

		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Hier wird der Stundenplan in eine flachere Struktur umgewandelt
		// und zwar in einen 2d Array
		// Im ExpandableList haben wir im Moment (Semester->Veranstaltung)
		// Hierarchie
		// In Zukunft soll da eine 3s Struktur entstehen
		// (Studiengang->Semester->Veranstaltung)
		// genau so wie in Stundenplan. Dann Wird diese Umstrukturierung nicht
		// mehr nötig sein.
		// (c) M.Goldenzweig
		
		for (int i = 0; i < 6; i++)
			veranstaltungenList.add(new LinkedList<veranstaltungView>());

		for (int studiengang = 0; studiengang < StudiengangWaehlen.checkedCourses.size(); studiengang++) {

			Semester sem;
			for (int i = 1; i <= 6; i++) {
				LinkedList<veranstaltungView> semVeranstaltungen = veranstaltungenList.get(i - 1);
				sem = stundenplan.getStudiengang(StudiengangWaehlen.checkedCourses.get(studiengang)).getSemester(i);
				if (null != sem) {
					int size = sem.size();
					for (int j = 0; j < size; j++) {
						Veranstaltung v = sem.getVeranstaltung(j);
						veranstaltungView vv = new veranstaltungView(v);
						if (gewaehlteVeranstaltungen != null &&
							gewaehlteVeranstaltungen.contains(v)) {
							vv.marked = true;
						}
						semVeranstaltungen.add(vv);
					}
					Collections.sort(semVeranstaltungen, c);
				}
			}
		}
	}

	private void createAllertDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false).setPositiveButton("Fortsetzen",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Log.i("HAWApp", "VeranstaltungenWaehlen -> Fortsetzen bei Ueberlappungen geklickt");
						WochenAnsicht.changesMade = true;
						Intent intent = new Intent(VeranstaltungenWaehlen.this,	WochenAnsicht.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
					}
				}).setNegativeButton("Editieren",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Log.i("HAWApp", "VeranstaltungenWaehlen -> Editieren bei Ueberlappungen geklickt");
						dialog.cancel();
					}
				});
		builder.setTitle("Überlappungen gefunden!");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		alert = builder.create();

	}

	/********************************************************************************************************/
	public class MyExpandableListAdapter extends BaseExpandableListAdapter {

		private String[] semesterArray = { "SEMESTER1", "SEMESTER2",
				"SEMESTER3", "SEMESTER4", "SEMESTER5", "SEMESTER6" };

		public Object getChild(int groupPosition, int childPosition) {
			Veranstaltung v = veranstaltungenList.get(groupPosition).get(
					childPosition).veranstaltung;
			
			String dozent = v.getDozent();
			
			if (dozent.equals("") == false) {
				dozent = " (" + dozent + ")  ";
			}
			else dozent = "   ";
			
			return v.getName() + dozent + v.getWochentag() + "  "
					+ v.getAnfangsZeit() + " - " + v.getEndeZeit() 
					+ "\n" + v.getKWs();
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return veranstaltungenList.get(groupPosition).size();
		}

		private TextView getGenericView() {
			// Layout parameters for the ExpandableListView
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, 50);
			TextView textView = new TextView(VeranstaltungenWaehlen.this);

			textView.setTextColor(WHITE);
			// textView.setBackgroundColor(0x000310);
			textView.setLayoutParams(lp);
			// Center the text vertically
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

			return textView;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			TextView textView = null;
			textView = getGenericView();
			// Set the text starting position
			textView.setPadding(26, 0, 0, 0);
			textView.setText((String) getChild(groupPosition, childPosition));
			textView.setSelected(veranstaltungenList.get(groupPosition).get(childPosition).marked);

			if (veranstaltungenList.get(groupPosition).get(childPosition).marked == true)
				textView.setBackgroundColor(ORANGE);
			else
				textView.setBackgroundColor(DARK);

			return textView;
		}

		public Object getGroup(int groupPosition) {
			return semesterArray[groupPosition];
		}

		public int getGroupCount() {
			return veranstaltungenList.size();
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView textView = getGenericView();
			// Set the text starting position
			textView.setPadding(36, 0, 0, 0);
			textView.setTextSize(20);
			textView.setBackgroundColor(GREY);
			textView.setText(getGroup(groupPosition).toString());
			return textView;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return true;
		}
	}

	/********************************************************************************************************/
	
	public int checkForCrossings(List<Veranstaltung> veranstaltungen) {
		
		int crossings_max = 0;
		String message = "";
		
		for(int i = 0; i < veranstaltungen.size(); i++) {
			
			Veranstaltung v1 = veranstaltungen.get(i);
			int crossings = 0;
			
			int anfangV1 = v1.getAnfangsZeitMinutes();
			int endeV1   = v1.getEndeZeitMinutes();
			
			for(int j = i+1; j < veranstaltungen.size(); j++) {
				
				Veranstaltung v2 = veranstaltungen.get(j);
				if (true == v1.equals(v2)) continue;
				// uberprüfe ob die Veranstaltungen am gleichen Tag stattfinden			
				if (true == v1.getWochentag().equals(v2.getWochentag())) {
	
					int anfangV2 = v2.getAnfangsZeitMinutes();
					int endeV2   = v2.getEndeZeitMinutes();
					
					// .. ob die sich zeitlich überkreuzen
					if (   // einmal von oben
						((anfangV2 <= anfangV1) && (endeV2 > anfangV1))
						|| // und einmal von unten
						((anfangV2 >= anfangV1) && (anfangV2 < endeV1))) {
						crossings++;
						
						message += v1.getName() + " überlappt mit "
						+  v2.getName() /*+ " in kw "
						+  v1.getKW()*/   + "\n";
					}
				}
			}
			if (crossings > crossings_max) crossings_max = crossings;
		}
		if (crossings_max > 0) {
			alert.setMessage(message);
			alert.show();
			alert.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
			if (crossings_max > 1) {
				alert.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
			}			
			return NOK;
		}
		return OK;
	}


	public void onTerminplanErstellenButton(View view) {		

		Log.i("HAWApp", "VeranstaltungenWaehlen -> onTerminplanErstellenButton");

		gewaehlteVeranstaltungen = new ArrayList<Veranstaltung>();

		for (int i = 0; i < veranstaltungenList.size(); i++) {
			List<veranstaltungView> semester = veranstaltungenList.get(i);
			for (int j = 0; j < semester.size(); j++) {
				if (semester.get(j).marked == true) {
					gewaehlteVeranstaltungen.add(semester.get(j).veranstaltung);
				}
			}
		}

		if (checkForCrossings(gewaehlteVeranstaltungen) == OK) {

			WochenAnsicht.changesMade = true;
			Intent intent = new Intent(this, WochenAnsicht.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}		
	}

	/***************************************************************************************************/

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i("HAWApp", "VeranstaltungenWaehlen -> zu StudiengangWaehlen uber Ruecktaste");
			Intent intent = new Intent(this, StudiengangWaehlen.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//finish();
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			Log.i("HAWApp", "VeranstaltungenWaehlen -> Menu in VeranstaltungenWaehlen aktiviert");
		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		Log.i("HAWApp", "VeranstaltungenWaehlen -> onResume()");
		
		changesMade = false;
		
		if(WochenAnsicht.changesMade == true) {
			for (int i = 0; i < veranstaltungenList.size(); i++) {
				List<veranstaltungView> semester = veranstaltungenList.get(i);
				for (int j = 0; j < semester.size(); j++) {
					if (false == gewaehlteVeranstaltungen.contains(semester.get(j).veranstaltung)) {
						semester.get(j).marked = false;
					}
				}
			}
		}
		
		if(StudiengangWaehlen.changesMade == true) {
			mAdapter = new MyExpandableListAdapter();
			generateData();
		}
	}
}