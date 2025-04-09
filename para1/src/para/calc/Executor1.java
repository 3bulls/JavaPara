package para.calc;

/**
 * 逆ポーランド記法によリ書かれた四則演算の文字列に対して数値計算をするクラス
 */
public class Executor1 extends ExecutorBase implements Executor{
  /** デフォルトコンストラクタ */
  public Executor1(){
  }

  @Override
  public void writeState(String state){
    System.out.print(state);
  }

  @Override
  public String operation(String data){
    init(data);
    result = null;
    boolean isSuccess=true;
    while(isSuccess && s.hasNext()){
      System.out.println();
      isSuccess = onestep();
      System.out.println(isSuccess);
    }
    return result;
  }
}
