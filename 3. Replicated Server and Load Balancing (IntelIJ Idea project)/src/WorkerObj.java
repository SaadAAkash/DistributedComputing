/**
 * Created by akash on 8/17/17.
 */
public class WorkerObj {
    private String ip;
    private int port;

    public WorkerObj(String ip, int port) {
        this.ip=ip;
        this.port=port;
    }
    public int getPort(){
        return port;
    }
    public String getIp(){
        return ip;
    }
    public void setIp(String ip){
        this.ip=ip;
    }
    public void setPort(int port){
        this.port=port;
    }


}
