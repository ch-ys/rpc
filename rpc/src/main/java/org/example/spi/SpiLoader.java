package org.example.spi;

import cn.hutool.core.io.resource.ResourceUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


//通用SPI加载器
public class SpiLoader {
    //维护接口class key 实现类之间的关系
    private static final Map<String,Map<String,Class<?>>> LOAD_MAP = new ConcurrentHashMap<>();
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

    // 注册器添加实例
    public static <T> T getInstance(Class<T> loadClass,String key){
        try {
            if(KEY_INSTANCE_MAP.get(key) != null){
                return (T) KEY_INSTANCE_MAP.get(key);
            }
            // 注册器中取出class数据
            Map<String, Class<?>> KeyClassMap = LOAD_MAP.get(loadClass.getName());
            if (KeyClassMap == null){
                System.out.println("注册器未加载该类型实现类");
            }
            Class<?> aClass = KeyClassMap.get(key);
            if (aClass == null){
                System.out.println("没有该类型实现类");
            }
            synchronized (SpiLoader.class){
                if(KEY_INSTANCE_MAP.get(key) != null){
                    return (T) KEY_INSTANCE_MAP.get(key);
                }
                KEY_INSTANCE_MAP.put(key,aClass.newInstance());
                return (T) KEY_INSTANCE_MAP.get(key);
            }
        } catch (Exception e) {
            System.out.println("加载类出错");
            return null;
        }
    }
}
