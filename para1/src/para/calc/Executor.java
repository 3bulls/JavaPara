package para.calc;

import javafx.scene.control.*;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * 電卓内の数値計算実装部のインタフェース
 */
public interface Executor{
  /**
   * 文字列を受け取り、処理を行う
   * @param data 逆ポーランド記法で記述された数式の文字列
   * @return 計算結果　文字列の文法が不完全のときは 不定
   */
  public String operation(String data);
}
