package gradientmod.progression.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import gradientmod.progression.Age;
import gradientmod.progression.AgeUtils;
import gradientmod.progression.Progression;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChangeAgeTrigger implements ICriterionTrigger<ChangeAgeTrigger.Instance> {
  private static final ResourceLocation ID = Progression.loc("changed_age");
  private final Map<PlayerAdvancements, Listeners> listeners = Maps.newHashMap();

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public void addListener(final PlayerAdvancements playerAdvancements, final ICriterionTrigger.Listener<ChangeAgeTrigger.Instance> listener) {
    ChangeAgeTrigger.Listeners listeners = this.listeners.get(playerAdvancements);

    if(listeners == null) {
      listeners = new ChangeAgeTrigger.Listeners(playerAdvancements);
      this.listeners.put(playerAdvancements, listeners);
    }

    listeners.add(listener);
  }

  @Override
  public void removeListener(final PlayerAdvancements playerAdvancements, final ICriterionTrigger.Listener<ChangeAgeTrigger.Instance> listener) {
    final ChangeAgeTrigger.Listeners listeners = this.listeners.get(playerAdvancements);

    if(listeners != null) {
      listeners.remove(listener);

      if(listeners.isEmpty()) {
        this.listeners.remove(playerAdvancements);
      }
    }
  }

  @Override
  public void removeAllListeners(final PlayerAdvancements playerAdvancements) {
    this.listeners.remove(playerAdvancements);
  }

  /**
   * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
   */
  @Override
  public ChangeAgeTrigger.Instance deserializeInstance(final JsonObject json, final JsonDeserializationContext context) {
    final Age age = Age.get(JSONUtils.getInt(json, "age"));
    return new ChangeAgeTrigger.Instance(age);
  }

  public void trigger(final ServerPlayerEntity player) {
    final ChangeAgeTrigger.Listeners listeners = this.listeners.get(player.getAdvancements());

    if(listeners != null) {
      listeners.trigger(player);
    }
  }

  public static class Instance implements ICriterionInstance {
    private final Age age;

    public Instance(final Age age) {
      this.age = age;
    }

    @Override
    public ResourceLocation getId() {
      return ChangeAgeTrigger.ID;
    }

    public boolean test(final PlayerEntity player) {
      return AgeUtils.playerMeetsAgeRequirement(player, this.age);
    }
  }

  static class Listeners {
    private final PlayerAdvancements playerAdvancements;
    private final Set<Listener<Instance>> listeners = Sets.newHashSet();

    public Listeners(final PlayerAdvancements playerAdvancements) {
      this.playerAdvancements = playerAdvancements;
    }

    public boolean isEmpty() {
      return this.listeners.isEmpty();
    }

    public void add(final ICriterionTrigger.Listener<ChangeAgeTrigger.Instance> listener) {
      this.listeners.add(listener);
    }

    public void remove(final ICriterionTrigger.Listener<ChangeAgeTrigger.Instance> listener) {
      this.listeners.remove(listener);
    }

    public void trigger(final PlayerEntity player) {
      List<Listener<Instance>> list = null;

      for(final ICriterionTrigger.Listener<ChangeAgeTrigger.Instance> listener : this.listeners) {
        if(listener.getCriterionInstance().test(player)) {
          if(list == null) {
            list = Lists.newArrayList();
          }

          list.add(listener);
        }
      }

      if(list != null) {
        for(final ICriterionTrigger.Listener<ChangeAgeTrigger.Instance> listener1 : list) {
          listener1.grantCriterion(this.playerAdvancements);
        }
      }
    }
  }
}
