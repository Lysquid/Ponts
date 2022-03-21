package ponts.physique.voiture;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import ponts.physique.ObjetPhysique;
import ponts.physique.barres.Barre;
import ponts.physique.environnement.Bord;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.dynamics.joints.WheelJoint;
import org.jbox2d.dynamics.joints.WheelJointDef;

public class Roue extends ObjetPhysique{
    
    public static final int CATEGORY = 0b1000;
    public static final int MASK = Bord.CATEGORY | Barre.CATEGORY;

    static final float RAYON = 0.5f;

    FixtureDef fixtureDef;
    Fixture fixture;

    WheelJoint joint;
    CircleShape shape;
    

    public Roue (World world, Vec2 pos){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position.set(pos);

        body = world.createBody(bodyDef);
        shape = new CircleShape();

        fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;

        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;

        
    }

    public void lierVoiture (World world, Carrosserie carosserie) {
        WheelJointDef jointDef = new WheelJointDef();
        // jointDef.initialize(body, carosserie.getBody(), carosserie.getPos());
        WheelJoint joint = (WheelJoint) world.createJoint(jointDef);
    }

    public void activerPhysique() {
        body.setType(BodyType.DYNAMIC);
    }


}
