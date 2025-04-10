package para.calc;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.geometry.Pos;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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
    input = new Label();
    output = new Label();
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
    Scene scene = new Scene(root, 200,200);
    Button[] buttons = new Button[16];
    Button buttoncal = new Button("=");
    double tmph = buttoncal.getHeight();
    buttoncal.setPrefHeight(56);
    Button buttondel = new Button("<");
    buttondel.setPrefHeight(56);
    StackPane stack = new StackPane();
    stack.getChildren().add(new Rectangle(140,30,Color.WHITE));
    stack.getChildren().add(input);
    root.getChildren().addAll(stack, output);
    root.getChildren().add(grid);
    grid.setAlignment(Pos.CENTER);
    for(int i=0;i<16;i++){
      buttons[i] = new Button(buttonname[i]);
      buttons[i].setPrefHeight(26);
      buttons[i].setPrefWidth(26);
      grid.add(buttons[i],i%4,i/4);
      Button btn = buttons[i];
      btn.setOnAction(e->{
        buff.append(btn.getText());
        input.setText(buff.toString());
      });
    }
    grid.add(buttondel,4,0,1,2);
    grid.add(buttoncal,4,2,1,2);

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
    
    scene.setOnKeyTyped(e->{
      String pressedChar = e.getCharacter();
      String retChar = "";
      if ("0123456789".contains(pressedChar)){
        retChar = pressedChar;
      } else {
        String operator = "+-*/,.";
        if(operator.contains(pressedChar)) {
          retChar = pressedChar;
        }
        if(pressedChar.equals("<")) {
          // System.out.println("in <");
          if (buff.length()!=0) {
            buff.deleteCharAt(buff.length()-1);
            input.setText(buff.toString());
           }
        }
        if(pressedChar.equals("=")) {
          String result = ex.operation(buff.toString());
          output.setText(result);
        }
      }
      buff.append(retChar);
      input.setText(buff.toString());
    });
    stage.setScene(scene);
    stage.setTitle("JavaFX Calc");
    stage.show();
  }
}
