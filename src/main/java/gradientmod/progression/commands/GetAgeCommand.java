package gradientmod.progression.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import gradientmod.progression.capabilities.PlayerProgressCapability;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public final class GetAgeCommand {
  private GetAgeCommand() { }

  public static ArgumentBuilder<CommandSource, ?> build() {
    return Commands.literal("getage")
      .requires(ctx -> ctx.hasPermissionLevel(4))
      .then(Commands.argument("targets", EntityArgument.players())
        .executes(ctx -> execute(ctx, EntityArgument.getPlayers(ctx, "targets")))
      );
  }

  private static int execute(final CommandContext<CommandSource> ctx, final Collection<ServerPlayerEntity> players) throws CommandSyntaxException {
    final ServerPlayerEntity sender = ctx.getSource().asPlayer();

    for(final ServerPlayerEntity target : players) {
      target
        .getCapability(PlayerProgressCapability.PLAYER_PROGRESS_CAPABILITY)
        .ifPresent(progress -> {
          sender.sendMessage(new TranslationTextComponent("commands.getage.get", target.getDisplayName(), progress.getAge().getDisplayName()));
        });
    }

    return Command.SINGLE_SUCCESS;
  }
}
