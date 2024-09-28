package net.fsmdev.hats;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class HatCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralCommandNode<ServerCommandSource> hatNode = CommandManager
                    .literal("hat")
                    .executes(new CustomHatCommand())
                    .build();
            dispatcher.getRoot().addChild(hatNode);
        });
    }

    public static class CustomHatCommand implements Command<ServerCommandSource> {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            if (context.getSource().getEntity() instanceof ServerPlayerEntity player) {
                if (!player.isSpectator()) {
                    PlayerInventory inventory = player.getInventory();
                    int helmetSlot = ((PlayerInventoryInterface) inventory).getHELMET_SLOTS()[0];
                    ItemStack temp = inventory.getArmorStack(helmetSlot);
                    if (player.isCreative() || !(temp.getEnchantments().getLevel(player.getWorld().getRegistryManager().getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.BINDING_CURSE)) > 0)) {
                        inventory.armor.set(helmetSlot, inventory.getStack(inventory.selectedSlot));
                        inventory.setStack(inventory.selectedSlot, temp);
                    } else {
                        context.getSource().sendFeedback(() -> Text.of("Can't remove gear with \"Curse of Binding\" enchantment."), false);
                    }
                } else {
                    context.getSource().sendFeedback(() -> Text.of("Can't use command while spectating."), false);
                }
            } else {
                context.getSource().sendFeedback(() -> Text.of("Can't use command from console."), true);
            }
            return 1;
        }
    }
}
