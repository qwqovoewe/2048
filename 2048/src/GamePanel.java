import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Stack;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
public class GamePanel extends JPanel implements ActionListener {//游戏面板的绘制和逻辑处理。
    private static final int ROWS = 4;
    private static final int COLS = 4;
    private JFrame frame = null;
    private GamePanel panel = null;

    private Card[][] cards = new Card[ROWS][COLS];
    private Stack<Card[][]> history=new Stack<>();

    private String gameFlag = "start";

    public GamePanel(JFrame frame) {
        this.setLayout(null);
        this.setOpaque(false);
        this.frame = frame;
        this.panel = this;

        creatMenu();//创建菜单
        //创建卡片
        initCard();
        //随机创建一个卡片
        creatRandomNum();

        //创建键盘监听
        creatKeyListener();

    }

    private void creatKeyListener() {
        KeyAdapter keyAdapter = new KeyAdapter() { //ctrl + o
            @Override
            public void keyPressed(KeyEvent e) {
                //System.out.println(111);
                if (!"start".equals(gameFlag)) {
                    return;
                }
                int key = e.getKeyCode();
                switch (key) {
                    //向上
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                        moveCard(1);
                        break;
                    //向右
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        moveCard(2);
                        break;
                    //向下
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        moveCard(3);
                        break;
                    //向左
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                        moveCard(4);
                        break;

                }
            }
        };
        frame.addKeyListener(keyAdapter);
    }

    //按方向移动卡片
    private void moveCard(int dir) {
        //清理卡片的合并标记
        clearCard();
        if (dir == 1) {
            moveCardTop(true);
        } else if (dir == 2) {
            moveCardRight(true);
        } else if (dir == 3) {
            moveCardBottom(true);
        } else if (dir == 4) {
            moveCardLeft(true);
        }
        //新创建卡片
        creatRandomNum();
        //重绘画布
        repaint();
        //判断游戏是否结束
        gameOverOrNot();
    }

    private void gameOverOrNot() {
        if (isWin()) {
            gameWin();
        } else if (cardIsFull()) {
            if (moveCardTop(false) ||
                    moveCardRight(false) ||
                    moveCardLeft(false) ||
                    moveCardBottom(false)) {
                return;

            } else {
                gameOver();
            }
        }
    }

    private void gameWin() {
        gameFlag ="end";
        //

        UIManager.put("JOptionPane.buttonFont",new FontUIResource(new Font("思源宋体",Font.ITALIC,18)));
        UIManager.put("JOptionPane.messageFont",new FontUIResource(new Font("思源宋体",Font.ITALIC,18)));
        JOptionPane.showMessageDialog(frame,"你成功了，太棒了！");
    }

