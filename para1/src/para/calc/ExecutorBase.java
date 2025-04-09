package para.calc;

import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * 逆ポーランド記法の数式文字列に対して数値計算をするための基本処理が定義されているクラス
 */
abstract public class ExecutorBase {
  /** 文字列を分解してトークン分解する*/
  protected Scanner s;
  /** 計算中に利用するスタック*/
  private Stack<Float> stack;
  /** 計算結果保存用 */
  protected String result;
  private String state;

  /** デフォルトコンストラクタ */
  protected ExecutorBase(){
  }

  /**
   * 処理対象文字列を設定し、処理のための初期化を行う
   * @param data 処理対象文字列
   */
  protected void init(String data){
    s = new Scanner(data);
    s.useDelimiter(",");
    stack = new Stack<Float>();
    state = "";
  }

  /**
   * 処理の1ステップ分を行う
   * @return 1ステップが処理できれば true 処理対象の文字列の文法誤りで処理できなればfalse
   */
  protected boolean onestep(){
    if(s.hasNext("\\-?+\\d++\\.?+\\d*")){
      float ret = Float.parseFloat(s.next("\\-?+\\d++\\.?+\\d*"));
      stack.push(ret);
      result = Float.toString(ret);
      showProgress();
      return true;
    }else if(s.hasNext("[\\+\\-\\*/]")){
      String op = s.next("[\\+\\-\\*/]");
      showProgress(op);
      if(stack.empty()){
        return false;
      }
      float b = stack.pop();
      if(stack.empty()){
        return false;
      }
      float a = stack.pop();
      float res=0;
      switch(op){
      case "+":
        res = a+b;
        break;
      case "-":
        res = a-b;
        break;
      case "*":
        res = a*b;
        break;
      case "/":
        res = a/b;
        break;
      }
      stack.push(res);
      result = Float.toString(res);
      showProgress();
      return true;
    }else{
      s.next();
      return false;
    }
  }

  /** 計算過程の状態を表示
   * @param op 数式のトークン
   */
  public void showProgress(String op){
    Stream<Float> st = stack.stream();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(bos); 
    ps.print("(");
    st.forEach(f->{ps.print(f+"|");});
    ps.print(op+")");
    state = bos.toString();
    writeState(state);
  }

  /**計算過程の状態を表示  */
  public void showProgress(){
    showProgress("");
  }

  /**
   * 状態を出力する
   * @param state 状態
   */
  abstract void writeState(String state);

  /**
   * 処理対象文字列を設定し、処理のための初期化を行う
   * @param data 処理対象文字列
   * @return 計算結果　文字列の文法が不完全のときは 不定
   */
  abstract String operation(String data);
}
