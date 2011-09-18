package parser;

import java.util.ArrayList;
import java.util.List;

import activities.HAWmain;
import activities.StudiengangWaehlen;
import android.util.Log;

/**
 * @author Mikhail Goldenzweig
 */

public class StundenplanParser {

//============================================================================================================================//	
	private int zeileNr;
	private String[] lines;
	private static StundenplanParser instance = null;
	
//============================================================================================================================//
	
	private StundenplanParser () {
		zeileNr = 0;
	}

//============================================================================================================================//
	
	public static StundenplanParser getInstance() {
		return instance == null ? new StundenplanParser() : instance;
	}
	
//============================================================================================================================//
	
	private String readLine() {
		
		String line = null;	
		do {
			if (lines.length != zeileNr) {
				line = lines[zeileNr];
				zeileNr++;
			}
			else break;
		} while(line == "");	
		return line;
	}
	
//============================================================================================================================//
	
	private void parseError(String errorMessage) {
		System.err.println(errorMessage);
		Log.e("HAWApp", "Parser: " + errorMessage);
		//throw new Exception("Parse Error, Zeile " + zeileNr + ": " + errorMessage);
	}
	
//============================================================================================================================//
	
	private E_Studiengang getStudiengang(String semestergruppe) {
		
		if(semestergruppe.contains("B-AI"))  return E_Studiengang.BAI;
		if(semestergruppe.contains("B-TI"))  return E_Studiengang.BTI;
		if(semestergruppe.contains("M-INF")) return E_Studiengang.MINF;
		
		return E_Studiengang.UNDEFINED;
	}

//============================================================================================================================//
	
	private String springeZuNaechsteSemestergruppe() {
		String line = "";
		do {
			line = readLine();
		} while(line != null && !line.startsWith("Semestergruppe"));
		return line;
	}

//============================================================================================================================//

	/**
	 * Dise Methode bildet die Schnittstelle nach auﬂen.
	 * @return Liste der Veranstaltungen
	 */
	public Stundenplan getVeranstaltungen() {
		
		List<E_Studiengang> studiengaenge = StudiengangWaehlen.checkedCourses;
		Stundenplan plan = new Stundenplan();
		
		lines = HAWmain.Sem_I.split("\r\n");
		
		try {		
			String line = readLine();
			
			int semester = 0;
			String kw = "";
			E_Studiengang studiengang = E_Studiengang.UNDEFINED;
			Veranstaltung veranstaltung = null;

			while (line != null) {				
				if(line.startsWith("Stundenplan")) {					
				}
				else
				if(line.startsWith("Semestergruppe")) {
						
					String[] tmp = line.split("[ \t]+");
					String semestergruppe = tmp[1];
					
					studiengang = getStudiengang(semestergruppe);
					
					if( (studiengang == E_Studiengang.BAI  && false == studiengaenge.contains(E_Studiengang.BAI)) ||
						(studiengang == E_Studiengang.BTI  && false == studiengaenge.contains(E_Studiengang.BTI)) ||
						(studiengang == E_Studiengang.MINF && false == studiengaenge.contains(E_Studiengang.MINF))||
						(studiengang == E_Studiengang.UNDEFINED)
					  ) {					
						line = springeZuNaechsteSemestergruppe();
						continue;
					}												
					
					semester = Integer.parseInt("" + semestergruppe.charAt(semestergruppe.length()-1));
				}
				else
				if(true == isValidKW(line)) {
					kw = line;
				}
				else
				if(line.equals("Name,Dozent,Raum,Tag,Anfang,Ende")) {
				}
				else { //soll eine Veranstaltung sein!					
					veranstaltung = Veranstaltung.parseVeranstaltung(line, kw);
					
					if(null != veranstaltung) {
						
						//doppelte Eintr‰ge elliminieren
						Semester s = plan.getStudiengang(studiengang).getSemester(semester);
						List<Veranstaltung> vListe = s.getAlleVeranstaltungen();
						if( true == vListe.contains(veranstaltung) ) {
							Veranstaltung v = vListe.get(vListe.indexOf(veranstaltung));
							v.addKW(veranstaltung.getKWs());
						}
						else {
							s.addVeranstung(veranstaltung);
						}
					}
				}				
				line = readLine();
			}
		} catch (Exception e) {
			parseError(e.getMessage());
		}
		
		for (E_Studiengang studiengang: studiengaenge) {
			for (int sem = 1; sem <= 6; sem ++) {
				for (int i = 0; i < plan.getStudiengang(studiengang).getSemester(sem).size(); i ++) {
					plan.getStudiengang(studiengang).getSemester(sem).getVeranstaltung(i).sortKWs();
				}
			}
		}

		return plan;
	}	
	
//============================================================================================================================//

