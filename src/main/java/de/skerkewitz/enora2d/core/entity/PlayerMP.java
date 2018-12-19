package de.skerkewitz.enora2d.core.entity;

import java.net.InetAddress;

import de.skerkewitz.enora2d.core.game.level.Level;
import de.skerkewitz.enora2d.core.input.InputHandler;
import de.skerkewitz.enora2d.core.net.GameClientProvider;

public class PlayerMP extends Player {

    public InetAddress ipAddress;
    public int port;

    public PlayerMP(int x, int y, InputHandler input, String username, InetAddress ipAddress, int port, GameClientProvider gameClientProvider) {
        super(x, y, input, username, gameClientProvider);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public PlayerMP(int x, int y, String username, InetAddress ipAddress, int port, GameClientProvider gameClientProvider) {
        super(x, y, null, username, gameClientProvider);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void tick(Level level, int tickTime) {
        super.tick(level, tickTime);
    }
}
