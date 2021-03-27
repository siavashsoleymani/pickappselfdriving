//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package service.impl;

import service.CommandSenderService;
import service.SpeedControlService;
import service.SteeringControlService;

public class CommandSenderImpl implements CommandSenderService {
  private final SpeedControlService speedControlService;
  private final SteeringControlService steeringControlService;

  public CommandSenderImpl(SpeedControlService speedControlService,
                           SteeringControlService steeringControlService) {
    this.speedControlService = speedControlService;
    this.steeringControlService = steeringControlService;
  }

  public void sendCommand(String command) {
    if (!command.equalsIgnoreCase("w") && !command.equalsIgnoreCase("s") &&
        !command.equalsIgnoreCase(" ") && !command.equalsIgnoreCase("q") &&
        !command.equalsIgnoreCase("e")) {
      if (!command.equalsIgnoreCase("a") && !command.equalsIgnoreCase("d") &&
          !command.equalsIgnoreCase("f")) {
        throw new IllegalArgumentException("Unknown commad received");
      }

      this.steeringControlService.changeSteer(command);
    } else {
      this.speedControlService.changeSpeed(command);
    }

  }
}
