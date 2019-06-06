package tetris;

import java.awt.Color;
import java.awt.Point;

public class ElementI extends Element {
	private int rotacija;
	
	public ElementI(int pocetak, int velicina) {
		super(new Point(pocetak, -90), velicina);
		
		for (int i = 0; i < 4; i++) {
			Point tacka = rotPozicija(0, i);
			delovi.add(new Kvadrat(new Color(255,190,40), tacka.x, tacka.y, velicina, new Color(201,122,32)));
		}
	}
	
	@Override
	public synchronized void rotiraj() {
		rotacija = rotacija == 0 ? 90 : 0;
		osveziPoziciju();
	}
	
	public synchronized Point rotPozicija(int r, int i) {
		int[][] pozicije = new int[][] {
			new int[] {
					pozicija.x, pozicija.x, pozicija.x, pozicija.x,
					pozicija.y, pozicija.y-velicina, pozicija.y+velicina, pozicija.y+2*velicina
			},
			new int[] {
					pozicija.x-velicina, pozicija.x, pozicija.x+velicina, pozicija.x+2*velicina,
					pozicija.y, pozicija.y, pozicija.y, pozicija.y
			}
		};
		
		return new Point(pozicije[r][i], pozicije[r][i+4]);
	}
	
	@Override	
	public synchronized void osveziPoziciju() {
		for (int i = 0; i < 4; i++) {
			Point tacka = rotPozicija(rotacija/90, i);
			delovi.get(i).podesiPoziciju(tacka.x, tacka.y);
		}
	}

	@Override
	public synchronized void pomeri(Smer smer) {
		if (smer == Smer.DOLE)
			this.pozicija = new Point(pozicija.x, pozicija.y+velicina);
		else if (smer == Smer.LEVO)
			this.pozicija = new Point(pozicija.x-velicina, pozicija.y);
		else
			this.pozicija = new Point(pozicija.x+velicina, pozicija.y);
		
		osveziPoziciju();
	}

}
