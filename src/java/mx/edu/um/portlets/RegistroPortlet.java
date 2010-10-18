package mx.edu.um.portlets;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.MembershipRequestServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import java.util.Date;
import java.util.List;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import mx.edu.um.cursos.dao.CursoDao;
import mx.edu.um.cursos.modelo.Curso;
import mx.edu.um.cursos.modelo.Periodo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;

@Controller
@RequestMapping("VIEW")
@SessionAttributes("usuario,curso")
public class RegistroPortlet {

    private static final Log log = LogFactory.getLog(RegistroPortlet.class);

    @Autowired
    private CursoDao cursoDao;

    @InitBinder
    protected void initBinder(PortletRequestDataBinder binder) {
    }

    @RequestMapping
    public String inicio(RenderRequest request, RenderResponse response, Model model) {
        log.debug("Vista del registro");
        try {
            ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
            Group grupo = themeDisplay.getLayout().getGroup();
            log.debug("Grupo: " + grupo.getDescriptiveName() + "|" + grupo.getPrimaryKey());
            model.addAttribute("comunidad", grupo);
            User user = PortalUtil.getUser(request);
            if (user != null) {
                if (user.getMyPlaces().contains(grupo)) {
                    model.addAttribute("pertenece", Boolean.TRUE);

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
                            PortletURL entrarURL = response.createActionURL();
                            entrarURL.setParameter(ActionRequest.ACTION_NAME, "entraCurso");
                            entrarURL.setParameter("cursoId", cursoId);
                            model.addAttribute("entrarURL",entrarURL.toString());
                        } else {
                            log.debug("Falta configurar portlet...");
                        }
                    } else {
                        log.debug("No tiene preferencias");
                    }

                } else {
                    model.addAttribute("pertenece", Boolean.FALSE);
                    if (grupo.getType() != GroupConstants.TYPE_COMMUNITY_RESTRICTED) {
                        model.addAttribute("publico", Boolean.TRUE);
                    }
                }
            } else {
                // Necesitamos firmar al usuario
                model.addAttribute("sign_in", Boolean.TRUE);
                model.addAttribute("sign_in_url", themeDisplay.getURLSignIn());
            }
        } catch (Exception e) {
            log.error("No se pudo ver la pagina de inicio al portlet de registro a la comunidad", e);
        }
        return "registro/inicio";
    }

    @RequestMapping(params = "action=unirse")
    public String unirse(RenderRequest request, RenderResponse response, Model model) {
        log.debug("Unirse a comunidad");
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        Group grupo = themeDisplay.getLayout().getGroup();

        if (grupo.getType() == GroupConstants.TYPE_COMMUNITY_RESTRICTED) {
            String comentarios = "Peticion para unirse al grupo el " + new Date();
            try {
                MembershipRequestServiceUtil.addMembershipRequest(grupo.getGroupId(), comentarios);
                SessionMessages.add(request, "membership_request_sent");
                model.addAttribute("enviado", Boolean.TRUE);
            } catch (Exception e) {
                SessionErrors.add(request, e.getMessage());
            }
        } else {
            log.debug("Uniendo a usuario a grupo");
            //LiveUsers.joinGroup(themeDisplay.getCompanyId(), grupo.getGroupId(), themeDisplay.getUserId());
        }
        return inicio(request, response, model);
    }

    @RequestMapping(params = "action=entraCurso")
    public void entraCurso(ActionRequest request, ActionResponse response, @RequestParam("cursoId") Long id) throws PortalException, SystemException {
        User user = PortalUtil.getUser(request);
        if (id != null) {
            if (user != null && user.getMyPlaces() != null) {
                Curso curso = cursoDao.valida(id, user);
//                log.debug("El usuario "+user+" esta intentando entrar al curso "+ curso.getNombre());
                if (curso != null) {
                    try {
//                        log.info("El usuario "+user+ " ha logrado entrar a su curso "+ curso.getNombre());
                        response.sendRedirect(curso.getUrl());
                    } catch (Exception e) {
                        throw new RuntimeException("No se pudo enviar a la url " + curso.getUrl(), e);
                    }
                } else {
                    log.info("HORARIO INVALIDO");
                    response.setRenderParameter("action", "horarioInvalido");
                    response.setRenderParameter("cursoId", id.toString());
                }
            } else {
                log.info("USUARIO DESCONOCIDO");
                response.setRenderParameter("action", "usuarioDesconocido");
            }
        } else {
            throw new RuntimeException("No encontro el curso");
        }
    }

    @RequestMapping(params = "action=horarioInvalido")
    public String horarioInvalido(@RequestParam("cursoId") Long id, Model model) {
        log.debug("Horario invalido");
        Curso curso = cursoDao.obtiene(id);
        List<Periodo> periodos = cursoDao.periodos(curso);
        model.addAttribute("periodos", periodos);
        model.addAttribute("curso",curso);
        return "horarioInvalido";
    }

    @RequestMapping(params = "action=usuarioDesconocido")
    public String usuarioDesconocido(Model model) {
        log.debug("Usuario Desconocido");
        return "usuarioDesconocido";
    }

}
