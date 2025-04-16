package para.calc;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.geometry.Pos;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.application.Platform;
import java.util.concurrent.*;;
/**
 * JavaFX 電卓アプリケーションのメインクラス
 */
public class Calculator extends Application{
  /** 入力数式の表示部 */
  final Label input;
  /** 計算結果の表示部 */
  final Label output;
  /** 入力数式の文字列作成用バッファ */
  final StringBuilder buff;
  /** 数式解釈器 */
  final Executor ex;
  /** The thread pool for calculation */
  final ExecutorService singlePool;

  /** 電卓の状態を保持するデータ領域、逆ポーランド記法解釈器の準備といった初期化行う.
   */
  public Calculator(){
    input = new Label();
    output = new Label();
    buff = new StringBuilder();
    // ex = new Executor1();
    ex = new Executor2(output);
    singlePool = Executors.newSingleThreadExecutor();
  }
  /** ボタンのラベル */
  final String[] buttonname = {"9","8","7","+",
                               "6","5","4","-",
                               "3","2","1","*",
                               "0",".",",","/"};

  public void start(Stage stage){
    VBox root = new VBox();
    root.setAlignment(Pos.TOP_CENTER);
    GridPane grid = new GridPane();
    Scene scene = new Scene(root, 200,300);
    Button[] buttons = new Button[16];
    Button buttoncal = new Button("=");
    double tmph = buttoncal.getHeight();
    buttoncal.setPrefHeight(56);
    Button buttondel = new Button("<");
    buttondel.setPrefHeight(56);
    StackPane stack = new StackPane();
    stack.getChildren().add(new Rectangle(140,30,Color.WHITE));
    stack.getChildren().add(input);
    for(int i=0;i<16;i++){
      buttons[i] = new Button(buttonname[i]);
      buttons[i].setPrefHeight(27);
      buttons[i].setPrefWidth(26);
      if(i%4 == 3){
        buttons[i].setOnAction(ev->{
            buff.append(((Button)ev.getSource()).getText()+",");
            input.setText(buff.toString());
          });
      }else{
        buttons[i].setOnAction(ev->{
            buff.append(((Button)ev.getSource()).getText());
            input.setText(buff.toString());
          });
      }
    }
    buttoncal.setOnAction(ev->{
        System.out.println("[["+buff.toString()+"]]");
        // TODO: create a single thread pool
        // everytime when you click the "=" button
        // you have a calculate task submitted to the pool

        // version1: 
        // Thread calcTh = new Thread(new Runnable(){
        //   @Override
        //   public void run(){
        //     String mid;
        //     mid = ex.operation(buff.toString());
        //     buff.delete(0,buff.length());
        //     Platform.runLater(()->{
        //       output.setText(mid);
        //     });
        //   }
        // });
        // calcTh.start();


        // version2: a single thread pool
        singlePool.submit(new Runnable(){
          @Override
          public void run(){
            String mid;
            String inputStr = buff.toString();
            buff.delete(0,buff.length());
            mid = ex.operation(inputStr);
            Platform.runLater(()->{
              output.setText(mid);
            });
          }
        });
      });
    buttondel.setOnAction(ev->{
        if(buff.length()!=0){
          buff.deleteCharAt(buff.length()-1);
        }
        input.setText(buff.toString());
      });
    root.getChildren().addAll(stack, output);
    root.getChildren().add(grid);
    grid.setAlignment(Pos.CENTER);
    for(int i=0;i<16;i++){
      grid.add(buttons[i],i%4,i/4);
    }
    grid.add(buttondel,4,0,1,2);
    grid.add(buttoncal,4,2,1,2);
    stage.setWidth(200);
    stage.setHeight(200);
    stage.setScene(scene);
    stage.setTitle("JavaFX Calc");
    stage.show();
  }
}
