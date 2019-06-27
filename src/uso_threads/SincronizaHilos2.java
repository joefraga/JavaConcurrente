package uso_threads;

public class SincronizaHilos2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HiloSimple hilo1=new HiloSimple(); 
		HilosSincronizado hilo2=new HilosSincronizado(hilo1); //le estamos diciendo que hasta que no termine el hilo1 empiece con los del 2

		//ya no importa el orden porque ya le dijimos antes cual depende de cual
		hilo2.start();
		hilo1.start();
	}

}

class HiloSimple extends Thread{
	
	public void run() {
		for(int i=0; i<15; i++) {
			System.out.println("Ejecutando hilo " + getName());	
		}
		
	}
	
}

class HilosSincronizado extends Thread{
	private Thread hilo;
	
	HilosSincronizado(Thread t){
		hilo=t;
	}
	
	public void run() {
		
		//primero sincronizo el hilo que me posan de parametro	
		try {
			hilo.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0; i<15; i++) {
			System.out.println("Ejecutando hilo " + getName());	
		}
		
	}
	
}