package gradientmod.progression.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerProgressProvider implements ICapabilitySerializable<INBT> {
  private final Capability<PlayerProgress> cap = PlayerProgressCapability.PLAYER_PROGRESS_CAPABILITY;
  private final PlayerProgress instance = this.cap.getDefaultInstance();
  private final LazyOptional<PlayerProgress> opt = LazyOptional.of(() -> this.instance);

  @Override
  @Nullable
  public INBT serializeNBT() {
    return this.cap.writeNBT(this.instance, null);
  }

  @Override
  public void deserializeNBT(final INBT nbt) {
    this.cap.readNBT(this.instance, null, nbt);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> capability, @Nullable final Direction facing) {
    return this.cap.orEmpty(capability, this.opt);
  }
}
