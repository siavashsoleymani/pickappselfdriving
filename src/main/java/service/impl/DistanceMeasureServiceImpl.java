//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package service.impl;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import service.DistanceMeasureService;

public class DistanceMeasureServiceImpl implements DistanceMeasureService {
  private final GpioPinDigitalOutput sensorTriggerPin;
  private final GpioPinDigitalInput sensorEchoPin;

  public DistanceMeasureServiceImpl() {
    GpioController gpio = GpioFactory.getInstance();
    this.sensorTriggerPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);
    this.sensorEchoPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_DOWN);
  }

  public double measureDistance() throws InterruptedException {
    this.sensorTriggerPin.high();
    Thread.sleep(0L);
    this.sensorTriggerPin.low();

    while(this.sensorEchoPin.isLow()) {
    }

    long startTime = System.nanoTime();

    while(this.sensorEchoPin.isHigh()) {
    }

    long endTime = System.nanoTime();
    return (double)(endTime - startTime) / 1000.0D / 2.0D / 29.1D;
  }
}
