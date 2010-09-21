<%@ include file="/WEB-INF/jsp/include.jsp" %>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css" type="text/css"/>
<div class="Curso">
    <h1>Cursos</h1>
    <portlet:actionURL var="actionUrl" >
        <portlet:param name="action" value="busca" />
    </portlet:actionURL>

    <form name="cursoForm" method="post" action="${actionUrl}" >
        <div class="search">
            <table>
                <tbody>
                    <tr class="prop">
                        <td valign="middle" class="name">
                            <label for="filtro">Buscar</label>
                        </td>
                        <td valign="top" class="value">
                            <input type="text" name="filtro" value="" />
                            <input type="submit" name="_busca" value="Busca"/>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </form>
    <c:if test="${cursos != null}">
        <div class="list">
            <table>
                <thead>
                    <tr>

                        <th>Nombre</th>

                        <th>Descripción</th>

                        <th>Comunidad</th>

                        <th>Fecha Inicio</th>

                        <th>Fecha Final</th>

                        <th>Liga a Portlet</th>

                        <th>Acciones</th>

                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${cursos}" var="curso" varStatus="status">
                        <tr class="${(status.count % 2) == 0 ? 'odd' : 'even'}">

                            <td>${curso.nombre}</td>

                            <td>${curso.descripcion}</td>

                            <td>${curso.comunidadNombre}</td>

                            <td>${curso.fechaInicio}</td>

                            <td>${curso.fechaFinal}</td>

                            <td>
                                <portlet:actionURL var="entraCurso">
                                    <portlet:param name="action" value="entraCurso"/>
                                    <portlet:param name="curso" value="${curso.id}"/>
                                </portlet:actionURL>
                                <a href="${entraCurso}">Liga</a>
                            </td>

                            <td>
                                <a href="<portlet:renderURL><portlet:param name="action" value="editaCurso"/><portlet:param name="curso" value="${curso.id}"/></portlet:renderURL>">[Edita]</a>
                                <a href="<portlet:renderURL><portlet:param name="action" value="verPeriodos"/><portlet:param name="curso" value="${curso.id}"/></portlet:renderURL>">[Periodos]</a>
                                <a href="<portlet:actionURL><portlet:param name="action" value="eliminaCurso"/><portlet:param name="curso" value="${curso.id}"/></portlet:actionURL>">[Elimina]</a>
                            </td>

                    </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
    <div class="buttons">
        <a class="save" href="<portlet:renderURL><portlet:param name="action" value="nuevoCurso"/></portlet:renderURL>">Nuevo Curso</a>
    </div>
    <script type="text/javascript">
        document.cursoForm.filtro.focus();
    </script>
</div>

