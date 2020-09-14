package NettyStudy.v11;

public class TankMsg {

    int x,y;

    public TankMsg(){

    }

    public TankMsg(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "TankMsg{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
