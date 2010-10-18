<%-- 
    Document   : inicio
    Created on : Sep 28, 2010, 10:51:11 AM
    Author     : jdmr
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css" type="text/css"/>
<div class="Curso">
    <c:choose>
        <c:when test="${curso != null}">
            <h1>El curso ${curso.nombre} esta definido</h1>
            <h2>Cambiar curso</h2>
        </c:when>
        <c:otherwise>
            <h1>Elije curso</h1>
        </c:otherwise>
    </c:choose>
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
                                <a href="<portlet:actionURL><portlet:param name="action" value="elijeCurso"/><portlet:param name="cursoId" value="${curso.id}"/></portlet:actionURL>">[Elegir]</a>
                            </td>

                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
    <script type="text/javascript">
        document.cursoForm.filtro.focus();
    </script>
</div>

