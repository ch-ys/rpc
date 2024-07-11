package org.example.spi;

import cn.hutool.core.io.resource.ResourceUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


//通用SPI加载器
public class SpiLoader {
    //维护接口 key 实现类之间的关系
    private static final Map<String,Map<String,Class<?>>> LOAD_MAP = new ConcurrentHashMap<>();
    // key 实现类
    private static final Map<String,Object> KEY_INSTANCE_MAP = new ConcurrentHashMap<>();
    //系统路径
    private static final String SYSTEM = "META-INF/rpc/system/";
    //自定义实现路径
    private static final String CUSTOM = "META-INF/rpc/custom/";
    //扫描的路径
    private static final String[] SCAN_DIRS = new String[]{SYSTEM,CUSTOM};

    // 加载key class文件map到loadMap
    public static Map<String, Class<?>> load(Class<?> loadClass){
        Map<String, Class<?>> KeyClassMap = new HashMap<>();
        for(String dir : SCAN_DIRS){
            List<URL> path = ResourceUtil.getResources(dir + loadClass.getName() );
            // 读取文件
            for(URL url : path){
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
                    BufferedReader buffer = new BufferedReader(inputStreamReader);
                    String line ;
                    while ((line = buffer.readLine()) != null){
                        String[] split = line.split("=");
                        String key = split[0];
                        String className = split[1];
                        KeyClassMap.put(key,Class.forName(className));
                        LOAD_MAP.put(loadClass.getName(),KeyClassMap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("SPI加载失败");
                }
            }
        }
        return KeyClassMap;
    }

    // 注册器添加实例（懒加载单例实例）
    public static <T> T getInstance(Class<T> loadClass,String key){
        try {
            // 存在实例直接返回
            if(KEY_INSTANCE_MAP.get(key) != null){
                return (T) KEY_INSTANCE_MAP.get(key);
            }
            // 查找接口 实现类 map
            Map<String, Class<?>> KeyClassMap = LOAD_MAP.get(loadClass.getName());
            if (KeyClassMap == null){
                System.out.println("注册器未加载该类型实现类");
            }
            // 注册器中取出实现类class
            Class<?> aClass = KeyClassMap.get(key);
            if (aClass == null){
                System.out.println("没有该类型实现类");
            }
            // 添加实例并返回
            // 双重判断单例
            synchronized (SpiLoader.class){
                if(KEY_INSTANCE_MAP.get(key) != null){
                    return (T) KEY_INSTANCE_MAP.get(key);
                }
                KEY_INSTANCE_MAP.put(key,aClass.newInstance());
                return (T) KEY_INSTANCE_MAP.get(key);
            }
        } catch (Exception e) {
            System.out.println("获取指定类出错");
            return null;
        }
    }

    // 双懒加载
    public static <T> T getInstance2Lazy(Class<T> loadClass,String key){
        // 加载过Load_Map 不一定加载过需要的实现类
        if (LOAD_MAP.get(loadClass.getName()) != null){
            // 单懒加载实例
          return getInstance(loadClass,key);
        }
        // 未加载
        // 防止并发
        synchronized (SpiLoader.class){
            // 双判定
            // 防止释放在进入
            if (LOAD_MAP.get(loadClass.getName()) != null){
                // 单懒加载实例
                return getInstance(loadClass,key);
            }
            load(loadClass);
            return getInstance(loadClass,key);
        }
    }
}
