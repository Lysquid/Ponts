package ponts.physique.voiture;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import ponts.ihm.Box2D;

import java.awt.Color;
import java.awt.Graphics;

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
    Color couleurContour;
    Color couleurRemplissage;
    

    public Roue (World world, Vec2 pos){

        couleurContour = Color.GREEN;
        

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
        jointDef.initialize(body, carosserie.getBody(), body.getPosition() ,carosserie.getPos());
        joint = (WheelJoint) world.createJoint(jointDef);
        joint.enableMotor(false);


    }

    public void activerPhysique() {
        body.setType(BodyType.DYNAMIC);
        joint.enableMotor(true);
        joint.setMotorSpeed(0.5f);
    }

    public void dessiner(Graphics g, Box2D box2d) {
        
        int x = box2d.worldToPixelX(getX());
        int y = box2d.worldToPixelY(getY());
        int r = box2d.worldToPixel(RAYON);

        g.setColor(couleurContour);
        

        
        g.fillOval(x - r, y - r, r * 2, r * 2);
        g.drawOval(x - r, y - r, r * 2, r * 2);
    }
        


}
