package mx.edu.um.cursos.modelo;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.Version;

/**
 *
 * @author jdmr
 */
@Entity
@NamedQuery(name="buscaPorCurso",query="select p from Periodo p where p.curso = :curso")
public class Periodo implements java.io.Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dia = new Date();
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date inicio = new Date();
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date fin = new Date();
    @ManyToOne
    private Curso curso;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return the dia
     */
    public Date getDia() {
        return dia;
    }

    /**
     * @param dia the dia to set
     */
    public void setDia(Date dia) {
        this.dia = dia;
    }

    /**
     * @return the inicio
     */
    public Date getInicio() {
        return inicio;
    }

    /**
     * @param inicio the inicio to set
     */
    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    /**
     * @return the fin
     */
    public Date getFin() {
        return fin;
    }

    /**
     * @param fin the fin to set
     */
    public void setFin(Date fin) {
        this.fin = fin;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdfDia = new SimpleDateFormat("EEEE");
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        return sdfDia.format(dia)+" ["+sdfHora.format(inicio)+" - "+sdfHora.format(fin)+"] " + curso;
    }

}
