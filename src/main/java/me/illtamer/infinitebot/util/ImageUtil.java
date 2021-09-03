package me.illtamer.infinitebot.util;

import me.illtamer.infinitebot.InfiniteBot;
import me.illtamer.infinitelib.util.StringUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;

import java.awt.*;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageUtil {
    private static final Font DEFAULT = new Font("微软雅黑", Font.PLAIN, 20);
    private static final AtomicInteger INDEX = new AtomicInteger();
    private static final File imageTempFolder;

    static {
        imageTempFolder = new File(InfiniteBot.getInstance().getDataFolder(), "data\\images");
        if (!imageTempFolder.exists()) {
            imageTempFolder.mkdirs();
        }
    }

    public static Image fileToImage(Contact contact, File file) {
        return ExternalResource.uploadAsImage(ExternalResource.create(file), contact);
    }

    public static Image strToImage(Contact contact, String... str) {
        File file = new File(imageTempFolder, StringUtil.format("temp{0}.jpg", INDEX.getAndIncrement()));
        StringUtil.strToImage(DEFAULT, file, str);
        return fileToImage(contact, file);
    }

}
