package com.example.CS6650_A1.Client1;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class NewThread extends Thread{

  public ApiClient apiClient;
  public SkiersApi skiersApi;
  private static final int MaxAttempt = 5;
  private int phaseNum;
  private int startSkierId;
  private int endSkierId;
  private int liftId;
  private int startTime;
  private int endTime;
  private int postRequestsForEachThread;
  private String serverAddress;
  private Count count;
  private CountDownLatch latch;
  private static final int RESORT_ID = 1;
  private static final String SEASON_ID = "2021";
  private static final String DAY_ID = "7";

  public NewThread(int phaseNum, int startSkierId, int endSkierId, int liftId, int startTime,
      int endTime, int postRequestsForEachThread, String serverAddress, Count count, CountDownLatch latch) {
    this.phaseNum = phaseNum;
    this.startSkierId = startSkierId;
    this.endSkierId = endSkierId;
    this.liftId = liftId;
    this.startTime = startTime;
    this.endTime = endTime;
    this.postRequestsForEachThread = postRequestsForEachThread;
    this.serverAddress = serverAddress;
    this.count = count;
    this.latch = latch;
  }

  @Override
  public void run() {
    apiClient = new ApiClient();
    skiersApi = new SkiersApi(apiClient);
    //apiClient.setBasePath(this.serverAddress + "/CS6650_A1_war_exploded/skiers/");
    apiClient.setBasePath(this.serverAddress + "/CS6650-A1_war/");

    for (int i = 0; i < postRequestsForEachThread; i++) {
      LiftRide liftRide = new LiftRide();
      int randomLiftId = ThreadLocalRandom.current().nextInt(1, this.liftId + 1);
      int randomTime = ThreadLocalRandom.current().nextInt(this.startTime, this.endTime + 1);
      int randomSkierId = ThreadLocalRandom.current().nextInt(this.startSkierId, this.endSkierId + 1);
      liftRide.setLiftID(randomLiftId);
      liftRide.setTime(randomTime);

      for (int attempt = 0; attempt < MaxAttempt; attempt++) {
        try {
          skiersApi.writeNewLiftRideWithHttpInfo(liftRide, RESORT_ID, SEASON_ID,
              DAY_ID, randomSkierId);
          this.count.incrementSuccess(1);
          break;
        } catch (ApiException e) {
          e.printStackTrace();
          this.count.incrementUnsuccessful(1);
        }
      }
    }
    this.latch.countDown();
  }
}
