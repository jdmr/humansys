<%@ include file="/WEB-INF/jsp/include.jsp" %>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css" type="text/css"/>
<div class="Curso">
    <h1>Periodos</h1>
    <c:if test="${periodos != null}">
        <div class="list">
            <table>
                <thead>
                    <tr>

                        <th>Dia</th>

                        <th>Hora Inicio</th>

                        <th>Hora Termina</th>

                        <th>Curso</th>

                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${periodos}" var="periodo" varStatus="status">
                        <tr class="${(status.count % 2) == 0 ? 'odd' : 'even'}">

                            <td><fmt:formatDate value="${periodo.dia}" pattern="EEEE" /></td>

                            <td><fmt:formatDate value="${periodo.inicio}" pattern="HH:mm" /></td>

                            <td><fmt:formatDate value="${periodo.fin}" pattern="HH:mm" /></td>
                            
                            <td>${periodo.curso.comunidadNombre}</td>

                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
    <div class="buttons">
        <a class="home" href="<portlet:renderURL portletMode="view"/>">Inicio</a>
        <a href="<portlet:renderURL><portlet:param name="action" value="editaPeriodo"/><portlet:param name="curso" value="${curso.id}"/></portlet:renderURL>">[Nuevo Periodo]</a>
    </div>
</div>

