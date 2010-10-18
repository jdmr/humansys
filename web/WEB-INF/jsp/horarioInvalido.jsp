<%@ include file="/WEB-INF/jsp/include.jsp" %>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css" type="text/css"/>
<div class="Curso">
    <h1>Horario inválido</h1>
    <p>El curso al que está intentando entrar no está activo en este momento favor de llamar al administrador para pedirle los horarios.</p>
    <c:if test="${curso != null}">
        <p>Curso: ${curso}</p>
        <p>Inicia: <fmt:formatDate value="${curso.fechaInicio}" /></p>
        <p>Termina: <fmt:formatDate value="${curso.fechaFinal}" /></p>
        <p>Comunidad: ${curso.comunidadNombre}</p>
    </c:if>
    <c:if test="${periodos != null}">
        <div class="list">
            <table>
                <thead>
                    <tr>

                        <th>Dia</th>

                        <th>Hora Inicio</th>

                        <th>Hora Termina</th>

                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${periodos}" var="periodo" varStatus="status">
                        <tr class="${(status.count % 2) == 0 ? 'odd' : 'even'}">

                            <td><fmt:formatDate value="${periodo.dia}" pattern="EEEE" /></td>

                            <td><fmt:formatDate value="${periodo.inicio}" pattern="HH:mm" /></td>

                            <td><fmt:formatDate value="${periodo.fin}" pattern="HH:mm" /></td>
                            
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
</div>

