import java.awt.Graphics;

import javax.swing.JPanel;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class Bridge {

    RevoluteJoint joint;
    Barre barre1;
    Barre barre2;

    final float BAR_W = 6;
    final float BAR_H = 1;

    public Bridge(World world, float x, float y) {

        barre1 = new Barre(world, x - BAR_W / 2 + BAR_H / 2, y, BAR_W, BAR_H);
        barre2 = new Barre(world, x + BAR_W / 2 - BAR_H / 2, y, BAR_W, BAR_H);

        RevoluteJointDef rjd = new RevoluteJointDef();

        rjd.initialize(barre1.body, barre2.body, new Vec2(x, y));

        rjd.enableMotor = true;
        rjd.maxMotorTorque = 200000000;

        joint = (RevoluteJoint) world.createJoint(rjd);

    }

    public void draw(Graphics g, Box2D box2d) {
        barre1.draw(g, box2d);
        barre2.draw(g, box2d);

    }

    public void checkBreak(World world, float dt) {
        if (joint != null) {
            Vec2 force = new Vec2();
            joint.getReactionForce(1 / dt, force);
            float forceLength = force.length();
            if (forceLength > 1500000) {
                world.destroyJoint(joint);
                joint = null;
            }

        }

    }

}
