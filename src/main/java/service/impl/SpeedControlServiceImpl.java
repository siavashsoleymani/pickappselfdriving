//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package service.impl;

import service.SpeedControlService;

public class SpeedControlServiceImpl implements SpeedControlService {
  private static final ElectronicSpeedControllerImpl electronicSpeedController =
      new ElectronicSpeedControllerImpl(12);

  public SpeedControlServiceImpl() {
  }

  @Override
  public void changeSpeed(String s) {
    if (s.equalsIgnoreCase("w")) {
      electronicSpeedController.increaseSpeed();
    } else if (s.equalsIgnoreCase("s")) {
      electronicSpeedController.decreaseSpeed();
    } else if (s.equalsIgnoreCase("q")) {
      electronicSpeedController.increaseSpeedByMultiple();
    } else if (s.equalsIgnoreCase("e")) {
      electronicSpeedController.decreaseSpeedByMultiple();
    } else {
      if (!s.equalsIgnoreCase(" ")) {
        throw new IllegalArgumentException("It must be one of the s,w,q,e or space commands");
      }
      electronicSpeedController.minSpeed();
      electronicSpeedController.changeSpeedTo(0);
    }
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
