package me.illtamer.infinitebot.bot;

public class WrongInstanceTypeException extends RuntimeException {

    public WrongInstanceTypeException(Object now, Class<?> needed) {
        super(
                "传入实例类型异常！" + "\n" +
                "\t需要: " + needed.getName() + "\n" +
                "\t传入: " + now.getClass().getName()
        );
    }

}
