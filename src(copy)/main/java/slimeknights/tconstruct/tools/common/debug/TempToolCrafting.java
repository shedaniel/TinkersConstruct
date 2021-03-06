package slimeknights.tconstruct.tools.common.debug;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.tools.ToolCore;

import javax.annotation.Nonnull;

public class TempToolCrafting extends Impl<Recipe> implements Recipe {

    private ItemStack outputTool;

    public TempToolCrafting() {
        this.setRegistryName(Util.getResource("tool"));
    }

    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting p_77572_1_) {
        return outputTool;
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        outputTool = ItemStack.EMPTY;

        DefaultedList<ItemStack> input = DefaultedList.create();

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack slot = inv.getStackInSlot(i);
            // empty slot
            if (slot.isEmpty()) {
                continue;
            }

            // save it
            input.add(slot);
        }

        DefaultedList<ItemStack> inputs = Util.deepCopyFixedNonNullList(input);
        for (ToolCore tool : TinkerRegistry.getTools()) {
            outputTool = tool.buildItemFromStacks(inputs);
            if (!outputTool.isEmpty()) {
                break;
            }
        }

        return outputTool != null;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return outputTool;
    }

    @Nonnull
    @Override
    public DefaultedList<ItemStack> getRemainingItems(@Nonnull InventoryCrafting inv) {
        return DefaultedList.create();
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }
}
