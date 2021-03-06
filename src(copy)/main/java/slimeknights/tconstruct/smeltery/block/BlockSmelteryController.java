package slimeknights.tconstruct.smeltery.block;

import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.smeltery.tileentity.TileSmeltery;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockSmelteryController extends BlockMultiblockController {

    public BlockSmelteryController() {
        super(Material.STONE);
        this.setCreativeTab(TinkerRegistry.tabSmeltery);
        this.setHardness(3F);
        this.setResistance(20F);
        this.setSoundType(BlockSoundGroup.METAL);
    }

    @Nonnull
    @Override
    public BlockEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileSmeltery();
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (isActive(world, pos)) {
            EnumFacing enumfacing = state.getValue(FACING);
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + 0.5D + (rand.nextFloat() * 6F) / 16F;
            double d2 = (double) pos.getZ() + 0.5D;
            double d3 = 0.52D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;

            spawnFireParticles(world, enumfacing, d0, d1, d2, d3, d4);
        }
    }

}
