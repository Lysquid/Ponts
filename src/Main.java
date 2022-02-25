import javax.swing.JFrame;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class Main extends JFrame {

    final int WIDTH = 600;
    final int HEIGHT = 600;
    int refreshRate;

    
    public Main() {
        setTitle("Project");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        refreshRate = getRefreshRate();

        GamePanel panel = new GamePanel();
        add(panel);

        setVisible(true);
        panel.init(refreshRate);

    }
    
    public int getRefreshRate() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] gs = ge.getScreenDevices();
            DisplayMode dm = gs[0].getDisplayMode();
            return dm.getRefreshRate();
        } catch (Exception e) {
            e.printStackTrace();
            return 60;
        }

    }
    
    public static void main(String[] args) {
        System.out.println("hello remy");
        new Main();
    }

}
