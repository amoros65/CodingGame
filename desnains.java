/**
 * Created by orel on 09/04/17.
 */
import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt(); // the number of relationships of influence
        Map <Integer, Tree> teteDeListe = new HashMap<Integer, Tree>();

        for (int j = 0; j < n; j++) {
            int x = in.nextInt(); // a relationship of influence between two people (x influences y)
            int y = in.nextInt();
            System.err.println(x+ " " + y);
            Set<Integer> keys = teteDeListe.keySet();

              /* commence ici a modifier */
            Tree treex = teteDeListe.get(x);
            if(treex ==null){
                for(int i : keys){
                    // System.err.println("Valeur de la clef pour la recherche ds truc "+i +" pour la valeur de " +x);
                    // System.err.println(teteDeListe.get(i).children.size());
                    treex=teteDeListe.get(i).contains(x);
                }
                //System.err.println("verification si ce n'est pas une tete de liste");
                if(treex == null){
                    System.err.println("Ajout de la tete de liste " + x);
                    treex = new Tree(x);
                    teteDeListe.put(x,treex);
                }
            }
            Tree treey=teteDeListe.get(y);
            if(treey == null){
               // Tree buff = null;
                for(int i : keys){
                    treey = teteDeListe.get(i).contains(y);
                    if(treey != null)
                        break;
                }
                if(treey == null){
                    System.err.println("Creation de l'arbre y pour y :" + y);
                    treey = new Tree(y);
                    treex.children.add(treey);
                }else{

                    treex.children.add(treey);
                }
            }else{
                System.err.println("Suppression de la tete de liste " + y);
                treex.children.add(treey);
                teteDeListe.remove(y);
            }

            /** fin de modification */
        }

        // Write an action using System.out.println()
        // To debug: System.err.println("Debug messages...");

        Set<Integer> keys = teteDeListe.keySet();
        for(int i : keys){
            System.err.print(" " +i);

        }

        int val = 0;
        for(int i : keys){
            System.err.println("valeur de la clef " +i);
            int j = teteDeListe.get(i).profondeur();
            if(val <j){
                val = j;
            }
        }
        // The number of people involved in the longest succession of influences
        System.out.println("La valeur est :" +val);
    }

    private static class Tree{
        int data;
        List<Tree> children ;
        public Tree (int n){
            data = n;
            children = new ArrayList<Tree>();
        }
        public int profondeur(){
            System.err.println("execution de la fonction profondeur pour l'arbre " +data);
            if(children == null){
                return 1;
            }else{
                int val =0;

                for(Tree tree : children){
                    System.err.print(" fils:" +tree.data);
                }
                System.err.println("");
                for(Tree tree : children){
                    int buff = tree.profondeur();


                    if(val<buff){
                        val = buff;
                    }
                   // System.err.println("Valeur de la valeur val " + val);
                }
                return ++val;
            }
        }

        public Tree contains(int n){
            if(n==data){
                return this;
            }
            Tree response = null;
            if(children!=null && children.size()!=0){
                for(Tree tree : children){
                    Tree buff = tree.contains(n);
                    if(buff !=null){
                        response = buff;
                        return response;
                    }
                }
            }
            return response;
        }

//        public Tree contains(int n){
//            Tree tree = this;
//            while(tree.children != null && tree.children.size()!=0){
//
//            }
//
//        }


    }
}
