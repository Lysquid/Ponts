import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class Pont {

    RevoluteJoint joint1;
    RevoluteJoint joint2;
    Barre barre1;
    Barre barre2;
    Liaison liaison;

    final float BAR_W = 8;
    final float BAR_H = 1;

    public Pont(World world, float x, float y) {

        barre1 = new Barre(world, x - BAR_W / 2 + BAR_H / 2, y, BAR_W, BAR_H);
        barre2 = new Barre(world, x + BAR_W / 2 - BAR_H / 2, y, BAR_W, BAR_H);
        liaison = new Liaison(world, x, y, BAR_H / 2);

        RevoluteJointDef rjd1 = new RevoluteJointDef();

        float maxMotorTorque = 1f;

        rjd1.initialize(barre1.body, liaison.body, new Vec2(x, y));
        rjd1.enableMotor = true;
        rjd1.maxMotorTorque = maxMotorTorque;

        joint1 = (RevoluteJoint) world.createJoint(rjd1);

        RevoluteJointDef rjd2 = new RevoluteJointDef();

        rjd2.initialize(liaison.body, barre2.body, new Vec2(x, y));
        rjd2.enableMotor = true;
        rjd2.maxMotorTorque = maxMotorTorque;

        joint2 = (RevoluteJoint) world.createJoint(rjd2);

    }

    public void draw(Graphics g, Box2D box2d) {
        barre1.draw(g, box2d);
        barre2.draw(g, box2d);
        liaison.draw(g, box2d);
    }

    public void checkBreak(World world, float dt) {
        if (joint1 != null) {
            Vec2 force = new Vec2();
            joint1.getReactionForce(1 / dt, force);
            float forceLength = force.length();
            if (forceLength > 1500000) {
                world.destroyJoint(joint1);
                joint1 = null;
            }

        }

    } 

}
