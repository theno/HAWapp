package parser;


import java.util.ArrayList;
import java.util.List;

public class Semester {
	
	private String name;
	private List<Veranstaltung> veranstaltungen;
	
	public Semester(String name) {
		this.name = name; 
		veranstaltungen = new ArrayList<Veranstaltung>();
	}
	
	public String getName() {
		return name;
	}
	public Veranstaltung getVeranstaltung(int i) {
		return veranstaltungen.get(i);
	}
	public List<Veranstaltung> getAlleVeranstaltungen() {
		return veranstaltungen;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setVeranstaltung(List<Veranstaltung> veranstaltung) {
		this.veranstaltungen = veranstaltung;
	}	
	public void setAlleVeranstaltungen(List<Veranstaltung> veranstaltung) {
		this.veranstaltungen = veranstaltung;
	}
	
	public void addVeranstung(Veranstaltung veranstaltung) {
		veranstaltungen.add(veranstaltung);
	}
	@Override
	public String toString() {
		return "Semester [name=" + name + ", veranstaltung=" + veranstaltungen
				+ "]";
	}
	public int size() {
		return veranstaltungen.size();
	}
	
}
