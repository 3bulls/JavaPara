package para.calc;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.geometry.Pos;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

/**
 * JavaFX 電卓アプリケーションのメインクラス
 */
public class Calculator extends Application {
  /** 入力数式表示用ラベル */
  Label input;
  /** 計算結果表示用ラベル */
  Label output;
  /** 数式格納用バッファ */
  StringBuilder buff;
  /** 計算エンジン */
  Executor ex;

  /** デフォルトコンストラクタ */
  public Calculator() {
    input = new Label("xxxxx");
    output = new Label("kkkk");
    buff = new StringBuilder();
    ex = new Executor1();
  }
  /** 格子状に置かれる電卓のボタン名 */
  String[] buttonname = {"9","8","7","+",
                         "6","5","4","-",
                         "3","2","1","*",
                         "0",".",",","/"};

  /** JavaFX 起動 */
  public void start(Stage stage){
    VBox root = new VBox();
    root.setAlignment(Pos.TOP_CENTER);
    GridPane grid = new GridPane();
    Scene scene = new Scene(root, 200,300);
    Button[] buttons = new Button[16];
    Button buttoncal = new Button("=");
    buttoncal.setPrefHeight(56);
    Button buttondel = new Button("<");
    StackPane stack = new StackPane();
    stack.getChildren().add(new Rectangle(140,30,Color.WHITE));
    stack.getChildren().add(input);
    root.getChildren().addAll(stack, output);
    for(int i=0;i<16;i++){
      buttons[i] = new Button(buttonname[i]);
      buttons[i].setPrefSize(28,28);
      buttons[i].setMaxSize(28,28);
      buttons[i].setMinSize(28,28);
      Button btn = buttons[i];
      btn.setOnAction(e->{
        buff.append(btn.getText());
        input.setText(buff.toString());
      });
    }
    GridPane buttonPane = new GridPane();
    buttonPane.setHgap(2);
    buttonPane.setVgap(2);
    buttonPane.setAlignment(Pos.TOP_CENTER);
    for(int i=0;i<16;i++){
      buttonPane.add(buttons[i],i%4,i/4);
    }
    buttonPane.add(buttoncal,4,0,1,2);
    buttonPane.add(buttondel,4,2,1,2);
    buttoncal.setPrefSize(28,56);
    buttondel.setPrefSize(28,56);

    buttondel.setOnAction(e->{
      if (buff.length()!=0) {
       buff.deleteCharAt(buff.length()-1);
       input.setText(buff.toString());
      }
    });
    
    buttoncal.setOnAction(e->{
      String result = ex.operation(buff.toString());
      output.setText(result);
    });

    root.getChildren().add(buttonPane);
    stage.setWidth(200);
    stage.setHeight(200);
    stage.setScene(scene);
    stage.setTitle("JavaFX Calc");
    stage.show();
  }
}
