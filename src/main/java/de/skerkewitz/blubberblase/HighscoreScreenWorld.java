package de.skerkewitz.blubberblase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import de.skerkewitz.blubberblase.esc.*;
import de.skerkewitz.blubberblase.util.TimeUtil;
import de.skerkewitz.enora2d.core.ecs.common.LifeTimeSystem;
import de.skerkewitz.enora2d.core.ecs.common.SoundSystem;
import de.skerkewitz.enora2d.core.ecs.common.TransformAnimatorSystem;
import de.skerkewitz.enora2d.core.game.GameConfig;
import de.skerkewitz.enora2d.core.game.world.World;

public class HighscoreScreenWorld extends World {

  private final GameConfig config;
  /**
   * How many seconds have the player until hurry up appears and monster turn angry.
   */
  public int hurryUpTimeLimitInSeconds = 30;
  public int hurryUpTimeLimitInFrameCount = TimeUtil.secondsToTickTime(hurryUpTimeLimitInSeconds);
  private AirflowSystem airflowSystem = new AirflowSystem();
  private AiBubbleSystem aiBubbleSystem = new AiBubbleSystem();
  private AiEnemySystem aiEnemySystem = new AiEnemySystem();
  private LifeTimeSystem lifeTimeSystem = new LifeTimeSystem();
  private AnimationSystem animationSystem = new AnimationSystem();
  private GroundDataSystemSystem groundDataSystemSystem = new GroundDataSystemSystem();
  private CollisionSystem collisionComponent = new CollisionSystem();
  private PlayerSystem playerSystem = new PlayerSystem();
  private ThrownEnemySystem thrownEnemySystem = new ThrownEnemySystem();
  private BonusItemSystem bonusItemSystem = new BonusItemSystem();
  private TargetMoveSystem targetMoveSystem = new TargetMoveSystem();
  private SoundSystem soundSystem = new SoundSystem();
  private TransformAnimatorSystem transformAnimatorSystem = new TransformAnimatorSystem();
  private RenderSpriteAlphaAnimatorSystem renderSpriteAlphaAnimatorSystem = new RenderSpriteAlphaAnimatorSystem();
  private InputSystem inputSystem = new InputSystem();
  private Sound hurryUp = Gdx.audio.newSound(Gdx.files.internal("sfx/hurry-up.mp3"));
  private boolean hurryMode = false;

  public HighscoreScreenWorld(GameConfig config, int frameCount) {
    super(null, frameCount);
    this.config = config;
  }

  public void tick(int tickTime, GameContext context) {

    super.tick(tickTime, context);

//    if (getWorldFrameCount(tickTime) > hurryUpTimeLimitInFrameCount && !hurryMode && config.noNextLevel) {
//      hurryUp.play();
//      hurryMode = true;
//
//      entityContainer.stream().filter(entity -> entity.hasComponent(EnemyComponent.class)).forEach(entity -> entity.getComponent(EnemyComponent.class).isAngry = true);
//    }
//
//    /* Update life time of entities and purge dead entities. */
//    lifeTimeSystem.update(tickTime, this, entityContainer.stream(), context);
//    entityContainer.purgeExpired();
//
//    boolean isTargetMoving = targetMoveSystem.update(tickTime, this, entityContainer.stream(), context);
//
//
//    collisionComponent.update(tickTime, this, entityContainer.stream(), context);
//
//    groundDataSystemSystem.update(tickTime, this, entityContainer.stream(), context);
//    inputSystem.update(tickTime, this, entityContainer.stream(), context);
//
//    aiBubbleSystem.update(tickTime, this, entityContainer.stream(), context);
//    airflowSystem.update(tickTime, this, entityContainer.stream(), context);
//
//    if (!isTargetMoving) {
//      aiEnemySystem.update(tickTime, this, entityContainer.stream(), context);
//      playerSystem.update(tickTime, this, entityContainer.stream(), context);
//    }
//
//    thrownEnemySystem.update(tickTime, this, entityContainer.stream(), context);
//    bonusItemSystem.update(tickTime, this, entityContainer.stream(), context);
//
//    animationSystem.update(tickTime, this, entityContainer.stream(), context);
//    soundSystem.update(tickTime, this, entityContainer.stream(), context);
//
//    transformAnimatorSystem.update(tickTime, this, entityContainer.stream(), context);
//    renderSpriteAlphaAnimatorSystem.update(tickTime, this, entityContainer.stream(), context);
//
//    /* Update player score. */
//    RenderTextComponent component = this.getPlayerScoreEntity().getComponent(RenderTextComponent.class);
//    component.text = StringUtils.center("" + context.scorePlayer1, 10);
  }
}
