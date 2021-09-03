import me.illtamer.infinitebot.api.IExpansion;
import me.illtamer.infinitebot.bot.event.Listener;
import me.illtamer.infinitebot.expansion.manager.InfiniteExpansion;
import me.illtamer.infinitebot.expansion.manager.InfinitePluginLoader;
import me.illtamer.infinitelib.util.DownloadUtil;
import me.illtamer.infinitelib.util.StringUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public class TestMain {
    static Logger logger = LoggerFactory.getLogger(TestMain.class);

    private static final String PATH = "E:\\JCode\\idea\\plugin\\default\\InfiniteBot2Expansions2\\TestExpansion\\out\\artifacts\\TestExpansion_jar\\TestExpansion.jar";
    private static final InfinitePluginLoader pluginLoader = new InfinitePluginLoader();


    private static final String JAR_URL = "/IllTamer_Gitee/libs/raw/master/Mirai-Core-2.6.7/Dependency-Mirai-2.6.7-{0}.jar";

    private static final HashMap<String, Set<String>> LISTENERS = new HashMap<>();
    public static void main(String[] args) throws Exception {
//        YamlConfig yaml = new YamlConfig(new File("F:\\Minecraft\\Server\\paper-1.15.2\\plugins\\InfiniteBot2"));
//        System.out.println(yaml.getPlayerData(765743073 ,"4ce4a7ff-f9e7-32af-9c15-ad176dbecf5a"));


//        Set<String> expansions = LISTENERS.get("instance");
//        if (expansions == null) {
//            LISTENERS.put("instance", new HashSet<>(Collections.singleton("listener")));
//        } else {
//            expansions.add("listener");
//        }
//        System.out.println(LISTENERS.size());
        File file = new File("C:\\Users\\Bacon\\Desktop\\InfiniteBotPlus-1.0.0-SNAPSHOT\\xxx\\aa.txt");
        file.createNewFile();
        System.out.println(UUID.randomUUID().toString().length());
    }


    private static void download() {
        for (int i = 1; i <= 1; i ++ ) {
            System.out.println("https://gitee.com/" + StringUtil.format(JAR_URL, i));
            DownloadUtil.file(new File("E:\\JCode\\idea\\plugin\\maven\\InfiniteBot2\\src\\main\\resources\\Dependency-Mirai-2.6.7.jar"),
                    /*"https://gitee.com/" + StringUtil.format(JAR_URL, i)*/
                    "https://tcs-devops.aliyuncs.com/storage/1328f0dab3e2cf8b9ab22a8fc9588c625e85?download=Dependency-Mirai-2.6.7.jar&Signature=eyJhbGciOiJIUzI1NiJ9.eyJyZXNvdXJjZSI6Ii9zdG9yYWdlLzEzMjhmMGRhYjNlMmNmOGI5YWIyMmE4ZmM5NTg4YzYyNWU4NSIsImV4cCI6MTYyODY4MzIwMH0.PUwcyEAzc8c4fXzuTlGxm_wR-EAAPs6NIN-DfH_tEBU&filekey=1328f0dab3e2cf8b9ab22a8fc9588c625e85");
        }
    }

    private static void log() {
        try {
            Logger logger = LoggerFactory.getLogger(TestMain.class);
            IExpansion expansion = pluginLoader.loadExpansion(new File(PATH));
            pluginLoader.enableExpansion(expansion);
            expansion.getLogger().info("adasds");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
