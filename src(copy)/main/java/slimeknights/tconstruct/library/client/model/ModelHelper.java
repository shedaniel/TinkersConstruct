package slimeknights.tconstruct.library.client.model;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import slimeknights.tconstruct.library.client.deserializer.ItemCameraTransformsDeserializer;
import slimeknights.tconstruct.library.client.deserializer.ItemTransformVec3fDeserializer;
import slimeknights.tconstruct.library.client.model.format.*;
import slimeknights.tconstruct.shared.client.BakedColoredItemModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class ModelHelper extends slimeknights.mantle.client.ModelHelper {
    
    public static final EnumFacing[] MODEL_SIDES = new EnumFacing[]{null, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST};
    
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(ModelTextureDeserializer.TYPE, ModelTextureDeserializer.INSTANCE).registerTypeAdapter(Offset.OffsetDeserializer.TYPE, Offset.OffsetDeserializer.INSTANCE).registerTypeAdapter(TransformDeserializer.TYPE, TransformDeserializer.INSTANCE)
            //.registerTypeAdapter(ImmutableMap.class, JsonUtils.ImmutableMapTypeAdapter.INSTANCE)
            .registerTypeAdapter(ItemCameraTransforms.class, ItemCameraTransformsDeserializer.INSTANCE).registerTypeAdapter(ItemTransformVec3f.class, ItemTransformVec3fDeserializer.INSTANCE)
            //.registerTypeAdapter(TRSRTransformation.class, ForgeBlockStateV1.TRSRDeserializer.INSTANCE)
            .registerTypeAdapter(ToolModelOverride.ToolModelOverrideListDeserializer.TYPE, ToolModelOverride.ToolModelOverrideListDeserializer.INSTANCE).registerTypeAdapter(AmmoPosition.AmmoPositionDeserializer.TYPE, AmmoPosition.AmmoPositionDeserializer.INSTANCE).create();
    
    public static IBakedModel getBakedModelForItem(ItemStack stack, World world, EntityLivingBase entity) {
        IBakedModel model = MinecraftClient.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, world, entity);
        if (model == null || model.isBuiltInRenderer()) {
            // missing model so people don't go paranoid when their chests go missing
            model = MinecraftClient.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getMissingModel();
        } else {
            // take color into account
            model = new BakedColoredItemModel(stack, model);
        }
        return model;
    }
    
    public static Reader getReaderForResource(Identifier location) throws IOException {
        return getReaderForResource(location, MinecraftClient.getMinecraft().getResourceManager());
    }
    
    public static Reader getReaderForResource(Identifier location, IResourceManager resourceManager)
            throws IOException {
        Identifier file = new Identifier(location.getResourceDomain(), location.getResourcePath() + ".json");
        IResource iresource = resourceManager.getResource(file);
        return new BufferedReader(new InputStreamReader(iresource.getInputStream(), Charsets.UTF_8));
    }
    
    public static Map<String, String> loadTexturesFromJson(Identifier location) throws IOException {
        Reader reader = getReaderForResource(location);
        try {
            return GSON.fromJson(reader, ModelTextureDeserializer.TYPE);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }
    
    public static Offset loadOffsetFromJson(Identifier location) throws IOException {
        Reader reader = getReaderForResource(location);
        try {
            return GSON.fromJson(reader, Offset.OffsetDeserializer.TYPE);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }
    
    public static AmmoPosition loadAmmoPositionFromJson(Identifier location) throws IOException {
        Reader reader = getReaderForResource(location);
        try {
            return GSON.fromJson(reader, AmmoPosition.AmmoPositionDeserializer.TYPE);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }
    
    public static ImmutableList<ToolModelOverride> loadToolModelOverridesFromJson(Identifier location)
            throws IOException {
        Reader reader = getReaderForResource(location);
        try {
            return GSON.fromJson(reader, ToolModelOverride.ToolModelOverrideListDeserializer.TYPE);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }
    
    public static ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> loadTransformFromJson(Identifier location)
            throws IOException {
        return loadTransformFromJson(location, "display");
    }
    
    public static ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> loadTransformFromJson(Identifier location, String tag)
            throws IOException {
        Reader reader = getReaderForResource(location);
        try {
            TransformDeserializer.tag = tag;
            ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms = GSON.fromJson(reader, TransformDeserializer.TYPE);
            
            // filter out missing/identity entries
            ImmutableMap.Builder<ItemCameraTransforms.TransformType, TRSRTransformation> builder = ImmutableMap.builder();
            for (Map.Entry<ItemCameraTransforms.TransformType, TRSRTransformation> entry : transforms.entrySet()) {
                if (!entry.getValue().equals(TRSRTransformation.identity())) {
                    builder.put(entry.getKey(), entry.getValue());
                }
            }
            
            return builder.build();
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }
    
    public static ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> loadTransformFromJsonBackup(Identifier location)
            throws IOException {
        Reader reader = getReaderForResource(location);
        try {
            // we abuse ModelBlock because all the deserializers are not accessible..
            ModelBlock modelBlock = ModelBlock.deserialize(reader);
            ItemCameraTransforms itemCameraTransforms = modelBlock.getAllTransforms();
            ImmutableMap.Builder<ItemCameraTransforms.TransformType, TRSRTransformation> builder = ImmutableMap.builder();
            for (ItemCameraTransforms.TransformType type : ItemCameraTransforms.TransformType.values()) {
                if (itemCameraTransforms.getTransform(type) != ItemTransformVec3f.DEFAULT) {
                    builder.put(type, new TRSRTransformation(itemCameraTransforms.getTransform(type)));
                }
            }
            return builder.build();
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }
    
    public static ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> getTransforms(IBakedModel model) {
        ImmutableMap.Builder<ItemCameraTransforms.TransformType, TRSRTransformation> builder = ImmutableMap.builder();
        for (ItemCameraTransforms.TransformType type : ItemCameraTransforms.TransformType.values()) {
            TRSRTransformation transformation = new TRSRTransformation(model.handlePerspective(type).getRight());
            if (!transformation.equals(TRSRTransformation.identity())) {
                builder.put(type, TRSRTransformation.blockCenterToCorner(transformation));
            }
        }
        return builder.build();
    }
    
    public static ImmutableList<Identifier> loadTextureListFromJson(Identifier location) throws IOException {
        ImmutableList.Builder<Identifier> builder = ImmutableList.builder();
        for (String s : loadTexturesFromJson(location).values()) {
            builder.add(new Identifier(s));
        }
        
        return builder.build();
    }
    
    public static Float[] loadLayerRotations(Identifier location) throws IOException {
        JsonReader reader = new JsonReader(getReaderForResource(location));
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                if ("layerrotation".equals(reader.nextName())) {
                    return GSON.fromJson(reader, Float[].class);
                } else {
                    reader.skipValue();
                }
            }
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return new Float[0];
    }
    
    public static Identifier getModelLocation(Identifier location) {
        return new Identifier(location.getResourceDomain(), "models/" + location.getResourcePath() + ".json");
    }
    
}
