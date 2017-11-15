package it.unicas.project.template.address.server;

import it.unicas.project.template.address.model.Colleghi;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.mysql.ColleghiDAOMySQLImpl;
import it.unicas.project.template.address.model.dao.xml.ColleghiListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {

    public static String 	HOST = "localhost";
    public static int 		PORT = 5555;


    private int port;
    public Server(int port){
        this.port = port;
    }


    public static void main(String[] args) throws Exception {




        //Class.forName("com.mysql.jdbc.Driver");
        ServerSocket ss = new ServerSocket(PORT);
        String response = null;
        while(true){
            System.out.println("Server in ascolto sulla porta 5555");
            Socket s = ss.accept();


            ServSingleRequestest servSingleRequestest = new ServSingleRequestest(s);

            servSingleRequestest.run();



            //System.out.println(colleghiData);

            s.close();

        }


    }

}

class ServSingleRequestest{

    private Socket socket = null;
    private ObservableList<Colleghi> colleghiData = FXCollections.observableArrayList();



    public ServSingleRequestest(Socket socket){
        this.socket = socket;
    }

    public void run(){
        JAXBContext context = null;
        BufferedReader reader = null;
        ProtocolManager protocolManager = new ProtocolManager();
        try {

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String className = reader.readLine();
            String command = reader.readLine();
            String payLoad = reader.readLine();

            ProtocolMessage protocolMessage = new ProtocolMessage(className, command, payLoad);
            protocolManager.process(protocolMessage);

            context = JAXBContext
            .newInstance(ColleghiListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the stream and unmarshalling.
            ColleghiListWrapper wrapper = (ColleghiListWrapper) um.unmarshal(socket.getInputStream());
            colleghiData.addAll(wrapper.getColleghis());

        } catch (JAXBException | IOException | ProtocolException e) {
            e.printStackTrace();
        }


    }

}





class ProtocolManager {

    public final static String INSERT = "I";
    public final static String UPDATE = "U";
    public final static String DELETE = "D";
    public final static String SELECT = "S";


    public void process(ProtocolMessage protocolMessage) throws ProtocolException {

        if (protocolMessage == null || protocolMessage.getCommand() == null || protocolMessage.getClassName() == null) {
            throw new ProtocolException("protocolMessage cannot be null!");
        }
        DAO dao = null;
        if (protocolMessage.getClassName().equals("ColleghiDAOMySQLImpl")) {
            dao = ColleghiDAOMySQLImpl.getInstance();
        } else {
            throw new ProtocolException("Class: " + protocolMessage.getClassName() + " unknown.");
        }

        if (protocolMessage.getCommand().equalsIgnoreCase(INSERT)) {

        } else if (protocolMessage.getCommand().equalsIgnoreCase(UPDATE)) {

        } else if (protocolMessage.getCommand().equalsIgnoreCase(DELETE)) {

        } else if (protocolMessage.getCommand().equalsIgnoreCase(SELECT)) {

        }
    }
}