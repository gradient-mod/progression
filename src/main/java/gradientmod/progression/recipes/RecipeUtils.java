package gradientmod.progression.recipes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraftforge.common.util.LazyOptional;

public final class RecipeUtils {
  private RecipeUtils() { }

  public static LazyOptional<PlayerEntity> findPlayerFromInv(final CraftingInventory inv) {
    final Container container = inv.field_70465_c;

    if(container instanceof PlayerContainer) {
      return LazyOptional.of(() -> ((PlayerContainer)container).player);
    }

    if(container instanceof WorkbenchContainer) {
      return LazyOptional.of(() -> ((WorkbenchContainer)container).player);
    }

    // Can't find player
    return LazyOptional.empty();
  }
}
