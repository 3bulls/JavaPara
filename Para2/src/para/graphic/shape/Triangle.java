package para.graphic.shape;
import para.graphic.target.Target;

/**
 * Triangle
 */
public class Triangle extends Shape{
  /** Three Points */
  private int x0;
  private int y0;
  private int x1;
  private int y1;
  private int x2;
  private int y2;

  private Attribute attr;

  /** Triangleを生成する
   *  @param id 識別子
   *  @param x0 x of the first point
   *  @param y0 y of the first point
   *  @param x1 x of the second point
   *  @param y1 y of the second point
   *  @param x2 x of the third point
   *  @param y2 y of the third point
   */
  public Triangle(int id, int x0, int y0, int x1,int y1,int x2,int y2){
    this(id, x0, y0, x1, y1, x2,y2, null);
  }

  /** 円を生成する
   *  @param id 識別子
   *  @param x  中心のx座標
   *  @param y  中心のy座標
   *  @param r  半径
   *  @param attr  属性
   */
  public Triangle(int id, int x0, int y0, int x1,int y1,int x2,int y2, Attribute attr){
    super(id, Math.min(Math.min(x0,x1),x2),
              Math.max(Math.max(x0,x1),x2),
              Math.min(Math.min(y0,y1),y2),
              Math.max(Math.max(y0,y1),y2));
    this.x0 = x0;
    this.x1 = x1;
    this.x2 = x2;
    this.y0 = y0; 
    this.y1 = y1;
    this.y2 = y2;

    this.attr = attr;
  }

  /** 属性を取得する */
  @Override
  public Attribute getAttribute(){
    return attr;
  }

  /** この円を出力する
   *  @param target 出力先
   */
  @Override
  public void draw(Target target){
    target.drawTriangle(id, x0, y0, x1, y1,x2,y2, attr);
  }

  public int getX0(){
    return x0;
  }

  public int getX1(){
    return x1;
  }

  public int getX2(){
    return x2;
  }

  public int getY0(){
    return y0;
  }

  public int getY1(){
    return y1;
  }
  
  public int getY2(){
    return y2;
  }
}
