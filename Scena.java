package tetris;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Scena extends Canvas implements Runnable {
	private Thread nit;
	private Igra igra;
	private Random rg = new Random(System.currentTimeMillis());
	private Kvadrat[][] kvadrati;
	private Element trenutni;	
	private boolean pauzirano = false, zavrseno = false;
	private long tajmer = 500;
	private int velicina = 30, r, k;
	
	
	public Scena(Igra igra) {
		this.igra = igra;

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				synchronized(this) {
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						if (pauzirano) nastavi();
						else pauziraj();
					}
					else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
						if (zavrseno) resetuj();
						else if (!pauzirano) rotiraj();	
					}
					
					if (zavrseno || pauzirano) return;
					
					if (e.getKeyCode() == KeyEvent.VK_LEFT && mozeLevo())
						trenutni.pomeri(Smer.LEVO);
					else if (e.getKeyCode() == KeyEvent.VK_RIGHT && mozeDesno())
						trenutni.pomeri(Smer.DESNO);
					else if (e.getKeyCode() == KeyEvent.VK_DOWN && mozeDole())
						trenutni.pomeri(Smer.DOLE);
					
					proveri();
				}
				repaint();
			}
		});
		
		setSize(510, 600);
		setBackground(Color.DARK_GRAY);
	}
	
	@Override
	public void paint(Graphics g) {
		if (nit == null && !zavrseno) resetuj();
		
		synchronized (this) {
			for (int i = 0; i < r; i++)
				for (int j = 0; j < k; j++)
					if (kvadrati[i][j] != null) kvadrati[i][j].crtaj(g);
			
			trenutni.crtaj(g);	
		}
	}
	
	@Override
	public void run() {
		try {
			while (!nit.isInterrupted()) {
				synchronized(this) {
					while(pauzirano) wait();
					proveri();
					trenutni.pomeri(Smer.DOLE);
				}
				repaint();
				Thread.sleep(tajmer);
			}
		} catch (InterruptedException e) {}
	}
	
	private void proveri() {
		if (!mozeDole()) udarac();
	}
	
	private void udarac() {
		ArrayList<Kvadrat> kv = trenutni.kvadrati();
		
		for (Kvadrat k : kv) {
			Point p = k.dohvatiPoziciju();
			
			int i = p.y / velicina;
			int j = p.x / velicina;
			
			if (i < 0 || j < 0) continue;
			
			try {
				kvadrati[i][j] = k;	
			} catch (Exception e) {}
		}
		
		for (Kvadrat k : kvadrati[0]) {
			if (k != null) {
				igra.kraj();
				nit.interrupt();
				nit = null;
				zavrseno = true;
				break;
			}	
		}

		trenutni = noviElement();
		ocisti();
	}
	
	private void ocisti() {
		for (int i = 0; i < r; i++) {
			int cnt = 0;
			
			for (int j = 0; j < k; j++) if (kvadrati[i][j] != null) ++cnt; 
			
			if (cnt == k) {
				igra.poen();
				for (int p = i; p > 0; p--) {
					for (int q = 0; q < k; q++) {
						kvadrati[p][q] = kvadrati[p-1][q];
						kvadrati[p-1][q] = null;
						if(kvadrati[p][q] != null)
							kvadrati[p][q].podesiPoziciju(q*velicina, p*velicina);
					}
				}
			}
		}
	}
	
	private boolean mozeLevo() {
		ArrayList<Kvadrat> kv = trenutni.kvadrati();
		for (Kvadrat k : kv) {
			Point p = k.dohvatiPoziciju();
			
			if (p.x == 0) return false;
			try {
				if (kvadrati[p.y/velicina][p.x/velicina-1] != null) return false;	
			} catch(IndexOutOfBoundsException e) { return true; } // element jos nije ceo na ekranu
		}
		
		return true;
	}
	
	private boolean mozeDole() {
		for (Kvadrat kv : trenutni.kvadrati()) {
			Point p = kv.dohvatiPoziciju();
			
			if (p.y + velicina >= getHeight()) return false;
			
			for (int i = 0; i < r; i++) {
				for (int j = 0; j < k; j++) {
					if (kvadrati[i][j] != null) {
						Point pp = kvadrati[i][j].dohvatiPoziciju();
						if (pp.x == p.x && pp.y == p.y+velicina) return false;		
					}
				}
			}
		}
		
		return true;
	}
	
	private boolean mozeDesno() {
		ArrayList<Kvadrat> kv = trenutni.kvadrati();
		for (Kvadrat k : kv) {
			Point p = k.dohvatiPoziciju();
			
			if (p.x+velicina == getWidth()) return false;
			try {
				if (kvadrati[p.y/velicina][p.x/velicina+1] != null) return false;	
			} catch(IndexOutOfBoundsException e) { return true; }  // element jos nije ceo na ekranu
		}
		
		return true;
	}
	
	private synchronized void rotiraj() {
		ArrayList<Kvadrat> kv = trenutni.kvadrati();
		trenutni.rotiraj();
		
		for (Kvadrat k : kv) {
			if (k.dohvatiPoziciju().x+velicina > getWidth()) {
				ponistiRotaciju();
				if (mozeLevo()) {
					trenutni.pomeri(Smer.LEVO);
					trenutni.rotiraj();
				}
			}
			
			if (k.dohvatiPoziciju().x < 0) {
				ponistiRotaciju();
				if (mozeDesno()) {
					trenutni.pomeri(Smer.DESNO);
					trenutni.rotiraj();
				}
			}
		}
	}
	
	private synchronized void resetuj() {
		zavrseno = false;
		
		if (nit != null)
			nit.interrupt();
		
		nit = new Thread(this);
		nit.start();
		
		igra.start();
		trenutni = noviElement();
		
		r = getHeight()/velicina;
		k = getWidth()/velicina;
		
		kvadrati = new Kvadrat[r][k];
	}
	
	private void ponistiRotaciju() {
		for (int i = 0; i < 3; i++) trenutni.rotiraj();
	}
	
	public Element noviElement() {
		int rnd = rg.nextInt(7);
		int start = getWidth()/2 - (getWidth()/2)%velicina;
		
		if (rnd == 0) return new ElementK(start, velicina); else
		if (rnd == 1) return new ElementI(start, velicina); else
		if (rnd == 2) return new ElementL(start, velicina); else
		if (rnd == 3) return new ElementC(start, velicina); else
		if (rnd == 4) return new ElementLR(start, velicina); else
		if (rnd == 5) return new ElementCR(start, velicina); else
		
		return new ElementT(start, velicina);
	}
	
	public synchronized void pauziraj() {
		if (zavrseno) return;
		igra.pauza();
		pauzirano = true;
	}
	
	public synchronized void nastavi() {
		igra.start();
		pauzirano = false;
		notify();
	}

	public void zaustavi() {
		if (nit != null)
			nit.interrupt();
	}
}
