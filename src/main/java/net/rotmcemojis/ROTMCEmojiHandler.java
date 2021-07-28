package net.rotmcemojis;

import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.slf4j.Logger;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//Plugin description
@Plugin(id = "rotmcemojis",
        name = "ROTMCEmojiHandler",
        version = "${project.version}",
        description = "Velocity plugin for handiling Emoji in proxy chat",
        authors = {"Dead_Of_Pool"}
)
//Constuctor for plugin
public class ROTMCEmojiHandler {

    //Toml config loader
    private Toml loadConfig(Path path) {
        File folder = path.toFile();
        File file = new File(folder, "net/rotmcemojis/config/config.toml");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try {
                InputStream input = this.getClass().getResourceAsStream("/" + file.getName());
                Throwable t = null;

                try {
                    if (input != null) {
                        Files.copy(input, file.toPath());
                    } else {
                        file.createNewFile();
                    }
                } catch (Throwable e) {
                    t = e;
                    throw e;
                } finally {
                    if (input != null) {
                        if (t != null) {
                            try {
                                input.close();
                            } catch (Throwable ex) {
                                t.addSuppressed(ex);
                            }
                        } else {
                            input.close();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return (new Toml()).read(file);
    }

    //Waits for proxy to initialize then starts the chat listener
    @Inject
    private ProxyServer proxy;
    @Inject
    private Logger logger;


    @Subscribe
    public void onEnable(ProxyInitializeEvent event)
        {
            //Checks to see if config is loaded
            Toml toml = this.loadConfig(Paths.get("config.toml"));
            if (toml == null) {
                logger.warn("Failed to load config.toml. Shutting down.");
            } else {
                //starts listener

                proxy.getEventManager().register(this, new ROTMCEmojiHandlerChat(proxy, toml));
                logger.info("Plugin has enabled!");
            }
        }
    }
