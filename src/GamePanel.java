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

    Box2D box2d;
    World world;
    Timer graphicsTimer;
    Timer physicsTimer;
    final int PHYSICS_TICK = 16;
    long physicsTime;

    ArrayList<Barre> boxes;
    ArrayList<Pont> bridges;

    boolean isMousePressed;
    int mouseButton;
    float mouseX;
    float mouseY;
    boolean initialized = false;

    long prevTime = 0;
    final int SPAWN_DELAY = 100;

    public GamePanel() {

        boxes = new ArrayList<Barre>();
        bridges = new ArrayList<Pont>();

    }

    public void init(int refreshRate) {

        box2d = new Box2D(getWidth(), getHeight());

        // Create World
        Vec2 gravity = new Vec2(0.0f, -9.81f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setContinuousPhysics(true);

        new Bord(world, box2d.largeur, box2d.hauteur);

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

        for (Barre box : boxes) {
            box.draw(g, box2d);
        }

        for (Pont bridge : bridges) {
            bridge.draw(g, box2d);
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
                            Barre newBox = new Barre(world, mouseX, mouseY, 5, 3);
                            boxes.add(newBox);
                            break;
                        case 3:
                            bridges.add(new Pont(world, mouseX, mouseY));
                            break;
                    }
                    prevTime = time;

                }

            }

            long newPhysicsTime = System.currentTimeMillis();
            float dt = (newPhysicsTime - physicsTime) / 1000f;
            physicsTime = newPhysicsTime;

            for (Pont bridge : bridges) {
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
        mouseX = box2d.pixelToWorldX(e.getX());
        mouseY = box2d.pixelToWorldY(e.getY());
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
        mouseX = box2d.pixelToWorldX(e.getX());
        mouseY = box2d.pixelToWorldY(e.getY());
        if (SwingUtilities.isLeftMouseButton(e)) {
            mouseButton = 1;
        } else if (SwingUtilities.isRightMouseButton(e)) {
            mouseButton = 3;
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = box2d.pixelToWorldX(e.getX());
        mouseY = box2d.pixelToWorldY(e.getY());
        if (SwingUtilities.isLeftMouseButton(e)) {
            mouseButton = 1;
        } else if (SwingUtilities.isRightMouseButton(e)) {
            mouseButton = 3;
        }

    }
}
