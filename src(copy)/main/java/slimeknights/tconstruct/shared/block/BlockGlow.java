package slimeknights.tconstruct.shared.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.gadgets.TinkerGadgets;
import slimeknights.tconstruct.gadgets.item.ItemThrowball;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockGlow extends Block {
    
    /* Bounds */
    private static final ImmutableMap<EnumFacing, BoundingBox> BOUNDS;
    public static PropertyDirection FACING = PropertyDirection.create("facing");
    
    static {
        ImmutableMap.Builder<EnumFacing, BoundingBox> builder = ImmutableMap.builder();
        builder.put(EnumFacing.UP, new BoundingBox(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D));
        builder.put(EnumFacing.DOWN, new BoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D));
        builder.put(EnumFacing.NORTH, new BoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D));
        builder.put(EnumFacing.SOUTH, new BoundingBox(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D));
        builder.put(EnumFacing.EAST, new BoundingBox(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D));
        builder.put(EnumFacing.WEST, new BoundingBox(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D));
        
        BOUNDS = builder.build();
    }
    
    public BlockGlow() {
        super(Material.PART);
        this.setTickRandomly(true);
        this.setHardness(0.0F);
        this.setLightLevel(0.9375F);
        this.setSoundType(BlockSoundGroup.WOOL);
        
        this.setDefaultState(this.stateFactory.getBaseState().withProperty(FACING, EnumFacing.DOWN));
    }
    
    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }
    
    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }
    
    @Nonnull
    @Override
    public ItemStack getPickBlock(
            @Nonnull IBlockState state, HitResult target,
            @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        // only use the glowball for pickblock if it was loaded (which happens when gadgets is loaded)
        if (TinkerGadgets.throwball != null) {
            return new ItemStack(TinkerGadgets.throwball, 1, ItemThrowball.ThrowballType.GLOW.ordinal());
        }
        
        // if unavailable, just return nothing, Minecraft will just not do anything on pick block
        return ItemStack.EMPTY;
    }
    
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        // if the location is not stable, break the block
        if (!canBlockStay(worldIn, pos, state.getValue(FACING))) {
            worldIn.setBlockToAir(pos);
        }
        
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }
    
    /**
     * Determines if a block side can contain a glow.
     * Returns true if the block side is solid and the block at the given BlockPos is not a liquid
     */
    protected boolean canBlockStay(World world, BlockPos pos, EnumFacing facing) {
        BlockPos placedOn = pos.offset(facing);
        
        boolean isSolidSide = world.getBlockState(placedOn).isSideSolid(world, placedOn, facing.getOpposite());
        boolean isLiquid = world.getBlockState(pos).getBlock() instanceof BlockLiquid;
        
        return !isLiquid && isSolidSide;
    }
    
    /**
     * Adds a glow block to the world, setting its facing based on the surroundings if not valid
     * Used since onBlockPlaced is not called when placing via World.setBlockState
     *
     * @param world  World object
     * @param pos    Position to place the block
     * @param facing Preferred facing, if the facing is not valid another one may be chosen
     * @return A boolean stating whether a glow was placed.
     * Will be false if the block position contains a non-replacable block or none of the surrounding blocks has a solid face
     */
    public boolean addGlow(World world, BlockPos pos, EnumFacing facing) {
        
        // only place the block if the current block at the location is replacable (eg, air, tall grass, etc.)
        IBlockState oldState = world.getBlockState(pos);
        if (oldState.getBlock().getMaterial(oldState).isReplaceable()) {
            
            // if the location is valid, place the block directly
            if (this.canBlockStay(world, pos, facing)) {
                if (!world.isClient) {
                    world.setBlockState(pos, getDefaultState().withProperty(FACING, facing));
                }
                return true;
            }
            // otherwise, try and place it facing a different way
            else {
                for (EnumFacing enumfacing : EnumFacing.VALUES) {
                    if (this.canBlockStay(world, pos, enumfacing)) {
                        if (!world.isClient) {
                            world.setBlockState(pos, getDefaultState().withProperty(FACING, enumfacing));
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        super.randomDisplayTick(stateIn, worldIn, pos, rand);
    }
    
    @Nonnull
    @Override
    public BoundingBox getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS.get(state.getValue(FACING));
    }
    
    @Override
    public BoundingBox getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
        return BlockFaceShape.UNDEFINED;
    }
    
    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }
    
    @Override
    public void getSubBlocks(CreativeTabs tab, DefaultedList<ItemStack> list) {
    }
}
