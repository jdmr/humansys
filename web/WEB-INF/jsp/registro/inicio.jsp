<%-- 
    Document   : inicio
    Created on : Sep 28, 2010, 10:51:11 AM
    Author     : jdmr
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ page import="javax.portlet.*"%>
<link rel="stylesheet" href="<%= request.getContextPath()%>/css/main.css" type="text/css"/>

<portlet:defineObjects />

<liferay-theme:defineObjects />

<div class="summary-container">
    <h2>Bienvenido a este espacio</h2>
    <c:choose>
        <c:when test="${pertenece}">
            <portlet:actionURL var="actionUrl" >
                <portlet:param name="action" value="entraCurso" />
                <portlet:param name="cursoId" value="${curso.id}" />
            </portlet:actionURL>

            <p><a href="${actionUrl}">Entrar a la sesi�n en vivo</a></p>
        </c:when>
        <c:when test="${sign_in}">
            <p><a href="${sign_in_url}">Reg�strate a SG Campus</a></p>
        </c:when>
        <c:when test="${enviado}">
            <p>Tu petici�n ha sido enviada al administrador, recibir�s una notificaci�n por correo electr�nico.</p>
        </c:when>
        <c:when test="${publico}">
            <%
                        String currentURL = com.liferay.portal.util.PortalUtil.getCurrentURL(request);
                        PortletURL joinGroupURL = renderResponse.createActionURL();

                        joinGroupURL.setParameter(ActionRequest.ACTION_NAME, "joinGroup");
                        joinGroupURL.setParameter("redirect", currentURL);
            %>

            <p class="join-community">
                <liferay-ui:icon
                    image="join"
                    message="join-community"
                    url="<%= joinGroupURL.toString()%>"
                    label="<%= true %>"
                    />
            </p>
        </c:when>
        <c:otherwise>
            <p><a class="save" href="<portlet:renderURL><portlet:param name="action" value="unirse"/></portlet:renderURL>">�nete a este espacio de aprendizaje</a></p>
        </c:otherwise>
    </c:choose>
</div>

