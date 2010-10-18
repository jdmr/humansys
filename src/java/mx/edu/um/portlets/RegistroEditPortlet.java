package mx.edu.um.portlets;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import mx.edu.um.cursos.dao.CursoDao;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;

@Controller
@RequestMapping("EDIT")
public class RegistroEditPortlet {

    private static final Log log = LogFactory.getLog(RegistroEditPortlet.class);

    @Autowired
    private CursoDao cursoDao;

    @InitBinder
    protected void initBinder(PortletRequestDataBinder binder) {
    }

    @RequestMapping
    public String inicio(RenderRequest request, Model model) throws SystemException {
        log.debug("Inicio del registro");
        String portletResource = ParamUtil.getString(
                request, "portletResource");

        PortletPreferences prefs =
                PortletPreferencesFactoryUtil.getPortletSetup(
                request, portletResource);

        if (prefs != null) {
            String cursoId = prefs.getValue("cursoId", "");
            if (cursoId != null && !"".equals(cursoId)) {
                Curso curso = cursoDao.obtiene(new Long(cursoId));
                model.addAttribute("curso",curso);
            }
        }
        return "registro/edita";
    }

    @RequestMapping(params = "action=busca")
    public void busca(ActionRequest request, ActionResponse response,
            @ModelAttribute("filtro") String filtrox, BindingResult result,
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

    @RequestMapping(params = "action=elijeCurso")
    public void elijeCurso(ActionRequest request, ActionResponse response, @RequestParam("cursoId") Long id) throws PortalException, SystemException {

        String portletResource = ParamUtil.getString(
                request, "portletResource");

        PortletPreferences prefs =
                PortletPreferencesFactoryUtil.getPortletSetup(
                request, portletResource);

        if (prefs != null) {
            try {
                prefs.setValue("cursoId", id.toString());
                prefs.store();
            } catch (Exception e) {
                log.error("No se pudo elejir el curso",e);
            }
        }
    }
}
