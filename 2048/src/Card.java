import java.awt.*;

public class Card {
    private int x = 0;//x坐标
    private int y = 0;//y坐标
    private int w = 80;//宽
    private int h = 80;//高
    private int i = 0;//下标i
    private int j = 0;//下标j

    private int start = 10; //偏移量

    private int num = 0;
    private boolean merge = false; //是否合并，如果已经合并则不能再合并

    public Card(int i, int j) {
        this.i = i;
        this.j = j;

        cal();
    }

    //计算坐标
    private void cal() {
        this.x = start + j * w + (j + 1) * 5;
        this.y = start + i * h + (i + 1) * 5;
    }

    //卡片的绘制
    public void draw(Graphics g) {
        //根据数字获取相应的颜色
        Color color = getColor();

        Color ocolor = g.getColor();
        //设置新颜色
        g.setColor(color);

        g.fillRoundRect(x, y, w, h, 4, 4);

        if (num != 0) {
            g.setColor(new Color(125, 78, 51));
            Font font = new Font("思源宋体", Font.BOLD, 30);
            g.setFont(font);
            String text = num + "";
            int textLen = getWordWidth(font, text, g);
            int tx = x + (w - textLen) / 2;
            int ty = y + 50;
            g.drawString(text, tx, ty);
        }
        //还原颜色
        g.setColor(ocolor);
    }

    public static int getWordWidth(Font font, String content, Graphics g) {
        FontMetrics metrics = g.getFontMetrics(font);
        int width = 0;
        for (int i = 0; i < content.length(); i++) {
            width += metrics.charWidth(content.charAt(i));
        }
        return width;
    }

    //获取颜色

    private Color getColor() {
        Color color = null;
        switch (num) {
            case 2:
                color = new Color(238, 244, 234);
                break;
            case 4:
                color = new Color(222, 236, 200);
                break;
            case 8:
                color = new Color(142, 201, 75);
                break;
            case 16:
                color = new Color(142, 201, 75);
                break;
            case 32:
                color = new Color(111, 148, 48);
                break;
            case 64:
                color = new Color(174, 213, 130);
                break;
            case 128:
                color = new Color(68, 180, 144);
                break;
            case 256:
                color = new Color(45, 130, 120);
                break;
            case 512:
                color = new Color(9, 97, 26);
                break;
            case 1024:
                color = new Color(242, 177, 121);
                break;
            case 2048:
                color = new Color(223, 185, 0);
                break;
            default:   //默认颜色
                color = new Color(92, 151, 117);
        }
        return color;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return this.num;
    }

    //向上移动的方法
    public boolean moveTop(Card[][] cards, boolean b) {
        //设定递归的退出条件
        if (i == 0) {
            return false;
        }
        //上一个卡片
        Card prev = cards[i - 1][j];

        if (prev.getNum() == 0) {//交换上去的
            if (b) {
                prev.num = this.num;
                this.num = 0;
                prev.moveTop(cards, b);
            }
            return true;
        } else if (prev.getNum() == num && !prev.merge) {//要合并
            if (b) {
                prev.merge = true;
                prev.num = this.num * 2;
                this.num = 0;
            }
            return true;
        } else {
            return false;
        }

    }

    public void setMerge(boolean b) {
        this.merge = b;
    }

    public boolean moveRight(Card[][] cards, boolean b) {
        if (j == 3) {
            return false;
        }
        //上一个卡片
        Card prev = cards[i][j + 1];

        if (prev.getNum() == 0) {//交换上去的
            if (b) {
                prev.num = this.num;
                this.num = 0;
                prev.moveRight(cards, b);
            }
            return true;
        } else if (prev.getNum() == num && !prev.merge) {//要合并
            prev.merge = true;
            prev.num = this.num * 2;
            this.num = 0;

            return true;
        } else {
            return false;
        }
    }

    public boolean moveLeft(Card[][] cards, boolean b) {
        if (j == 0) {
            return false;
        }
        //上一个卡片
        Card prev = cards[i][j - 1];

        if (prev.getNum() == 0) {//交换上去的
            if (b) {
                prev.num = this.num;
                this.num = 0;
                prev.moveLeft(cards, b);
            }
            return true;

        } else if (prev.getNum() == num && !prev.merge) {//要合并
            if (b) {
                prev.merge = true;
                prev.num = this.num * 2;
                this.num = 0;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean moveBottom(Card[][] cards, boolean b) {
        //设置递归停止条件
        if (i == 3) {
            return false;
        }
        //上一个卡片
        Card prev = cards[i + 1][j];

        if (prev.getNum() == 0) {//交换上去的
            if (b) {
                prev.num = this.num;
                this.num = 0;
                prev.moveBottom(cards, b);
            }
            return true;
        } else if (prev.getNum() == num && !prev.merge) {//要合并
            if (b) {
                prev.merge = true;
                prev.num = this.num * 2;
                this.num = 0;
            }
            return true;
        } else {
            return false;
        }
    }
    public static boolean moveBack(Card[][] cards, boolean b){
        return true;

    }
}
