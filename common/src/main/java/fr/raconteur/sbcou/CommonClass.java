package fr.raconteur.sbcou;

import fr.raconteur.sbcou.db.versions.SbcouVersionsDataBase;
import fr.raconteur.sbcou.file.JsonConfigFileHandler;
import fr.raconteur.sbcou.flatobject.FlatKey;
import fr.raconteur.sbcou.flatobject.FlatObject;
import fr.raconteur.sbcou.platform.Services;
import fr.raconteur.sbcou.types.SbcouData;
import fr.raconteur.sbcou.types.nested.SbcouList;
import fr.raconteur.sbcou.types.nested.SbcouObject;
import fr.raconteur.sbcou.types.primitives.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

import java.sql.SQLException;
import java.util.HashMap;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class CommonClass {

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {

        Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        Constants.LOG.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));

        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.
        if (Services.PLATFORM.isModLoaded("examplemod")) {

            Constants.LOG.info("Hello to examplemod");
        }
        try {
            SbcouVersionsDataBase test = new SbcouVersionsDataBase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        new SbcouInteger(42);
        new SbcouReal(42.42);
        new SbcouBoolean(true);
        new SbcouString("test");
        new SbcouString("test");
        new SbcouString("test2");
        new SbcouInteger(42);
        new SbcouList(new SbcouInteger(42), new SbcouInteger(43));
        new SbcouObject(new HashMap<>() {{
            put("test", new SbcouInteger(42));
            put("test2", new SbcouString("test2"));
            put("test3",  new SbcouNull());
        }});
        new SbcouNull();
        new SbcouNaN("-NaN");

        Constants.LOG.info(String.valueOf(SbcouData.sbcouDataFromId(1).getValue()));

        FlatKey key = FlatKey.getSinglePartFlatKeyFromString("test\\\"").getChild(FlatKey.getSinglePartFlatKeyFromString(""));

        Constants.LOG.info(key.getFormattedKey());

        SbcouData<?> jsonData;
        try {
            jsonData = new JsonConfigFileHandler(Services.PLATFORM.getMinecraftInstanceDirectory(), "config/betterf3.json").read();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        FlatObject flatJson = jsonData.flatten();

        Constants.LOG.info(jsonData.getDisplayValue());

        Constants.LOG.info(flatJson.getDisplayValue());
    }
}