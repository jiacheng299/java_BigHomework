package application;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
/**
 * class that handles the pet's movement
 * @version 0.1.0
 * @author ppcat
 * @since 0.1.0
 * @date 2022-12-18 18:09:38
 **/
public class Move extends Thread {
    private long time;
    private ImageView imageView;
    private int direID;
    double x;
    double maxx;
    double width;
    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    Stage stage;
    private EventListener listen;
    boolean exit;

    public Move(long time, ImageView imgView, int dire, Stage primaryStage, EventListener el) {
        this.time = time;
        imageView = imgView;
        direID = dire;
        stage = primaryStage;
        listen = el;
    }

    public void run() {
        //点击就停下
        /*使用listen.petID而不是定义一个变量int petID = listen.petID;
         *是因为在运动过程中点击“切换宠物”时实际的petID会改变，所以使用listen.petID就可以做到同步改变。
         *若下面使用listen.mainimg(petID,0)显示的就是点击“切换宠物”前的宠物，这个petID就是旧的petID。
         */
        imageView.addEventHandler(MouseEvent.MOUSE_PRESSED,
                e -> {
                    exit = true;
                    listen.mainImg();
                    Main.getUi().getMessageBox().setVisible(false);
                });
        while (!exit) {
            //如果petID!=listen.petID，则已“切换宠物”，此时要结束运动。
            if (!"Move".equals(listen.getBehavior()))
                break;
            width = imageView.getBoundsInLocal().getMaxX() - 600;
            x = stage.getX();
            maxx = screenBounds.getMaxX();
            double speed = 0.12;
            if ((direID == 1 && x + speed + width >= maxx) || time <= 0) {
                this.interrupt();
                listen.mainImg();
                return;
            } else if (direID == 0 && x - speed <= 0) {
                this.interrupt();
                listen.mainImg();
                return;
            }
            if (direID == 0) {    //向左走
                stage.setX(x - speed);
            } else if (direID == 1) {    //向右走
                stage.setX(x + speed);
            }
            time -= 1;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
