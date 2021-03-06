package slimeknights.tconstruct.world.entity;

import net.minecraft.entity.SpawnType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.block.BlockLiquidSlime;
import slimeknights.tconstruct.world.TinkerWorld;

import javax.annotation.Nonnull;

public class EntityBlueSlime extends SlimeEntity {

    public static final Identifier LOOT_TABLE = Util.getResource("entities/blueslime");

    public EntityBlueSlime(World worldIn) {
        super(worldIn);
    }

    // we're using this instead of getDropItem because we need the metadata
    @Override
    public EntityItem dropItemWithOffset(@Nonnull Item itemIn, int size, float offsetY) {
        ItemStack stack = TinkerCommons.matSlimeBallBlue.copy();
        stack.setCount(size);
        return this.entityDropItem(stack, offsetY);
    }
    
    @Override
    protected Identifier getLootTableId() {
        return this.getSize() == 1 ? LOOT_TABLE : LootTableList.EMPTY;
    }
    
    @Override
    public boolean getCanSpawnHere() {
        if (this.getEntityWorld().getBlockState(this.getPosition()).getBlock() instanceof BlockLiquidSlime) {
            return true;
        }
        return this.getEntityWorld().getBlockState(this.getPosition().down()).getBlock() == TinkerWorld.slimeGrass;
    }

    @Override
    protected boolean spawnCustomParticles() {
        if (this.getEntityWorld().isClient) {
            int i = this.getSlimeSize();
            for (int j = 0; j < i * 8; ++j) {
                float f = this.random.nextFloat() * (float) Math.PI * 2.0F;
                float f1 = this.random.nextFloat() * 0.5F + 0.5F;
                float f2 = MathHelper.sin(f) * (float) i * 0.5F * f1;
                float f3 = MathHelper.cos(f) * (float) i * 0.5F * f1;
                double d0 = this.x + (double) f2;
                double d1 = this.z + (double) f3;
                double d2 = this.getEntityBoundingBox().minY;
                TinkerWorld.proxy.spawnSlimeParticle(this.getEntityWorld(), d0, d2, d1);
            }
        }
        return true;
    }
}
