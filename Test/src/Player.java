/**
 * Created by orel on 14/04/17.
 */
import java.util.*;
import java.io.*;
import java.math.*;

class Player {

    final static String FACTORY = "FACTORY";
    final static String TROOP = "TROOP";
    final static String BOMB ="BOMB";

    //liste de toutes les factories
    private static Map<Integer, Factory> factories = null;
    //Liste des troupes enemies et amis
    private static Map<Integer, Troop> troupes = null;
    //Liste des factories qui m'appartiennent
    private static Map<Integer, Factory> myListFactory = null;
    //liste des attack
    private static Map<Integer,SortedSet<Attack>> matriceMaj = null;
    private static List<Bomb> bombes = null;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int factoryCount = in.nextInt(); // the number of factories
        int linkCount = in.nextInt(); // the number of links between factories
        factories = new HashMap<>(factoryCount);
        troupes = new HashMap<>();
        myListFactory = new HashMap<>();
        matriceMaj = new HashMap<>();
        bombes = new ArrayList<>(4);

        boolean hasBomb = true;
        Factory enemie = null;
        for (int i = 0; i < linkCount; i++) {
            int idFactory1 = in.nextInt();
            int idFactory2 = in.nextInt();
            Factory factory1;
            Factory factory2;

            //check if factory1 exists already
            if (factories.get(idFactory1) == null) {
                factory1 = new Factory(idFactory1);
                factories.put(idFactory1, factory1);
            }else {
                factory1 = factories.get(idFactory1);
            }
            //check if factory2 exists already
            if (factories.get(idFactory2) == null) {
                factory2 = new Factory(idFactory2);
                factories.put(idFactory2, factory2);
            }else {
                factory2 = factories.get(idFactory2);
            }
            int distance = in.nextInt();
            System.err.println("Fact :"+ idFactory1+" Fact "+ idFactory2+" distance : "+ distance);
            factory1.children.put(factory2, distance);
            factory2.children.put(factory1, distance);
        }
        boolean hasFreeFactory = true;

