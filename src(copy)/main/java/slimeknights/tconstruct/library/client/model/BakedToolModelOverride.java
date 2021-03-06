package slimeknights.tconstruct.library.client.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Map;

// Basically ItemOverride but with models instead of locations as output
@SideOnly(Side.CLIENT)
public class BakedToolModelOverride {

    public final ImmutableMap<Identifier, Float> predicates;
    public final BakedToolModel bakedToolModel;

    public BakedToolModelOverride(ImmutableMap<Identifier, Float> predicates, BakedToolModel bakedToolModel) {
        this.predicates = predicates;
        this.bakedToolModel = bakedToolModel;
    }

    public boolean matches(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
        Item item = stack.getItem();

        for (Map.Entry<Identifier, Float> entry : predicates.entrySet()) {
            ItemPropertyGetter iitempropertygetter = item.getPropertyGetter(entry.getKey());

            if (iitempropertygetter == null || iitempropertygetter.apply(stack, worldIn, entityIn) < entry.getValue()) {
                return false;
            }
        }

        return true;
    }
}
