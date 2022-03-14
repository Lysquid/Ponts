import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class LiaisonFixe extends Liaison {

    public LiaisonFixe(World world, Vec2 pos) {
        super(world, pos, false);
    }
}
