package para;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.*;

import para.graphic.target.*;
import para.graphic.shape.*;
import para.graphic.parser.MainParser;

/** クライアントからの通信を受けて描画するサーバプログラム。
 * 監視ポートは30000番
 */
public class Main07{
  final public int PORTNO=30000;
  final int MAXCONNECTION=3;
  final Target target;
  final ShapeManager[] sms;
  final ServerSocket ss;

  final ExecutorService pool = Executors.newFixedThreadPool(3);

  /** 受け付け用ソケットを開くこと、受信データの格納場所を用意すること
   * を行う
   */
  public Main07(){
    target = new JavaFXTarget("Server", 320*MAXCONNECTION, 240);
    //target = new TextTarget(System.out);
    ServerSocket tmp=null;
    try{
      tmp = new ServerSocket(PORTNO);
    }catch(IOException ex){
      System.err.println(ex);
      System.exit(1);
    }
    ss = tmp;
    sms = new ShapeManager[MAXCONNECTION];
    for(int i=0;i<MAXCONNECTION;i++){
      sms[i] = new OrderedShapeManager();
    }
  }

  /** 受け付けたデータを表示するウィンドウの初期化とそこに受信データを表示するスレッドの開始
   */
  public void init(){
    target.init();
    target.clear();
    target.flush();
    new Thread(()->{
        while(true){
          target.clear();
          for(ShapeManager sm: sms){
            synchronized(sm){
              target.draw(sm);
            }
          }
          target.flush();
          try{
            Thread.sleep(100);
          }catch(InterruptedException ex){
          }
        }
    }).start();
  }

  /** 受信の処理をする
   */
  public void start(){

    /*  ***important***: you need to use try, and use while in this try area 
            because if you dont use try here.
            when you write Socket s = ss.accept(),
            java will enfore you to try-resouce,
            but when you leave the try area, the main thread will close the socket resource before child thread.
    */
    try (ss) {
      while(true){
        // !!! if you write try(Socket s = ss.accept()){....new thread}
        //      when try area is done, the socket resouce will be released by main thread
        Socket s = ss.accept();
        pool.submit(new HandleTask(sms,s));
      }
    } catch(IOException ex){
      System.err.println(ex);
      System.exit(1);
    }
  }
  
  public static void main(String[] args){
    Main07 m = new Main07();
    m.init();
    m.start();
  } 
}

class HandleTask implements Runnable{
  ShapeManager[] sms;
  Socket s;
  public HandleTask(ShapeManager[] sms,Socket s){
    this.sms = sms;
    this.s = s;
  }

  @Override
  public void run() {
    int i = ThreadId.get();
    System.out.println("thread"+i);
    try(Socket socket = s){
      System.out.println("you have a connect " + socket.getRemoteSocketAddress());
      BufferedReader r =
        new BufferedReader(new InputStreamReader(socket.getInputStream()));
      ShapeManager dummy = new ShapeManager();
      sms[i].clear();
      sms[i].put(new Rectangle(10000*i,320*i,0,320,240,
                                new Attribute(0,0,0,true)));
      MainParser parser
        = new MainParser(new TranslateTarget(sms[i],
                          new TranslationRule(10000*i, new Vec2(320*i,0))),
                          dummy);
      parser.parse(new Scanner(r));
      System.out.println("owari");
    }catch(IOException e){
      System.err.print(e);
    }
  }
}

class ThreadId {
  // Atomic integer containing the next thread ID to be assigned
  private static final AtomicInteger nextId = new AtomicInteger(0);

  // Thread local variable containing each thread's ID
  private static final ThreadLocal<Integer> threadId =
      new ThreadLocal<Integer>() {
          @Override protected Integer initialValue() {
              return nextId.getAndIncrement()%3;
      }
  };

  // Returns the current thread's unique ID, assigning it if necessary
  public static int get() {
      return threadId.get();
  }
}