package para.paint;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.canvas.*;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.beans.value.ObservableValue;
import javafx.css.Size;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser.*;
import javafx.util.Builder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import java.io.IOException;

import javax.swing.ButtonGroup;


/**
 * JavaFX お絵描きアプリケーションのメインクラス
 */
public class Paint extends Application {
  /** 描画領域 */
  Canvas canvas;
  Pane canvasPane;
  List<Canvas> layers;
  /** 描画領域のGraphicsContext */
  GraphicsContext gc;
  /** 直前のポインタのx座標 */
  double oldx;
  /** 直前のポインタのy座標 */
  double oldy;
  /** 描画領域の大きさ */
  final int SIZE=600;
  final int MaxSizeOfPic=300;
  /** 全消去ボタン */
  Button clear;
  /** upload image */
  Button upload;
  /** rotate the whole canvas */
  Button rotate;
  /** undo last stroke*/
  Button undo;
  /** save button*/
  Button save;
  /** record all the strokes*/
  List<OneStroke> strokes;
  Button addLayer;
  Button removeLayer;

  /** RED GREEN BLUE 0-255*/
  int RED = 0;
  int BLUE = 255;
  int GREEN = 0;
  double OPACITY = 1.0;
  double Width = 4;

  /**
   * お絵描きプログラムの準備をして、ウィンドウを開きます
   */
  public void start(Stage stage){
    Group group = new Group();
    canvas = new Canvas(SIZE,SIZE);
    layers = new ArrayList<Canvas>();
    layers.add(canvas);
    canvasPane = new Pane();
    canvasPane.setPrefSize(SIZE,SIZE);
    canvasPane.getChildren().add(canvas);

    strokes = new ArrayList<OneStroke>();
    gc = canvas.getGraphicsContext2D();
    drawShapes(gc);
    canvas.setOnMousePressed(ev->{
        oldx = ev.getX();
        oldy = ev.getY();
        OneStroke oneStroke = new OneStroke();
        strokes.add(oneStroke);
      });

    // TODO: for debug
    // canvas.setOnMouseReleased(ev->{
    //     System.out.println(strokes.size());
    //     OneStroke lastStroke = strokes.getLast();
    //     for (LineData linedata : lastStroke.lineDataList) {
    //       System.out.println("" + linedata.getXold() + " " +linedata.getYold());
    //       System.out.println("" + linedata.getXnew() + " " +linedata.getYnew());
    //     }
    // });

    canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                           new EventHandler<MouseEvent>(){
                             public void handle(MouseEvent ev){
                               gc.strokeLine(oldx, oldy, ev.getX(), ev.getY());
                               LineData oneline = new LineData(oldx,oldy,ev.getX(),ev.getY());
                               OneStroke currentStroke = strokes.getLast();
                               currentStroke.add(oneline);
                               oldx = ev.getX();
                               oldy = ev.getY();
                               // TODO: olny for debug
                              //  System.out.println(""+oldx+" "+oldy);
                             }
                           });
    HBox hb = new HBox();
    save = new Button("save");
    save.setOnAction(e->{
      FileChooser filechooser = new FileChooser();
      filechooser.setTitle("Save The Pic as .png");
      filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG File","*.png"));
      File file = filechooser.showSaveDialog(stage);

      if (file!=null) {
        WritableImage image = new WritableImage(SIZE,SIZE);
        canvas.snapshot(null,image);
        try{
          ImageIO.write(SwingFXUtils.fromFXImage(image,null),"png",file);
        } catch(IOException ex) {
          ex.printStackTrace();
        }
      }
    });
    undo = new Button("undo");
    undo.setOnAction(e->{
      strokes.removeLast();
      gc.clearRect(0,0,SIZE,SIZE);
      for (OneStroke oneStroke : strokes) {
        for (LineData oneline : oneStroke.lineDataList){
          gc.strokeLine(oneline.getXold(), oneline.getYold(), oneline.getXnew(), oneline.getYnew());
        }
      }
    });
    upload = new Button("upload");
    upload.setOnAction(e->{
      FileChooser filechooser = new FileChooser();
      filechooser.setTitle("Open Pic File");
      filechooser.getExtensionFilters().addAll(
        new ExtensionFilter("Image File","*.png","*.jpg","*.jpeg")
      );
      File selectedFile = filechooser.showOpenDialog(stage);
      if (selectedFile!=null){
        System.out.print(selectedFile.getAbsolutePath());
        Image pic = new Image(selectedFile.toURI().toString(),MaxSizeOfPic,MaxSizeOfPic,true,true);
        
        double x = (canvas.getWidth()-pic.getWidth())/2;
        double y = (canvas.getHeight()-pic.getHeight())/2;
        gc.drawImage(pic,x,y);
      }
    });
    rotate = new Button("rotate");
    rotate.setOnAction(e->{
          canvas.setRotate(canvas.getRotate()+90);
    });
    clear = new Button("clear");
    clear.setOnAction(e->{
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,SIZE,SIZE);});
    Rectangle colorSquare = new Rectangle(20,20,new Color(0, 0, 1, 1));
    // TODO: too many redundant code, need to abstract 
    addLayer = new Button("add");
    addLayer.setOnAction(e->{
      layers.add(canvas);
      canvas = new Canvas(SIZE,SIZE);
      canvasPane.getChildren().add(canvas);
      gc = canvas.getGraphicsContext2D();
      gc.setFill(new Color(1,1,1,0));
      gc.fillRect(0,0,SIZE,SIZE);
      gc.setStroke(Color.rgb(RED,GREEN,BLUE,OPACITY));
      gc.setLineWidth(Width);

      strokes = new ArrayList<OneStroke>();
      canvas.setOnMousePressed(ev->{
          oldx = ev.getX();
          oldy = ev.getY();
          OneStroke oneStroke = new OneStroke();
          strokes.add(oneStroke);
        });
      canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                          new EventHandler<MouseEvent>(){
                            public void handle(MouseEvent ev){
                              gc.strokeLine(oldx, oldy, ev.getX(), ev.getY());
                              LineData oneline = new LineData(oldx,oldy,ev.getX(),ev.getY());
                              OneStroke currentStroke = strokes.getLast();
                              currentStroke.add(oneline);
                              oldx = ev.getX();
                              oldy = ev.getY();
                            }
                          });
    });
    removeLayer = new Button("remove");
    removeLayer.setOnAction(ev->{
      if (layers.size()==1){
        return;
      }
      canvasPane.getChildren().remove(canvasPane.getChildren().size()-1);
      layers.remove(layers.size()-1);
      canvas = layers.get(layers.size()-1);
      gc = canvas.getGraphicsContext2D();
    });
    
    hb.getChildren().addAll(save,undo,rotate,upload,clear,colorSquare,addLayer,removeLayer);
    hb.setAlignment(Pos.CENTER);
    

    BorderPane bp = new BorderPane();
    VBox vb = new VBox();
    Slider sliderr = new Slider(0, 1, 0);
    Slider sliderg = new Slider(0, 1, 0);
    Slider sliderb = new Slider(0, 1, 1);
    Slider slidert = new Slider(0, 1, 1);
    Slider sliderwidth = new Slider(0, 20, 1);
    
    sliderr.valueProperty().addListener((ObservableValue<? extends Number> ov,
                                         Number oldv, Number nv)->{
                                          RED = (int)(255.0 * nv.floatValue());
                                          gc.setStroke(Color.rgb(RED,GREEN,BLUE,OPACITY));
                                          colorSquare.setFill(Color.rgb(RED,GREEN,BLUE,OPACITY));
      });
    sliderg.valueProperty().addListener((ObservableValue<? extends Number> ov,
                                         Number oldv, Number nv)->{
                                          GREEN = (int)(255.0 * nv.floatValue());
                                          gc.setStroke(Color.rgb(RED,GREEN,BLUE,OPACITY));
                                          colorSquare.setFill(Color.rgb(RED,GREEN,BLUE,OPACITY));
      });
    sliderb.valueProperty().addListener((ObservableValue<? extends Number> ov,
                                         Number oldv, Number nv)->{
                                          BLUE = (int)(255.0 * nv.floatValue());
                                          gc.setStroke(Color.rgb(RED,GREEN,BLUE,OPACITY));
                                          colorSquare.setFill(Color.rgb(RED,GREEN,BLUE,OPACITY));
      });

    slidert.valueProperty().addListener((ObservableValue<? extends Number> ov,
                                        Number oldv, Number nv)->{
                                        OPACITY  = nv.floatValue();
                                        gc.setStroke(Color.rgb(RED,GREEN,BLUE,OPACITY));
                                        colorSquare.setFill(Color.rgb(RED,GREEN,BLUE,OPACITY));
    });
    sliderwidth.valueProperty().addListener((ObservableValue<? extends Number> ov,
                                        Number oldv, Number nv)->{
                                        Width= nv.floatValue();
                                        gc.setLineWidth(Width);
    });


    vb.setAlignment(Pos.CENTER);
    vb.getChildren().add(sliderr);
    vb.getChildren().add(sliderg);
    vb.getChildren().add(sliderb);
    vb.getChildren().add(slidert);
    vb.getChildren().add(sliderwidth);
    vb.getChildren().add(hb);
    bp.setTop(vb);
    bp.setCenter(canvasPane);
    Scene scene = new Scene(bp);
    stage.setScene(scene);
    stage.setTitle("JavaFX Draw");
    stage.show();
  }

  /**
   * 初期化メソッド、startメソッドの呼び出され方とは異なる呼び出され方をする。必要ならば定義する
   */
  public void init(){
  }

  /**
   * 図形を描きます。
   * 図形描画の実装サンプルです
   */
  private void drawShapes(GraphicsContext gc) {
    gc.setFill(Color.WHITE);
    gc.fillRect(0,0,SIZE,SIZE);
    gc.setFill(Color.GREEN);
    gc.setStroke(Color.BLUE);
    gc.setLineWidth(Width);
    gc.strokeLine(40, 10, 10, 40);
    gc.fillOval(60, 10, 30, 30);
    gc.strokeOval(110, 10, 30, 30);
    gc.fillRoundRect(160, 10, 30, 30, 10, 10);
  }

}

/**
 * ont line : (xold,yold) - (xnew,ynew)
 */
class LineData{
  private double xold,yold,xnew,ynew;
  LineData(double xold,double yold,double xnew,double ynew){
    this.xold = xold;
    this.xnew = xnew;
    this.yold = yold;
    this.ynew = ynew;
  }
  public double getXold(){
    return this.xold;
  }
  public double getYold(){
    return this.yold;
  }
  public double getXnew(){
    return this.xnew;
  }
  public double getYnew(){
    return this.ynew;
  }
}
/**
 * all lines in one stroke
 */
class OneStroke{
  public List<LineData> lineDataList;
  OneStroke(){
    lineDataList = new ArrayList<>();
  }
  public void add(LineData lineData){
    this.lineDataList.add(lineData);
  }
  public void removeLast(){
    if (this.lineDataList.isEmpty()){
      return;
    }
    else {
      this.lineDataList.removeLast();
    }
  }
  public LineData getLastLineData(){
    if(!lineDataList.isEmpty()) {
      return lineDataList.getLast();
    }
    return null;
  }
}