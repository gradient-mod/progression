package gradientmod.progression.capabilities;

import gradientmod.progression.Age;
import gradientmod.progression.Progression;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Progression.MOD_ID)
public final class PlayerProgressEvents {
  @CapabilityInject(PlayerProgress.class)
  private static Capability<PlayerProgress> CAP;

  static Int2ObjectMap<Age> deferredAgeUpdates = new Int2ObjectAVLTreeMap<>();

  private PlayerProgressEvents() { }

  @SubscribeEvent
  public static void attachOnSpawn(final AttachCapabilitiesEvent<Entity> event) {
    if(event.getObject() instanceof PlayerEntity) {
      event.addCapability(PlayerProgressCapability.ID, new PlayerProgressProvider());
    }
  }

  @SubscribeEvent
  public static void onSpawn(final EntityJoinWorldEvent event) {
    if(event.getEntity() instanceof PlayerEntity) {
      if(event.getWorld().isRemote) {
        final Age age = deferredAgeUpdates.remove(event.getEntity().getEntityId());

        if(age != null) {
          event
            .getEntity()
            .getCapability(CAP)
            .ifPresent(progress -> progress.setAge(age));
        }

        return;
      }

      PlayerProgressUpdatePacket.send((ServerPlayerEntity)event.getEntity());
    }
  }

  @SubscribeEvent
  public static void playerClone(final PlayerEvent.Clone event) {
    if(event.isWasDeath()) {
      final LazyOptional<PlayerProgress> newProgressOpt = event.getPlayer().getCapability(CAP);
      final LazyOptional<PlayerProgress> oldProgressOpt = event.getOriginal().getCapability(CAP);

      oldProgressOpt.ifPresent(oldProgress -> newProgressOpt.ifPresent(newProgress -> {
        final INBT nbt = CAP.writeNBT(oldProgress, null);
        CAP.readNBT(newProgress, null, nbt);
      }));
    }
  }

  public static void deferAgeUpdate(final int id, final Age age) {
    deferredAgeUpdates.put(id, age);
  }
}
