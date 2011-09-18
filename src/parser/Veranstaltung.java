package parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import android.text.Editable;
import android.util.Log;

/**
 * 
 * @author Zabihullah Safai && Mikhail Goldenzweig
 * @desc Eine Container-Klasse fuer die Veranstaltungen
 *
 */
public class Veranstaltung extends AbstractVeranstaltung {
	
	//Alle durch "," getrennte Kalenderwochen an den die Veranstaltung stattfindet
	private String kws; 
	private String anfang;	//Anfangszeit (z.b. "12:30")
	private String ende;	//Endezeit    (z.b. "16:45")
	
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String getKWs() {
		return kws;
	}	
	public String getAnfangsZeit(){
		return anfang;
	}	
	public String getEndeZeit(){
		return ende;
	}
	public int getAnfangsZeitMinutes(){
		return toMinutes(anfang);
	}	
	public int getEndeZeitMinutes(){
		return toMinutes(ende);
	}

	public void setKws(String kws) {
		this.kws = kws;
	}
	public void setAnfang(String anfang) {
		this.anfang = anfang;
	}
	public void setEnde(String ende) {
		this.ende = ende;
	}
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////	
	
	public String toString() {
		return name+","+dozent+","+raum+","+wochentag+","+anfang+","+ende+","+kws+","+notitz;
	}	
	
//////////////////////////////////////////////////////////////////////////////////////////////////	
		

	private Veranstaltung() {}

	public Veranstaltung(	String name,
							String dozent,
							String raum,
							String wochentag,
							String anfang,
							String ende,
							String kw,
							String notitz) {
		
		this.name = name;
		this.dozent = dozent;
		this.raum = raum;
		this.wochentag = wochentag;		
		
		this.anfang = anfang;
		this.ende = ende;		
		
		this.kws = kw;
		this.notitz = notitz;
	}	
		
	public static Veranstaltung parseVeranstaltung(String source, String kw) {
		
		Veranstaltung self = null;
		
		if (null != source && !source.equals("")) {			
			
			self = new Veranstaltung();
			//StringTokenizer durch split ersetzt weil problemme mit leere Felder 
			//z.B. "INF-WP-C2,ESS,,Fr,8:15,11:30"
			String tmp[] = source.split(",");
			//INF-WPP-B2/01,SRS/[Kas],1102,Mi,8:15,11:30
			
			//if (tmp.length>0)
			self.name = tmp[0].trim();
			if (tmp.length>1) self.dozent = tmp[1].trim();
			if (tmp.length>2) self.raum = tmp[2].trim();
			if (tmp.length>3) self.wochentag = tmp[3].trim();
			if (tmp.length>4) self.anfang = tmp[4].trim();//self.createCalendarObj(tmp[4].trim());
			if (tmp.length>5) self.ende   = tmp[5].trim();//self.createCalendarObj(tmp[5].trim());
			
			self.kws = self.splitKWs(kw);
			self.notitz = "";
		}
		return self;
	}
	
	private int toMinutes(String zeit) {
		
		String t[] = zeit.split(":");
		int hours = Integer.parseInt(t[0]);
		int mins  = Integer.parseInt(t[1]);
		
		return (hours*60 + mins);
	}
	
	private String splitKWs(String _kw) {

		String kwochen = "";		
		String[] arr_kwochen = _kw.split(",");
		int i = 0, j = 0;
		
		for(i = 0; i  < arr_kwochen.length; i++) {
			if(arr_kwochen[i].contains("-")) {
				String[] arr_kwochen2 = arr_kwochen[i].split("-");
				int von = Integer.parseInt(arr_kwochen2[0].trim());
				int bis = Integer.parseInt(arr_kwochen2[1].trim());
				for(j = von; j <= bis; j++) {
					if(kwochen.equals("")) {
						kwochen += j;
					}
					else {
						kwochen += "," + j;
					}
				}
			}
			else {
				if(kwochen.equals("")) {
					kwochen += arr_kwochen[i].trim();
				}
				else {
					kwochen += "," + arr_kwochen[i].trim();
				}
			}
		}

		return kwochen;
	}
	
	
	public Calendar createCalendarObj(String _zeit) {
		
		Calendar cal = Calendar.getInstance();
	
		String time[] = _zeit.split(":");
	
		try {
			int hour   = Integer.parseInt(time[0].trim());
			int minute = Integer.parseInt(time[1].trim());		
		
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, minute);
			
		} catch (Exception e) {
			//System.err.println("createCalendarObj :: parseIntexception : " + _zeit);
			Log.e("Veranstaltung.createCalendarObj", "parseIntexception: " + _zeit);
		}
		
		return cal;
	}
	
	public List<Termin> getTermine() {
		
		List<Termin> termine = new ArrayList<Termin>();
		
		//get Calendar weeks as list of Integers
		List<Integer> kwochen = new ArrayList<Integer>();		
		String[] arr_kwochen = kws.split(",");
		int i = 0, j = 0;
		
		for(i = 0; i  < arr_kwochen.length; i++) {
			if(arr_kwochen[i].contains("-")) {
				String[] arr_kwochen2 = arr_kwochen[i].split("-");
				int von = Integer.parseInt(arr_kwochen2[0].trim());
				int bis = Integer.parseInt(arr_kwochen2[1].trim());
				for(j = von; j <= bis; j++) {
					kwochen.add(j);
				}
			}
			else {
				kwochen.add(Integer.parseInt(arr_kwochen[i].trim()));
			}
		}
		
		//for each Calendar week create a new Termin and add to the termine list.
		for(i = 0; i < kwochen.size(); i++) {

			Termin termin = new Termin(this, kwochen.get(i));
			termine.add(termin);			
		}			
		
		return termine;
	}
	
	public void sortKWs() {
		
		LinkedList<String> sorted = new LinkedList<String>(); 
		String[] s = kws.split(",");
		for(String kw: s) sorted.add(kw);
		Collections.sort(sorted, new Comparator<String>() {
			public int compare(String object1, String object2) {
				return object1.compareTo(object2);
			}
		});
		String sortedKws = sorted.get(0);		
		for(int i = 1; i < sorted.size(); i++) sortedKws += (","+sorted.get(i));
		kws = sortedKws;
	}
		
	public void addKW(String _kw) {
		
		String[] newKWs = _kw.split(",");
		
		for(String k: newKWs) {
			if(kws.contains(k) == false) {
				kws += "," + k;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dozent == null) ? 0 : dozent.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Veranstaltung other = (Veranstaltung) obj;
		if (dozent == null) {
			if (other.dozent != null)
				return false;
		} else if (!dozent.equals(other.dozent))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
