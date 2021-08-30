package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrimeFinderThread extends Thread{
        private boolean despierto;
        private Control control;
        int a,b;
	private List<Integer> primes;
	
	public PrimeFinderThread(int a, int b, Control control) {
            super();
            despierto = true;
            this.primes = new LinkedList<>();
            this.a = a;
            this.b = b;
            this.control = control;
        }

        @Override
	public void run(){
                      
            for (int i= a;i < b;i++){	
                if(!despierto){
                    try {
                        synchronized(control){
                            control.wait();
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PrimeFinderThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                if (isPrime(i)){
                    primes.add(i);
                    System.out.println(i);
                }
            }
            
            

	}
	
	boolean isPrime(int n) {
	    boolean ans;
            if (n > 2) { 
                ans = n%2 != 0;
                for(int i = 3;ans && i*i <= n; i+=2 ) {
                    ans = n % i != 0;
                }
            } else {
                ans = n == 2;
            }
	    return ans;
	}

	public List<Integer> getPrimes() {
		return primes;
	}
        
        /**
         * Metodo encargado de dormir al hilo
         */
        public void dormir(){
            despierto = false;
        }
        
        /**
         * Metodo encargado de despertar al hilo
         */
        public void despertar(){
            despierto = true;
        }
        /**
         * Metodo encargado de retornar la cantidad de primos encontrados en cada hilo
         * @return int: Cantidad de Primor encontrados
         */
        public int getCantidadPrimosEncontrados(){
            return primes.size();
        }
        
        /**
         * Metodo encargado de saber si el hilo se encuentra esperando (WAITING)
         * @return 
         */
        public boolean estaEsPerando(){
            return this.getState() == Thread.State.WAITING; 
        }
                 
}
