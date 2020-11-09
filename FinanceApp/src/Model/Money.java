/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author chees
 */
@Entity
@Table(name = "MONEY")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Money.findAll", query = "SELECT m FROM Money m")
    , @NamedQuery(name = "Money.findById", query = "SELECT m FROM Money m WHERE m.id = :id")
    , @NamedQuery(name = "Money.findBySpent", query = "SELECT m FROM Money m WHERE m.spent = :spent")
    , @NamedQuery(name = "Money.findByAdded", query = "SELECT m FROM Money m WHERE m.added = :added")
})
public class Money implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "SPENT")
    private Double spent;
    @Column(name = "ADDED")
    private Double added;

    public Money()
    {
    }

    public Money(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Double getSpent()
    {
        return spent;
    }

    public void setSpent(Double spent)
    {
        this.spent = spent;
    }

    public Double getAdded()
    {
        return added;
    }

    public void setAdded(Double added)
    {
        this.added = added;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Money))
        {
            return false;
        }
        Money other = (Money) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Model.Money[ id=" + id + " ]";
    }
    
}
