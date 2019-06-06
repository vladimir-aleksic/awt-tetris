package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Kvadrat {
	private Color boja, bojaIvice;
	private int velicina;
	private static final int ivica = 2;
	private int x, y;
	
	public Kvadrat(Color boja, int x, int y, int velicina) {
		this(boja, x, y, velicina, Color.BLACK);
	}
	
	public Kvadrat(Color boja, int x, int y, int velicina, Color bojaIvice) {
		this.boja = boja;
		this.velicina = velicina;
		this.x = x;
		this.y = y;
		this.bojaIvice = bojaIvice;
	}
	
	public void podesiPoziciju(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void crtaj(Graphics g) {
		if (x < 0 || y < 0) return;
		g.setColor(bojaIvice);
		g.fillRect(x,y, velicina, velicina);
		g.setColor(boja);
		g.fillRect(x+ivica, y+ivica, velicina-2*ivica, velicina-2*ivica);
	}
	
	public Point dohvatiPoziciju() {
		return new Point(x, y);
	}
}
