package slimeknights.tconstruct.smeltery.client;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class TankItemModel extends BakedModelWrapper<IBakedModel> {

    private static final Cache<TankCacheKey, IBakedModel> CACHE = CacheBuilder.newBuilder().maximumSize(30).build();

    private ItemOverrideList overrides;

    public TankItemModel(IBakedModel originalModel) {
        super(originalModel);
        overrides = new ItemTextureOverride(originalModel.getOverrides());
    }

    private static IBakedModel getTexturedModel(IBakedModel original, Identifier location, Fluid fluid) {
        try {
            IModel model = ModelLoaderRegistry.getModel(location);
            IModel retextured = model.retexture(ImmutableMap.of("fluid", fluid.getStill().toString()));
            return retextured.bake(retextured.getDefaultState(), VertexFormats.POSITION_COLOR_UV_NORMAL, ModelLoader.defaultTextureGetter());
        } catch (Exception e) {
            e.printStackTrace();
            return original;
        }
    }

    @Nonnull
    @Override
    public ItemOverrideList getOverrides() {
        return overrides;
    }

    private static class ItemTextureOverride extends ItemOverrideList {

        private ItemOverrideList parent;

        private ItemTextureOverride(ItemOverrideList list) {
            // the constructor flips the order of the overrides, so we have to flip them when copying lest they become backwards
            super(Collections.emptyList());
            this.parent = list;
        }

        @Override
        @Deprecated
        public Identifier applyOverride(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
            return parent.applyOverride(stack, worldIn, entityIn);
        }

        @Nonnull
        @Override
        public IBakedModel handleItemState(
                @Nonnull IBakedModel original, ItemStack stack, World world, EntityLivingBase entity) {
            if (stack.isEmpty() || !stack.getItem().hasCustomProperties() || !stack.hasTagCompound()) {
                return original;
            }
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
            if (fluid == null || fluid.amount == 0 || fluid.getFluid() == null) {
                return original;
            }

            @SuppressWarnings("deprecation") Identifier location = parent.applyOverride(stack, world, entity);
            if (location == null) {
                return original;
            }

            try {
                return CACHE.get(new TankCacheKey(location, fluid.getFluid()), () -> getTexturedModel(original, location, fluid.getFluid()));
            } catch (ExecutionException e) {
                TinkerSmeltery.log.error(e);
                return original;
            }
        }

        @Override
        public ImmutableList<ItemOverride> getOverrides() {
            return parent.getOverrides();
        }
    }

    private static class TankCacheKey {
        private Identifier location;
        private Fluid fluid;

        private TankCacheKey(@Nonnull Identifier location, @Nonnull Fluid fluid) {
            this.location = location;
            this.fluid = fluid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            TankCacheKey that = (TankCacheKey) o;

            return that.fluid == this.fluid && that.location.equals(this.location);
        }

        @Override
        public int hashCode() {
            return 31 * location.hashCode() + fluid.hashCode();
        }
    }
}
