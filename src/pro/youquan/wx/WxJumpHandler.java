/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pro.youquan.wx;

import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Fitz
 */
public class WxJumpHandler {

    private WxJumpFrame wxFrame;
    private JLabel imageLabel;
    private boolean isFirst = true;

    private Point firstPoint;
    private Point secondPoint;
    private double resizedDistancePressTimeRatio = 0;
    private float imageresizedRatio = 1;
    private ImageIcon imageIcon;
    private IniEditor settings = new IniEditor();
    private long screenshotInterval = 500;
   private  Random random = new Random();
    public WxJumpHandler(WxJumpFrame wxFrame, JLabel imageLabel) {
        this.imageLabel = imageLabel;
        this.wxFrame = wxFrame;
        imageIcon = new ImageIcon(Constants.SCREENSHOT_LOCATION);
        try {
            settings.load(Constants.INI_SETTING_CONFIG_FILE);
            String distancePressTimeRatioStr = settings.get("general", "distancePressTimeRatio");
            String imageresizedRatioStr = settings.get("general", "imageresizedRatio");
            String screenshotIntervalStr = settings.get("general", "screenshotInterval");
            resizedDistancePressTimeRatio = Float.parseFloat(distancePressTimeRatioStr);
            imageresizedRatio = Float.parseFloat(imageresizedRatioStr);
            screenshotInterval = Long.parseLong(screenshotIntervalStr);
            String adbPath = settings.get("general", "adbPath");
            if (adbPath == null || "".equals(adbPath.trim())) {
                AdbCaller.setAdbPath(Constants.ADB_PATH);
            } else {
                AdbCaller.setAdbPath(adbPath);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            showErrorMsg("读取配置失败");
        }
    }

    public void loadImage() {
        wxFrame.setTitle("截图中，请稍候");
       long t1 = System.currentTimeMillis();
        AdbCaller.printScreen();

        
        imageIcon = new ImageIcon(Constants.SCREENSHOT_LOCATION);
        imageIcon.getImage().flush();
        imageIcon = resizeImage(imageIcon, imageresizedRatio);
        imageLabel.setIcon(imageIcon);
        long t2 = System.currentTimeMillis();
        System.out.println("显示图片耗时："+(t2-t1));
        wxFrame.setTitle("请选择起始点位置");
        
        isFirst = true;
    }

    public void onImageMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1) {
            if (isFirst) {
                System.out.println("first " + event.getX() + " " + event.getY());
                firstPoint = event.getPoint();
                isFirst = false;
                wxFrame.setTitle("请选择目标点位置");
            } else {
                System.out.println("sencond" + event.getX() + " " + event.getY());
                secondPoint = event.getPoint();
                float distance = distance(firstPoint, secondPoint) * imageresizedRatio;
                wxFrame.setTitle("模拟点击，请稍候");
                int x = 170 + random.nextInt(500);
                int y = 187 +random.nextInt(500);
                AdbCaller.longPress(x,y,distance * resizedDistancePressTimeRatio);
                wxFrame.setTitle("模拟点击，请稍候");
                try {
                    Thread.sleep(screenshotInterval);
                } catch (Exception ex) {

                }
                loadImage();
            }

        }
    }

    public int distance(Point a, Point b) {// 求两点距离
        return (int) Math.sqrt((a.x - b.getX()) * (a.x - b.getX()) + (a.y - b.getY()) * (a.y - b.getY()));
    }

    public ImageIcon resizeImage(ImageIcon imageIcon, float ratio) {
        if (ratio == 1 || ratio <= 0) {
            return imageIcon;
        }
        int width = imageIcon.getIconWidth();
        int height = imageIcon.getIconHeight();
        Image image = imageIcon.getImage().getScaledInstance((int) (width / ratio), (int) (height / ratio), Image.SCALE_FAST);
        imageIcon.setImage(image);
        return imageIcon;
    }

    public void displayLastConfig(JTextField etDisPressRatio, JTextField etImageResizeRatio,JTextField etScreenshotInterval,JTextField etAdbPath) {
        etDisPressRatio.setText(String.valueOf(this.resizedDistancePressTimeRatio));
        etImageResizeRatio.setText(String.valueOf(this.imageresizedRatio));
        etScreenshotInterval.setText(String.valueOf(this.screenshotInterval));
        etAdbPath.setText(AdbCaller.adbPath);
    }

    public void updateConfig(float disPressRatio, float imageResizeRatio, long screenshotInterval, String adbPath) {
        this.imageresizedRatio = imageResizeRatio;
        this.resizedDistancePressTimeRatio = disPressRatio;
        this.screenshotInterval = screenshotInterval;
        if (adbPath == null || "".equals(adbPath.trim())) {
            AdbCaller.setAdbPath(Constants.ADB_PATH);
        } else {
            AdbCaller.setAdbPath(adbPath);
        }
        settings.set("General", "distancePressTimeRatio", "" + disPressRatio);
        settings.set("General", "imageresizedRatio", "" + imageResizeRatio);
        settings.set("General", "screenshotInterval", "" + screenshotInterval);
        settings.set("General", "adbPath", "" + adbPath);
        try{
             settings.save(Constants.INI_SETTING_CONFIG_FILE);
        }catch(Exception e){
            
        }
   
        JOptionPane.showMessageDialog(null, "保存成功", "错误", JOptionPane.ERROR_MESSAGE);
        loadImage();
    }

    public void showErrorMsg(String msg) {
        JOptionPane.showMessageDialog(null, msg, "错误", JOptionPane.ERROR_MESSAGE);
    }
}
