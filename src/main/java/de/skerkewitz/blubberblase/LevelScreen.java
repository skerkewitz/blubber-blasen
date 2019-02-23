package de.skerkewitz.blubberblase;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.blubberblase.esc.systems.RenderDebugSystem;
import de.skerkewitz.blubberblase.esc.systems.RenderSpriteSystem;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.entity.MoveableLegacyEntity;
import de.skerkewitz.enora2d.core.game.GameConfig;
import de.skerkewitz.enora2d.core.game.Screen;
import de.skerkewitz.enora2d.core.game.level.World;
import de.skerkewitz.enora2d.core.game.level.tiles.BasicTile;
import de.skerkewitz.enora2d.core.gfx.GdxTextureContainer;
import de.skerkewitz.enora2d.core.gfx.ImageDataContainer;
import de.skerkewitz.enora2d.core.input.GdxKeyboardInputHandler;
import de.skerkewitz.enora2d.core.input.InputHandler;

import java.io.IOException;

class LevelScreen implements Screen {

  private final GameConfig config;
  SpriteBatch spriteBatch = new SpriteBatch();
  private World world;
  private ImageDataContainer imageDataContainer = new ImageDataContainer();
  private GdxTextureContainer gdxTextureContainer = new GdxTextureContainer();

  private RenderSpriteSystem renderSpriteSystem = new RenderSpriteSystem();
  private RenderDebugSystem renderDebugSystem = new RenderDebugSystem(null);

  public LevelScreen(GameConfig config, World world) {
    this.config = config;
    this.world = world;

    this.world = new MainWorld(config);

//    Controller first = Controllers.getControllers().first();
//    InputHandler handler = first == null ? new GdxKeyboardInputHandler() : new GdxGamepadInputHandler(first);
    InputHandler handler = new GdxKeyboardInputHandler();

    this.world.addEntity(EntityFactory.spawnBubblun(handler));
    this.world.addEntity(EntityFactory.spawnBubble(0, new Point2i(8 * 8, 24 * 8), MoveableLegacyEntity.MoveDirection.Right));
    this.world.addEntity(EntityFactory.spawnZenChan());
  }

  @Override
  public void update(int tickTime) {
    world.tick(tickTime);
  }

  @Override
  public void render(int tickTime, Camera camera) throws IOException {

    renderStaticWorld(tickTime, camera);

    /* Render all the entities. */
    renderSpriteSystem.applyActiveCamera(camera);
    renderSpriteSystem.update(tickTime, world, world.getEntityContainer().stream());

    if (config.cmd.hasOption("showbbox")) {
      renderDebugSystem.update(tickTime, world, world.getEntityContainer().stream());
    }
  }

  private void renderStaticWorld(int tickTime, Camera camera) throws IOException {

    spriteBatch.setProjectionMatrix(camera.combined);
    spriteBatch.begin();
    for (int y = 0; y < world.numVerticalTiles; y++) {
      for (int x = 0; x < world.numHorizontalTiles; x++) {
        final BasicTile tile = (BasicTile) world.backgroundLayer.getTile(x, y);

        int xTile = tile.getTileId() % 32;
        int yTile = tile.getTileId() / 32;

        Sprite sprite = gdxTextureContainer.getTextureNamedResourceAndPalette(Ressources.SpriteSheet, tile.getTileColour(), imageDataContainer);
        sprite.setSize(8, 8);
        sprite.setPosition(x * 8, y * 8);
        sprite.setRegion(xTile * 8, yTile * 8, 8, 8);
        sprite.setFlip(false, true);
        sprite.draw(spriteBatch);
      }
    }
    spriteBatch.end();
  }
}
