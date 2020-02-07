/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithread;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static multithread.denetleyici.alt_thread_kapasite_2;
/**
 *
 * @author anil_
 */
public class Multithread extends Thread {
public int alt_thread_kapasite;
    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
        
    public static void main(String[] args) throws InterruptedException{
        anaSunucu ana = new anaSunucu();
        ana.run();
        Random rand = new Random();
        int veri;
        anaSunucu.kapasite = 0;
        baslatici basla = new baslatici();
        Thread t1 = new Thread(basla);
        t1.start();
        denetleyici denetle = new denetleyici();
        Thread t2 = new Thread(denetle);
        t2.start();
        int loop = 1;
        while(loop == 1){
            int a = (int) ((System.currentTimeMillis()-anaSunucu.start)%200);
            if(a == 0 && anaSunucu.kapasite != 0){
                veri = rand.nextInt(50);
                anaSunucu.kapasite -= veri;
                if(anaSunucu.kapasite < 0) anaSunucu.kapasite = 0;
                Thread.sleep(1);
            }
            int b = (int) ((System.currentTimeMillis()-anaSunucu.start)%500);
            if(b == 0){
                veri =rand.nextInt(500);
                anaSunucu.kapasite += veri;
                if(anaSunucu.kapasite > 10000) anaSunucu.kapasite = 10000;
                //System.out.println("Gecen ms = "+ (System.currentTimeMillis()-anaSunucu.start) + " Ana Sunucu " + anaSunucu.kapasite);
                Thread.sleep(1);
            }
        }
    }
}

class altSunucu implements Runnable{       
        int sunucu;
        int kapasite;
        public static int alt_thread_kapasite_0;
        public static int alt_thread_kapasite_1;
        private Object lock1 = new Object();
        public altSunucu(int sayi){
            this.sunucu = sayi;
        }
        public altSunucu(){}
     
