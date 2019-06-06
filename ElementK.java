package tetris;

import java.awt.Color;
import java.awt.Point;

public class ElementK extends Element {	
	public ElementK(int pocetak, int velicina) {
		super(new Point(pocetak, -60), velicina);
		
		Color boja = new Color(64,209,54),
			  ivica = new Color(46,135,40);
		
		delovi.add(new Kvadrat(boja, pozicija.x, pozicija.y, velicina, ivica));
		delovi.add(new Kvadrat(boja, pozicija.x+velicina, pozicija.y, velicina, ivica));
		delovi.add(new Kvadrat(boja, pozicija.x, pozicija.y+velicina, velicina, ivica));
		delovi.add(new Kvadrat(boja, pozicija.x+velicina, pozicija.y+velicina, velicina, ivica));
	}
	
	@Override
	public synchronized void rotiraj() {
		return;
	}

	@Override
	public synchronized void osveziPoziciju() {
		delovi.get(0).podesiPoziciju(pozicija.x, pozicija.y);
		delovi.get(1).podesiPoziciju(pozicija.x+velicina, pozicija.y);
		delovi.get(2).podesiPoziciju(pozicija.x, pozicija.y+velicina);
		delovi.get(3).podesiPoziciju(pozicija.x+velicina, pozicija.y+velicina);
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
