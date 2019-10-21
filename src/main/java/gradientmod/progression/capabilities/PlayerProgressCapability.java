package gradientmod.progression.capabilities;

import gradientmod.progression.Age;
import gradientmod.progression.Progression;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public final class PlayerProgressCapability {
  private PlayerProgressCapability() { }

  public static final ResourceLocation ID = Progression.loc("progress");

  @CapabilityInject(PlayerProgress.class)
  public static Capability<PlayerProgress> PLAYER_PROGRESS_CAPABILITY;

  public static void register() {
    CapabilityManager.INSTANCE.register(PlayerProgress.class, new Capability.IStorage<PlayerProgress>() {
      @Override
      public INBT writeNBT(final Capability<PlayerProgress> capability, final PlayerProgress instance, final Direction side) {
        final CompoundNBT tags = new CompoundNBT();

        tags.putString("age", instance.getAge().name());

        return tags;
      }

      @Override
      public void readNBT(final Capability<PlayerProgress> capability, final PlayerProgress instance, final Direction side, final INBT base) {
        if(!(base instanceof CompoundNBT)) {
          return;
        }

        final CompoundNBT nbt = (CompoundNBT)base;

        if(nbt.contains("age")) {
          instance.setAge(Age.valueOf(nbt.getString("age")));
        } else {
          instance.setAge(Age.AGE1);
        }
      }
    }, PlayerProgress::new);
  }
}
