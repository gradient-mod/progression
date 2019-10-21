package gradientmod.progression;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import gradientmod.progression.advancements.AdvancementTriggers;
import gradientmod.progression.capabilities.PlayerProgressCapability;
import gradientmod.progression.commands.GetAgeCommand;
import gradientmod.progression.commands.SetAgeCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Progression.MOD_ID)
public class Progression {
  public static final String MOD_ID = "gradient-progression";

  public static final Logger LOGGER = LogManager.getLogger();

  public Progression() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
  }

  private void setup(final FMLCommonSetupEvent event) {
    LOGGER.info("{} is loading!", MOD_ID);
    AdvancementTriggers.register();
    PlayerProgressCapability.register();
    Network.register();
  }

  private void serverStarting(final FMLServerStartingEvent event) {
    final LiteralArgumentBuilder<CommandSource> root = Commands.literal(MOD_ID)
      .then(GetAgeCommand.build())
      .then(SetAgeCommand.build());

    event.getCommandDispatcher().register(root);
  }

  public static ResourceLocation loc(final String path) {
    return new ResourceLocation(MOD_ID, path);
  }
}
