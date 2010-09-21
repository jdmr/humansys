package mx.edu.um.portlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.sql.DataSource;
import mx.edu.um.cursos.modelo.Curso;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;

/**
 *
 * @author jdmr
 */
@Controller
@RequestMapping("VIEW")
public class UsuarioPortlet {

    private static final Log log = LogFactory.getLog(UsuarioPortlet.class);
    
    @Autowired
    private DataSource dataSource;

    @InitBinder
    protected void initBinder(PortletRequestDataBinder binder) {
        // lo que hay que asignar entre que se envia la forma y se recibe
        // en el action
    }

    @RequestMapping
    public String inicio(Model model) {
        model.addAttribute("comunidades", obtieneComunidades());
        return "usuarios";
    }

    @RequestMapping(params = "action=busca")
    public void busca(ActionRequest request, ActionResponse response,
            @ModelAttribute("curso") Curso curso, BindingResult result,
            Model model) {
        log.debug("Buscando usuarios");
        String[] comunidades = request.getParameterValues("comunidades");
        String todos = request.getParameter("todos");
        StringBuilder sb = new StringBuilder();
        if (todos != null && todos.equals("on")) {
            log.debug("Busca todos los usuarios de sg");
            sb.append(obtieneTodosLosUsuarios());
        } else {
            for(String comunidadId : comunidades) {
                log.debug("Obteniendo usuarios de la Comunidad: "+comunidadId);
                sb.append(obtieneUsuariosPorComunidad(new Long(comunidadId)));
            }
        }
        model.addAttribute("usuarios", sb.toString());
    }

    private Map<Long, String> obtieneComunidades() {
        Connection conn;
        Statement stmt;
        ResultSet rs;
        Map<Long, String> comunidades = new LinkedHashMap<Long, String>();
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select c.groupId, c.name from Group_ c where c.type_ = 1 and c.companyId = 24988 and c.groupId != 25008");
            while (rs.next()) {
                comunidades.put(rs.getLong("groupId"), rs.getString("name"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Tuvimos algun problema al intentar conseguir las comunidades", e);
        }

        return comunidades;
    }

    private String obtieneUsuariosPorComunidad(Long comunidadId) {
        StringBuilder sb = new StringBuilder();
        Connection conn;
        Statement stmt;
        ResultSet rs;
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select u.screenName, c.firstName, c.middleName, c.lastName, u.emailAddress from User_ u, Users_Groups ug, Contact_ c "
                    + "where c.contactId = u.contactId and ug.userId = u.userId and ug.groupId = "+comunidadId);
            while (rs.next()) {
                sb.append("\"").append(rs.getString("screenName")).append("\"").append(",");
                sb.append("\"").append(rs.getString("firstName")).append("\"").append(",");
                sb.append("\"").append(rs.getString("middleName")).append("\"").append(",");
                sb.append("\"").append(rs.getString("lastName")).append("\"").append(",");
                sb.append("\"").append(rs.getString("emailAddress")).append("\"").append(",");
                sb.append("\n");
            }
        } catch (Exception e) {
            throw new RuntimeException("Tuvimos algun problema al intentar conseguir las comunidades", e);
        }
        return sb.toString();
    }

    private String obtieneTodosLosUsuarios() {
        StringBuilder sb = new StringBuilder();
        Connection conn;
        Statement stmt;
        ResultSet rs;
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select u.screenName, c.firstName, c.middleName, c.lastName, u.emailAddress from User_ u, Contact_ c where c.contactid = u.contactId and c.companyId = 24988");
            while (rs.next()) {
                sb.append("\"").append(rs.getString("screenName")).append("\"").append(",");
                sb.append("\"").append(rs.getString("firstName")).append("\"").append(",");
                sb.append("\"").append(rs.getString("middleName")).append("\"").append(",");
                sb.append("\"").append(rs.getString("lastName")).append("\"").append(",");
                sb.append("\"").append(rs.getString("emailAddress")).append("\"").append(",");
                sb.append("\n");
            }
        } catch (Exception e) {
            throw new RuntimeException("Tuvimos algun problema al intentar conseguir las comunidades", e);
        }
        return sb.toString();
    }
}
