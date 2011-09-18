package parser;


public class Stundenplan {
	
	private Studiengang ti;
	private Studiengang ai;
	private Studiengang mi;
	
	public Stundenplan() {
		ai = new Studiengang("AI");
		ti = new Studiengang("TI");
		mi = new Studiengang("MI");
	}
	
	public Studiengang getStudiengang(E_Studiengang studiengang) {
		switch(studiengang) {
		case BAI:  return ai;
		case BTI:  return ti;
		case MINF: return mi;
		}
		return null;
	}

//dead code: never used
//	public void setMi(Studiengang mi) {
//		this.mi = mi;
//	}
//	public void setTi(Studiengang ti) {
//		this.ti = ti;
//	}
//	public void setAi(Studiengang ai) {
//		this.ai = ai;
//	}
	
	@Override
	public String toString() {
		return "Stundenplan [ti=" + ti + ", ai=" + ai + ", mi=" + mi + "]";
	}	
}
