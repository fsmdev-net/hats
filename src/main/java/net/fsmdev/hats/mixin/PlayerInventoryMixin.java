package net.fsmdev.hats.mixin;

import net.fsmdev.hats.PlayerInventoryInterface;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements PlayerInventoryInterface {
    @Accessor
    public abstract int[] getHELMET_SLOTS();
}
