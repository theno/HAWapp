package parser;


import java.util.ArrayList;
import java.util.List;

public class Studiengang {

	private String name;
	private List<Semester> semester;
	
	public Studiengang(String name) {
		
		this.name = name;		
		semester = new ArrayList<Semester>();
		
		for (int i = 0; i < 6; i++) {
			semester.add(i, new Semester(name+(i+1)));
		}		
	}
	
	public String getName() {
		return name;
	}
	/**
	 * 
	 * @param s semester nummer 1 bis 6, NICHT 0!!!
	 * @return Semester Objekt
	 */
	public Semester getSemester(int s) {
		return semester.get(s-1);
	}
	public List<Semester> getAlleSemester() {
		return semester;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setSemester(List<Semester> semester) {
		this.semester = semester;
	}
	public void setAlleSemester(List<Semester> semester) {
		this.semester = semester;
	}
	
	@Override
	public String toString() {
		return "Studiengang [name=" + name + ", semester=" + semester + "]";
	}
	
	
}
