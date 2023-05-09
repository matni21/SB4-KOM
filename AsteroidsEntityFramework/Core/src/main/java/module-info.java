import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import com.badlogic.gdx;

module Core {
    requires Common;
    requires java.desktop;
    requires com.badlogic.gdx;

    uses IGamePluginService;
    uses IEntityProcessingService;
    uses IPostEntityProcessingService;

}