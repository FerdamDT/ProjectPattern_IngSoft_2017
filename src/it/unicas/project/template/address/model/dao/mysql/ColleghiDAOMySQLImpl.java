package it.unicas.project.template.address.model.dao.mysql;

import it.unicas.project.template.address.model.Colleghi;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;
import it.unicas.project.template.address.model.dao.xml.ColleghiListWrapper;
import it.unicas.project.template.address.server.Server;
import it.unicas.project.template.address.util.DateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class ColleghiDAOMySQLImpl implements DAO<Colleghi> {

    private ColleghiDAOMySQLImpl(){}

    private static DAO dao = null;
    private static Logger logger = null;
    public static DAO getInstance(){
        if (dao == null){
            dao = new ColleghiDAOMySQLImpl();
            logger = Logger.getLogger(ColleghiDAOMySQLImpl.class.getName());

        }
        return dao;
    }

    public static void main(String args[]) throws DAOException {
        ColleghiDAOMySQLImpl c = new ColleghiDAOMySQLImpl();

        /*
        c.insert(new Colleghi("Mario", "Rossi", "0824981", "molinara@uni.it", "21-10-2017", null));
        c.insert(new Colleghi("Carlo", "Ciampi", "0824982", "ciampi@uni.it", "22-02-2017", null));
        c.insert(new Colleghi("Ornella", "Vaniglia", "0824983", "vaniglia@uni.it", "23-05-2017", null));
        c.insert(new Colleghi("Cornelia", "Crudelia", "0824984", "crudelia@uni.it", "24-05-2017", null));
        c.insert(new Colleghi("Franco", "Bertolucci", "0824985", "bertolucci@uni.it", "25-10-2017", null));
        c.insert(new Colleghi("Carmine", "Labagnara", "0824986", "lagbagnara@uni.it", "26-10-2017", null));
        c.insert(new Colleghi("Mauro", "Cresta", "0824987", "cresta@uni.it", "27-12-2017", null));
        c.insert(new Colleghi("Andrea", "Coluccio", "0824988", "coluccio@uni.it", "28-01-2017", null));
        */

        List<Colleghi> list = c.select(null);
        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }


        Colleghi toDelete = new Colleghi();
        toDelete.setNome("");
        toDelete.setCognome("");
        toDelete.setEmail("");
        toDelete.setTelefono("");
        toDelete.setIdColleghi(56);

        c.delete(toDelete);

        list = c.select(null);

        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }

    }


    @Override
    public List<Colleghi> select(Colleghi a) throws DAOException {

        if (a == null){
            a = new Colleghi("", "", "", "", "", null); // Cerca tutti gli elementi
        }

        ArrayList<Colleghi> lista = new ArrayList<Colleghi>();
        try{

            if (a == null || a.getCognome() == null
                    || a.getNome() == null
                    || a.getEmail() == null
                    || a.getTelefono() == null){
                throw new DAOException("In select: any field can be null");
            }

            Statement st = DAOMySQLSettings.getStatement();

            String sql = "select * from colleghi where cognome like '";
            sql += a.getCognome() + "%' and nome like '" + a.getNome();
            sql += "%' and telefono like '" + a.getTelefono() + "%'";
            if (a.getCompleanno() != null){
                sql += "%' and compleanno like '" + a.getCompleanno() + "%'";
            }
            sql += " and email like '" + a.getEmail() + "%'";

            logger.info("SQL: " + sql);
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                lista.add(new Colleghi(rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("compleanno"),
                        rs.getInt("idcolleghi")));
            }
            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException sq){
            throw new DAOException("In select(): " + sq.getMessage());
        }
        return lista;
    }


    private void sendColleghi(Colleghi colleghi) throws Exception {

        ObservableList<Colleghi> colleghiData = FXCollections.observableArrayList();
        colleghiData.add(colleghi);

        Socket s = new Socket("localhost", Server.PORT);

        JAXBContext context = JAXBContext
                .newInstance(ColleghiListWrapper.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Wrapping our Colleghi data.
        ColleghiListWrapper wrapper = new ColleghiListWrapper();
        wrapper.setColleghis(colleghiData);

        // Marshalling and saving XML to the file.
        m.marshal(wrapper, s.getOutputStream());

        s.close();

    }



    @Override
    public void delete(Colleghi a) throws DAOException {
        if (a == null || a.getCognome() == null
                || a.getNome() == null
                || a.getEmail() == null
                || a.getTelefono() == null){
            throw new DAOException("In delete: any field can be null");
        }
        String query = "DELETE FROM colleghi WHERE idcolleghi='" + a.getIdColleghi() + "';";
        logger.info("SQL: " + query);

        Statement st = null;
        try {
            st = DAOMySQLSettings.getStatement();
            int n = st.executeUpdate(query);

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException e) {
            throw new DAOException("In delete(): " + e.getMessage());
        }
    }


    @Override
    public void insert(Colleghi a) throws DAOException {


        /*try {
            sendColleghi(a);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        if (a == null || a.getCognome() == null
                || a.getNome() == null
                || a.getEmail() == null
                || a.getCompleanno() == null
                || a.getTelefono() == null){
            throw new DAOException("In select: any field can be null");
        }


        String query = "INSERT INTO colleghi (nome, cognome, telefono, email, compleanno, idcolleghi) VALUES  ('" +
                a.getNome() + "', '" + a.getCognome() + "', '" +
                a.getTelefono() + "', '" + a.getEmail() + "', '" +
                DateUtil.format(a.getCompleanno()) + "', NULL)";
        logger.info("SQL: " + query);

        try {
            Statement st = DAOMySQLSettings.getStatement();
            int n = st.executeUpdate(query);

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException e) {
            throw new DAOException("In update(): " + e.getMessage());
        }
    }


    @Override
    public void update(Colleghi a) throws DAOException {
        if (a == null || a.getCognome() == null
                || a.getNome() == null
                || a.getEmail() == null
                || a.getCompleanno() == null
                || a.getTelefono() == null){
            throw new DAOException("In select: any field can be null");
        }

        String query = "UPDATE colleghi SET nome = '" + a.getNome() + "', cognome = '" + a.getCognome() + "',  telefono = '" + a.getTelefono() + "', email = '" + a.getEmail() + "', compleanno = '" + DateUtil.format(a.getCompleanno()) + "'";
        query = query + " WHERE idcolleghi = " + a.getIdColleghi() + ";";
        logger.info("SQL: " + query);

        try {
            Statement st = DAOMySQLSettings.getStatement();
            int n = st.executeUpdate(query);

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException e) {
            throw new DAOException("In update(): " + e.getMessage());
        }
    }

}
