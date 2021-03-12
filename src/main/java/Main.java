//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import service.CommandSubscriber;
import service.DistanceMeasureService;
import service.impl.CommandSenderImpl;
import service.impl.DistanceMeasureServiceImpl;
import service.impl.SpeedControlServiceImpl;
import service.impl.SteeringControlServiceImpl;
import service.impl.TCPCommandSubscriberImpl;

public class Main {
  public Main() {
  }

  public static void main(String[] args) throws Exception {
    ExecutorService executorService = Executors.newCachedThreadPool();
    SpeedControlServiceImpl speedControlService = new SpeedControlServiceImpl();
    SteeringControlServiceImpl steeringControlService = new SteeringControlServiceImpl();
    CommandSubscriber commandSubscriber = TCPCommandSubscriberImpl.getInstance("snapptix.ir", 8081,
        new CommandSenderImpl(speedControlService, steeringControlService));
    DistanceMeasureService distanceMeasureService = new DistanceMeasureServiceImpl();
    executorService.submit(commandSubscriber::startReceivingCommands);
    executorService.submit(() -> {
      while (true) {
        Thread.sleep(50);
        double v = distanceMeasureService.measureDistance();
        if (v < 100 && speedControlService.CURRENT_SPEED>1500) {
          System.out.println("Thread in distance: " + Thread.currentThread().getId());
          speedControlService.emergencyBreak();
        }
      }
    });
    System.in.read();
  }
}
