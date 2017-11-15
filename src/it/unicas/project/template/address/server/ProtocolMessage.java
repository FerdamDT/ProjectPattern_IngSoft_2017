package it.unicas.project.template.address.server;

public class ProtocolMessage {

    private String className = "";
    private String command = "";
    private String payload = "";

    public ProtocolMessage(String className, String command, String payload) {
        this.className = className;
        this.command = command;
        this.payload = payload;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
