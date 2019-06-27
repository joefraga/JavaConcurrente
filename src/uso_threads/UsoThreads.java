package uso_threads;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.*;

public class UsoThreads {

	public static void main(String[] args) {
		
		VentanaRebote miventana=new VentanaRebote();
		miventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		miventana.setVisible(true);
		
	}

}

class VentanaRebote extends JFrame{
	
	private LaminaPelota lamina;
	private Thread t1,t2,t3;
	private JButton arranca1, arranca2, arranca3, detener1, detener2, detener3;
	
	public VentanaRebote() {
		setBounds(600,300,700,350);
		setTitle("Rebotes");
		
		lamina=new LaminaPelota();
		add(lamina, BorderLayout.CENTER);
		
		JPanel lamina_botones=new JPanel();
		
		arranca1=new JButton("Hilo1");
		arranca1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comienza_juego(e);
			}	
		});
		
		arranca2=new JButton("Hilo2");
		arranca2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comienza_juego(e);
			}	
		});
		arranca3=new JButton("Hilo3");
		arranca3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comienza_juego(e);
			}	
		});
		
		detener1=new JButton("Detener1");
		detener1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				detener(e);
			}	
		});
		
		detener2=new JButton("Detener2");
		detener2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				detener(e);
			}	
		});
		
		detener3=new JButton("Detener3");
		detener3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				detener(e);
			}	
		});
		
		lamina_botones.add(arranca1);
		lamina_botones.add(arranca2);
		lamina_botones.add(arranca3);
		
		lamina_botones.add(detener1);
		lamina_botones.add(detener2);
		lamina_botones.add(detener3);
		
		
		//dibija el boton de action de la pelota
		/*poner_boton(lamina_botones, "Ok", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comienza_juego();
			}		
		});*/	
		
		//Dibuja el boton de salida
		poner_boton(lamina_botones, "Salir", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);	
			}	
		});
		
		//dibuja el boton de bloqueo
		/*poner_boton(lamina_botones, "Bloquea", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				detener();
			}	
		});*/
		
		
		
		add(lamina_botones, BorderLayout.SOUTH);
	}
	
	public void poner_boton(Container c, String titulo, ActionListener oyente) {
		
		JButton boton=new JButton(titulo);
		c.add(boton);
		boton.addActionListener(oyente);
		
	}
	
	public void comienza_juego(ActionEvent e) {
		Pelota pelota=new Pelota();		
		lamina.add(pelota);		
		Runnable r=new Pelota_Hilos(pelota, lamina);
		if(e.getSource().equals(arranca1)){
			t1=new Thread(r);
			t1.start();	
		}else if(e.getSource().equals(arranca2)) {
			t2=new Thread(r);
			t2.start();
		}else if(e.getSource().equals(arranca3)) {
			t3=new Thread(r);
			t3.start();
		}
	}
	
	public void detener(ActionEvent e){
		//t.stop();
		
		//vamos a interrumpir el hilo en lugar de pararlo
		
		if(e.getSource().equals(detener1)){
			t1.interrupt();			
		}else if(e.getSource().equals(detener2)) {
			t2.interrupt();
		}else if(e.getSource().equals(detener3)) {
			t3.interrupt();
		}
		
	}
}

class Pelota{
	
	private static final int TAMX=15;
	private static final int TAMY=15;
	private double x=0;
	private double y=0;
	private double dx=1;
	private double dy=1;
	
	public void mueve_pelota(Rectangle2D limites) {
		
		x+=dx;
		y+=dy;
		
		if(x<limites.getMinX()) {
			x=limites.getMinX();
			dx=-dx;
		}
		
		if(x + TAMX >= limites.getMaxX()) {
			x=limites.getMaxX() - TAMX;
			dx=-dx;
		}
		
		if(y<limites.getMinY()) {
			y=limites.getMinY();
			dy=-dy;
		}
		
		if(y+TAMY>=limites.getMaxY()) {
			y=limites.getMaxY()-TAMY;
			dy=-dy;
		}
	}
	
	public Ellipse2D getShape() {
		return new Ellipse2D.Double(x, y, TAMX, TAMY); 
	}
}

class LaminaPelota extends JPanel{
	
	private ArrayList<Pelota> pelotas=new ArrayList<Pelota>();
	
	public void add(Pelota b) {
		pelotas.add(b);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D) g;
		for(Pelota b:pelotas) {
			g2.fill(b.getShape());
		}
		
	}
}

class Pelota_Hilos implements Runnable{
	private Pelota pelota;
	private Component componente;
	
	public Pelota_Hilos(Pelota p, Component c ) {
		pelota=p;
		componente=c;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(int i=0; i<=3000; i++) {
			pelota.mueve_pelota(componente.getBounds());
			componente.paint(componente.getGraphics());
			
			try {			///aqui alentamos el thread del hilo
				Thread.sleep(4);
			} catch (InterruptedException e) {
				
				//e.printStackTrace();
				Thread.currentThread().interrupt();
			}
			
		}
	}
	
}