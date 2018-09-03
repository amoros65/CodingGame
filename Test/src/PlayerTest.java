import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class PlayerTest {

    private static List<Factory> myFactories;
    private static List<Factory> Factories;
    private static List<Bomb> bombs;
    private static final String BOMB = "bmbe";
    private static final  String FACTORY = "factory"; //TODO verifier ces valeurs
    private static final String TROOP = "troupe";
    private static List<Attack> attackList ;
    private static Map<Integer,SortedSet<Attack>> AttacksPerFactory;
    public static void main(String args[])
    {

        myFactories = new ArrayList<>();
//        attackList = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        AttacksPerFactory = new HashMap<>();
        int factoryCount = in.nextInt(); // the number of factories
        Factories = new ArrayList<>(factoryCount);
        bombs = new ArrayList<>();
        int linkCount = in.nextInt(); // the number of links between factories
        for (int i = 0; i < linkCount; i++) {
            int factory1 = in.nextInt();
            int factory2 = in.nextInt();
            int distance = in.nextInt();
            //prepare les structures de données, le plateau
            fillFactories(factory1,factory2,distance);
        }

        // game loop
        while (true) {
            boolean hasFreeFactory = false;
            int entityCount = in.nextInt(); // the number of entities (e.g. factories and troops)
            for (int i = 0; i < entityCount; i++)
            {
                int entityId = in.nextInt();
                String entityType = in.next();
                int arg1 = in.nextInt();
                int arg2 = in.nextInt();
                int arg3 = in.nextInt();
                int arg4 = in.nextInt();
                int arg5 = in.nextInt();
                //TODO : revoir l'heuristique dans son ensemble

                updateGame( entityType, arg1, arg2, arg3, arg4, arg5, entityId);
                if(arg1 == 0 && entityType.equalsIgnoreCase(FACTORY) ) //TODO : to check if arg = 0 intervient seulement dans le cas des factory
                {
                    hasFreeFactory = true;
                }
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");


            // Any valid action, such as "WAIT" or "MOVE source destination cyborgs"
            StringBuilder sb = Heuristic(hasFreeFactory);
            System.out.println(sb + " WAIT");

        }
    }


    private static void fillFactories(int factory1, int factory2, int distance)
    {
        if (Factories.get(factory1) == null)
        {
            Factories.add(new Factory(factory1));
        }

        if (Factories.get(factory2) == null)
        {
            Factories.add(new Factory(factory2));
        }
        Factories.get(factory1).children.put(Factories.get(factory2), distance);
        Factories.get(factory2).children.put(Factories.get(factory1), distance);
    }

    private static void updateGame(String entityType, int arg1, int arg2,int arg3,int arg4, int arg5, int entityId)
    {
        attackList = new ArrayList<>();
        switch (entityType)
        {
            case TROOP: updateTroop(arg1,arg2,arg3,arg4,arg5);
            break;
            case BOMB : updateBomb( arg1,  arg2, arg3,  arg4,  entityId);
                break;
            case FACTORY : updateFactory(arg1,arg2,arg3,arg4,entityId);
                break;
        }
    }

    //TODO implementer les methodes pour update la liste des troupes, la liste des factory et les bombs

    private static void updateTroop(int arg1, int arg2, int arg3, int arg4,int arg5)
    {
        SortedSet<Attack> attacks =AttacksPerFactory.get(arg3);
        if( attacks == null)
        {
            attacks = new TreeSet<>();
            attacks.add(new Attack(arg1,arg2,arg3,arg4,arg5));
            AttacksPerFactory.put(arg3,attacks);
        }
        else
        {
            attacks.add(new Attack(arg1,arg2,arg3,arg4,arg5));
        }
    }

    private static void  updateFactory(int arg1, int arg2, int arg3, int arg4, int entityId)
    {
        //TODO : reflechir a voir comment update les factory avec les != attacks etc
        Factory factory = Factories.get(entityId);
        if(factory == null){
            return;
        }

        factory.joueur =arg1;
        factory.nbCyborg = arg2;
        factory.production = arg3;
        factory.nbTourBeforeProduction = arg4;
    }

    private static void updateBomb(int arg1, int arg2,int arg3, int arg4, int entityId)
    {
        bombs.set(entityId,new Bomb(entityId,arg2,arg3,arg1,arg4)) ;
    }

    private  static StringBuilder Heuristic(boolean hasFreeFactory)
    {
        //TODO : si plus d'usine de libre, on se resout a augmenter la production si on n'est pas en train de perdre des usnines
        StringBuilder sb = new StringBuilder();
        if(hasFreeFactory)
        {
            for (Factory factory: myFactories )
            {
                while(factory.nbCyborg >= 0)
                {
                    Factory target = factory.FactoryTheMostValuable(); //TODO : preciser peut etre la possesion et mettre a jour cette factory
                    int distance = factory.children.get(target);
                    int nbCyborg = target.nbCyborg + distance * target.production;
                    factory.nbCyborg -= nbCyborg;
                    sb.append("MOVE " + factory.id +" " + target.id +" "+nbCyborg);
                }
            }    
        }
        else
        {
            for (Factory factory: myFactories )
            {
                Factory target = factory.FactoryNeedSupport();
                if(target == null)
                {
                    if(factory.nbCyborg >= 10)
                    {
                        sb.append("INC " + factory.id);
                    }
                    continue;
                }
                int nbCyborg = target.nbCyborg;
                sb.append("MOVE " + factory.id +" " + target.id +" "+nbCyborg);
            }
        }
        return sb;
    }


      static class Factory{

        int id;
        int joueur;
        int nbCyborg;
        int production;
        int nbTourBeforeProduction;
        //Map les usines voisines avec leur distances associée
        Map<Factory, Integer> children;


        private Factory(int id) {
            this.id = id;
            children = new HashMap<>();
        }

        /**
         *
         * Represente le poids de la factory (valeur à faire evoluer peut etre)
         * @param factory
         * @param distance
         * @return
         */
        //TODO : revoir cette methode
        private double valeur(Factory factory, int distance)
        {
            return 0;
        }

        @Override
        public String toString() {
            return "id : " + id;
        }

        @Override
        public boolean equals(Object fac) {
            if (!(fac instanceof Factory))
                return false;

            Factory factory = (Factory) fac;

            return factory.id == id;
        }

        public Factory FactoryTheMostValuable()
        {
            double max = 0;
            Factory result = null;
            for (Map.Entry<Factory,Integer> entry : children.entrySet())
            {
                double value = 10;
                if(value > max)
                {
                    max = value;
                    result = entry.getKey();
                }
            }
            return result;
        }

        public Factory FactoryNeedSupport()
        {
            double max =0;
            Factory result = null;
            for(Map.Entry<Factory, Integer> entry : children.entrySet())
            {
                Factory factoryTampon = entry.getKey();
                if(factoryTampon.joueur ==1 && factoryTampon.nbCyborg <0)
                {
                    int nbCyborg = factoryTampon.nbCyborg;
                    return factoryTampon;
                }
            }
            return null;
        }
    }



    public static class Attack implements Comparable<Attack> {
        int nbTours;
        int nbCyborg=0;
        int idBeginFactory;
        int idEndFactory;
        int possession;

        Attack(){}

        public Attack(int possession, int idBeginFactory, int idEndFactory ,int nbTours, int nbCyborg)
        {
            this.nbTours = nbTours;
            this.nbCyborg = nbCyborg;
            this.possession = possession;
            this.idBeginFactory = idBeginFactory;
            this.idEndFactory = idEndFactory;
        }

        void add(int Cyborg){
            nbCyborg += Cyborg;
        }

        @Override
        public int hashCode() {
            return nbTours;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Attack && nbTours == ((Attack) obj).nbTours;
        }

        @Override
        public int compareTo(Attack o) {
            if(nbTours > o.nbTours)
                return 1;
            if(nbTours==o.nbTours)
                return 0;
            return -1;
        }
    }

    public static class Bomb
    {
        int id;
        int source;
        int destination;
        int joueur;
        int nbTourArrive;

        Bomb(int id, int id1) {
            source = id;
            destination = id1;
        }

        Bomb(int id, int source, int destination, int joueur, int nbTourArrive) {
            this.id = id;
            this.source = source;
            this.destination = destination;
            this.joueur = joueur;
            this.nbTourArrive = nbTourArrive;
        }

        @Override
        public String toString()
        {
            return source+" "+ destination;
        }

        @Override
        public int hashCode()
        {
            return id*source*destination;
        }

        @Override
        public boolean equals(Object obj)
        {
            return obj instanceof Player.Bomb && id == ((Player.Bomb) obj).id;
        }
    }
}
