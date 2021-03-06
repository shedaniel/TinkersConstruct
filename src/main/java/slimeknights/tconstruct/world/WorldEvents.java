package slimeknights.tconstruct.world;

import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import slimeknights.tconstruct.entity.WorldEntities;

public class WorldEvents {
    
    // Custom slime spawning on slime islands
    private Biome.SpawnEntry magmaSlimeSpawn = new Biome.SpawnEntry(EntityType.field_6102, 150, 4, 6);
    private Biome.SpawnEntry blueSlimeSpawn = new Biome.SpawnEntry(WorldEntities.blue_slime_entity, 15, 2, 4);
    
    @SubscribeEvent
    public void extraSlimeSpawn(WorldEvent.PotentialSpawns event) {
        if (event.getType() == EntityCategory.field_6302) {
            // inside a magma slime island?
            if (TinkerWorld.NETHER_SLIME_ISLAND.isPositionInsideStructure(event.getWorld(), event.getPos().down(3))) {
                // spawn magma slime, pig zombies have weight 100
                event.getList().clear();
                event.getList().add(this.magmaSlimeSpawn);
            }
            // inside a slime island?
            if (TinkerWorld.SLIME_ISLAND.isPositionInsideStructure(event.getWorld(), event.getPos().down(3))) {
                // spawn blue slime, most regular mobs have weight 10
                event.getList().clear();
                event.getList().add(this.blueSlimeSpawn);
            }
        }
    }
}
