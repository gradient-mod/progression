package gradientmod.progression.advancements;

import net.minecraft.advancements.CriteriaTriggers;

public final class AdvancementTriggers {
  private AdvancementTriggers() { }

  public static final ChangeAgeTrigger CHANGE_AGE = new ChangeAgeTrigger();

  public static void register() {
    CriteriaTriggers.register(CHANGE_AGE);
  }
}
