import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

            boolean Hasboost = true ;
            boolean use = false;
        // game loop
        while (true) {
            int x = in.nextInt();
            int y = in.nextInt();
            int nextCheckpointX = in.nextInt(); // x position of the next check point
            int nextCheckpointY = in.nextInt(); // y position of the next check point
            int nextCheckpointDist = in.nextInt(); // distance to the next checkpoint
            int nextCheckpointAngle = in.nextInt(); // angle between your pod orientation and the direction of the next checkpoint
            int opponentX = in.nextInt();
            int opponentY = in.nextInt();

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");


            // You have to output the target position
            // followed by the power (0 <= thrust <= 100)
            // i.e.: "x y thrust"
            int thrust=100;

             if(nextCheckpointAngle > 90 ||nextCheckpointAngle <-90){
                thrust = 0;
                }else{
                    double distance = (double) (nextCheckpointX-x) * (double) (nextCheckpointX-x)+
                    (double) (nextCheckpointY-y)* (double) (nextCheckpointY-y);
                    System.err.println("Valeur de distance :" + distance);
                    distance= distance / 100000;
                    if(distance < 100){
                        thrust = 75;
                    }
                    if(distance<75){
                      thrust=35;
                    }else{
                      if(distance > 500 && Hasboost){
                        use = true;
                        Hasboost = false;
                      }
                      thrust = 100;
                    }
                  }
                if(use){
                  System.out.println(nextCheckpointX + " " + nextCheckpointY + " " + "BOOST");

                }else{
                  System.out.println(nextCheckpointX + " " + nextCheckpointY + " " + thrust);
                }
                use = false;
            }
        }

        public double ()
}
