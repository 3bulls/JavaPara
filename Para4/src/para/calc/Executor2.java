package para.calc;

import javafx.scene.control.*;
import javafx.application.Platform;

public class Executor2 extends ExecutorBase implements Executor{
  Label label;
  protected Boolean readyForNextStep;
  public Executor2(Label label){
    super();
    this.label = label;
  }

  public void writeState(String state){
    System.err.println(Thread.currentThread().getName());//hint
    // TODO: 1.output every step to the calculator (you should immediately flush the canvsa)
    // this state is the content of stack
    
    System.out.println("state:"+state);
    Platform.runLater(()->{
        synchronized(lock){
          label.setText(state);
          System.err.println("ui+"+Thread.currentThread().getName());//hint
          lock.notify();
        }
      });
  }

  synchronized public String operation(String data){
    init(data);
    result = null;
    boolean isSuccess=true;
    while(isSuccess && s.hasNext()){
      isSuccess = onestep();
    }
    return result;
  }
}
