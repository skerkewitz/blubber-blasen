package de.skerkewitz.blubberblase.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.enora2d.core.game.GameConfig;

public class TitleScreen extends AbstractWorldRenderScreen {

  private final Music music;
  private final Texture texture;
  private final Sprite sprite;
  private final BitmapFont font;
  private SpriteBatch spriteBatch = new SpriteBatch();

  private boolean wasSpacePressed = false;

  public TitleScreen(GameConfig config) {
    super(config, new GameContext());

    texture = new Texture(Gdx.files.internal("png/tile-screen.png"));
    sprite = new Sprite(texture);

    sprite.setSize(256, 180);
    sprite.setFlip(false, true);

    music = Gdx.audio.newMusic(Gdx.files.internal("music/title.mp3"));
    music.setLooping(true);

    Texture texture = new Texture(Gdx.files.internal("font/text.png"), true); // true enables mipmaps
    texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear); // linear filtering in nearest mipmap image
    font = new BitmapFont(Gdx.files.internal("font/text.fnt"), new TextureRegion(texture), true);
  }


  @Override
  public ScreenAction update(int tickTime) {

    if (!wasSpacePressed) {
      if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
        wasSpacePressed = true;
      }
    } else {
      if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
        return ScreenAction.GoLevel;
      }
    }

    return ScreenAction.None;
  }


  @Override
  public void render(int tickTime, Camera camera) {

    spriteBatch.setProjectionMatrix(camera.combined);
    spriteBatch.begin();
    sprite.draw(spriteBatch);

    if (((tickTime / 15) % 2 == 0) || (tickTime / 300 % 2 == 0)) {
      font.draw(spriteBatch, "Press space to start", 128, 182, 0.01f, Align.center, false);
    }
    spriteBatch.end();
  }

  @Override
  public void screenWillDisappear() {
    music.stop();
  }

  @Override
  public void screenDidAppear() {
    music.play();
    wasSpacePressed = false;
  }
}
