package de.skerkewitz.blubberblase.esc.systems;

import de.skerkewitz.blubberblase.esc.component.InputComponent;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.ecs.system.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.system.ComponentSystem;
import de.skerkewitz.enora2d.core.game.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A system to render all SpriteComponents.
 */
public class InputSystem extends BaseComponentSystem<InputSystem.Tuple, InputSystem.TupleFactory> {

  private static final Logger logger = LogManager.getLogger(InputSystem.class);

  public InputSystem() {
    super(new TupleFactory());
  }

  public void execute(int tickTime, Tuple t, Level level) {
    InputComponent inputComponent = t.inputComponent;

    inputComponent.shoot = inputComponent.inputHandler.getFireA().isPressed();
    inputComponent.jump = inputComponent.inputHandler.getUp().isPressed();

    if (inputComponent.inputHandler.getLeft().isPressed()) {
      inputComponent.horizontal = -1;
    } else if (inputComponent.inputHandler.getRight().isPressed()) {
      inputComponent.horizontal = +1;
    } else {
      inputComponent.horizontal = 0;
    }
  }

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    InputComponent inputComponent;

    Tuple(Entity entity, InputComponent inputComponent) {
      this.entity = entity;
      this.inputComponent = inputComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {

    public Tuple map(Entity entity) {
      var inputComponent = entity.getComponent(InputComponent.class);
      if (inputComponent != null) {
        return new Tuple(entity, inputComponent);
      } else {
        return null;
      }
    }
  }


}