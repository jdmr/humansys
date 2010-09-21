package mx.edu.um.portlets;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.sql.DataSource;
import mx.edu.um.cursos.dao.CursoDao;
import mx.edu.um.cursos.modelo.Curso;
import mx.edu.um.cursos.modelo.Periodo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;

@Controller
@RequestMapping("VIEW")
@SessionAttributes("curso,periodo")
public class CursoPortlet {

    private static final Log log = LogFactory.getLog(CursoPortlet.class);
    @Autowired
    private CursoDao cursoDao;
    @Autowired
    private DataSource dataSource;

    @InitBinder
    protected void initBinder(PortletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping
    public String lista(Model model) {
        log.debug("Lista de cursos");
        log.debug("Model: " + model);
        //model.addAttribute("curso",new Curso());
        return "cursos";
    }

    @RequestMapping(params = "action=busca")
    public void busca(ActionRequest request, ActionResponse response,
            @ModelAttribute("curso") Curso curso, BindingResult result,
            Model model) {
        log.debug("Buscando curso");
        log.debug("Params:" + request.getParameterMap());
        String filtro = request.getParameter("filtro");
        log.debug(filtro);
        if (filtro != null) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("filtro", filtro);
            log.debug(cursoDao.lista(params));
            List<Curso> cursos = cursoDao.lista(params);
            if (cursos != null && cursos.size() > 0) {
                model.addAttribute("cursos", cursos);
            }
        }
    }

    @RequestMapping(params = "action=nuevoCurso")
    public String nuevoCurso(Model model) {
        log.debug("Nuevo curso");
        Calendar cal = Calendar.getInstance();
        Curso curso = new Curso();
        curso.setFechaInicio(cal.getTime());
        cal.add(Calendar.MONTH, 1);
        curso.setFechaFinal(cal.getTime());
        model.addAttribute("curso", curso);
        model.addAttribute("comunidades", obtieneComunidades());

        return "nuevoCurso";
    }

    @RequestMapping(params = "action=nuevoCurso")
    public void nuevoCurso(ActionRequest request, ActionResponse response,
            @ModelAttribute("curso") Curso curso, BindingResult result,
            Model model) {
        log.debug("Guardando el curso");
        curso.setComunidadNombre(obtieneNombreComunidad(curso.getComunidadId()));
        cursoDao.crea(curso);
    }

    @RequestMapping(params = "action=editaCurso")
    public String editaCurso(@RequestParam("curso") Long id, Model model) {
        log.debug("Edita curso");
        model.addAttribute("curso", cursoDao.obtiene(id));
        model.addAttribute("comunidades", obtieneComunidades());
        return "editaCurso";
    }

    @RequestMapping(params = "action=editaCurso")
    public void editaCurso(ActionRequest request, ActionResponse response,
            @ModelAttribute("curso") Curso curso, BindingResult result,
            Model model) {
        log.debug("Guardando el curso");
        curso.setComunidadNombre(obtieneNombreComunidad(curso.getComunidadId()));
        cursoDao.actualiza(curso);
    }

    @RequestMapping(params = "action=editaPeriodo")
    public String editaPeriodo(Model model) {
        log.debug("Creando periodo");
        System.out.println("Creando periodo");
        Curso curso = cursoDao.obtiene(1L);
        Periodo periodo = new Periodo();
        periodo.setCurso(curso);
        Date date = new Date();
        periodo.setDia(date);
        periodo.setInicio(date);
        periodo.setFin(date);
        model.addAttribute("periodo", periodo);

        Map<Date, String> dias = new LinkedHashMap<Date, String>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        dias.put(cal.getTime(), "DOMINGO");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        dias.put(cal.getTime(), "LUNES");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        dias.put(cal.getTime(), "MARTES");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        dias.put(cal.getTime(), "MIERCOLES");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        dias.put(cal.getTime(), "JUEVES");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        dias.put(cal.getTime(), "VIERNES");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        dias.put(cal.getTime(), "SABADO");
        model.addAttribute("dias", dias);

        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        Map<Date, String> horas = new LinkedHashMap<Date, String>();
        cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        for (int hora = 0; hora < 24; hora++) {
            cal.set(Calendar.HOUR_OF_DAY, hora);
            horas.put(cal.getTime(), sdfHora.format(cal.getTime()));
        }
        model.addAttribute("horas", horas);
        return "nuevoPeriodo";
    }

