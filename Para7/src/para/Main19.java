package para;

import para.Main15;
import javafx.application.Application;
/** スライダにより画像処理効果が変わるプログラ  */
class Main19{
    public static void main(String[] args){
        args = new String[]{"imagefilter.cl","Filter5"};
        Main15.setFileAndKernel(args);
        javafx.application.Application.launch(Main15.class,args);
    }
}

