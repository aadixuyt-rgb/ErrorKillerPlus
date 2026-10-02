package pl.bkubiak.errorkillerplus.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.bkubiak.errorkiller.ErrorKillerController;

@Mixin(ErrorKillerController.class)
public abstract class ErrorKillerControllerMixin {
    private static boolean errorKillerPlus$offhandCobweb;

    @Shadow private static int step;

    @Redirect(
        method = "detectAutoTrigger",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/class_746;method_6047()Lnet/minecraft/class_1799;"
        )
    )
    private static ItemStack errorKillerPlus$triggerStack(ClientPlayerEntity player) {
        ItemStack offhand = player.getOffHandStack();
        if (offhand.isOf(Items.COBWEB)) {
            errorKillerPlus$offhandCobweb = true;
            return offhand;
        }
        errorKillerPlus$offhandCobweb = false;
        return player.getMainHandStack();
    }

    @Redirect(
        method = "runMacro",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/class_1661;method_5438(I)Lnet/minecraft/class_1799;"
        )
    )
    private static ItemStack errorKillerPlus$macroStack(PlayerInventory inventory, int slot) {
        if (errorKillerPlus$offhandCobweb && slot == 0) {
            return MinecraftClient.getInstance().player.getOffHandStack();
        }
        return inventory.getStack(slot);
    }

    @ModifyArg(
        method = "runMacro",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/class_636;method_2896(Lnet/minecraft/class_746;Lnet/minecraft/class_1268;Lnet/minecraft/class_3965;)Lnet/minecraft/class_1269;"
        ),
        index = 1
    )
    private static net.minecraft.util.Hand errorKillerPlus$blockHand(net.minecraft.util.Hand hand) {
        return errorKillerPlus$offhandCobweb ? Hand.OFF_HAND : hand;
    }

    @ModifyArg(
        method = "runMacro",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/class_636;method_2919(Lnet/minecraft/class_1657;Lnet/minecraft/class_1268;)Lnet/minecraft/class_1269;"
        ),
        index = 1
    )
    private static net.minecraft.util.Hand errorKillerPlus$itemHand(net.minecraft.util.Hand hand) {
        return errorKillerPlus$offhandCobweb ? Hand.OFF_HAND : hand;
    }

    @Inject(method = "reset", at = @At("TAIL"))
    private static void errorKillerPlus$clear(MinecraftClient client, CallbackInfo ci) {
        errorKillerPlus$offhandCobweb = false;
    }
}
