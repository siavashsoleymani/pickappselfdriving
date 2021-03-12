//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package service.impl;

import service.SpeedControlService;

public class SpeedControlServiceImpl implements SpeedControlService {
  private static final int MIN_SPEED = 700;
  private static final int MAX_SPEED = 2000;
  private static final int NEUTRAL_SPEED = 1500;
  private static final int SPEED_STEP = 20;
  private static final int HIGH_SPEED_STEP = 20;
  public static int CURRENT_SPEED = 1500;

  public SpeedControlServiceImpl() {
  }

  private static void setSpeed(int speed) {
    try {
      ProcessBuilder builder = new ProcessBuilder(new String[0]);
      builder.command("sh", "-c", "pigs s 4 " + speed);
      Process process = builder.start();
      process.waitFor();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  public int changeSpeed(String s) {
    if (s.equalsIgnoreCase("w")) {
      if (CURRENT_SPEED + SPEED_STEP > MAX_SPEED) {
        CURRENT_SPEED = MAX_SPEED;
      } else {
        CURRENT_SPEED += SPEED_STEP;
      }
    } else if (s.equalsIgnoreCase("s")) {
      if (CURRENT_SPEED - SPEED_STEP < MIN_SPEED) {
        CURRENT_SPEED = MIN_SPEED;
      } else {
        CURRENT_SPEED -= SPEED_STEP;
      }
    } else if (s.equalsIgnoreCase("q")) {
      if (CURRENT_SPEED + HIGH_SPEED_STEP > MAX_SPEED) {
        CURRENT_SPEED = MAX_SPEED;
      } else {
        CURRENT_SPEED += HIGH_SPEED_STEP;
      }
    } else if (s.equalsIgnoreCase("e")) {
      if (CURRENT_SPEED - HIGH_SPEED_STEP < MIN_SPEED) {
        CURRENT_SPEED = MIN_SPEED;
      } else {
        CURRENT_SPEED -= HIGH_SPEED_STEP;
      }
    } else {
      if (!s.equalsIgnoreCase(" ")) {
        throw new IllegalArgumentException("It must be one of the s,w,q,e or space commands");
      }

      CURRENT_SPEED = NEUTRAL_SPEED;
    }

    System.out.println(CURRENT_SPEED);
    setSpeed(CURRENT_SPEED);
    return CURRENT_SPEED;
  }

  @Override
  public void emergencyBreak() {
    System.out.println("Thread in emergency break: " + Thread.currentThread().getId());
    changeSpeed(" ");
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
