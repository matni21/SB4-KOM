package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Entity processing service for after ordinary processing
 * @author jcs
 */
public interface IPostEntityProcessingService  {
        /**
         * Process entity after all ordinary processing.<br />
         * This can be for collision detection or similar elements, that needs to be processed after all entities has been processed.
         * <br />
         * Pre-condition: The game has parsed at least once since last call and all entities have been processed.<br />
         * Post-condition: The entity has been processed and updated.
         *
         * @param gameData Data for the game
         * @param world World of the game
         *
         * @see GameData
         * @see World
         */
        void process(GameData gameData, World world);
}
