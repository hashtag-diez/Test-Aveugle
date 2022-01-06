package client.src.test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Timer {
  public volatile int nbQuestions = 10; 
  public volatile boolean ongoing = true;
  public static void main(String[] args) throws InterruptedException {
    Timer timer = new Timer();
    ExecutorService pool = Executors.newCachedThreadPool();
    pool.submit(() -> {
      System.out.println("TIMER");
      while(timer.nbQuestions>0){
        long debut=System.currentTimeMillis();
        while(timer.ongoing){
          try {
            TimeUnit.SECONDS.sleep(1);
          } catch (InterruptedException e) {}
          long enCours=System.currentTimeMillis()-debut;
          long secondes=enCours/1000;
          System.out.println(secondes + "s sont passées");
        }
        timer.nbQuestions --;
        System.out.println("TIMER RESET");;
        timer.ongoing = true;
      }
    });
    int i = 0;
    while(i<=10){
      TimeUnit.SECONDS.sleep(new Random().nextInt(10) + 1);
      timer.ongoing = false;
    }
  }
}
