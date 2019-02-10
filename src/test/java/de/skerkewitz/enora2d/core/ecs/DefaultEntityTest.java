package de.skerkewitz.enora2d.core.ecs;

import de.skerkewitz.enora2d.core.ecs.component.Component;
import de.skerkewitz.enora2d.core.ecs.component.TransformComponent;
import de.skerkewitz.enora2d.core.ecs.entity.DefaultEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultEntityTest {

  @Test
  public void testAddComponent() {

    var entity = new DefaultEntity();
    var result = entity.addComponent(new TransformComponent());

    assertTrue(result, "Should have modified the component list.");
  }

  @Test
  public void testGetComponent() {

    var entity = new DefaultEntity();
    entity.addComponent(new TransformComponent());
    entity.addComponent(new TestComponent());

    var component = entity.getComponent(TestComponent.class);

    assertNotNull(component, "Should find the component.");
  }

  public static class TestComponent implements Component {
    // nothing
  }
}