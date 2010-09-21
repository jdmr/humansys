package mx.edu.um.cursos.dao;

import com.liferay.portal.model.User;
import java.util.List;
import java.util.Map;
import mx.edu.um.cursos.modelo.Curso;
import mx.edu.um.cursos.modelo.Periodo;

/**
 *
 * @author jdmr
 */
public interface CursoDao {
    public List<Curso> lista(Map<String, String> params);
    public Curso obtiene(Long id);
    public Curso crea(Curso curso);
    public Curso actualiza(Curso curso);
    public String elimina(Long id);
    public void creaPeriodo(Periodo periodo);
    public List<Periodo> periodos(Curso curso);
    public Curso valida(Long id, User user);
}
