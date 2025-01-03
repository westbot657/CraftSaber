package io.github.westbot.craftsaber.mixin;

import io.github.westbot.craftsaber.data.Stash;
import io.github.westbot.craftsaber.items.Saber;
import io.github.westbot.craftsaber.mixin_data.ItemStackWithStash;
import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder, FabricItemStack, ItemStackWithStash {

    @Shadow @Final @Deprecated @Nullable private Item item;
    @Unique
    public Optional<Stash<Pair<Vector3f, Vector3f>>> optionalStash = Optional.empty();

    @Unique
    public void initStash(int size) {
        this.optionalStash = Optional.of(new Stash<>(size));
    }

    @Unique
    public Stash<Pair<Vector3f, Vector3f>> craftSaber$getStash() {
        return optionalStash.orElseGet(() -> new Stash<>(2));
    }


    @Inject(method = "<init>(Lnet/minecraft/item/ItemConvertible;ILnet/minecraft/component/ComponentMapImpl;)V", at = @At("TAIL"))
    public void init(ItemConvertible item, int count, ComponentMapImpl components, CallbackInfo ci) {
        if (this.item instanceof Saber) {
            this.initStash(120);
        }
    }

}
