package para.graphic.shape;
import para.graphic.target.Target;

/** 
 * 図形の抽象クラス
 */
abstract public class Shape{
  /** 識別子 */
  final protected int id;
  /** 図形を囲む矩形領域の左端*/
  final protected int left;
  /** 図形を囲む矩形領域の右端*/
  final protected int right;
  /** 図形を囲む矩形領域の上端*/
  final protected int top;
  /** 図形を囲む矩形領域の下端*/
  final protected int bottom;
  /**
   * 図形の識別子と図形を囲む矩形領域の範囲を登録する。
   * @param id 識別子
   * @param l 図形を囲む矩形領域の左端
   * @param r 図形を囲む矩形領域の右端
   * @param t 図形を囲む矩形領域の上端
   * @param b 図形を囲む矩形領域の下端
   */
  protected Shape(int id, int l, int r, int t, int b){
    left = l; 
    right = r;
    top = t;
    bottom =b;
    this.id = id;
  }

  /** 
   * ID番号を取得する 
   * @return ID
   */
  public int getID(){
    return id;
  }
  
  /** 
   *  属性を取得する
   * @return 属性
   */
  abstract public Attribute getAttribute();

  /** 図形を出力する
   *  @param target 出力装置
   */
  abstract public void draw(Target target);
}
