package com.example.CS6650_A1.Client1;

import java.util.concurrent.CountDownLatch;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Client client = new Client();
        client.parameters(args);

        Count count = new Count();

        long start = System.currentTimeMillis();
        int numLifts = client.getNumLifts();
        int numSkiers = client.getNumSkiers();

        //phase 1 start
        int threadsForPhase1 = client.getNumThreads() / 4;
        //int numSkiersId = (int) (client.getNumSkiers() / threadsForPhase1);
        int startTime = 1;
        int endTime = 90;
        int postRequestsForEachThread = (int) ((client.getNumRuns() * 0.2) * (client.getNumSkiers() / (threadsForPhase1)));
        double percentCompleted1 = 0.2;
        CountDownLatch latch1 = new CountDownLatch(
            (int) (threadsForPhase1 * percentCompleted1));
        Phase phase1 = new Phase(1, threadsForPhase1, numSkiers, numLifts, startTime, endTime, postRequestsForEachThread,
            latch1, count, client.getServerAddress());
        phase1.startPhase();
        //phase1.waitTime();

        //phase 2 start
        int threadsForPhase2 = client.getNumThreads();
        startTime = 91;
        endTime = 360;
        //numSkiersId = (int) (client.getNumSkiers() / threadsForPhase2);
        postRequestsForEachThread = (int) (client.getNumRuns() * 0.6) * (client.getNumSkiers() / threadsForPhase2);
        double percentCompleted2 = 0.2;
        CountDownLatch latch2 = new CountDownLatch(
            (int) ((threadsForPhase2) * percentCompleted2));
        Phase phase2 = new com.example.CS6650_A1.Client1.Phase(2, threadsForPhase2, numSkiers, numLifts, startTime, endTime, postRequestsForEachThread,
            latch2, count, client.getServerAddress());

        latch1.await();
        phase2.startPhase();
        //phase2.waitTime();

        //phase 3 start
        int threadsForPhase3 = (int) (client.getNumThreads() * 0.1);
        //numSkiersId = (int) (client.getNumSkiers() / threadsForPhase3);
        startTime = 361;
        endTime = 420;
        postRequestsForEachThread = (int) (0.1 * client.getNumRuns());
        double percentCompleted3 = 0.1;
        CountDownLatch latch3 = new CountDownLatch((int) (threadsForPhase3 * percentCompleted3));
        Phase phase3 = new Phase(3, threadsForPhase3, numSkiers, numLifts, startTime, endTime, postRequestsForEachThread,
            latch3, count, client.getServerAddress());

        latch2.await();
        phase3.startPhase();
        //phase3.waitTime();

        //wait
        phase1.waitTime();
        phase2.waitTime();
        phase3.waitTime();

        //print the summary
        long end = System.currentTimeMillis();

        System.out.println("Threads: " + client.getNumThreads());
        PrintOut print = new PrintOut(count, start, end);
        print.Print();
    }
}
