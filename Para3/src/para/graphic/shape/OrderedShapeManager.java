package para.graphic.shape;
import java.util.*;
import java.util.stream.Stream;

import para.graphic.parser.ShapeParser;
import para.graphic.target.Target;

/** 描画順序をidの順とする図形集合
 */
public class OrderedShapeManager extends ShapeManager{
    /**
     *  priorityQueue
     */
    public OrderedShapeManager(){
        super();
        this.data = new PriorityQueue<Shape>(Comparator.comparing(Shape::getID).reversed());
    }
}