        @Override
        public synchronized void run(){
            synchronized (lock1) {
            Random rand = new Random();
            int veri;
            int a=1;
            while(anaSunucu.flags[sunucu] == true){
                int x = (int) ((System.currentTimeMillis()-anaSunucu.start)%500);
                if(anaSunucu.kapasite >= 0 && x == 0){
                    veri = rand.nextInt(200);
                    if(anaSunucu.kapasite < veri){
                        anaSunucu.altSunucu[sunucu] += anaSunucu.kapasite;
                        anaSunucu.kapasite = 0;
                    }
                    else{
                        anaSunucu.kapasite -= veri;
                        anaSunucu.altSunucu[sunucu] += veri;
                    }
                    if(anaSunucu.altSunucu[sunucu] > 5000) anaSunucu.altSunucu[sunucu] = 5000;
                    if(sunucu==0)  alt_thread_kapasite_0 = anaSunucu.altSunucu[sunucu];
                    if(sunucu==1)  alt_thread_kapasite_1 = anaSunucu.altSunucu[sunucu];
                    //System.out.println("Gecen ms = "+ (System.currentTimeMillis()-anaSunucu.start) + " Alt Sunucu "+ Thread.currentThread().getName()+" : "+ anaSunucu.altSunucu[sunucu]);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(altSunucu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                int y = (int) ((System.currentTimeMillis()-anaSunucu.start)%300);
                if(anaSunucu.altSunucu[sunucu] > 0 && y == 0){
                    veri = rand.nextInt(50);
                    anaSunucu.altSunucu[sunucu] -= veri;
                    if(anaSunucu.altSunucu[sunucu] < 0) {
                        anaSunucu.altSunucu[sunucu] = 0;
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(altSunucu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            }
        }
    }
class baslatici implements Runnable{
    private Object lock2 = new Object();
    @Override
    public synchronized void run(){
        synchronized (lock2) {
        int a=1;
        while(a==1){
            if(anaSunucu.calisan < 2){
                altSunucu alt = new altSunucu(anaSunucu.sunucu);
                anaSunucu.flags[anaSunucu.sunucu] = true;
                anaSunucu.sunucular[anaSunucu.sunucu] = new Thread(alt);
                anaSunucu.sunucular[anaSunucu.sunucu].start();
                anaSunucu.calisan ++;
                anaSunucu.sunucu ++;
            }
            for(int i = 0; i <= anaSunucu.sunucu; i++){
                if(anaSunucu.altSunucu[i] > 3500){
                    anaSunucu.altSunucu[i] /= 2;
                    anaSunucu.flags[anaSunucu.sunucu] = true;
                    altSunucu alt = new altSunucu(anaSunucu.sunucu);
                    anaSunucu.sunucular[anaSunucu.sunucu] = new Thread(alt);
                    anaSunucu.sunucular[anaSunucu.sunucu].start();
                    anaSunucu.altSunucu[anaSunucu.sunucu] = anaSunucu.altSunucu[i];
                    //System.out.println(anaSunucu.sunucu);                   
                    anaSunucu.calisan++;
                    anaSunucu.sunucu++;
                }
            }
            int x = (int) ((System.currentTimeMillis()-anaSunucu.start)%500);
            if(x == 0){
                for(int i = 0; i<anaSunucu.sunucu; i++){
                    if(anaSunucu.calisan > 2 && anaSunucu.altSunucu[i] == 0 && anaSunucu.flags[i] == true){
                        anaSunucu.flags[i] = false;
                        anaSunucu.calisan --;
                        System.out.println("Sunucu "+ i +" kapatıldı.");
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(baslatici.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }             
            }
        }
        }
    }
}
class denetleyici implements Runnable{
    public static int alt_thread_kapasite_2;
    public static int alt_thread_kapasite_3;
    public static int alt_thread_kapasite_4;
    public static int alt_thread_kapasite_5;
    public static int alt_thread_kapasite_6;
    public static int alt_thread_kapasite_7;
    public static int alt_thread_kapasite_8;
    public static int alt_thread_kapasite_9;
    public static int alt_thread_kapasite_10;
    public static int alt_thread_kapasite_11;
    public static int alt_thread_kapasite_12;
    
    private Object lock3 = new Object();
    @Override
    public synchronized void run(){
        synchronized (lock3){
            int a=1;
            while(a == 1){
                for(int i = 0; i <= anaSunucu.sunucu; i++){
                    if(anaSunucu.sunucu==2) alt_thread_kapasite_2=anaSunucu.altSunucu[anaSunucu.sunucu];
                    if(anaSunucu.sunucu==3) alt_thread_kapasite_3=anaSunucu.altSunucu[anaSunucu.sunucu];
                    if(anaSunucu.sunucu==4) alt_thread_kapasite_4=anaSunucu.altSunucu[anaSunucu.sunucu];
                    if(anaSunucu.sunucu==5) alt_thread_kapasite_5=anaSunucu.altSunucu[anaSunucu.sunucu];
                    if(anaSunucu.sunucu==6) alt_thread_kapasite_6=anaSunucu.altSunucu[anaSunucu.sunucu];
                    if(anaSunucu.sunucu==7) alt_thread_kapasite_7=anaSunucu.altSunucu[anaSunucu.sunucu];
                    if(anaSunucu.sunucu==8) alt_thread_kapasite_8=anaSunucu.altSunucu[anaSunucu.sunucu];
                    if(anaSunucu.sunucu==9) alt_thread_kapasite_9=anaSunucu.altSunucu[anaSunucu.sunucu];
                    if(anaSunucu.sunucu==10) alt_thread_kapasite_10=anaSunucu.altSunucu[anaSunucu.sunucu];
                    if(anaSunucu.sunucu==11) alt_thread_kapasite_11=anaSunucu.altSunucu[anaSunucu.sunucu];
                }
            }
        }
        
    }

}