import org.tanukisoftware.wrapper.WrapperListener;
import org.tanukisoftware.wrapper.WrapperManager;

/**
 * Package: PACKAGE_NAME
 * User: min_xu
 * Date: 16/4/1 下午1:12
 * 说明：
 */
public class BootWrapper implements WrapperListener{

    public static void main(String[] args) {
        // 打印参数
        for (String arg : args)
            System.out.println(arg);
        WrapperManager.start(new BootWrapper(), args);
    }

    @Override
    public void controlEvent(int event) {
        System.out.println("controlEvent(" + event + ")");
        if ((event == WrapperManager.WRAPPER_CTRL_LOGOFF_EVENT) && (WrapperManager.isLaunchedAsService() || WrapperManager.isIgnoreUserLogoffs())) {
        } else {
            WrapperManager.stop(0);
        }
    }

    @Override
    public Integer start(String[] args) {
        // 打印参数
        for (String arg : args)
            System.out.println(arg);
        System.out.println("hello world!");
        return null;
    }

    @Override
    public int stop(int exitCode) {
        System.out.println("stop(" + exitCode + ")");
        return exitCode;
    }

}
