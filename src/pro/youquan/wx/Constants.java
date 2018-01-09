package pro.youquan.wx;

import java.io.File;

/**
 * Created by RoyZ on 2017/12/29.
 */
public class Constants {
    /**
     * adb所在位置
     */
  public static final String ADB_PATH = System.getProperty("user.dir") +File.separator+ "adb"+File.separator+"adb.exe";
    //   public static final String ADB_PATH ="adb ";
    /**
     * 截屏文件所在位置
     */
    public static final String SCREENSHOT_LOCATION =  System.getProperty("user.dir") +File.separator+ "adb"+File.separator+"s.png";
    /**
     * INI配置文件
     */
    public static final String INI_SETTING_CONFIG_FILE =  System.getProperty("user.dir") +File.separator+ "adb"+File.separator+"setting.ini";
    /**
     * 窗体显示的图片宽度
     */
    public static final int RESIZED_SCREEN_WIDTH = 675;

    /**
     * 窗体显示的图片高度
     */
    public static final int RESIZED_SCREEN_HEIGHT = 1200;

    /**
     * 在675*1200分辨率下，跳跃蓄力时间与距离像素的比率<br>
     * 可根据实际情况自行调整
     */
    public static final float RESIZED_DISTANCE_PRESS_TIME_RATIO = 2.175f;

    /**
     * 截图间隔
     */
    public static final int SCREENSHOT_INTERVAL = 3000; // ms

    /**
     * 手动模式
     */
    public static final int MODE_MANUAL = 1;
    /**
     * 半自动模式,只需要点secondPoint
     */
    public static final int MODE_SEMI_AUTO = 2;
    /**
     * 自动模式
     */
    public static final int MODE_AUTO = 3;
}
