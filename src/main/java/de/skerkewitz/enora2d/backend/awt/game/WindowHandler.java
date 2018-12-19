package de.skerkewitz.enora2d.backend.awt.game;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowHandler implements WindowListener {

  private final AwtGame game;

  public WindowHandler(AwtGame game) {
    this.game = game;
    this.game.frame.addWindowListener(this);
  }

  @Override
  public void windowActivated(WindowEvent event) {
  }

  @Override
  public void windowClosed(WindowEvent event) {
  }

  @Override
  public void windowClosing(WindowEvent event) {

  }

  @Override
  public void windowDeactivated(WindowEvent event) {
  }

  @Override
  public void windowDeiconified(WindowEvent event) {
  }

  @Override
  public void windowIconified(WindowEvent event) {
  }

  @Override
  public void windowOpened(WindowEvent event) {
  }

}
