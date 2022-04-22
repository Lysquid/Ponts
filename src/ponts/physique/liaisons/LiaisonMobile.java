package ponts.physique.liaisons;

import java.awt.Color;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class LiaisonMobile extends Liaison {

    public LiaisonMobile(World world, Vec2 pos) {
        super(world, pos);

        couleurRemplissage = Color.decode("#e3f069");
        apercu = true;
    }

    public void supprimer(World world) {
        world.destroyBody(body);
    }

    public void activerPhysique() {
        body.setType(BodyType.DYNAMIC);
        apercu = false;
    }

    @Override
    public void creerObjetPhysique(World world) {
        creerObjetPhysique(world, BodyType.KINEMATIC);

    }

}
