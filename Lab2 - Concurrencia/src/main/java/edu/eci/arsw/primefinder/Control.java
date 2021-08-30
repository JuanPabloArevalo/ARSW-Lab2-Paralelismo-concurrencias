/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;
    private Control control;
    private PrimeFinderThread pft[];
    
    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];
        control = this;
        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA,this);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1,this);
    }
    
    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();
        }
           
        while(true){
            try {
                System.out.println("Entro");
                sleep(TMILISECONDS);
//Detener todos los hilos
                for(int i = 0;i < NTHREADS;i++ ) {
                    pft[i].dormir();
                }
                              
                System.out.println("Hasta el momento se han encontrado: "+traerCantidadPrimosEncontrados()+" numeros primos. Presione una tecla para continuar.");
                Scanner entradaEscaner = new Scanner (System.in);
                entradaEscaner.nextLine ();              
//Despertar
                for(int i = 0;i < NTHREADS;i++ ) {
                    pft[i].despertar();
                }
                synchronized(control){
                    control.notifyAll();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    /**
     * Metodo encargado de traer la cantidad de primos encontrados, asegurando de tener todos los hilos
     * en estado Waiting
     * @return int : Cantidad de Primos encontrados
     * 
     */
    public int traerCantidadPrimosEncontrados(){
        int total = 0;
        int i=0;
        while(i<NTHREADS){
            if(pft[i].estaEsPerando()){
                total = total+pft[i].getCantidadPrimosEncontrados();
                i++;
            }
        }
        return total;
    }
    
}