    /*
    @RequestMapping(params="action=editaPeriodo")
    public void editaPeriodo(ActionRequest request, ActionResponse response,
    @ModelAttribute("periodo") Periodo periodo, BindingResult result,
    Model model) {
    log.debug("Guardando el periodo");
    System.out.println("Guardando el periodo");
    response.setRenderParameter("action", "editaPeriodo");
    }*/
    @RequestMapping(params = "action=eliminaCurso")
    public void eliminaCurso(ActionResponse response, @RequestParam("curso") Long id) {
        log.debug("Eliminando curso");
        cursoDao.elimina(id);
    }

    @RequestMapping(params = "action=nuevoPeriodo")
    public String nuevoPeriodo(@RequestParam("curso") Long id, Model model) {
        log.debug("Nuevo periodo");
        return "nuevoPeriodo";
    }

    @RequestMapping(params = "action=nuevoPeriodo")
    public void nuevoPeriodo(ActionRequest request, ActionResponse response,
            @ModelAttribute("periodo") Periodo periodo, BindingResult result, Model model) {
        log.debug("GUARDANDO PERIODO");
        log.debug(periodo);
        cursoDao.creaPeriodo(periodo);
        response.setRenderParameter("action", "verPeriodos");
        response.setRenderParameter("curso", periodo.getCurso().getId().toString());
    }

    @RequestMapping(params = "action=verPeriodos")
    public String verPeriodos(@RequestParam("curso") Long id, Model model) {
        log.debug("Nuevo periodo");
        Curso curso = cursoDao.obtiene(id);
        List<Periodo> periodos = cursoDao.periodos(curso);
        model.addAttribute("periodos", periodos);
        model.addAttribute("curso", curso);
        return "verPeriodos";
    }

    @RequestMapping(params = "action=entraCurso")
    public void entraCurso(ActionRequest request, ActionResponse response, @RequestParam("curso") Long id) throws PortalException, SystemException {
        User user = PortalUtil.getUser(request);
        if (id != null) {
            if (user != null && user.getMyPlaces() != null) {
                Curso curso = cursoDao.valida(id, user);
                if (curso != null) {
                    try {
                        response.sendRedirect(curso.getUrl());
                    } catch (Exception e) {
                        throw new RuntimeException("No se pudo enviar a la url " + curso.getUrl(), e);
                    }
                } else {
                    response.setRenderParameter("action", "horarioInvalido");
                }
            } else {
                response.setRenderParameter("action", "usuarioDesconocido");
            }
        } else {
            throw new RuntimeException("No encontro el curso");
        }
    }

    @RequestMapping(params = "action=horarioInvalido")
    public String horarioInvalido(Model model) {
        log.debug("Horario invalido");
        return "horarioInvalido";
    }

    @RequestMapping(params = "action=usuarioDesconocido")
    public String verPeriodos(Model model) {
        log.debug("Usuario Desconocido");
        return "usuarioDesconocido";
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

    private String obtieneNombreComunidad(Long id) {
        log.debug("Buscando el nombre de la comunidad " + id);
        Connection conn;
        Statement stmt;
        ResultSet rs;
        String nombre = "";
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select c.name from Group_ c where c.groupId = " + id);
            if (rs.next()) {
                nombre = rs.getString("name");
            }
        } catch (Exception e) {
            throw new RuntimeException("Tuvimos algun problema al intentar conseguir el nombre de la comunidad", e);
        }

        return nombre;
    }
}
