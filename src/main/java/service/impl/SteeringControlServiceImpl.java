//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package service.impl;

import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import java.math.BigDecimal;
import service.SteeringControlService;

public class SteeringControlServiceImpl implements SteeringControlService {
  private static final int SERVO_DURATION_MIN = 900;
  private static final int SERVO_DURATION_MAX = 2100;
  private static final int SERVO_STEP_OFFSET = 60;
  private static final int SERVO_DURATION_NEUTRAL = 1380;
  private static int SERVO_CURRENT_VALUE = 1380;
  private final PCA9685GpioProvider provider;

  public SteeringControlServiceImpl() throws Exception {
    BigDecimal frequency = new BigDecimal("48.828");
    BigDecimal frequencyCorrectionFactor = new BigDecimal("1.0578");
    I2CBus bus = I2CFactory.getInstance(1);
    this.provider = new PCA9685GpioProvider(bus, 64, frequency, frequencyCorrectionFactor);
    this.provisionPwmOutputs(this.provider);
    this.provider.reset();
    this.provider.setPwm(PCA9685Pin.PWM_05, 1380);
    this.provider.setPwm(PCA9685Pin.PWM_07, 1380);
  }

  public int changeSteer(String direct) {
    if (direct.equalsIgnoreCase("d")) {
      if (SERVO_CURRENT_VALUE + 60 > 2100) {
        SERVO_CURRENT_VALUE = 2100;
      } else {
        SERVO_CURRENT_VALUE += 60;
      }
    } else if (direct.equalsIgnoreCase("a")) {
      if (SERVO_CURRENT_VALUE - 60 < 900) {
        SERVO_CURRENT_VALUE = 900;
      } else {
        SERVO_CURRENT_VALUE -= 60;
      }
    } else {
      if (!direct.equalsIgnoreCase("f")) {
        throw new IllegalArgumentException("Provide a for left and d for right");
      }

      SERVO_CURRENT_VALUE = 1380;
    }

    this.provider.setPwm(PCA9685Pin.PWM_05, SERVO_CURRENT_VALUE);
    this.provider.setPwm(PCA9685Pin.PWM_07, SERVO_CURRENT_VALUE);
    return SERVO_CURRENT_VALUE;
  }

  private GpioPinPwmOutput[] provisionPwmOutputs(PCA9685GpioProvider gpioProvider) {
    GpioController gpio = GpioFactory.getInstance();
    GpioPinPwmOutput[] myOutputs = new GpioPinPwmOutput[] {
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_00, "Pulse 00"),
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_01, "Pulse 01"),
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_02, "Pulse 02"),
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_03, "Pulse 03"),
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_04, "Pulse 04"),
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_05, "Pulse 05"),
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_06, "Pulse 06"),
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_07, "Pulse 07"),
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_08, "Pulse 08"),
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_09, "Pulse 09"),
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_10, "Always ON"),
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_11, "Always OFF"),
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_12, "Servo pulse MIN"),
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_13, "Servo pulse NEUTRAL"),
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_14, "Servo pulse MAX"),
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_15, "not used")};
    return myOutputs;
  }
}