        // game loop
        while(true) {
//            hasFreeFactory = false;
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

                switch (entityType){
                    case FACTORY :
                        updateFactory(entityId, arg1, arg2, arg3);
                        break;

                    case TROOP :
                        //update des troupes et mise a jour du niveaux de cyborg
                        //arg 1 = idjoueur; arg2 factory de depart arg3 factory d'arrive arg4 nb de cyborg arg 5 tour d'arrivé
                        troupes.putIfAbsent(entityId, new Troop(entityId));
                        updateTroop(entityId, arg1, arg2, arg3, arg4, arg5);
                        Factory factory = factories.get(arg3);
                        if (matriceMaj.get(arg3) != null) {
                            SortedSet<Attack> set = matriceMaj.get(arg3);
                            Attack attack = new Attack(arg5, arg4);
                            if (set.contains(attack)) {
                                for (Attack attack1 : set)
                                {
                                    if (attack1.nbTours == attack.nbTours) {
                                        if (arg1 == factory.joueur)
                                            attack1.add(attack.nbCyborg);
                                        else
                                            attack1.add(-1 * attack.nbCyborg);
                                    }
                                    break;
                                }
                            } else
                                set.add(attack);
                        } else {
                            matriceMaj.put(arg3, new TreeSet<>());
                            if (arg1 == factory.joueur)
                                matriceMaj.get(arg3).add(new Attack(arg5, arg4));
                            else
                                matriceMaj.get(arg3).add(new Attack(arg5, -1 * arg4));
                        }
                        break;
                    case BOMB :  updateBombs(entityId,arg1, arg2, arg3, arg4);
                        break;

                }

            //On met a jour le nombre de cyborg dans toutes les usines
            for(Factory factory : factories.values()){
                SortedSet<Attack> set = matriceMaj.get(factory.id);
                if(set !=null)
                    updateForCast(factory,set);
            }

            matriceMaj= new HashMap<>();
            StringBuilder sb = new StringBuilder();
            // La creation d'ordre commence
            sb.append("WAIT");
            List<Troop> orders = AttackHeuristic();
            for(Troop troop : orders){
                sb.append(";");
                sb.append("MOVE ");
                sb.append(troop);
            }
//            if(hasBomb) {
//                List<Bomb> bombs = AlgoBomb(enemie);
//                for (Bomb bomb : bombs) {
//                    sb.append(";");
//                    sb.append("BOMB ");
//                    sb.append(bomb);
//                }
//                hasBomb = false;
//            }
            System.out.println(sb);
        }
    }
    }


    private static List<Troop> AttackHeuristic()
    {
        System.err.println("debut de la recherche heuristique pour l'attaque");
        Set<Factory> alreadyVisited = new HashSet<>();
        List<Troop> orders = new ArrayList<>();
        for(Factory factory : myListFactory.values())
        {
            System.err.println("Factory : "+ factory.id+" Nombre de Cyborg " + factory.nbCyborg);
            while(factory.nbCyborg>0)
            {
                double buff = 0;
                Factory facto = null;
                for(Map.Entry<Factory,Integer> entry: factory.children.entrySet())
                {
                    double weigth = factory.valeur(entry.getKey(),entry.getValue());
                    if((facto == null ||weigth>buff) && !alreadyVisited.contains(entry.getKey())){
                        buff =weigth;
                        facto = entry.getKey();
                    }
                }
                if(facto == null)
                    break;
                int nbCyborg = Math.abs(factory.children.get(facto)*facto.production*facto.joueur) + Math.abs(facto.nbCyborg) + 1 ;
                // System.err.println("Envoie de "+ nbCyborg +" pour l'usine " + facto.id);
                orders.add(new Troop(factory.id,facto.id,nbCyborg));
                if(factory.nbCyborg>=nbCyborg)
                {
                    alreadyVisited.add(facto);
                }
                factory.nbCyborg-=nbCyborg;
            }
        }
        return orders;
    }

    private static List<Bomb> AlgoBomb(Factory factory){

        Set<Factory> alreadyVisited = new HashSet<>(2);
        List<Bomb> bombes = new ArrayList<>(2);
        int nbBomb= 2;
        while(nbBomb >0) {
            double buff = 0.9;
            Factory arrive= null;
            for (Map.Entry<Factory, Integer> entry : factory.children.entrySet()) {
                double val =   factory.valeur(entry.getKey(),entry.getValue());
                if((arrive == null ||val>buff) && !alreadyVisited.contains(entry.getKey())){
                    buff =val;
                    arrive = entry.getKey();
                }
            }
            Factory depart = null;
            for(Factory factory2 : arrive.children.keySet()){
                if(factory2.id ==1 && (depart == null || arrive.children.get(factory2)< arrive.children.get(depart))){
                    depart = factory2;
                }
            }
            bombes.add(new Bomb(depart.id,arrive.id));
            alreadyVisited.add(arrive);
            --nbBomb;
        }
        return bombes;
    }

    /**
     * check si une factory est deja la cible d'une bombe
     * @param destination
     * @return
     */
    private static boolean hasABomb(int destination){
        if(bombes.size()<0)
            return false;
        for(Bomb bomb : bombes){
            if(bomb.destination == destination)
                return  true;
        }
        return false;
    }

    /**
     * check la factory est cible d'une de mes troupes
     * @param destination
     * @return
     */
    private static boolean hasMyTroop(int destination){
        if(troupes.size()<0)
            return false;
        for(Troop troop : troupes.values()){
            if(troop.joueur ==1 && troop.factoryArrive == destination){
                return true;
            }
        }
        return false;
    }

    private static void updateBombs(int id, int arg1,int arg2,int arg3, int arg4 ){
        //id, source, destination, le joueur qui l'a possede, nombre de tour avant destination
        Bomb bomb = new Bomb(id, arg2,arg3,arg1,arg4);
        if(!bombes.contains(bomb))
            bombes.add(bomb);
    }

    private static void updateFactory(int id, int arg1, int arg2, int arg3) {
        Factory factory = factories.get(id);
        factory.nbCyborg = arg2;
        factory.production = arg3;
        factory.joueur = arg1;
        if (arg1 == 1) {
            myListFactory.putIfAbsent(id, factory);
        }
        if (arg1 == -1) {
            myListFactory.remove(id);
        }

    }

    //arg 1 = idjoueur; arg2 factory de depart arg3 factory d'arrive arg4 nb de cyborg arg 5 tour d'arrivé
    private static void updateTroop(int id, int arg1, int arg2, int arg3, int arg4, int arg5) {
        Troop troop = troupes.get(id);
        troop.joueur = arg1;
        troop.factoryDepart = arg2;
        troop.factoryArrive = arg3;
        troop.nbTourArrive = arg5;
        troop.nbCyborg = arg4;
    }

    /**
     * update le nombre de cyborg d'une usine en considérente toutes les troupes
     * dans sa direction
     * @param factory la foctory pris en compte
     * @param attacks la liste de troupe presente sur le plateau
     */
    private static void updateForCast(Factory factory, SortedSet<Attack> attacks){
        Attack oldAttack = null;
        for(Attack attack: attacks){
            factory.nbCyborg = factory.nbCyborg + attack.nbCyborg;
            if(oldAttack == null)
            {
                factory.nbCyborg += attack.nbTours*factory.production;
            }
            else
            {
                factory.nbCyborg += (attack.nbTours - oldAttack.nbTours)*factory.production;
            }

            oldAttack = attack;
            if(factory.id==0)
                System.err.print("MAJ factory dde l'usine 0" + factory.nbCyborg+" attack.nbCyborg : " +attack.nbCyborg );
        }
    }

    /**
     * Classe factory qui represente les usines, avec des methodes propres à elles
     */
    private static class Factory {
        int id;
        int joueur;
        int nbCyborg;
        int production;

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
        private double valeur(Factory factory, int distance) {
            double response;
            if(factory.joueur != 1){
                if(factory.production != 0)
                    response = (double)(1+ factory.production)/(double) (distance*production+factory.nbCyborg)/ (double) distance;
                else
                    response = 1/(double) (distance*production+factory.nbCyborg)/ (double) distance;
            }else {
                if(factory.nbCyborg >0)
                    response = (double) ( 3 - factory.production)/(double) distance;
                else

                    response = -1 * factory.nbCyborg / distance;
            }
            System.err.println("Factory depart : " + this.id+" Factory arrive : " +factory.id+" distance : " + distance+" valeur :" +response );
            // System.err.println("Factory nbCyborg : " + factory.nbCyborg);
            return response ;
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
            if (factory == null)
                return false;

            return factory.id == id;
        }
    }

    /**
     * Classe qui represente les troupes envoyées
     * pour l'instant, elle sert juste à stocker des données
     * Pas de réel interet dans ma stratégie
     */
    private static class Troop {
        int id;
        int joueur;
        int factoryDepart;
        int factoryArrive;
        int nbCyborg;
        int nbTourArrive;

        Troop(int id) {
            this.id = id;
        }

        //Used when i create the troop to send orders
        Troop(int id, int id1, int nbCyborg) {
            factoryDepart = id;
            factoryArrive = id1;
            this.nbCyborg= nbCyborg;
        }


        @Override
        public String toString() {
            return factoryDepart + " " + factoryArrive + " " + nbCyborg;
        }

        @Override
        public int hashCode() {
            return id*joueur*factoryDepart*factoryArrive*nbCyborg*nbTourArrive;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Troop)
                return id==((Troop) obj).id;
            return false;
        }
    }

    public static class Attack implements Comparable<Attack> {
        int nbTours;
        int nbCyborg=0;

        public Attack(){}

         Attack(int nbTours, int nbCyborg) {
            this.nbTours = nbTours;
            this.nbCyborg = nbCyborg;
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

    public static class Bomb {
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
        public String toString() {
            return source+" "+ destination;
        }

        @Override
        public int hashCode() {
            return id*source*destination;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Bomb && id == ((Bomb) obj).id;
        }
    }
}