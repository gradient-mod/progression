package gradientmod.progression.integrations.jei;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import gradientmod.progression.recipes.AgeRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ShapelessAgeCraftingExtension<T extends IRecipe<CraftingInventory> & AgeRecipe> implements ICraftingCategoryExtension {
  protected final T recipe;

  public ShapelessAgeCraftingExtension(final T recipe) {
    this.recipe = recipe;
  }

  @Nullable
  @Override
  public ResourceLocation getRegistryName() {
    return this.recipe.getId();
  }

  @Override
  public void drawInfo(final int recipeWidth, final int recipeHeight, final double mouseX, final double mouseY) {
    GlStateManager.pushMatrix();
    GlStateManager.translatef(103.0f, 42.0f, 0.0f);
    GlStateManager.scalef(0.75f, 0.75f, 0.0f);
    Minecraft.getInstance().textureManager.bindTexture(this.recipe.getAge().getIcon());
    AbstractGui.blit(0, 0, 0, 0.0f, 0.0f, 16, 16, 16, 16);
    GlStateManager.popMatrix();
  }

  @Override
  public List<String> getTooltipStrings(final double mouseX, final double mouseY) {
    if(mouseX >= 103 && mouseX <= 115) {
      if(mouseY >= 42 && mouseY <= 54) {
        final String age = I18n.format("age." + this.recipe.getAge().value());
        final String requirement = I18n.format("jei.age.requirement", age);
        return Lists.newArrayList(requirement);
      }
    }

    return Collections.emptyList();
  }

  @Override
  public void setIngredients(final IIngredients ingredients) {
    ingredients.setInputIngredients(this.recipe.getIngredients());
    ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getRecipeOutput());
  }
}
