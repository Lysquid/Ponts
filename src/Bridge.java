import java.awt.Graphics;

import javax.swing.JPanel;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.dynamics.joints.WeldJoint;
import org.jbox2d.dynamics.joints.WeldJointDef;

public class Bridge {

    RevoluteJoint joint;
    Box bar1;
    Box bar2;

    final int BAR_W = 100;
    final int BAR_H = 10;

    public Bridge(World world, int x, int y) {

        bar1 = new Box(world, x - BAR_W / 2 + BAR_H / 2, y, BAR_W, BAR_H);
        bar2 = new Box(world, x + BAR_W / 2 - BAR_H / 2, y, BAR_W, BAR_H);

        RevoluteJointDef rjd = new RevoluteJointDef();

        rjd.initialize(bar1.body, bar2.body, new Vec2(x, y));

        rjd.enableMotor = true;
        rjd.maxMotorTorque = 200000000;

        joint = (RevoluteJoint) world.createJoint(rjd);

    }

    public void draw(Graphics g, JPanel frame) {
        bar1.draw(g, frame);
        bar2.draw(g, frame);

    }

    public void checkBreak(World world, float dt) {
        Vec2 force = new Vec2();
        joint.getReactionForce(1 / dt, force);
        float forceLength = force.length();
        if (forceLength > 1500000) {
            world.destroyJoint(joint);
        }

    }

}
