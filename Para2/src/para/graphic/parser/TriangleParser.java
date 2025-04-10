package para.graphic.parser;
import java.util.Scanner;

import para.graphic.shape.Attribute;
import para.graphic.shape.Triangle;

public class TriangleParser implements ShapeParser{
    TriangleParser(){
    }

    @Override
    public Triangle parse(Scanner s, int id){
        int x0 = s.nextInt();
        int y0 = s.nextInt();
        int x1 = s.nextInt();
        int y1 = s.nextInt();
        int x2 = s.nextInt();
        int y2 = s.nextInt();
        Triangle ret;
        Attribute attr=null;
        if(s.hasNext("Attribute")){
            attr = AttributeParser.parse(s);
            // TODO: debug need to be deleted
            // System.out.println("atrribute input ok");
        }
        // System.out.println(attr);
        ret = new Triangle(id,x0,y0,x1,y1,x2,y2,attr);
        return ret;
    }

}
