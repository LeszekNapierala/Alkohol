<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Alkohol</title>
</head>
<body>
<form action="/calculatePower" method="get">
<table cellpadding="0" cellspacing="0">
<tr>
	<td><label>Moc odczytana</label></td>	
	<td><label>Temperatura</label></td>
</tr>
<tr>
	<td><input type="text" placeholder="70 do 101,9  co 0,1" name="powerMeasured" /></td>	
	<td><input type="text" placeholder="-10 do +30  co 0,5" name="temperature" /></td>
	
</tr>
</table>

		<input type="submit" />
	</form>
	
</body>
</html>
