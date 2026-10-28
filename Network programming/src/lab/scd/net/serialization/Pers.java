package lab.scd.net.serialization;

import java.io.Serializable;

public class Pers implements Serializable {
	//String nume; // the server serializes the name
	transient String nume; // name = null
	int varsta;

	Pers(String n, int v) {
		nume = n;
		varsta = v;
	}

	public String toString() {
		return "Persoana: " + nume + " varsta: " + varsta;
	}
}
