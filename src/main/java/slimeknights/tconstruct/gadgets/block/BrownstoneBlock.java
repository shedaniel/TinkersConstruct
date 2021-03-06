package slimeknights.tconstruct.gadgets.block;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BrownstoneBlock extends Block {
    
    public BrownstoneBlock() {
        super(Block.Settings.create(Material.STONE).hardnessAndResistance(3.0F, 20.0F).sound(BlockSoundGroup.STONE));
    }
    
    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn.isInWater()) {
            entityIn.setMotion(entityIn.getMotion().mul(1.20D, 1.0D, 1.20D));
        } else {
            entityIn.setMotion(entityIn.getMotion().mul(1.25D, 1.0D, 1.25D));
        }
    }
}
