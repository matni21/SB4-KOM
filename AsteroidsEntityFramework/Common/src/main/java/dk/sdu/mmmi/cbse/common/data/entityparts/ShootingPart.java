package dk.sdu.mmmi.cbse.common.data.entityparts;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

public class ShootingPart implements EntityPart{

    private float cooldownTime;
    private float cooldown;
    private boolean shooting;

    public ShootingPart(float cooldownTime) {
        this.cooldownTime = cooldownTime;
    }

    public void setShooting(boolean shooting) {
        if (!shooting) {
            this.shooting = false;
            return;
        }

        if (cooldown > 0) {
            this.shooting = false;
            return;
        }

        //System.out.println("Entity shoots!");
        this.shooting = true;
        this.cooldown = this.cooldownTime;
    }

    public boolean getShooting() {
        return this.shooting;
    }

    @Override
    public void process(GameData gameData, Entity entity) {
        //if
        if (this.cooldown > 0) {
            this.cooldown -= gameData.getDelta();
            this.shooting = false;
        } else {
            this.cooldown = 0;
        }
    }
}