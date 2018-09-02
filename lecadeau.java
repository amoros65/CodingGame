import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 * Resolution de l'egnime le cadeau
 **/
class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int prix = in.nextInt();
        System.err.println("Nombre de participant : "+N);
        System.err.println("Prix du cadeau : "+prix);
        int[] tableau = new int[N];
        int somme = 0;
        for (int i = 0; i < N; i++) {
            int B = in.nextInt();
            tableau[i] = B;
            somme +=B;
        }

        // Write an action using System.out.println()
        // To debug: System.err.println("Debug messages...");
        if(somme<prix){
        System.out.println("IMPOSSIBLE");
        }else{
          somme = prix;
          Arrays.sort(tableau);
          int prixapayer = (int) somme/N;
          for(int i=0;i<N;i++){
            if(tableau[i]<prixapayer){
              System.out.println(tableau[i]);
              somme-=tableau[i];
              double justeprix = (double)somme/(N-i-1);
              prixapayer=somme/(N-i-1);
              //System.err.println("Reste à payer " + somme);
              System.err.println("Prix a payer "+ prixapayer);
            }else{
                if(i == N-1){
                  System.out.println(somme);
                  System.err.println("La somme final payer est de " + somme);
                }else{
                  System.out.println(prixapayer);
                  somme-=prixapayer;
                  prixapayer=somme/(N-i-1);
                }
            }
          }
        }
    }
}
