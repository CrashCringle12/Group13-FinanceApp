/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lamar Cooley
 */
@Entity
@Table(name = "ALERTMODEL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alertmodel.findAll", query = "SELECT a FROM Alertmodel a"),
    @NamedQuery(name = "Alertmodel.findById", query = "SELECT a FROM Alertmodel a WHERE a.id = :id"),
    @NamedQuery(name = "Alertmodel.findBySeverity", query = "SELECT a FROM Alertmodel a WHERE a.severity = :severity"),
    @NamedQuery(name = "Alertmodel.findByAccountname", query = "SELECT a FROM Alertmodel a WHERE a.accountname = :accountname"),
    @NamedQuery(name = "Alertmodel.findByAccountnameAdvanced", query = "SELECT a FROM Alertmodel a WHERE LOWER(a.accountname) LIKE CONCAT('%', LOWER(:accountname), '%')"),
    @NamedQuery(name = "Alertmodel.findByDescription", query = "SELECT a FROM Alertmodel a WHERE LOWER(a.description) LIKE CONCAT('%', LOWER(:description), '%')"),
    @NamedQuery(name = "Alertmodel.findByDate", query = "SELECT a FROM Alertmodel a WHERE a.date = :date")})

public class Alertmodel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "SEVERITY")
    private Boolean severity;
    @Lob        @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ACCOUNTNAME")
    private String accountname;
    @Column(name = "DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Alertmodel() {
    }

    public Alertmodel(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getSeverity() {
        return severity;
    }

    public void setSeverity(Boolean severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alertmodel)) {
            return false;
        }
        Alertmodel other = (Alertmodel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Alertmodel[ id=" + id + " ]";
    }
    
}
