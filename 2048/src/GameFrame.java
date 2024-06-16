import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public GameFrame(){
        setTitle("2048");  //标题
        setSize(370,420);  //设置窗体大小
        getContentPane().setBackground(new Color(66,136,83));  //设置默认背景颜色
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //关闭进程后退出
        setLocationRelativeTo(null);  //居中
        setResizable(false);  //设置窗体不允许变大




    }
}