	private boolean isValidKW (String kw) {
		
		if (kw == null || "".equals(kw.trim())) return false;
		String[] arr_kwochen = kw.split(",");
		try {							
			for(int i = 0; i  < arr_kwochen.length; i++) {
				if(arr_kwochen[i].contains("-")) {
					String[] arr_kwochen2 = arr_kwochen[i].split("-");
					for(int j = 0; j < arr_kwochen2.length; j++) {
						Integer.parseInt(arr_kwochen2[j].trim());
					}
				}
				else {
					Integer.parseInt(arr_kwochen[i].trim());
				}
			}
		} catch (NumberFormatException e) {					
			return false;
		}		
		return true;
	}

//============================================================================================================================//	
	
	public Stundenplan getDummyVeranstaltungen() {
	
		Stundenplan sp = new Stundenplan();
		Semester ai_sem1 = sp.getStudiengang(E_Studiengang.BAI).getSemester(1);
		ai_sem1.addVeranstung(Veranstaltung.parseVeranstaltung("INF-WP-A3,WST,1486,Di,8:15,11:30", "39") );
		ai_sem1.addVeranstung(Veranstaltung.parseVeranstaltung("INF-WPP-C1/01,BRN/[Loh],0701,Fr,8:15,11:30", "40") );
		ai_sem1.addVeranstung(Veranstaltung.parseVeranstaltung("AI6-IS,NTZ,0481,Di,8:15,11:30", "39, 40") );
		ai_sem1.addVeranstung(Veranstaltung.parseVeranstaltung("AI6-ISP/01,NTZ,1101a,Do,12:30,15:45", "39-50") );
		
		return sp;
	}
	
//============================================================================================================================//
	
	public List<Veranstaltung> getDummyVeranstaltungsList() {
		
		List<Veranstaltung> list = new ArrayList<Veranstaltung>();
		
		try {
			list.add(Veranstaltung.parseVeranstaltung("INF-WP-A3,WST,1486,Di,8:15,11:30", "39"));		
			list.add(Veranstaltung.parseVeranstaltung("INF-WPP-C1/01,BRN/[Loh],0701,Fr,8:15,11:30", "40"));
			list.add(Veranstaltung.parseVeranstaltung("AI6-IS,NTZ,0481,Do,8:15,11:30", "39, 40"));
			list.add(Veranstaltung.parseVeranstaltung("AI6-ISP/01,NTZ,1101a,Do,12:30,15:45", "39-50"));
			list.add(Veranstaltung.parseVeranstaltung("INF-WPP-C2/01,ESS,1101a,Fr,8:15,11:30", "39"));
			list.add(Veranstaltung.parseVeranstaltung("BAI2-LB,KLC,1001,Mi,8:15,11:30", "39"));
			list.add(Veranstaltung.parseVeranstaltung("BAI2-RMP,CNZ,0265,Fr,8:15,11:30", "39"));
			list.add(Veranstaltung.parseVeranstaltung("BAI2-RMPP/01,CNZ/[Ben],0709,Fr,12:30,15:45", "39"));
		} catch (Exception e) {
			Log.e("getDummyVeranstaltungsList", e.getMessage());
			e.printStackTrace();
		}
		
		return list;
	}
	
	
//============================================================================================================================//
	

	
//	public static void main(String[] args) {
//		
//		StundenplanParser parser = StundenplanParser.getInstance();
//		
////		Stundenplan sp = parser.getVeranstaltungen(getResources().openRawResource(R.raw.sem));
//		
//		List<E_Studiengang> l = new ArrayList<E_Studiengang>();
//		l.add(E_Studiengang.BAI);
//		
//		InputStream is = null;
//		try {
//			is = new FileInputStream("sem");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		Stundenplan sp = parser.getVeranstaltungen(is, l);
//		
//		System.out.println("finitto");
//		
////		parser.printVeranstaltungen();
//	}
}