    private boolean isWin() {
        Card card;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                card = cards[i][j];
                if (card.getNum() == 2048) {
                    return true;
                }
            }
        }
        return false;
    }

    private void gameOver() {
        gameFlag ="end";

        UIManager.put("JOptionPane.buttonFont",new FontUIResource(new Font("思源宋体",Font.ITALIC,18)));
        UIManager.put("JOptionPane.messageFont",new FontUIResource(new Font("思源宋体",Font.ITALIC,18)));
        JOptionPane.showMessageDialog(frame,"你失败了，再接再厉！");
    }

    private void clearCard() {
        Card card;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                card = cards[i][j];
                card.setMerge(false);
            }
        }
    }

    private boolean moveCardTop(boolean b) {
        boolean res = false;
        System.out.println("movetop");
        Card card;
        history.push(Clone(cards));
        for (int i = 1; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                card = cards[i][j];

                if (card.getNum() != 0) {//只要卡片不是空白卡片，就要移动
                    if (card.moveTop(cards, b)) {
                        res = true;
                        card.moveTop(cards, b);
                    }
                }
            }
        }
        return res;
    }

    private Card[][] Clone(Card[][] cards) {
            Card[][] clonedCards = new Card[ROWS][COLS];
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++) {
                    clonedCards[i][j] = cards[i][j].CLone();
                }
            }
            return clonedCards;

    }

    private boolean moveCardLeft(boolean b) {
        boolean res = false;
        System.out.println("moveleft");
        Card card;
        history.push(Clone(cards));
        for (int i = 0; i < ROWS; i++) {
            for (int j = 1; j < COLS; j++) {
                card = cards[i][j];

                if (card.getNum() != 0) {//只要卡片不是空白卡片，就要移动
                    if (card.moveLeft(cards, b)) {
                        res = true;
                    }
                }
            }
        }
        return res;
    }

    private boolean moveCardBottom(boolean b) {
        boolean res = false;
        System.out.println("movebottom");
        Card card;
        history.push(Clone(cards));
        for (int i = ROWS - 2; i >= 0; i--) {
            for (int j = 0; j < COLS; j++) {
                card = cards[i][j];

                if (card.getNum() != 0) {//只要卡片不是空白卡片，就要移动
                    if (card.moveBottom(cards, b)) {
                        res = true;
                    }
                }
            }
        }
        return res;
    }

    private boolean moveCardRight(boolean b) {
        System.out.println("moveright");
        boolean res = false;
        Card card;
        history.push(Clone(cards));
        for (int i = 0; i < ROWS; i++) {
            for (int j = COLS - 2; j >= 0; j--) {
                card = cards[i][j];

                if (card.getNum() != 0) {//只要卡片不是空白卡片，就要移动
                    if (card.moveRight(cards, b)) {
                        res = true;
                    }
                }
            }
        }
        return res;
    }


    private void creatRandomNum() {
        int num = 0;
        Random random = new Random();
        int n = random.nextInt(5) + 1; //随机取出1-5
        if (n == 1) {
            num = 4;
        } else {
            num = 2;
        }
        //如果格子满了，则不需要找了
        if (cardIsFull()) {
            return;
        }
        //取到卡片
        Card card = getRandomCard(random);

        if (card != null) {
            card.setNum(num);

        }


        //设置卡片

    }

    private boolean cardIsFull() {
        Card card;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                card = cards[i][j];
                if (card.getNum() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private Card getRandomCard(Random random) {
        int i = random.nextInt(ROWS);
        int j = random.nextInt(COLS);
        Card card = cards[i][j];
        if (card.getNum() == 0) {//如果是空白的卡片，则直接返回
            return card;
        }
        //没找到，则递归，继续找
        return getRandomCard(random);


    }

    //创建卡片
    private void initCard() {
        Card card;
        history=new Stack<>();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                card = new Card(i, j);
                cards[i][j] = card;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //绘制卡片
        drawCard(g);
    }

    private void drawCard(Graphics g) {
        Card card;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                card = cards[i][j];
                card.draw(g);
            }
        }
    }

    //创建字体
    private Font creatFont() {
        return new Font("思源宋体", Font.BOLD, 18);
    }

    //创建菜单
    private void creatMenu() {
        // 创建字体
        Font tfont = creatFont();
        // 创建JMenuBar
        JMenuBar jmb = new JMenuBar();

        JMenu jMenu1 = new JMenu("游戏");
        jMenu1.setFont(tfont);

        // 创建子项
        JMenuItem jmi1 = new JMenuItem("新游戏");
        jmi1.setFont(tfont);
        JMenuItem jmi2 = new JMenuItem("退出");
        jmi2.setFont(tfont);
        JMenuItem jmi5 = new JMenuItem("返回上一步");
        jmi5.setFont(tfont);
        JMenuItem jmi6 = new JMenuItem("保存游戏");
        jmi6.setFont(tfont);
        JMenuItem jmi7 = new JMenuItem("加载游戏");
        jmi7.setFont(tfont);

        jMenu1.add(jmi1);
        jMenu1.add(jmi5);
        jMenu1.add(jmi6);
        jMenu1.add(jmi7);
        jMenu1.add(jmi2);

        JMenu jMenu2 = new JMenu("帮助");
        jMenu2.setFont(tfont);

        JMenuItem jmi3 = new JMenuItem("操作帮助");
        jmi3.setFont(tfont);

        JMenuItem jmi4 = new JMenuItem("胜利条件");
        jmi4.setFont(tfont);

        jMenu2.add(jmi3);
        jMenu2.add(jmi4);

        jmb.add(jMenu1);
        jmb.add(jMenu2);

        frame.setJMenuBar(jmb);

        // 添加事件监听(点击)
        jmi1.addActionListener(this);
        jmi2.addActionListener(this);
        jmi3.addActionListener(this);
        jmi4.addActionListener(this);
        jmi5.addActionListener(this);
        jmi6.addActionListener(this);
        jmi7.addActionListener(this);

        // 设置指令
        jmi1.setActionCommand("restart");
        jmi2.setActionCommand("exit");
        jmi3.setActionCommand("help");
        jmi4.setActionCommand("win");
        jmi5.setActionCommand("return");
        jmi6.setActionCommand("save");
        jmi7.setActionCommand("load");
    }

    /*@Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(command.equals("restart")){
            System.out.println("新游戏");
            restart();
        }
        else if(command.equals("exit")){
            System.out.println("退出");
            Object[] options = {"确定","取消"};
            int res =JOptionPane.showOptionDialog(this,"你确定退出游戏吗？","",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
            if(res == 0){
                System.exit(0);
            }
        }
        else if(command.equals("help")){
            System.out.println("帮助");
            JOptionPane.showMessageDialog(null, "通过键盘的上下左右来移动，相同的数字会合并",
                    "提示！", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(command.equals("win")){
            System.out.println("胜利条件");
            JOptionPane.showMessageDialog(null, "得到数字2048获得胜利，当没有空卡片则失效",
                    "提示！", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(command.equals("return")){
            System.out.println("返回上一步");
            Back();
        }
    }*/
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("restart")) {
            restart();
        } else if (command.equals("exit")) {
            Object[] options = {"确定", "取消"};
            int res = JOptionPane.showOptionDialog(this, "你确定要退出游戏吗？", "", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (res == 0) {
                System.exit(0);
            }
        } else if (command.equals("help")) {
            JOptionPane.showMessageDialog(null, "通过键盘的上下左右来移动，相同的数字会合并", "提示！", JOptionPane.INFORMATION_MESSAGE);
        } else if (command.equals("win")) {
            JOptionPane.showMessageDialog(null, "得到数字2048获得胜利，当没有空卡片则失效", "提示！", JOptionPane.INFORMATION_MESSAGE);
        } else if (command.equals("return")) {
            Back();
        } else if (command.equals("save")) {
            saveGameState("game_save.dat");
        } else if (command.equals("load")) {
            loadGameState("game_save.dat");
        }
    }

    private void Back() {
        if(!history.isEmpty()){
            Card card;
            Card[][] cards1 = new Card[ROWS][COLS];
            cards1=history.pop();
            setCurrentCards(cards1);
            //重绘画布
            repaint();
        }
        else{
            JOptionPane.showMessageDialog(null, "没有上一步了",
                    "提示！", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }

    private void setCurrentCards(Card[][] cards1) {
        this.cards=cards1;
    }

    // 新游戏
    private void restart() {
        GameFrame frame1 = new GameFrame();
        GamePanel panel1 = new GamePanel(frame1);
        frame1.add(panel1);
        frame1.setVisible(true);
        frame.setVisible(false);
    }
    //保存游戏状态到文件
    public void saveGameState(String filename) {
        GameState gameState = new GameState(cards, gameFlag);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(gameState);
            JOptionPane.showMessageDialog(null, "游戏进度已保存！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "保存游戏进度失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    //从文件中加载游戏状态
    public void loadGameState(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            GameState gameState = (GameState) ois.readObject();
            this.cards = gameState.getCards();
            this.gameFlag = gameState.getGameFlag();
            repaint();
            JOptionPane.showMessageDialog(null, "游戏进度已加载！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "加载游戏进度失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
