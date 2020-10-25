//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package service;

import java.io.IOException;

public interface CommandSubscriber {
  void connect();

  void disconnect() throws IOException;

  void startReceivingCommands();

  void stopReceivingCommands();
}
