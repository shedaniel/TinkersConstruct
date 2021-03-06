package slimeknights.tconstruct.gadgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.ClientProxy;
import slimeknights.tconstruct.gadgets.client.FancyItemFrameRenderer;
import slimeknights.tconstruct.gadgets.entity.EflnBallEntity;
import slimeknights.tconstruct.gadgets.entity.FancyItemFrameEntity;
import slimeknights.tconstruct.gadgets.entity.FrameType;
import slimeknights.tconstruct.gadgets.entity.GlowballEntity;
import slimeknights.tconstruct.gadgets.item.SlimeBootsItem;
import slimeknights.tconstruct.gadgets.item.SlimeSlingItem;
import slimeknights.tconstruct.items.GadgetItems;

import javax.annotation.Nonnull;

public class GadgetClientProxy extends ClientProxy {
    
    public static MinecraftClient minecraft = MinecraftClient.getInstance();
    
    @Override
    public void preInit() {
        super.preInit();
    }
    
    @Override
    public void init() {
        super.init();
        
        final ItemColors colors = minecraft.getItemColors();
        
        colors.register((@Nonnull
                ItemStack stack, int tintIndex) -> SlimeSlingItem.getColorFromStack(stack), GadgetItems.slime_sling_blue, GadgetItems.slime_sling_purple, GadgetItems.slime_sling_magma, GadgetItems.slime_sling_green, GadgetItems.slime_sling_blood);
        colors.register((@Nonnull
                ItemStack stack, int tintIndex) -> SlimeBootsItem.getColorFromStack(stack), GadgetItems.slime_boots_blue, GadgetItems.slime_boots_purple, GadgetItems.slime_boots_magma, GadgetItems.slime_boots_green, GadgetItems.slime_boots_blood);
    }
    
    @Override
    public void registerModels() {
        super.registerModels();
        
        // TODO: reinstate when Forge fixes itself
        //StateContainer<Block, BlockState> dummyContainer = new StateContainer.Builder<Block, BlockState>(Blocks.AIR).add(BooleanProperty.create("map")).create(BlockState::new);
        //for (FrameType frameType : FrameType.values()) {
        //  ResourceLocation fancyFrame = new ResourceLocation(TConstruct.modID, frameType.getName() + "_frame");
        //  for (BlockState state : dummyContainer.getValidStates()) {
        //    ModelLoader.addSpecialModel(BlockModelShapes.getModelLocation(fancyFrame, state));
        //  }
        //}
        
        for (FrameType frameType : FrameType.values()) {
            ModelLoader.addSpecialModel(new ModelIdentifier(new Identifier(TConstruct.modID, frameType.getName() + "_frame_empty"), "inventory"));
            ModelLoader.addSpecialModel(new ModelIdentifier(new Identifier(TConstruct.modID, frameType.getName() + "_frame_map"), "inventory"));
        }
    }
    
    @Override
    public void clientSetup() {
        super.clientSetup();
        
        MinecraftClient mc = MinecraftClient.getInstance();
        
        RenderingRegistry.registerEntityRenderingHandler(FancyItemFrameEntity.class, (manager) -> new FancyItemFrameRenderer(manager, mc.getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(GlowballEntity.class, (manager) -> new FlyingItemEntityRenderer(manager, mc.getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(EflnBallEntity.class, (manager) -> new FlyingItemEntityRenderer(manager, mc.getItemRenderer()));
    }
    
}
