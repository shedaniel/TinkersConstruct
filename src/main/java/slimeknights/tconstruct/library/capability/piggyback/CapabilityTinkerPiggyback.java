package slimeknights.tconstruct.library.capability.piggyback;

import net.minecraft.nbt.Tag;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityTinkerPiggyback implements Capability.IStorage<ITinkerPiggyback> {
    
    private static final CapabilityTinkerPiggyback INSTANCE = new CapabilityTinkerPiggyback();
    @CapabilityInject(ITinkerPiggyback.class) public static Capability<ITinkerPiggyback> PIGGYBACK = null;
    
    private CapabilityTinkerPiggyback() {
    }
    
    public static void register() {
        CapabilityManager.INSTANCE.register(ITinkerPiggyback.class, INSTANCE, TinkerPiggybackHandler::new);
    }
    
    @Nullable
    @Override
    public Tag writeNBT(Capability<ITinkerPiggyback> capability, ITinkerPiggyback instance, Direction side) {
        return null;
    }
    
    @Override
    public void readNBT(Capability<ITinkerPiggyback> capability, ITinkerPiggyback instance, Direction side, Tag nbt) {
    
    }
}
