package parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.util.Log;

public class Termin extends AbstractVeranstaltung {
	
	private int kw;
	private Calendar anfang;
	private Calendar ende;

	public int getKW() {
		return kw;
	}
	public Calendar getAnfang() {
		return anfang;
	}
	public Calendar getEnde() {
		return ende;
	}

	public Termin(Veranstaltung veranstaltung, int kw) {
		
		this.name = veranstaltung.name;
		this.dozent = veranstaltung.dozent;
		this.raum = veranstaltung.raum;
		this.wochentag = veranstaltung.wochentag;
		
		this.anfang = Calendar.getInstance();
		this.ende = Calendar.getInstance();

		this.notitz = "";
		this.kw = kw;
		
		setDates();
	}

	private List<Integer> weekMonths = new ArrayList<Integer>() {
		private static final long serialVersionUID = 1L;{
		add(0);	add(31); add(59); add(90); add(120); add(151); add(181);  
		add(212); add(243);	add(273); add(304);	add(334);}};
		
	private List<Integer> leapWeekMonths = new ArrayList<Integer>() {
		private static final long serialVersionUID = 1L;{
		add(0);	add(31); add(60); add(91); add(121); add(152); add(182);  
		add(213); add(244); add(274); add(305); add(335);}};
	
	
	//Daten anhand kw und wochentag ausrechnen und in anfang/ende reinschreiben
	//http://en.wikipedia.org/wiki/ISO_week_date
	//--> Calculating a date given the year, week number and weekday
	private void setDates() {		 
		 
		int day = 0;			
		int int_wochentag = getWochentagAsInt();
		
		if (int_wochentag == -1) {
			Log.e("Veranstaltung::setDates", "Unbekanntes Wochentag: " + wochentag);
		}
		else {
			
			//Example: year 2008, week 39, Friday (day 5)
			
			//Correction for 2008: 5 + 3 = 8
			int correction = int_wochentag + 3;
			//(39 * 7) + 5 = 278
			int tmp = (kw*7) + int_wochentag;
			//278 - 8 = 270
			int ordinalDay = tmp - correction;
			
			Calendar cal = Calendar.getInstance();
			
			List<Integer> _weekMonths = weekMonths;
			if ((cal.get(Calendar.YEAR) == 2012) || (cal.get(Calendar.YEAR) == 2016))
			_weekMonths = leapWeekMonths;
			
			int i = 0;
			for(i = 0; i < 12; i++) {
				if (_weekMonths.get(i) > ordinalDay) break;
			}
			
			//Ordinal day 270 of a leap year is day 270 - 244 = 26 September
			day = ordinalDay - _weekMonths.get(i-1);
			
			
			//TODO: d.h. anfang und ende kann nur am gleichen Tag sein!!!
			anfang.set(Calendar.MONTH, i-1);
			anfang.set(Calendar.DAY_OF_MONTH, day);
			anfang.set(Calendar.WEEK_OF_YEAR, kw);
			
			ende.set(Calendar.MONTH, i-1);
			ende.set(Calendar.DAY_OF_MONTH, day);
			ende.set(Calendar.WEEK_OF_YEAR, kw);
		}
	}
}
