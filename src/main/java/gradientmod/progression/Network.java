package gradientmod.progression;

import gradientmod.progression.capabilities.PlayerProgressUpdatePacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class Network {
  private Network() { }

  private static final String PROTOCOL_VERSION = "1";
  public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
    .named(new ResourceLocation(Progression.MOD_ID, "main_channel"))
    .clientAcceptedVersions(PROTOCOL_VERSION::equals)
    .serverAcceptedVersions(PROTOCOL_VERSION::equals)
    .networkProtocolVersion(() -> PROTOCOL_VERSION)
    .simpleChannel();

  private static int id;

  static void register() {
    CHANNEL.registerMessage(id++, PlayerProgressUpdatePacket.class, PlayerProgressUpdatePacket::encode, PlayerProgressUpdatePacket::decode, PlayerProgressUpdatePacket::handle);
  }
}
