package slimeknights.tconstruct.gadgets.block;

import net.minecraft.block.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.sound.BlockSoundGroup;
import slimeknights.mantle.block.EnumBlockSlab;
import slimeknights.tconstruct.gadgets.TinkerGadgets;
import slimeknights.tconstruct.library.TinkerRegistry;

public class BlockDriedClaySlab extends EnumBlockSlab<BlockDriedClay.DriedClayType> {

    public BlockDriedClaySlab() {
        super(Material.STONE, BlockDriedClay.TYPE, BlockDriedClay.DriedClayType.class);
        this.setCreativeTab(TinkerRegistry.tabGadgets);
        this.setHardness(3F);
        this.setResistance(20F);
        this.setSoundType(BlockSoundGroup.STONE);
    }

    @Override
    public IBlockState getFullBlock(IBlockState state) {
        if (TinkerGadgets.driedClay == null) {
            return null;
        }
        return TinkerGadgets.driedClay.getDefaultState().withProperty(BlockDriedClay.TYPE, state.getValue(BlockDriedClay.TYPE));
    }
}
