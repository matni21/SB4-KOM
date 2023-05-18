package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Entity processing service
 */

public interface IEntityProcessingService {
    /**
     * Process the entity.
     * <br />
     * Pre-condition: Game has parsed once since last call.<br />
     * Post-condition: Entity has been processed and updated.
     *
     * @param gameData Data for the game
     * @param world World of the game
     *
     * @see GameData
     * @see World
     */
    void process(GameData gameData, World world);
}
