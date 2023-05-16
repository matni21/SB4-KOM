//import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
//import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
//import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

module Core {
    requires Common;
    requires java.desktop;
    requires com.badlogic.gdx;
//    exports dk.sdu.mmmi.cbse.main;
    uses dk.sdu.mmmi.cbse.common.services.IGamePluginService;
    uses dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

}