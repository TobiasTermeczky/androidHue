package nl.yzaazy.hue.Models;

public class HueBridge {
    String name;
    String ip;

    public HueBridge(String name, String ip){
        this.name = name;
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }
}
