package gradientmod.progression.recipes;

import gradientmod.progression.Progression;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Progression.MOD_ID)
public final class RecipeSerializers {
  private RecipeSerializers() { }

  public static final IRecipeSerializer<ShapedAgeRecipe>    SHAPED    = null;
  public static final IRecipeSerializer<ShapelessAgeRecipe> SHAPELESS = null;

  @Mod.EventBusSubscriber(modid = Progression.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
  public static final class Registration {
    private Registration() { }

    @SubscribeEvent
    public static void onRegistration(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
      Progression.LOGGER.info("Registering recipe serializers...");

      final IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();

      registry.register(new ShapedAgeRecipe.Serializer().setRegistryName(Progression.loc("shaped")));
      registry.register(new ShapelessAgeRecipe.Serializer().setRegistryName(Progression.loc("shapeless")));
    }
  }
}
