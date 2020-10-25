//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import service.CommandSenderService;
import service.CommandSubscriber;

public class TCPCommandSubscriberImpl implements CommandSubscriber {
  private static Socket socket;
  private final String host;
  private final int port;
  private static volatile boolean receiving = false;
  private static CommandSubscriber COMMAND_SUBSCRIBER = null;
  private final CommandSenderService commandSenderService;

  private TCPCommandSubscriberImpl(String host, int port, CommandSenderService commandSenderService) {
    this.host = host;
    this.port = port;
    this.commandSenderService = commandSenderService;
  }

  public static CommandSubscriber getInstance(String host, int port, CommandSenderService commandSenderService) {
    if (COMMAND_SUBSCRIBER == null) {
      COMMAND_SUBSCRIBER = new TCPCommandSubscriberImpl(host, port, commandSenderService);
    }

    return COMMAND_SUBSCRIBER;
  }

  public void connect() {
    try {
      socket = new Socket(this.host, this.port);
      this.sendDeviceId();
      this.startHeartBeat();
      if (receiving) {
        this.startReceivingCommands();
      }
    } catch (IOException var4) {
      System.out.println("cant connect to server :" + var4.getMessage());

      try {
        Thread.sleep(2000L);
      } catch (InterruptedException var3) {
        var3.printStackTrace();
      }

      this.connect();
    }

  }

  private void sendDeviceId() throws IOException {
    OutputStream outputStream = socket.getOutputStream();
    outputStream.write("2001\n".getBytes());
    outputStream.flush();
  }

  public void disconnect() throws IOException {
    if (socket.isConnected()) {
      receiving = false;
      socket.close();
      socket = null;
    }

  }

  public void startReceivingCommands() {
    if (Objects.isNull(socket) || !socket.isConnected()) {
      this.connect();
    }

    receiving = true;
    CompletableFuture.runAsync(() -> {
      while(true) {
        while(true) {
          try {
            Thread.sleep(0L, 500);
            if (!receiving) {
              System.out.println("Stopping receiving commands");
              return;
            }

            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            if (bufferedReader.ready()) {
              String message = bufferedReader.readLine();
              System.out.println("MESSAGE IS : " + message);
              if (!message.startsWith("h")) {
                String[] messages = message.split(",");
                String command = messages[0];
                long timestamp = Long.parseLong(messages[1]);
                long latency = System.currentTimeMillis() - timestamp;
                System.out.println("Latency is: " + latency);
                this.commandSenderService.sendCommand(command);
              }
            }
          } catch (Exception var10) {
            var10.printStackTrace();
          }
        }
      }
    });
  }

  public void stopReceivingCommands() {
    receiving = false;
  }

  private void startHeartBeat() {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.submit(() -> {
      while(true) {
        try {
          OutputStream outputStream = socket.getOutputStream();
          outputStream.write("h\n".getBytes());
          outputStream.flush();
          Thread.sleep(5000L);
        } catch (Exception var2) {
          var2.printStackTrace();
          this.connect();
        }
      }
    });
  }
}
