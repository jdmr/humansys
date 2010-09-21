<%@ include file="/WEB-INF/jsp/include.jsp" %>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css" type="text/css"/>
<div class="Curso">
    <h1>Nuevo Curso</h1>
    <portlet:actionURL var="actionUrl">
        <portlet:param name="action" value="nuevoCurso"/>
    </portlet:actionURL>

    <form:form name="cursoForm" commandName="curso" method="post" action="${actionUrl}" >
        <div class="dialog">
            <table>
                <tbody>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="nombre">Nombre</label>
                        </td>
                        <td valign="top" class="value">
                            <form:input path="nombre" maxlength="64"/>
                            <form:errors cssClass="errors" path="nombre" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="descripcion">Descripción</label>
                        </td>
                        <td valign="top" class="value">
                            <form:textarea path="descripcion" />
                            <form:errors path="descripcion" cssClass="errors" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="url">URL</label>
                        </td>
                        <td valign="top" class="value">
                            <form:textarea path="url"  />
                            <form:errors path="url" cssClass="errors" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="comunidadId">Comunidad</label>
                        </td>
                        <td valign="top" class="value">
                            <form:select path="comunidadId" items="${comunidades}" />
                            <form:errors cssClass="errors" path="comunidadId" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="fechaInicio">Fecha Inicio</label>
                        </td>
                        <td valign="top" class="value">
                            <form:input path="fechaInicio" maxlength="64"/>
                            <form:errors cssClass="errors" path="fechaInicio" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="fechaFinal">Fecha Final</label>
                        </td>
                        <td valign="top" class="value">
                            <form:input path="fechaFinal" maxlength="64"/>
                            <form:errors cssClass="errors" path="fechaFinal" />
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
        document.cursoForm.nombre.focus();
    </script>
</div>

