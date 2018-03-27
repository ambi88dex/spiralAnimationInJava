import processing.core.PApplet;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class MySketch extends PApplet{

    private static int sides_count;
    private static int x_dimen;
    private static int y_dimen;

    void addRotatedPointInTheQueue(Deque<Coord>minCoordQueue, Deque<Coord> coordQueue, float side,float angleOfRotation)
    {
        Coord newPoint = rotateSegment(minCoordQueue.peekLast(),minCoordQueue.peekFirst(),side,angleOfRotation);
//        System.out.println(newPoint.x+" "+newPoint.y+" "+angleOfRotation);
//        if(Float.isNaN(newPoint.x))
//        {
//            System.out.println("NaN found.");
//            System.out.println(minCoordQueue.peekLast().x+" "+minCoordQueue.peekLast().y+" "+minCoordQueue.peekFirst().x+" "+minCoordQueue.peekFirst().x);
//        }
        minCoordQueue.removeLast();
        coordQueue.addFirst(newPoint);
        minCoordQueue.addFirst(newPoint);
    }

    float getDistance(Coord p1, Coord p2)
    {
        return sqrt(pow((p1.x-p2.x),2)+pow((p1.y-p2.y),2));
    }

    float getAugmentedAngle(Coord l, Coord pivot, Coord r)
    {
        float adg1=getDistance(l,pivot);
        float adg2=getDistance(pivot,r);
        float opp=getDistance(l,r);

        float retAngle=(adg1*adg1+adg2*adg2-opp*opp)/(2f*adg1*adg2);
//        System.out.println(adg1+" "+adg2+ " "+opp+"Value of gAA="+retAngle);
        retAngle=(float)Math.toDegrees(acos(retAngle));
        return retAngle;
    }

    Coord getNexCoordinate(Coord c1, Coord c2 ,float len) // c1 is the included point
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
                alpha=90f;
            }
            else if(c1.y!=c2.y)
            {
                alpha=-90f;
            }
        }
        float temp=(float)Math.toRadians(alpha+angleInDegrees);
        float x=c2.x+side_length*cos((float)Math.toRadians(alpha+angleInDegrees));
        float y=c2.y+side_length*sin((float)Math.toRadians(alpha+angleInDegrees));
//        System.out.println("Rotate Segment Coords=>"+angleInDegrees+" "+x+" "+ y);
        return new Coord(x,y);

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
        float side=80;
        Deque<Coord> coordQueue = new LinkedList<>();
        Deque<Coord>minCoordQueue = new LinkedList<>();
        side=80f;
        coordQueue.addFirst(new Coord(0,0));
        minCoordQueue.addFirst(new Coord(0,0));
        coordQueue.addFirst(new Coord(side,0));
        minCoordQueue.addFirst(new Coord(side,0));
        int iteration_count=sides_count+1;
        int it=0;
        float angleOfRotation=180f-360f/sides_count;
        while(it<iteration_count)
        {
            System.out.println(it);
            // print the line as per the mincoord deque
            line(minCoordQueue.peekFirst().x,minCoordQueue.peekFirst().y,minCoordQueue.peekLast().x,minCoordQueue.peekLast().y);
            System.out.printf("%f,%f,%f,%f\n",minCoordQueue.peekFirst().x,minCoordQueue.peekFirst().y,minCoordQueue.peekLast().x,minCoordQueue.peekLast().y);
            if(it%(sides_count-1)==sides_count-2)
            {
                //change the parameters;
                side*=1.1;
                Coord nextElem=getNexCoordinate(coordQueue.peekFirst(),coordQueue.peekLast(),side);
                minCoordQueue.removeLast();
                coordQueue.removeLast();
                angleOfRotation=getAugmentedAngle(coordQueue.peekFirst(),nextElem,coordQueue.peekLast());
                System.out.println("aOR after updation="+angleOfRotation);
                minCoordQueue.addFirst(nextElem);
                coordQueue.addFirst(nextElem);
            }
            else
            {
                addRotatedPointInTheQueue(minCoordQueue,coordQueue,side,angleOfRotation);
                if(coordQueue.size()>sides_count)
                {
                    coordQueue.removeLast();
                }
                //System.out.println(it+" "+coordQueue.size());
            }
            //update the minCoord deque for the next iteration
            it++;
        }
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
