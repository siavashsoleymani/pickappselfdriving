//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package service;

import com.pi4j.wiringpi.Gpio;

public class ElectronicSpeedControllerImpl {
  final float PWM_RANGE = 2000.0F;
  int operatingFrequency = Math.round(62.5F);
  final float DISARM_ESC_SPEED = 0.0F;
  final float MIN_ESC_SPEED = 700.0F;
  final float MAX_ESC_SPEED = 2000.0F;
  final float ARM_ESC_SPEED = 1500.0F;
  final float SPEED_RANGE = 1300.0F;
  private int SPEED_CHANGE_MULTIPLE_OF = 10;
  private int SPEED_CHANGE_UNIT = 1;
  final int TIME_INTERVAL = 1000;
  int currentESCSpeed = 0;
  int bcmPinNumber = -1;

  private ElectronicSpeedControllerImpl() {
  }

  public ElectronicSpeedControllerImpl(int bcmPin) {
    this.bcmPinNumber = bcmPin;
    this.initializeESC();
  }

  private void initializeESC() {
    Gpio.wiringPiSetupGpio();
    Gpio.pinMode(this.bcmPinNumber, 2);
    Gpio.pwmSetMode(0);
    Gpio.pwmSetClock(192);
    Gpio.pwmSetRange(2000);
  }

  public void calibrate() {
    try {
      this.fullSpeed();
      System.out.println("Connect your power");
      Thread.sleep(5000L);
      this.minSpeed();
      Thread.sleep(1000L);
    } catch (Exception var2) {
      this.disArm();
    }

    this.disArm();
  }

  public void arm(boolean isCalibrated) {
    if (isCalibrated) {
      try {
        this.disArm();
        Thread.sleep(1000L);
        this.fullSpeed();
        Thread.sleep(1000L);
        this.minSpeed();
        Thread.sleep(2000L);
      } catch (Exception var3) {
        this.disArm();
      }
    }

    this.currentESCSpeed = Math.round(1500.0F);
    this.updateSpeed(this.currentESCSpeed);
  }

  public void testFlight() {
    try {
      System.out.println("Ready for arming");
      Thread.sleep(1000L);
      this.arm(false);
      Thread.sleep(3000L);
      this.minSpeed();
      Thread.sleep(2000L);

      for(int i = 65; i < 100; ++i) {
        this.changeSpeedTo(i + 1);
        Thread.sleep(1000L);
      }

      Thread.sleep(3000L);
      this.disArm();
    } catch (Exception var2) {
      this.disArm();
    }

    this.disArm();
  }

  public void disArm() {
    this.currentESCSpeed = Math.round(0.0F);
    this.updateSpeed(this.currentESCSpeed);
  }

  public void updateSpeed(int speed) {
    Gpio.pwmWrite(this.bcmPinNumber, speed);
  }

  public void fullSpeed() {
    this.currentESCSpeed = Math.round(2000.0F);
    this.updateSpeed(this.currentESCSpeed);
  }

  public void minSpeed() {
    this.currentESCSpeed = Math.round(700.0F);
    this.updateSpeed(this.currentESCSpeed);
  }

  public int calculateSpeed(int percentage) {
    int updatedSpeed = Math.round((float)percentage / 100.0F * 1300.0F);
    return updatedSpeed;
  }

  public void changeSpeedTo(int percentage) {
    this.currentESCSpeed = Math.round(700.0F + (float)this.calculateSpeed(percentage));
    System.out.println("changeSpeedTo=" + this.currentESCSpeed + "  calculateSpeed(percentage) = " + percentage);
    this.updateSpeed(this.currentESCSpeed);
  }

  public void increaseSpeed() {
    this.currentESCSpeed += this.calculateSpeed(this.SPEED_CHANGE_UNIT);
    if ((float)this.currentESCSpeed >= 2000.0F) {
      this.currentESCSpeed = Math.round(2000.0F);
    }

    this.updateSpeed(this.currentESCSpeed);
  }

  public void decreaseSpeed() {
    this.currentESCSpeed -= this.calculateSpeed(this.SPEED_CHANGE_UNIT);
    if ((float)this.currentESCSpeed <= 700.0F) {
      this.currentESCSpeed = Math.round(700.0F);
    }

    this.updateSpeed(this.currentESCSpeed);
  }

  public void increaseSpeedByMultiple() {
    this.currentESCSpeed += this.calculateSpeed(this.SPEED_CHANGE_MULTIPLE_OF);
    if ((float)this.currentESCSpeed >= 2000.0F) {
      this.currentESCSpeed = Math.round(2000.0F);
    }

    this.updateSpeed(this.currentESCSpeed);
  }

  public void decreaseSpeedByMultiple() {
    this.currentESCSpeed -= this.calculateSpeed(this.SPEED_CHANGE_MULTIPLE_OF);
    if ((float)this.currentESCSpeed <= 700.0F) {
      this.currentESCSpeed = Math.round(700.0F);
    }

    this.updateSpeed(this.currentESCSpeed);
  }

  public void setSpeedMultiple(int multiple) {
    this.SPEED_CHANGE_MULTIPLE_OF = multiple;
  }
}
