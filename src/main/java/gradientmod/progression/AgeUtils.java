package gradientmod.progression;

import gradientmod.progression.capabilities.PlayerProgress;
import gradientmod.progression.recipes.RecipeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public final class AgeUtils {
  @CapabilityInject(PlayerProgress.class)
  private static Capability<PlayerProgress> CAP;

  private AgeUtils() { }

  public static Age getAgeNear(final Entity entity, final float distanceSquared) {
    Age age = Age.AGE1;

    for(final PlayerEntity player : entity.world.getPlayers()) {
      if(entity.getDistanceSq(player) <= distanceSquared) {
        final Age playerAge = player
          .getCapability(CAP)
          .map(PlayerProgress::getAge)
          .orElse(Age.AGE1);

        if(playerAge.compareTo(age) > 0) {
          age = playerAge;
        }
      }
    }

    return age;
  }

  public static Age getPlayerAge(final Entity player) {
    return player
      .getCapability(CAP)
      .map(PlayerProgress::getAge)
      .orElse(Age.AGE1);
  }

  public static boolean playerMeetsAgeRequirement(final Entity player, final Age age) {
    return player
      .getCapability(CAP)
      .map(progress -> progress.meetsAgeRequirement(age))
      .orElse(false);
  }

  public static boolean playerMeetsAgeRequirement(final CraftingInventory inv, final Age age) {
    return RecipeUtils
      .findPlayerFromInv(inv)
      .map(player -> playerMeetsAgeRequirement(player, age))
      .orElse(false);
  }
}
