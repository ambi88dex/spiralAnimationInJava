import processing.core.PApplet;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class MySketch extends PApplet{

    private static int sides_count;
    private static int x_dimen;
    private static int y_dimen;

    void addRotatedPointInTheQueue(Queue<Coord>minCoordQueue, Deque<Coord> coordQueue, float side,float angleOfRotation)
    {
        Coord front= minCoordQueue.peek();
        minCoordQueue.remove();
        Coord newPoint = rotateSegment(front,minCoordQueue.peek(),side,angleOfRotation);
        coordQueue.addFirst(newPoint);
        minCoordQueue.add(newPoint);
    }

    Coord getNexCoordinate(Coord c1, Coord c2 ,float len)
    {
        if(c1.x!=c2.x)
        {
            float slope=(c2.y-c1.y)/(c2.x-c1.x);
            float y=c1.y+slope*len/sqrt(1f+slope*slope);
            float x=c1.x+len/sqrt(1f+slope*slope);
            return new Coord(x,y);
        }
        else
        {
            float y=((c2.y-c1.y)/(abs(c2.y-c1.y)))*len;
            return new Coord(c1.x,y);
        }
    }

    Coord rotateSegment(Coord c1, Coord c2 , float side_length, float angleInDegrees)
    {
        // In this code point c1 is being rotated about c2.

        float angleInRadians = (float)Math.toRadians(angleInDegrees/2);

        float alpha = (float)Math.toDegrees(atan((c1.y-c2.y)/(c1.x-c2.x)));
        if(c1.x<c2.x) // Second and Third Quadrant
        {
            alpha+=180;
        }
        else if(c1.x==c2.x)
        {
            if(c1.y>c2.y)
            {
                alpha=90;
            }
            else if(c1.y!=c2.y)
            {
                alpha=-90;
            }
        }

        float x=c2.x+side_length*cos((float)Math.toRadians(alpha+angleInDegrees));
        float y=c2.y+side_length*sin((float)Math.toRadians(alpha+angleInDegrees));
        Coord ret = new Coord(x,y);
        return ret;

    }

    @Override
    public void setup() {
        super.setup();
        noLoop();
    }

    @Override
    public void settings() {
        super.settings();
        size(x_dimen,y_dimen);
    }

    @Override
    public void draw()
    {
        translate(x_dimen/2,y_dimen/2);
        int offset=0;
        int side=80;
        Deque<Coord> coordQueue = new LinkedList<>();
        Queue<Coord>minCoordQueue = new LinkedList<>();
        for(int i=0;i<sides_count;i++)
        {
            if(i==0)
            {
                coordQueue.addFirst(new Coord(0,0));
                minCoordQueue.add(new Coord(0,0));
            }
            else if(i==1)
            {
                coordQueue.addFirst(new Coord(side,0));
                minCoordQueue.add(new Coord(side,0));
            }
            else
            {
                float angleOfRotation=180f-360f/sides_count;
                addRotatedPointInTheQueue(minCoordQueue,coordQueue,side,angleOfRotation);
            }
        }
        //System.out.println(coordQueue.size());
//        Coord c1= new Coord(0,0);
//        Coord c2 = new Coord(1,0);
//        Coord cresult=getNexCoordinate(c1,c2,1.1f);
//        System.out.println(String.valueOf(cresult.x)+":"+String.valueOf(cresult.y));
//        c2.y=1;
//        cresult=getNexCoordinate(c1,c2,2*sqrt(2));
//        System.out.println(String.valueOf(cresult.x)+":"+String.valueOf(cresult.y));
        while(!minCoordQueue.isEmpty())
        {
            minCoordQueue.remove();
        }
        while(!coordQueue.isEmpty())
        {
            minCoordQueue.add(coordQueue.peekLast());
            coordQueue.removeLast();
        }
        /**
         *
         * System.out.println(minCoordQueue.size());
         * gives output 3
         */
        Coord peek= minCoordQueue.peek();
        coordQueue.addFirst(peek);
        minCoordQueue.remove();
        /**
         * line(0,0,90,90);
         * This comment is used to check if the line is getting printed in the processing console or not.
         */

        while(minCoordQueue.size()>0)
        {
//
//          System.out.println("Entered the intitialising while loop " +String.valueOf(minCoordQueue.size()));
//            System.out.printf("%f,%f,%f,%f\n",peek.x,peek.y,minCoordQueue.peek().x,minCoordQueue.peek().y);
            line(minCoordQueue.peek().x,minCoordQueue.peek().y,peek.x,peek.y);
//            System.out.println(String.valueOf(peek.x)+","+String.valueOf(peek.y)+":"+String.valueOf(coordQueue.peek().x)+","+String.valueOf(coordQueue.peek().y));
//            System.out.printf("%f\n",sqrt(pow(peek.y-coordQueue.peek().y,2)+pow(peek.x - coordQueue.peek().x,2)));
            peek= minCoordQueue.peek();
            coordQueue.addFirst(peek);
            minCoordQueue.remove();
        }
//        System.out.println(coordQueue.size());
        int iteration_count=1;
        int it=0;
        float angleOfRotation;
//        while(it<iteration_count)
//        {
//            System.out.println(it);
//            if(it%(sides_count-1)==0)
//            {
//                side*=1.1;
//                Coord nextElem = getNexCoordinate(coordQueue.peekFirst(),coordQueue.peekLast(),side);
//                minCoordQueue.add(coordQueue.peekFirst());
//                minCoordQueue.add(nextElem);
//                line(nextElem.x,nextElem.y,coordQueue.peekFirst().x,coordQueue.peekFirst().y);
////                System.out.printf("%f,%f,%f,%f\n",nextElem.x,nextElem.y,coordQueue.peekLast().x,coordQueue.peekLast().y);
//                coordQueue.addFirst(nextElem);
//            }
//            else
//            {
//                addRotatedPointInTheQueue(minCoordQueue,coordQueue,side,0.0f);
//                peek = minCoordQueue.peek();
//                minCoordQueue.remove();
//                line(peek.x,peek.y,minCoordQueue.peek().x,minCoordQueue.peek().y);
//            }
//            it++;
//            coordQueue.removeLast();
//        }
    }

    public void mousePressed()
    {
        background(64);
    }

    public static void main(String[] args)
    {
        String[] processingArgs={"MySketch"};
        MySketch mySketch = new MySketch();
        sides_count=Integer.parseInt(args[0]);
        x_dimen=Integer.parseInt(args[1]);
        y_dimen=Integer.parseInt(args[2]);
        PApplet.runSketch(processingArgs,mySketch);
    }
}

class Coord
{
    float x;
    float y;
    Coord(float _x ,float _y)
    {
        x=_x;
        y=_y;
    }
}
