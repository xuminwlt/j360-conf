package me.j360.conf.jsw;

import org.apache.commons.lang3.ArrayUtils;

public class Bootstrap {

    public static void main(String[] args) throws InterruptedException {
        if(ArrayUtils.isNotEmpty(args)){
            // 打印参数
            for (String arg : args)
                System.out.println("j360-jsw-> " + arg);
        }else{
            System.out.println("j360-jsw args = null");
        }
    }

}
