package me.illtamer.infinitebot.expansion;

import me.illtamer.infinitebot.expansion.manager.InfinitePluginLoader;

import java.io.File;

/**
 * 插件加载异常
 * @see InfinitePluginLoader#loadExpansion(File)
 * */
public class InvalidExpansionException extends Exception {

    public InvalidExpansionException() {}

    public InvalidExpansionException(Throwable cause) {
        super(cause);
    }

    public InvalidExpansionException(String message) {
        super(message);
    }

    public InvalidExpansionException(String message, Throwable cause) {
        super(message, cause);
    }
}
