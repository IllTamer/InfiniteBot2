### 前言  
InfiniteBot-v2.+较一代完善了附属开发方面的不足。拓展系统借鉴了Buukit-Plugin模式，由插件主体提供可热加载、具有完善监听、独特API的附属开发模式。  
即：附属除无需配置plugin.yml外其他一切写法与bukkit-plugin类似！

### 附属主类注册  
附属主类需继承抽象类 `InfiniteExpansion` 并重写方法
-  `onEnable` 附属加载时调用
-  `onDisable` 附属卸载时调用
- `getExpansionName` 用于插件注册附属，不为空！  

其他父类自带方法详见[IExpansion](www.xxx.com)

示例代码
 ``` java
public class ExampleExapnsion extend InfiniteExpansion {
    @Override
    public void onEnable() {
        //TODO
    }

    @Override
    public void onDisable() {
        //TODO
    }

    @NotNull
    public String getExpansionName() {
        return "ExampleExapnsion";
    }
}
```

### 附属配置文件注册
您的配置文件应在附属初始化时被注册，当插件加载附属配置文件时，会从附属jar中寻找对应URL，若找到加载到缓存并自动生成到 `plugins/InfiniteBot2/expansionConfigs/附属名称` 下  
示例代码  
```java
public class ExampleExapnsion extend InfiniteExpansion {
    private IExpansion instance;
    private ExpansiobConfig configFile;
    @Override
    public void onEnable() {
        instance = this;
        configFile = new ExpansiobConfig(instance, "config.yml");
        ...
    }
}
```
ExpansionConfig已封装常用方法保存/重载/获取yml文件，详见[ExpansionConfig](www.xxx.com)

### 插件事件监听+注册
注：该示例中所用到的类全位于 `me.illtamer.infinitebot.api` 包下(包括Bukkit、Mirai的同名类)  
您的监听类应实现`Listener`接口，监听的方法应标有`@EventHandler`注解，方法中事件参数应选择`InfiniteBot`事件

监听部分示例代码
```java
public class ExampleListener implements Listener {
    @EventHandler // 可选优先级设置项 property 优先级高的方法将被优先调用
    public void onEvent(MessageReceiceEvent event) {
        event.reply("收到"); // 引用发送者的消息发送一条内容为“收到”的消息
    }
}
```

注册监听部分示例代码
```java
public class ExampleExapnsion extend InfiniteExpansion {
    private IExpansion instance;
    @Override
    public void onEnable() {
        instance = this;
        EventExecutor.registerListener(instance, new ExampleListener());
        ...
    }
}
```
