package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public abstract class Element {
	protected ArrayList<Kvadrat> delovi = new ArrayList<>();
	protected Point pozicija;
	protected int velicina;
	
	public Element(Point pozicija, int velicina) {
		this.pozicija = pozicija;
		this.velicina = velicina;
	}
	
	public abstract void rotiraj();
	public abstract void pomeri(Smer smer);
	public abstract void osveziPoziciju();
	
	public ArrayList<Kvadrat> kvadrati() {
		return delovi;
	};
	
	public void crtaj(Graphics g) {
		for (Kvadrat k : delovi) k.crtaj(g);
	}
}
