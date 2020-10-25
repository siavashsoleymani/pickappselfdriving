//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package service.impl;

import java.io.IOException;
import service.SpeedControlService;

public class SpeedControlServiceImpl implements SpeedControlService {
  private static final int MIN_SPEED = 700;
  private static final int MAX_SPEED = 2000;
  private static final int NEUTRAL_SPEED = 1500;
  private static final int SPEED_STEP = 20;
  private static final int HIGH_SPEED_STEP = 20;
  private static int CURRENT_SPEED = 1500;

  public SpeedControlServiceImpl() {
  }

  private static void setSpeed(int speed) {
    try {
      ProcessBuilder builder = new ProcessBuilder(new String[0]);
      builder.command("sh", "-c", "pigs s 4 " + speed);
      Process process = builder.start();
      process.waitFor();
    } catch (IOException var3) {
      var3.printStackTrace();
    } catch (InterruptedException var4) {
      var4.printStackTrace();
    }

  }

  public int changeSpeed(String s) {
    if (s.equalsIgnoreCase("w")) {
      if (CURRENT_SPEED + 20 > 2000) {
        CURRENT_SPEED = 2000;
      } else {
        CURRENT_SPEED += 20;
      }
    } else if (s.equalsIgnoreCase("s")) {
      if (CURRENT_SPEED - 20 < 700) {
        CURRENT_SPEED = 700;
      } else {
        CURRENT_SPEED -= 20;
      }
    } else if (s.equalsIgnoreCase("q")) {
      if (CURRENT_SPEED + 20 > 2000) {
        CURRENT_SPEED = 2000;
      } else {
        CURRENT_SPEED += 20;
      }
    } else if (s.equalsIgnoreCase("e")) {
      if (CURRENT_SPEED - 20 < 700) {
        CURRENT_SPEED = 700;
      } else {
        CURRENT_SPEED -= 20;
      }
    } else {
      if (!s.equalsIgnoreCase(" ")) {
        throw new IllegalArgumentException("It must be one of the s,w,q,e or space commands");
      }

      CURRENT_SPEED = 1500;
    }

    System.out.println(CURRENT_SPEED);
    setSpeed(CURRENT_SPEED);
    return CURRENT_SPEED;
  }
}
