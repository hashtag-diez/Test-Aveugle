package client.src.test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Timer {
  public volatile int nbQuestions = 10; 
  public volatile boolean ongoing = true;
  public static void main(String[] args) throws InterruptedException {
    Timer timer = new Timer();
    Instant server = Instant.now().plus(10, ChronoUnit.SECONDS);
    System.out.println("Envoi de la requete SCORE_REFRESH");
    TimeUnit.SECONDS.sleep(8);
    System.out.println("Réception de la réponse");
    Instant client = Instant.now();
    Duration d = Duration.between(client, server);
    ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
    Runnable countdown = () -> {
      System.out.println("DEBUT TIMER");   
      long debut=System.currentTimeMillis();
        while(timer.ongoing){
          try {
            TimeUnit.SECONDS.sleep(1);
          } catch (InterruptedException e) {}
          long enCours=System.currentTimeMillis()-debut;
          long secondes=enCours/1000;
          if(secondes == 10){timer.ongoing = false;}
          System.out.println(secondes + "s");
        }
      System.out.println("FIN TIMER");;
      timer.ongoing = true; 
      ses.shutdown();
    };
    ses.schedule(countdown, d.toSeconds() , TimeUnit.SECONDS);
  }
}
