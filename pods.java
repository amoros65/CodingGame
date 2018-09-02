import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

  int x ; //x position of the pod
  int y ; //y position of the pod
  int nextCheckpointX ; // x position of the next check point
  int nextCheckpointY ; // y position of the next check point
  int nextCheckpointDist; // distance to the next checkpoint
  int nextCheckpointAngle ; // angle between your pod orientation and the direction of the next checkpoint
  int opponentX ; //of the other pod
  int opponentY ; //of the other pod
  boolean Hasboost;
  boolean use;
  int thrust ; // vitessse

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
            Player player = new Player();
            player.Hasboost = true ;
            player.use = false;
        // game loop
        while (true) {
             player.x in.nextInt();
             player.y = in.nextInt();
             player.nextCheckpointX = in.nextInt(); // x position of the next check point
             player.nextCheckpointY = in.nextInt(); // y position of the next check point
             player.nextCheckpointDist = in.nextInt(); // distance to the next checkpoint
             player.nextCheckpointAngle = in.nextInt(); // angle between your pod orientation and the direction of the next checkpoint
             player.opponentX = in.nextInt();
             player.opponentY = in.nextInt();

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");


            // You have to output the target position
            // followed by the power (0 <= thrust <= 100)
            // i.e.: "x y thrust"
            player.thrust=100;

             if(nextCheckpointAngle > 90 ||nextCheckpointAngle <-90){
                thrust = 0;
                }else{
                    distance= dplayer.nextCheckpointDist / 100000;
                    System.err.println("Valeur de distance :" + distance);
                    if(distance < 100){
                        player.thrust = 75;
                    }
                    if(distance<75){
                      player.thrust=35;
                    }else{
                      if(distance > 500 && Hasboost){
                        player.use = true;
                        player.Hasboost = false;
                      }
                      player.thrust = 100;
                    }
                  }
                if(use){
                  System.out.println(nextCheckpointX + " " + nextCheckpointY + " " + "BOOST");

                }else{
                  System.out.println(nextCheckpointX + " " + nextCheckpointY + " " + player.thrust());
                }
                use = false;
            }
        }

        public player(){
          use = false;
          HasBoost = true;
        }
        /**
          *return the distance of the pod from the other checkpoint
          */
        public double distance(){
        return nextCheckpointDist;
        }

        public double distance2pod(){
          return  (double) (opponentX-x) * (double) (opponentX-x)+
            (double) (opponentY-y)* (double) (opponentY-y);
        }
}
