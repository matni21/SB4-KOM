package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

public interface IBulletCreator {
    /**
     * Start the plugin.
     * <br />
     * Pre-condition: Game is running and shooter needs a bullet to appear.<br />
     * Post-condition: Bullet entity ready to be added in the world.
     *
     * @param shooter World of the game
     * @param gameData Data for the game
     *
     * @return Bullet Entity
     *
     * @see Entity
     * @see GameData
     */
    Entity create(Entity shooter, GameData gameData);
}
