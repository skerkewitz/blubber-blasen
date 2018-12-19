package de.skerkewitz.enora2d.core.net;

import de.skerkewitz.enora2d.core.game.AbstractGame;
import de.skerkewitz.enora2d.core.entity.PlayerMP;
import de.skerkewitz.enora2d.core.net.packets.Packet;
import de.skerkewitz.enora2d.core.net.packets.Packet.PacketTypes;
import de.skerkewitz.enora2d.core.net.packets.Packet00Login;
import de.skerkewitz.enora2d.core.net.packets.Packet01Disconnect;
import de.skerkewitz.enora2d.core.net.packets.Packet02Move;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;

public class GameClient extends Thread {

  public static final Logger logger = LogManager.getLogger(GameClient.class);

  private InetAddress ipAddress;
  private DatagramSocket socket;
  private AbstractGame game;

  public GameClient(AbstractGame game, String ipAddress) {
    this.game = game;
    try {
      this.socket = new DatagramSocket();
      this.ipAddress = InetAddress.getByName(ipAddress);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void run() {
    while (true) {
      byte[] data = new byte[1024];
      DatagramPacket packet = new DatagramPacket(data, data.length);
      try {
        socket.receive(packet);
      } catch (IOException e) {
        e.printStackTrace();
      }
      this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
    }
  }

  private void parsePacket(byte[] data, InetAddress address, int port) {
    String message = new String(data).trim();
    PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
    switch (type) {
      default:
      case INVALID:
        break;
      case LOGIN: {
        var packet = new Packet00Login(data);
        handleLogin(packet, address, port);
        break;
      }
      case DISCONNECT: {
        var packet = new Packet01Disconnect(data);
        logger.info("[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername() + " has left the world...");
        game.level.removePlayerMP(packet.getUsername());
        break;
      }
      case MOVE: {
        var packet = new Packet02Move(data);
        handleMove(packet);
      }
    }
  }

  public void sendData(byte[] data) {
    DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
    try {
      socket.send(packet);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handleLogin(Packet00Login packet, InetAddress address, int port) {
    logger.info("[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername() + " has joined the game...");
    PlayerMP player = new PlayerMP(packet.getX(), packet.getY(), packet.getUsername(), address, port, game);
    game.level.spawnEntity(player);
  }

  private void handleMove(Packet02Move packet) {
    this.game.level.movePlayer(packet.getUsername(), packet.getX(), packet.getY(), packet.getNumSteps(), packet.isMoving(), packet.getMovingDir());
  }
}
