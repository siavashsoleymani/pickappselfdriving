//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import service.CommandSubscriber;
import service.DistanceMeasureService;
import service.SpeedControlService;
import service.SteeringControlService;
import service.impl.CommandSenderImpl;
import service.impl.DistanceMeasureServiceImpl;
import service.impl.ElectronicSpeedControllerImpl;
import service.impl.SpeedControlServiceImpl;
import service.impl.SteeringControlServiceImpl;
import service.impl.TCPCommandSubscriberImpl;

public class Main {
  public static ExecutorService executorService =
      Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  public static void main(String[] args) throws Exception {
    ElectronicSpeedControllerImpl electronicSpeedController = new ElectronicSpeedControllerImpl(12);
    electronicSpeedController.calibrate();

    SpeedControlService speedControlService = new SpeedControlServiceImpl();
    SteeringControlService steeringControlService = new SteeringControlServiceImpl();
    CommandSubscriber commandSubscriber = TCPCommandSubscriberImpl.getInstance("snapptix.ir", 8081,
        new CommandSenderImpl(speedControlService, steeringControlService), executorService);
    DistanceMeasureService distanceMeasureService = new DistanceMeasureServiceImpl();

    executorService.submit(commandSubscriber::startReceivingCommands);
    executorService.submit(() -> {
      while (true) {
        Thread.sleep(50);
        double distance = distanceMeasureService.measureDistance();
        if (distance < 100 && electronicSpeedController.getCurrentESCSpeed() >
            electronicSpeedController.getMIN_ESC_SPEED()) {
          System.out.println("Thread in distance: " + Thread.currentThread().getId());
          speedControlService.emergencyBreak();
        }
      }
    });
  }
}
