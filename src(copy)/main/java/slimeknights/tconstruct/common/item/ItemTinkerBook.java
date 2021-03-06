package slimeknights.tconstruct.common.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextFormat;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import slimeknights.mantle.util.LocUtils;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.book.TinkerBook;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemTinkerBook extends Item {
    
    public ItemTinkerBook() {
        this.setCreativeTab(TinkerRegistry.tabGeneral);
        this.setMaxStackSize(1);
    }
    
    @Nonnull
    @Override
    public TypedActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        if (worldIn.isClient) {
            TinkerBook.INSTANCE.openGui(itemStack);
        }
        return new TypedActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, TooltipContext flagIn) {
        if (I18n.canTranslate(super.getUnlocalizedName(stack) + ".tooltip")) {
            tooltip.addAll(LocUtils.getTooltips(TextFormat.field_1080.toString() + LocUtils.translateRecursive(super.getUnlocalizedName(stack) + ".tooltip")));
        }
    }
}
