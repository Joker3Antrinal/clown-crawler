package com.joker.just4fun.demo

import javax.swing.BorderFactory
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.SwingConstants
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.GraphicsEnvironment
import java.awt.Image
import java.awt.Rectangle
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * @author Administrator
 * @date 2024/2/5 11:09
 * @version 1.0
 */
class CountDownDialog extends JFrame {

    private JLabel backgroundLabel// 主题背景面板
    private JLabel countDownLabel// 倒计时面板

    private static final int WIDTH = 380
    private static final int HEIGHT = 240

    CountDownDialog(LocalDateTime targetDateTime) {

        //设置JFrame窗口基本属性
        setTitle("日期倒计时")
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        setSize(WIDTH, HEIGHT)
        setLocationRelativeTo(null) //设置居中显示

        //获取屏幕大小
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        Rectangle screenBounds = ge.getMaximumWindowBounds()

        //设置窗口位置在屏幕右上角
        int x = (int) (screenBounds.x + screenBounds.width - getWidth())
        int y = (int) screenBounds.y
        setLocation(x, y)

        /*创建中间组件并添加到JFrame主窗口*/
        JPanel panel = new JPanel(new BorderLayout())
//        panel.setBackground(Color.BLUE) //主窗口背景色

        //加载本地图片资源到背景Label中
        URL resource = getClass().getResource("/static/images/mt_bg.png")
        ImageIcon imageIcon = new ImageIcon(resource)
        backgroundLabel = new JLabel(imageIcon)
        backgroundLabel.setLayout(new BorderLayout())

        // 将图片转换为适合组件大小的背景图像
        Image img = imageIcon.getImage()
        Image scaledImg = img.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH)
        ImageIcon scaledImageIcon = new ImageIcon(scaledImg)

        backgroundLabel.setOpaque(true) //禁用label透明（必须是不透明才能看到背景）
        backgroundLabel.setBackground(new Color(0, 0, 0, 0)) //设置背景色为透明
        backgroundLabel.setIcon(scaledImageIcon) //添加图片


        /*文字Label及其样式设置*/
        countDownLabel = new JLabel(formattedText(targetDateTime))
        //设置水平和垂直对齐方式
        countDownLabel.setHorizontalAlignment(SwingConstants.CENTER)
        countDownLabel.setVerticalAlignment(SwingConstants.CENTER)
        //确保容器也能根据内容调整大小（仅在某些布局管理器下适用）
        countDownLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)) //添加一定的内边距
        countDownLabel.setPreferredSize(new Dimension(0, 0)) //设置首选尺寸为0，允许根据内容自动调整

        //设置字体样式、大小和颜色
        Font font = new Font("Microsoft YaHei", Font.BOLD, 22) //字体名称、样式（粗体）、大小
        //将字体和颜色应用到JLabel
        countDownLabel.setFont(font)
        countDownLabel.setForeground(Color.WHITE) //文本颜色
        countDownLabel.setOpaque(false)
//        countDownLabel.setBackground(Color.BLUE) //背景色
//        countDownLabel.repaint() //强制重绘组件

        backgroundLabel.add(countDownLabel, BorderLayout.CENTER)

        panel.add(backgroundLabel, BorderLayout.CENTER)
//        panel.add(countDownLabel, BorderLayout.CENTER)

        getContentPane().add(panel, BorderLayout.CENTER)

        startCountDown(targetDateTime)

//        pack() //自适应
        setAlwaysOnTop(true) //始终顶层显示
        setVisible(true) //显隐开关
    }

    /**
     * 启动倒计时任务
     * @param targetDateTime
     */
    void startCountDown(LocalDateTime targetDateTime) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor()
        Runnable updateCountDown = () -> {
            LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault())
            Duration remaining = Duration.between(now, targetDateTime)
            //剩余时间作判断
            if (!remaining.isNegative()) {
                countDownLabel.setText(formattedText(targetDateTime))
//                SwingUtilities.invokeLater(() -> {
//                })
            } else {
                scheduler.shutdown() //倒计时结束，关闭执行器
                JOptionPane.showMessageDialog(null, "倒计时已结束！")
//                SwingUtilities.invokeLater(() -> {
//                })
            }
        }
        //计算首次执行与当前时间的差值，并设置首次执行时间，之后每隔1秒执行1次
        long initSec = Duration.between(LocalDateTime.now(), targetDateTime).getSeconds()
        scheduler.schedule(updateCountDown, Math.max(0, initSec), TimeUnit.MILLISECONDS)
        scheduler.scheduleAtFixedRate(updateCountDown, 0, 1, TimeUnit.SECONDS)
    }

    /**
     * 格式化文本信息
     * @param dateTime
     * @return
     */
    private static String formattedText(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault())
        Duration duration = Duration.between(now, dateTime)
        long days = duration.toDays()
        duration = duration.minusDays(days)
        long hours = duration.toHours()
        duration = duration.minusHours(hours)
        long minutes = duration.toMinutes()
        duration = duration.minusMinutes(minutes)
        long seconds = duration.toSeconds()
        return "<html>郎酒浓酱兼香百亿销售倒计时: <br/><br/>" +
                "<p style=\"text-align: center\">" +
                "<font color=\"red\">${days}</font> 天 " +
                "<font color=\"red\">${hours}</font> 时 " +
                "<font color=\"red\">${minutes}</font> 分 " +
                "<font color=\"red\">${seconds}</font> 秒" +
                "</p>" +
                "<html/>"
    }

    static void main(String[] args) {
        LocalDateTime targetDate = LocalDateTime.of(2026, 1, 1, 0, 0)
        new CountDownDialog(targetDate)
//        SwingUtilities.invokeLater(() -> {
//        })
    }
}
