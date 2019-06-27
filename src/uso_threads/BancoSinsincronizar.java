package uso_threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BancoSinsincronizar {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Banco banco=new Banco();
		for(int i=0; i<100; i++) {
			EjecucionTransferencias r=new EjecucionTransferencias(banco, i, 2000);
			Thread t=new Thread(r);  //creo el hilo
			t.start();
		}
	}

}

class Banco {
	private final double[] cuentas;
	private Lock cierreBanco=new ReentrantLock();  //hago uso de la interface Lock
	private Condition saldoSuficiente;
	
	public Banco() {
		
		cuentas=new double[100];
		
		for(int i=0;i<cuentas.length; i++) {
			cuentas[i]=2000;			
		}
		
		saldoSuficiente=cierreBanco.newCondition();  //aqui defeino la condicion del bloqueo
	}
	
	public void transferencia(int cuentaOrigen, int cuentaDestino, double cantidad) throws InterruptedException {
		
		//cierreBanco.lock();  //inicio el bloqueo abriendo un try
		
		//try {
			while(cuentas[cuentaOrigen]<cantidad) {  //checar que no se quiera transferir mas del saldo de la cuenta
				//System.out.println("CANTIDAD INSUFICIENTE DE LA CUENTA " + cuentaOrigen + " Tiene saldo: " + cuentas[cuentaOrigen]);
				//return;
				//saldoSuficiente.await(); //lo ponemos a la espera
				wait();
			}
		
			System.out.println(Thread.currentThread());
		
			cuentas[cuentaOrigen]-=cantidad;
			cuentas[cuentaDestino]+=cantidad;
			
			System.out.printf("%10.2f de %d para %d%n", cantidad, cuentaOrigen,cuentaDestino);
			
			System.out.printf("Saldo Total: %10.2f%n", getSaldoTotal());
			
			//saldoSuficiente.signalAll();  //avisa a todos los hilos que este se desbloquea
			
			notifyAll();
			
		/*}finally {
			//cierreBanco.unlock();  //desbloqueo el peozado de codigo
		}*/
		
	}
	
	public double getSaldoTotal() {
		
		double saldo=0;
		
		for(double a:cuentas ) {
			
			saldo+=a;					
		}
		return saldo;
	}
}

class EjecucionTransferencias implements Runnable{
	
	private Banco banco;
	private int deCuenta;
	private double cantidadMax;
	
	public void run() {
		try {
			while(true) { //bucle infinito
				int paraLaCuenta=(int)(100 * Math.random());  //hacemos random la cuenta que va a recibir la transferencia
				double cantidad=cantidadMax * Math.random();  //hacemos random la cantidad a transferir
				banco.transferencia(deCuenta, paraLaCuenta, cantidad);  //hago la transferencia
				Thread.sleep((int) Math.random()*10);
			}	
		}catch(InterruptedException e) {
			
		}		
	}
	
	public EjecucionTransferencias(Banco b, int de, double max) { //constructor
		banco=b;
		deCuenta=de;
		cantidadMax=max;
	}
}
