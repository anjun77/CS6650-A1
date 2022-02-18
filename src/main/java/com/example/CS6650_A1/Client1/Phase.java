package com.example.CS6650_A1.Client1;

import java.util.concurrent.CountDownLatch;

public class Phase {

  private int phaseNumber;
  private int numThreads;
  private int numSkiers;
  private int numLifts;
  private int startTime;
  private int endTime;
  private int numPostReq;
  private CountDownLatch latch;
  private Count count;
  private String serverAddress;

  public Phase(int phaseNumber, int numThreads, int numSkiers, int numLifts, int startTime, int endTime,
      int numPostReq, CountDownLatch latch, Count count, String serverAddress) {
    this.phaseNumber = phaseNumber;
    this.numThreads = numThreads;
    this.numSkiers = numSkiers;
    this.numLifts = numLifts;
    this.startTime = startTime;
    this.endTime = endTime;
    this.numPostReq = numPostReq;
    this.latch = latch;
    this.count = count;
    this.serverAddress = serverAddress;
  }

  public void startPhase() {
    int skierIdRange = numSkiers / numThreads;
    for (int i = 0; i < numThreads; i++) {
      int startSkierId = i * skierIdRange + 1;
      int endSkierId;
      if (i == numThreads - 1) {
        endSkierId = numSkiers;
      } else {
        endSkierId = skierIdRange * (i + 1);
      }
//      NewThread newThread = new NewThread(phaseNumber, startSkierId, endSkierId, numLifts, startTime,
//          endTime, numPostReq, serverAddress, this.count, latch);
//      Thread thread = new Thread(newThread);
      Thread thread = new NewThread(phaseNumber, startSkierId, endSkierId, numLifts, startTime,
          endTime, numPostReq, serverAddress, this.count, latch);
      thread.start();
    }
  }

  public void waitTime() throws InterruptedException {
    latch.await();
  }
}
