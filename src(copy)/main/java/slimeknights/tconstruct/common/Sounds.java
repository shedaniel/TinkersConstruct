package slimeknights.tconstruct.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.tconstruct.library.Util;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Util.MODID)
public abstract class Sounds {
    
    // remember, this needs to be at the top because of initialization order
    private static final List<SoundEvent> sounds = new ArrayList<>();
    public static final SoundEvent saw = sound("little_saw");
    public static final SoundEvent frypan_boing = sound("frypan_hit");
    public static final SoundEvent toy_squeak = sound("toy_squeak");
    public static final SoundEvent slimesling = sound("slimesling");
    public static final SoundEvent shocking_charged = sound("charged");
    public static final SoundEvent shocking_discharge = sound("discharge");
    public static final SoundEvent stone_hit = sound("stone_hit");
    public static final SoundEvent wood_hit = sound("wood_hit");
    public static final SoundEvent crossbow_reload = sound("crossbow_reload");
    
    private Sounds() {
    }
    
    private static SoundEvent sound(String name) {
        Identifier location = Util.getResource(name);
        SoundEvent event = new SoundEvent(location);
        sounds.add(event);
        return event;
    }
    
    public static void playSoundForAll(Entity entity, SoundEvent sound, float volume, float pitch) {
        entity.getEntityWorld().playSound(null, entity.getBlockPos(), sound, entity.getSoundCategory(), volume, pitch);
    }
    
    public static void PlaySoundForPlayer(Entity entity, SoundEvent sound, float volume, float pitch) {
        if (entity instanceof ServerPlayerEntity) {
            TinkerNetwork.sendPacket(entity, new SPacketSoundEffect(sound, entity.getSoundCategory(), entity.x, entity.y, entity.z, volume, pitch));
        }
    }
    
    @SubscribeEvent
    public static void registerSoundEvent(RegistryEvent.Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> registry = event.getRegistry();
        sounds.forEach(registry::register);
    }
}
