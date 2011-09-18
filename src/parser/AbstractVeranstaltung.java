package parser;

public class AbstractVeranstaltung {

	protected String name;
	protected String dozent;
	protected String raum;
	protected String wochentag;
	protected String notitz;
	
	public String getName() {
		return name;
	}	
	public String getDozent(){
		return dozent;
	}	
	public String getRaum(){
		return raum;
	}	
	public String getWochentag(){
		return wochentag;
	}
	public String getNotitz() {
		return notitz;
	}	

	public void setName(String name) {
		this.name = name;
	}
	public void setDozent(String dozent) {
		this.dozent = dozent;
	}
	public void setRaum(String raum) {
		this.raum = raum;
	}
	public void setWochentag(String wochentag) {
		this.wochentag = wochentag;
	}
	public void setNotitz(String notitz) {
		this.notitz = notitz;
	}
	
	
	public int getWochentagAsInt() {
		
		if(wochentag.equals("Mo")) return 1;
		if(wochentag.equals("Di")) return 2;
		if(wochentag.equals("Mi")) return 3;
		if(wochentag.equals("Do")) return 4;
		if(wochentag.equals("Fr")) return 5;
		if(wochentag.equals("Sa")) return 6;
		if(wochentag.equals("So")) return 7;

		return -1;
	}

}
