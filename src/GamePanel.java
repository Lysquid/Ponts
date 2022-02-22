import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class GamePanel extends JPanel implements ActionListener, MouseInputListener {
    World world;
    Timer graphicsTimer;
    Timer physicsTimer;
    final int PHYSICS_TICK = 8 * 2;
    long physicsTime;

    ArrayList<Box> boxes;
    ArrayList<Bridge> bridges;

    final int WIDTH = 40;

    boolean isMousePressed;
    int mouseButton;
    int mouseX;
    int mouseY;
    boolean initialized = false;

    long prevTime = 0;
    final int SPAWN_DELAY = 100;

    public GamePanel() {
        boxes = new ArrayList<Box>();
        bridges = new ArrayList<Bridge>();

    }

    public void init(int refreshRate) {

        // Create World
        Vec2 gravity = new Vec2(0.0f, 500.0f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setContinuousPhysics(true);

        new Boundary(world, getWidth(), getHeight());

        addMouseListener(this);
        addMouseMotionListener(this);

        physicsTimer = new Timer(PHYSICS_TICK, this);
        physicsTimer.start();
        physicsTime = System.currentTimeMillis();

        int fps = (int) (1.0 / refreshRate * 1000.0);
        graphicsTimer = new Timer(fps, this);
        graphicsTimer.start();

        initialized = true;

    }

    public void paint(Graphics g) {

        Toolkit.getDefaultToolkit().sync();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (Box box : boxes) {
            box.draw(g, this);
        }

        for (Bridge bridge : bridges) {
            bridge.draw(g, this);
        }

    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == physicsTimer) {

            if (isMousePressed) {

                long time = System.currentTimeMillis();

                // System.out.println(time - prevTime);

                if (time - prevTime > SPAWN_DELAY) {
                    switch (mouseButton) {
                        case 1:
                            Box newBox = new Box(world, mouseX, mouseY, WIDTH, WIDTH);
                            boxes.add(newBox);
                            break;
                        case 3:
                            bridges.add(new Bridge(world, mouseX, mouseY));
                            break;
                    }
                    prevTime = time;

                }

            }

            long newPhysicsTime = System.currentTimeMillis();
            float dt = (newPhysicsTime - physicsTime) / 1000f;
            physicsTime = newPhysicsTime;

            for (Bridge bridge : bridges) {
                bridge.checkBreak(world, dt);
            }

            world.step(dt, 10, 8);
        }

        if (e.getSource() == graphicsTimer) {
            repaint();
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        isMousePressed = true;
        mouseX = e.getX();
        mouseY = e.getY();
        if (SwingUtilities.isLeftMouseButton(e)) {
            mouseButton = 1;
        } else if (SwingUtilities.isRightMouseButton(e)) {
            mouseButton = 3;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isMousePressed = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        if (SwingUtilities.isLeftMouseButton(e)) {
            mouseButton = 1;
        } else if (SwingUtilities.isRightMouseButton(e)) {
            mouseButton = 3;
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        if (SwingUtilities.isLeftMouseButton(e)) {
            mouseButton = 1;
        } else if (SwingUtilities.isRightMouseButton(e)) {
            mouseButton = 3;
        }

    }
}
