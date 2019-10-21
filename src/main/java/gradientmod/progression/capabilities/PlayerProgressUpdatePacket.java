package gradientmod.progression.capabilities;

import gradientmod.progression.Age;
import gradientmod.progression.AgeUtils;
import gradientmod.progression.Network;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class PlayerProgressUpdatePacket {
  public static void send(final ServerPlayerEntity player) {
    Network.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PlayerProgressUpdatePacket(player));
  }

  private final int playerId;
  private final Age playerAge;

  public PlayerProgressUpdatePacket(final PlayerEntity player) {
    this(player.getEntityId(), AgeUtils.getPlayerAge(player));
  }

  public PlayerProgressUpdatePacket(final int playerId, final Age playerAge) {
    this.playerId = playerId;
    this.playerAge = playerAge;
  }

  public static void encode(final PlayerProgressUpdatePacket packet, final PacketBuffer buffer) {
    buffer.writeVarInt(packet.playerId);
    buffer.writeVarInt(packet.playerAge.value());
  }

  public static PlayerProgressUpdatePacket decode(final PacketBuffer buffer) {
    return new PlayerProgressUpdatePacket(buffer.readVarInt(), Age.get(buffer.readVarInt()));
  }

  public static void handle(final PlayerProgressUpdatePacket packet, final Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      final Entity entity = Minecraft.getInstance().world.getEntityByID(packet.playerId);

      if(entity == null) {
        PlayerProgressEvents.deferAgeUpdate(packet.playerId, packet.playerAge);
        return;
      }

      entity
        .getCapability(PlayerProgressCapability.PLAYER_PROGRESS_CAPABILITY, null)
        .ifPresent(progress -> progress.setAge(packet.playerAge));
    });
  }
}
