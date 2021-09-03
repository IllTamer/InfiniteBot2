package me.illtamer.infinitebot.api.event.group;

/**
 * 方法未启用异常
 * */
public class NotEnabledException extends RuntimeException {

    public NotEnabledException() {
        super("The method is not enabled !");
    }

}
