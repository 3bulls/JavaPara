package para;

import java.util.Scanner;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import para.graphic.target.*;
import para.graphic.shape.*;
import para.graphic.parser.*;
import para.game.*;
import javafx.application.Platform;

/** モグラたたきゲームの雛形
 */
public class Game01 extends GameFrame{
  final static int WIDTH=400;
  final static int HEIGHT=700;
  final int MCOUNT=9;
  final int XCOUNT=3;
  Target inputside;
  Target outputside;
  Thread thread;
  ShapeManager osm;
  ShapeManager ism;
  private long prev; 
  Random rand;
  private int last;
  private  int[] slot;
  Attribute mogattr;
  private int score;
  private int count;
  
  public Game01(){
    super(new JavaFXCanvasTarget(WIDTH, HEIGHT));
    canvas.init();
    title = "Mole";
    outputside = canvas;
    inputside = canvas;
    osm = new ShapeManager();
    ism = new ShapeManager();
    rand = new Random(System.currentTimeMillis());
    slot = new int[MCOUNT];
    mogattr = new Attribute(158,118,38);
    score = 0;
  }

  public void gamestart(int v){
    if(thread != null){
      return;
    }
    this.countDown();
    thread = new Thread(()->{
        Attribute attr = new Attribute(250,80,80);
        while(true){
          try{
            Thread.sleep(80);
          }catch(InterruptedException ex){
          }
          SynchronizedPoint p = xy.copy();
          if(prev != p.getTime()){
            // when user clicks the screen
            prev = p.getTime();
            ism.put(new Circle(v, (int)p.getXY()[0], (int)p.getXY()[1],20, attr));
            Shape s = InsideChecker.check(osm,
                                          new Vec2(p.getXY()[0], p.getXY()[1]));
            if(s != null){
              slot[(s.getID()-10)/10]=0;
              System.out.println(p.getXY()[0]+" "+p.getXY()[1]+" "+p.getTime());
              score++;
              this.showScore();
            }
          }else if(300 < System.currentTimeMillis()-prev){
            ism.remove(v);
          }
          inputside.clear();
          mole();
          inputside.draw(ism);
          inputside.flush();
        }
      });
    thread.start();
  }

  private void mole(){
    int appear = rand.nextInt(40);
    if(30<appear){
      int head = rand.nextInt(MCOUNT-1)+1;
      int duration = rand.nextInt(600)+200;

      if(slot[(last+head)%MCOUNT] <=0){
        slot[(last+head)%MCOUNT] = duration;
      }
    }
    // slot[i] store the duration of circle(10+i*10), and reduce radius of 10 per loop(80ms). but actually slot[i]/25 is the radius
    for(int i=0;i<MCOUNT;i++){
      slot[i]=slot[i]-10;
    }
    for(int i=0;i<MCOUNT;i++){
      if(0<slot[i]){
        // XCOUNT means that at most there XCOUNT in a line
        // in this case, it means 3*3 grid
        // osm.put(new Circle(10+i*10, (i%XCOUNT)*130+60, (i/XCOUNT)*130+60, slot[i]/25,mogattr));
        // i have to say, why use this xxxx/25 to represent the scale, suck code
        Garden.setMole(10+10*i,(i%XCOUNT)*130+60, (i/XCOUNT)*130+60,slot[i]/25,osm); 
      }else{
        // osm.remove(10+i*10);
        Garden.removeMole(10+i*10,osm);
      }
    }
    inputside.draw(osm);
  }

  private void showScore(){
    System.out.println("score:"+score);
    osm.put(new Digit(101,160,550,30, score%10, new Attribute(200,200,200)));
    osm.put(new Digit(102, 90,550,30, score/10, new Attribute(200,200,200)));
  }

  private void countDown(){
    count = 31;
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run(){
        count--;
        System.out.println(count);
        if (count < 0)  {
          timer.cancel();
        } else {
          Platform.runLater(()->{
            osm.put(new Digit(103,320,550,30, count%10, new Attribute(0,0,0)));
            osm.put(new Digit(104,250,550,30, count/10, new Attribute(0,0,0)));
          });
        }
      }
    }, 0, 1000);
  }
}
