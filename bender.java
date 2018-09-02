import java.util.*;
import java.io.*;
import java.math.*;

/**
* Auto-generated code below aims at helping you parse
* the standard input according to the problem statement.
Solution for bender by orel
**/
/*
*creer un tableau de direction avec les 4 directions, et
on incremente l'index de la direction avec les trucs des obstacles
*/
class Solution {

  public static String[] direction = {"SOUTH","EAST","NORTH","WEST"};
  public static  final int NORTH = 2;
  public static  final int SOUTH = 0;
  public static  final int EAST =1;
  public static  final int WEST=3;
  public static char[][] plateau;
  public static int posx;
  public static int posy;
  public static int dir = 0;

  public static void main(String args[]) {
    //define constante according to the array


    Scanner in = new Scanner(System.in);
    int L = in.nextInt();
    int C = in.nextInt();
    in.nextLine();
    plateau = new char[L][C];

    /**
    * creation du plateau de jeu
    */
    for (int i = 0; i < L; i++) {
      String row = in.nextLine();
      char[] ligne = row.toCharArray();
      for(int j =0;j<ligne.length;j++){
        plateau[i][j]=ligne[j];
        if(plateau[i][j]=='@'){
          posx = i;
          posy = j;
        }
      }
      System.err.println(row);
    }

    // Write an action using System.out.println()
    // To debug: System.err.println("Debug messages...");
    boolean stop = true;

    while(stop){
      dir = hayObstacle(dir);
      System.out.println(direction[dir]);

      /**
      * If we are on a letter
      */
      int n = directionChange(plateau[posx][posy]);
      // System.err.println("valeur de n : " + n);
      if(n ==4){
        stop = false;
      }else if(n>=0&& n < 4){
        dir = n;
      }
    }
  }
  static int hayObstacle(int dir){
    switch(dir){
      case SOUTH:
      if (plateau[posx+1][posy] == '#'||plateau[posx+1][posy] == 'X') {
        //EAST bloque aussi
        if(plateau[posx][posy + 1] == '#'||plateau[posx][posy+1] == 'X'){
          // Nord bloque
          if(plateau[posx - 1][posy] == '#'||plateau[posx-1][posy] == 'X'){
            dir=WEST;
            posy--;
          }//end north bloque
          else{ dir = NORTH; posx--;}
        }//end EAST Bloque
        else{
          dir=EAST;posy++;
        }
      }//end sud bloque
      else{
        // dir =SOUTH;
        posx++;
      }
      break;
      case EAST:
      if (plateau[posx][posy + 1] == '#'||plateau[posx][posy+1] == 'X') {
        //SUD
        if (plateau[posx+1][posy] == '#'||plateau[posx+1][posy] == 'X') {
          //nord
          if(plateau[posx - 1][posy] == '#'||plateau[posx-1][posy] == 'X'){
            dir = WEST; posy--;
          }//end nord
          else{
            dir = NORTH;
            posx--;
          }
        }//end sud bloque
        else{
          dir = SOUTH;
          posx++;
        }
      }else{
        //dir = EAST;
        posy++;
      }
      break;
      case NORTH:
      //nord bloque
      if (plateau[posx - 1][posy] == '#'||plateau[posx-1][posy] == 'X') {
        //SUd bloque
        if (plateau[posx+1][posy] == '#'||plateau[posx+1][posy] == 'X') {
          //EAST bloque aussi
          if(plateau[posx][posy + 1] == '#'||plateau[posx][posy+1] == 'X'){
            dir=WEST;
            posy--;
          }//end EAST bloque
          else{
            dir=EAST;posy++;
          }
        }//end sud bloque
        else{
          dir = SOUTH;
          posx++;
        }
      }else{
        //dir = NORTH;
        posx--;
      }
      break;
      case WEST:
      //west
      if (plateau[posx][posy - 1] == '#'||plateau[posx][posy-1] == 'X') {
        //SUD bloque
        if (plateau[posx+1][posy] == '#'||plateau[posx+1][posy] == 'X') {
          //EAST bloque aussi
          if(plateau[posx][posy + 1] == '#'||plateau[posx][posy+1] == 'X'){
            dir=NORTH;
            posx--;
          }//end EAST bloque
          else{
            dir=EAST;
            posy++;
          }
        }//end sud bloque
        else{
          dir = SOUTH;
          posx++;
        }
      }else{
        //dir = WEST;
        posy--;
      }break;
    }
    return dir;
   System.err.println(dir);
}
static int  directionChange(char c) {
  switch(c){
    case 'S' : return 0;
    case 'E' : return 1;
    case 'N' : return 2;
    case 'W' : return 3;
    case '$' : return 4;
    default : return -1;
  }
}
}
