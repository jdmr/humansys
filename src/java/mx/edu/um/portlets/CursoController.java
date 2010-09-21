package mx.edu.um.portlets;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

public class CursoController extends AbstractController {

    @Override
    protected void handleActionRequestInternal(ActionRequest arg0, ActionResponse arg1) 
            throws Exception {
        super.handleActionRequestInternal(arg0, arg1);
    }
    
    @Override
    protected ModelAndView handleRenderRequestInternal(RenderRequest request,
            RenderResponse response) throws Exception {

        if (request.getPortletMode().equals(PortletMode.VIEW)) 
            return _doView(request, response);
        
        return null;
    }
    
    public ModelAndView _doView(RenderRequest request, RenderResponse response) {
        ModelAndView modelAndView = new ModelAndView("Cursos_view");
        return modelAndView;
    }
}
