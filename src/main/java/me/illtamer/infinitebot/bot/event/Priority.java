package me.illtamer.infinitebot.bot.event;

/**
 * 优先级枚举
 * */
public enum Priority {

    /**
     * 最低 (最后触发)
     * */
    LOWEST,

    /**
     * 较低 (较后触发)
     * */
    LOW,

    /**
     * 默认 (默认触发)
     * */
    DEFAULT,

    /**
     * 高 (较先触发)
     * */
    HIGH,

    /**
     * 最高 (最先触发)
     * */
    HIGHEST;

}
