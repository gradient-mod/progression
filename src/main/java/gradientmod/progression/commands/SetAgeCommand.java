package gradientmod.progression.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import gradientmod.progression.Age;
import gradientmod.progression.capabilities.PlayerProgressCapability;
import gradientmod.progression.capabilities.PlayerProgressUpdatePacket;
import gradientmod.progression.advancements.AdvancementTriggers;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public final class SetAgeCommand {
  private SetAgeCommand() { }

  public static ArgumentBuilder<CommandSource, ?> build() {
    return Commands.literal("setage")
      .requires(ctx -> ctx.hasPermissionLevel(4))
      .then(Commands.argument("age", IntegerArgumentType.integer(1, Age.highest().value()))
        .then(Commands.argument("targets", EntityArgument.players())
          .executes(ctx -> execute(ctx, Age.get(IntegerArgumentType.getInteger(ctx, "age")), EntityArgument.getPlayers(ctx, "targets")))
        )
      );
  }

  private static int execute(final CommandContext<CommandSource> ctx, final Age age, final Collection<ServerPlayerEntity> players) throws CommandSyntaxException {
    final ServerPlayerEntity sender = ctx.getSource().asPlayer();

    for(final ServerPlayerEntity target : players) {
      target
        .getCapability(PlayerProgressCapability.PLAYER_PROGRESS_CAPABILITY)
        .ifPresent(progress -> {
          progress.setAge(age);

          AdvancementTriggers.CHANGE_AGE.trigger(target);
          PlayerProgressUpdatePacket.send(target);

          target.sendMessage(new TranslationTextComponent("commands.setage.set", age.getDisplayName()));

          if(sender != target) {
            sender.sendMessage(new TranslationTextComponent("commands.setage.set_other", target.getDisplayName(), age.getDisplayName()));
          }
        });
    }

    return Command.SINGLE_SUCCESS;
  }
}
