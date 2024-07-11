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
}
