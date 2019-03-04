package de.skerkewitz.blubberblase.esc;

import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.game.world.World;

/**
 * A system to render all SpriteComponents.
 */
public class InputSystem extends BaseComponentSystem<InputSystem.Tuple, InputSystem.TupleFactory> {

  public InputSystem() {
    super(new TupleFactory());
  }

  public void execute(int tickTime, Tuple t, World world, GameContext context) {
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