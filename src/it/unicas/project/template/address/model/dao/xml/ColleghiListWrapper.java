package it.unicas.project.template.address.model.dao.xml;

import it.unicas.project.template.address.model.Colleghi;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of colleghis. This is used for saving the
 * list of colleghis to XML.
 *
 * @author Marco Jakob
 */
@XmlRootElement(name = "colleghis")
public class ColleghiListWrapper {

    private List<Colleghi> colleghis;

    @XmlElement(name = "colleghi")
    public List<Colleghi> getColleghis() {
        return colleghis;
    }

    public void setColleghis(List<Colleghi> colleghis) {
        this.colleghis = colleghis;
    }
}