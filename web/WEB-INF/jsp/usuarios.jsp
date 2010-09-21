<%@ include file="/WEB-INF/jsp/include.jsp" %>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css" type="text/css"/>
<div class="Curso">
    <h1>Usuarios</h1>
    <portlet:actionURL var="actionUrl" >
        <portlet:param name="action" value="busca" />
    </portlet:actionURL>

    <form name="usuarioForm" method="post" action="${actionUrl}" >
        <div class="search">
            <table>
                <tbody>
                    <tr class="prop">
                        <td valign="middle" class="name">
                            <label for="comunidades">Buscar</label>
                        </td>
                        <td valign="top" class="value">
                            <select name="comunidades" id="comunidades" multiple="true" size="5">
                                <c:forEach items="${comunidades}" var="comunidad" >
                                    <option value="${comunidad.key}">${comunidad.value}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="todos">Todos los usuarios</label>
                        </td>
                        <td valign="top" class="value">
                            <input type="checkbox" name="todos" id="todos" />
                        </td>
                    </tr>
                    <tr class="prop">
                        <td valign="top" class="name" colspan="2">
                            <input type="submit" name="_busca" value="Busca"/>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </form>
    <c:if test="${usuarios != null}">
        <div class="list">
            <textarea cols="300" rows="10" style="width: 100%;">${usuarios}</textarea>
        </div>
    </c:if>
    <script type="text/javascript">
        document.usuarioForm.comunidades.focus();
    </script>
</div>

