package mx.edu.um.cursos.dao.impl;

import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import mx.edu.um.cursos.dao.CursoDao;
import mx.edu.um.cursos.modelo.Curso;
import mx.edu.um.cursos.modelo.Periodo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jdmr
 */
@Repository("cursoDao")
@Transactional
public class CursoDaoImpl implements CursoDao {
    private static final Log log = LogFactory.getLog(CursoDaoImpl.class);

    private HibernateTemplate hibernateTemplate;

    @Autowired
    protected void setSessionFactory(SessionFactory sessionFactory) {
        hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    @Transactional(readOnly=true)
    public List<Curso> lista(Map<String, String> params) {
        log.debug("Lista de cursos");
        List<Curso> cursos;
        if (params != null && params.containsKey("filtro")) {
            String filtro = "%"+params.get("filtro").trim().toUpperCase()+"%";
            cursos = hibernateTemplate.findByNamedQueryAndNamedParam("buscaPorFiltro", "filtro", filtro);
        } else {
            cursos = hibernateTemplate.loadAll(Curso.class);
        }
        return cursos;
    }

    @Transactional(readOnly=true)
    public Curso obtiene(Long id) {
        log.debug("Buscando el curso "+id);
        return hibernateTemplate.get(Curso.class, id);
    }

    public Curso crea(Curso curso) {
        log.debug("Creando el curso "+curso);
        Long id = (Long)hibernateTemplate.save(curso);
        curso.setId(id);
        return curso;
    }

    public Curso actualiza(Curso curso) {
        log.debug("Actualizando el curso "+curso);
        hibernateTemplate.update(curso);
        return curso;
    }

    public String elimina(Long id) {
        log.debug("Eliminando el curso "+id);
        Curso curso = hibernateTemplate.load(Curso.class, id);
        String nombre = id.toString();
        hibernateTemplate.delete(curso);
        return nombre;
    }

    public void creaPeriodo(Periodo periodo) {
        log.debug("Creando el periodo "+periodo+" para el curso "+ periodo.getCurso());
        Curso curso = periodo.getCurso();
        hibernateTemplate.refresh(curso);
        hibernateTemplate.save(periodo);
    }

    public List<Periodo> periodos(Curso curso) {
        log.debug("Buscando los periodos del curso "+curso);
        List<Periodo> periodos = hibernateTemplate.findByNamedQueryAndNamedParam("buscaPorCurso", "curso", curso);
        return periodos;
    }

    public Curso valida(Long id, User user) {
        boolean esHora = false;
        boolean miComunidad = false;
        Curso curso = hibernateTemplate.get(Curso.class, id);
        for(Group group : user.getMyPlaces()) {
            log.debug("Grupo "+group.getGroupId()+":"+group.getDescriptiveName()+" | "+curso.getComunidadId());
            if (group.getGroupId()==curso.getComunidadId()) {
                miComunidad = true;
                break;
            }
        }
        if (miComunidad) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date hoy = new Date();
            if (hoy.after(curso.getFechaInicio()) && hoy.before(curso.getFechaFinal())) {
                log.debug("CURSO ACTIVO");
                for(Periodo periodo : curso.getPeriodos()) {
                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTime(hoy);
                    cal1.add(Calendar.HOUR_OF_DAY, -5);
                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(periodo.getDia());
                    log.debug("Comparando dias "+cal1.get(Calendar.DAY_OF_WEEK) + " y "+ cal2.get(Calendar.DAY_OF_WEEK));
                    if (cal1.get(Calendar.DAY_OF_WEEK) == cal2.get(Calendar.DAY_OF_WEEK)) {
                        Calendar cal3 = Calendar.getInstance();
                        cal3.setTime(periodo.getInicio());
                        cal3.set(Calendar.HOUR_OF_DAY, cal1.get(Calendar.HOUR_OF_DAY));
                        cal3.set(Calendar.MINUTE, cal1.get(Calendar.MINUTE));
                        cal3.set(Calendar.SECOND, cal1.get(Calendar.SECOND));
                        cal3.set(Calendar.MILLISECOND, cal1.get(Calendar.MILLISECOND));
                        log.debug("Comparando "+sdf.format(cal3.getTime())+" con "+ sdf.format(periodo.getInicio()) + " y " + sdf.format(periodo.getFin()));
                        if (cal3.getTime().after(periodo.getInicio()) && cal3.getTime().before(periodo.getFin())) {
                            log.debug("ES HORA!!!!!!!!!!");
                            esHora = true;
                            break;
                        }
                    }
                }
            } else {
                log.debug("CURSO INACTIVO");
            }
        } else {
            throw new RuntimeException("No pertenece a esta comunidad favor de inscribirse");
        }

        if (!esHora) {
            curso = null;
        }
        return curso;
    }

}
