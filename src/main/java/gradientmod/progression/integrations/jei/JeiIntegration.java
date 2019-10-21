package gradientmod.progression.integrations.jei;

import gradientmod.progression.Progression;
import gradientmod.progression.recipes.ShapedAgeRecipe;
import gradientmod.progression.recipes.ShapelessAgeRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JeiIntegration implements IModPlugin {
  @Override
  public ResourceLocation getPluginUid() {
    return Progression.loc(Progression.MOD_ID);
  }

  @Override
  public void registerVanillaCategoryExtensions(final IVanillaCategoryExtensionRegistration registration) {
    registration.getCraftingCategory().addCategoryExtension(ShapedAgeRecipe.class, ShapedAgeCraftingExtension::new);
    registration.getCraftingCategory().addCategoryExtension(ShapelessAgeRecipe.class, ShapelessAgeCraftingExtension::new);
  }
}
