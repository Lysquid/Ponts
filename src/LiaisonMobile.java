import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class LiaisonMobile extends Liaison {

    public LiaisonMobile(World world, Vec2 pos) {
        super(world, pos, true);
    }

}
