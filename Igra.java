package tetris;

import java.awt.*;
import java.awt.event.*;

public class Igra extends Frame {
	private int poeni;
	private Label lp = new Label("0");
	
	public Igra() {
		Scena scena = new Scena(this);
		Panel p = new Panel();
		
		lp.setAlignment(Label.CENTER);
		lp.setFont(new Font(null, Font.BOLD, 25));
		lp.setBackground(Color.WHITE);
		
		p.setBackground(Color.DARK_GRAY);
		p.add(scena);
		add(p);
		add(lp, BorderLayout.NORTH);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				scena.zaustavi();
				dispose();
			}
		});
		
		setSize(510, 680);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		new Igra();
	}

	public void poen() {
		poeni += 10;
		lp.setText("Poeni: "+poeni);
	}
	
	public void kraj() {
		poeni = 0;
		lp.setBackground(Color.RED);
	}
	
	public void start() {
		lp.setBackground(Color.WHITE);
		lp.setText("Poeni: "+poeni);
	}
	
	public void pauza() {
		lp.setText("Pauza");
		lp.setBackground(Color.YELLOW);
	} 
}
