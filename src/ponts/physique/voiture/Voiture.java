package ponts.physique.voiture;


import java.util.LinkedList;

import ponts.ihm.Box2D;
import ponts.physique.ObjetPhysique;
import ponts.physique.environnement.Bord;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Rot;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class Voiture {
    
    LinkedList<Roue> roues;
    Carrosserie carosserie;
}
