<%@ include file="/WEB-INF/jsp/include.jsp" %>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css" type="text/css"/>
<div class="Curso">
    <h1>Nuevo Periodo</h1>
    <portlet:actionURL var="actionUrl">
        <portlet:param name="action" value="nuevoPeriodo"/>
    </portlet:actionURL>

    <form:form name="periodoForm" commandName="periodo" method="post" action="${actionUrl}" >
        <form:hidden path="curso.id" />
        <div class="dialog">
            <table>
                <tbody>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="dia">Día</label>
                        </td>
                        <td valign="top" class="value">
                            <form:select path="dia" items="${dias}" />
                            <form:errors cssClass="errors" path="dia" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="inicio">Inicia</label>
                        </td>
                        <td valign="top" class="value">
                            <form:select path="inicio" items="${horas}" />
                            <form:errors path="inicio" cssClass="errors" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="fin">Termina</label>
                        </td>
                        <td valign="top" class="value">
                            <form:select path="fin" items="${horas}" />
                            <form:errors path="fin" cssClass="errors" />
                        </td>
                    </tr>

                </tbody>
            </table>
        </div>
        <div class="buttons">
            <span class="button"><input type="submit" name="_crea" class="save" value="Crea"/></span>
        </div>
    </form:form>
    <a href="<portlet:renderURL portletMode="view"/>">Inicio</a>
    <script type="text/javascript">
        document.periodoForm.dia.focus();
    </script>
</div>

