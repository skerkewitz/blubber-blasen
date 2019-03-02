package de.skerkewitz.blubberblase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import de.skerkewitz.blubberblase.esc.component.*;
import de.skerkewitz.blubberblase.util.TimeUtil;
import de.skerkewitz.enora2d.core.ecs.system.MovementSystem;
import de.skerkewitz.enora2d.core.game.GameConfig;
import de.skerkewitz.enora2d.core.game.world.StaticMapContent;
import de.skerkewitz.enora2d.core.game.world.World;

public class MainWorld extends World {


  private MovementSystem movementSystem = new MovementSystem();
  private AiBubbleSystem aiBubbleSystem = new AiBubbleSystem();
  private AiEnemySystem aiEnemySystem = new AiEnemySystem();
  private LifeTimeSystem lifeTimeSystem = new LifeTimeSystem();
  private AnimationSystem animationSystem = new AnimationSystem();
  private GroundDataSystemSystem groundDataSystemSystem = new GroundDataSystemSystem();
  private CollisionSystem collisionComponent = new CollisionSystem();
  private PlayerSystem playerSystem = new PlayerSystem();
  private ThrownEnemySystem thrownEnemySystem = new ThrownEnemySystem();
  private BonusItemSystem bonusItemSystem = new BonusItemSystem();

  private InputSystem inputSystem = new InputSystem();

  private Sound hurryUp = Gdx.audio.newSound(Gdx.files.internal("sfx/hurry-up.mp3"));

  private boolean hurryMode = false;

  /**
   * How many seconds have the player until hurry up appears and monster turn angry.
   */
  public int hurryUpTimeLimitInSeconds = 30;
  public int hurryUpTimeLimitInFrameCount = TimeUtil.secondsToTickTime(hurryUpTimeLimitInSeconds);

  public MainWorld(GameConfig config, StaticMapContent staticMapContent, int frameCount) {
    super(staticMapContent, frameCount);
  }

  public void tick(int tickTime, GameContext context) {

    super.tick(tickTime, context);

    if (getWorldFrameCount(tickTime) > hurryUpTimeLimitInFrameCount && !hurryMode) {
      hurryUp.play();
      hurryMode = true;

      entityContainer.stream().filter(entity -> entity.hasComponent(EnemyComponent.class)).forEach(entity -> entity.getComponent(EnemyComponent.class).isAngry = true);
    }

    /* Update life time of entities and purge dead entities. */
    lifeTimeSystem.update(tickTime, this, entityContainer.stream(), context);
    entityContainer.purgeExpired();

    collisionComponent.update(tickTime, this, entityContainer.stream(), context);

    groundDataSystemSystem.update(tickTime, this, entityContainer.stream(), context);
    inputSystem.update(tickTime, this, entityContainer.stream(), context);

    aiBubbleSystem.update(tickTime, this, entityContainer.stream(), context);
    aiEnemySystem.update(tickTime, this, entityContainer.stream(), context);
    movementSystem.update(tickTime, entityContainer.stream());

    playerSystem.update(tickTime, this, entityContainer.stream(), context);
    thrownEnemySystem.update(tickTime, this, entityContainer.stream(), context);
    bonusItemSystem.update(tickTime, this, entityContainer.stream(), context);

    animationSystem.update(tickTime, this, entityContainer.stream(), context);
  }
}
