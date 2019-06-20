package com.company;

public class DiningPhilosophers{
    public static void main(String[] args){
        int rounds = 10;

        Fork[] Forks = new Fork[5];
        for(int i=0; i< Forks.length; i++) {
            Forks[i] = new Fork();
        }

        Philosopher[] philosophers = new Philosopher[5];
        philosophers[0] = new Philosopher("Plato(0) ", Forks[0], Forks[1], rounds);
        philosophers[1] = new Philosopher("Socrates(1) ", Forks[1], Forks[2], rounds);
        philosophers[2] = new Philosopher("Euclid(2) ", Forks[2], Forks[3], rounds);
        philosophers[3] = new Philosopher("Skovoroda(3) ", Forks[3], Forks[4], rounds);
        philosophers[4] = new Philosopher("Freud(4) ", Forks[4], Forks[0], rounds);

        for (int i = 0; i < philosophers.length; i++ ){
            System.out.println("Thread "+ i + " started");
            Thread t = new Thread(philosophers[i]);
            t.start();
        }
    }
}

class Philosopher extends Thread{
    private Fork leftFork;
    private Fork rightFork;
    private String _name;
    private int _rounds;

    public Philosopher ( String name, Fork left, Fork right, int rounds){
        this._name = name;
        leftFork = left;
        rightFork = right;
        _rounds = rounds;
    }

    public void eat(){
        if(tryPick(leftFork, rightFork, _name)){
            try{
                Thread.sleep(500);
            }
            catch(InterruptedException ex){ }

            rightFork.Put(_name, "right");
            leftFork.Put(_name, "left");
        }
        think();
    }


    public void think(){
        System.out.println(_name + "is thinking");
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ex){ }

    }

    public static synchronized boolean tryPick(Fork left, Fork right, String phName){
        if(!left.inUse && !right.inUse){
            left.Pick(phName, "left");
            right.Pick(phName, "right");
            System.out.println(phName + "IS EATING");
            return true;
        }
        return false;
    }

    public void run(){
        for(int i = 0; i <= _rounds; i++){
            eat();
        }
    }
}

class Fork{
    public  boolean inUse;
    public Fork(){
        inUse = false;
    }

    public synchronized void Pick(String phName, String forkName){
        this.inUse = true;
        System.out.println(String.format("%s picked up %s fork", phName, forkName));
    }
    public synchronized void Put(String phName, String forkName){
        this.inUse = false;
        System.out.println(String.format("%s put down %s fork", phName, forkName));
    }
}