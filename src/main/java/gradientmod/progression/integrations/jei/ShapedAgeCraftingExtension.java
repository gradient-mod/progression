package gradientmod.progression.integrations.jei;

import gradientmod.progression.recipes.ShapedAgeRecipe;
import net.minecraftforge.common.util.Size2i;

import javax.annotation.Nullable;

public class ShapedAgeCraftingExtension extends ShapelessAgeCraftingExtension<ShapedAgeRecipe> {
  public ShapedAgeCraftingExtension(final ShapedAgeRecipe recipe) {
    super(recipe);
  }

  @Nullable
  @Override
  public Size2i getSize() {
    return new Size2i(this.recipe.getRecipeWidth(), this.recipe.getRecipeHeight());
  }
}
