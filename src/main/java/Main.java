//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import service.CommandSubscriber;
import service.impl.CommandSenderImpl;
import service.impl.SpeedControlServiceImpl;
import service.impl.SteeringControlServiceImpl;
import service.impl.TCPCommandSubscriberImpl;

public class Main {
  public Main() {
  }

  public static void main(String[] args) throws Exception {
    CommandSubscriber commandSubscriber = TCPCommandSubscriberImpl.getInstance("snapptix.ir", 8081, new CommandSenderImpl(new SpeedControlServiceImpl(), new SteeringControlServiceImpl()));
    commandSubscriber.startReceivingCommands();
    System.in.read();
  }
}
