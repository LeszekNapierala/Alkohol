<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="UTF-8">
    <title>alkohol</title>
    <style type="text/css">
    .f1 {
	opacity:0.6;
}   
    </style>
</head>
<body>




<section class="data-to-be-entered">
    <dl>
        <dt><b><center>Obliczenia alkoholu metoda objętościowa</center></b></dt>
        <dd>
            <form action="/calculatePower" method="get">
<table border="5" cellpadding="10" cellspacing="0" width="95%" align="center">
<tr style="text-align: center; ">
	<td colspan="3">Moc alkoholu etylowego</td>
	<td colspan="3">Poprawka wg. objętości</td>
	<td rowspan="2">Obj. 1000kg alk. w 20ºC </td>
	<td rowspan="2">waga netto</td>
	<td rowspan="2">dm3 w 20ºC</td>
	<td rowspan="2">dm3 100% vol.</td>
</tr>
<tr style="text-align: center; ">
	<td><label>Moc pozorna</label></td>	
	<td><label>Temperatura zmierzona</label></td>
	<td>% vol. w 20ºC</td>
	<td><label>dm3</label></td>	
	<td><label>Temperatura w zbiorniku</label></td>
	<td>poprawka z tab.</td>
</tr>
<tr class="f1" style="text-align: center; ">
	<td width="10%"><input type="text" placeholder="np 85,4" name="powerMeasured" maxlength="5" value="${powerMeasured}" /></td>	
	<td width="10%"><input type="text" placeholder="np 24,5" name="temperature" maxlength="4" value="${temperatureDouble}" /></td>
	<td bgcolor="yellow" width="10%">${powerCalculated}</td>
	<td width="10%"><input type="text" placeholder="np 12010" name="givenVolume" maxlength="7" value="${givenVolumeInt}" /></td>
	<td width="10%"><input type="text" placeholder="np 22,5" name="temperatureTank" maxlength="4" value="${temperatureTankDouble}"/></td>
	<td bgcolor="yellow" width="10%">${correctionCalculated}</td>
	<td bgcolor="yellow" width="10%"></td>
	<td bgcolor="yellow" width="10%"></td>
	<td bgcolor="yellow" width="10%"></td>
	<td bgcolor="yellow" width="10%"></td>
</tr>
</table>

		<input type="submit" />       <input type="reset" value="Wyczyść formularz" />
	</form>

        </dd>

        <br><br><dt><b><center>Obliczenia alkoholu metoda wagowa</center></b></dt>
        <dd>
 <form action="/calculatePower" method="get">
 <table border="5" cellpadding="10" cellspacing="0" width="80%" align="center">
<tr style="text-align: center; ">
	<td colspan="3">Moc alkoholu etylowego</td>
	<td colspan="3">wg masy w kg</td>
	<td rowspan="2">dm3 w 20ºC </td>
	<td rowspan="2">dm3 100% vol.</td>
	
</tr>
<tr style="text-align: center; ">
	<td><label>Moc pozorna</label></td>	
	<td><label>Temperatura zmierzona</label></td>
	<td>% vol. w 20ºC</td>
	<td><label>brutto</label></td>	
	<td><label>tara</label></td>
	<td>netto</td>
</tr>
<tr class="f1" style="text-align: center; ">
	<td width="10%"><input type="text" placeholder="np 85,4" name="powerMeasured" maxlength="5" /></td>	
	<td width="10%"><input type="text" placeholder="np 24,5" name="temperature" maxlength="4" /></td>
	<td bgcolor="yellow" width="10%"></td>
	<td width="10%"><input type="text" placeholder="np 12010" name="grossWeight" maxlength="7" /></td>
	<td width="10%"><input type="text" placeholder="np 1980" name="tareWeigth" maxlength="7" /></td>
	<td bgcolor="yellow" width="10%"></td>
	<td bgcolor="yellow" width="10%"></td>
	<td bgcolor="yellow" width="10%"></td>
	
</tr>
</table>  
<input type="submit" />       <input type="reset" value="Wyczyść formularz" />
	</form>
        
        </dd>
        
    </dl>
</section>


<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" defer="defer">
$(function(){
	var ex3 = $('.data-to-be-entered');
	//var isVisible = true;
	ex3.find('dd').hide();
	var dts = ex3.find('dt');
	dts.on('click',function(){
		$(this).next().toggle();
	});
});
</script>
</body>
</html>
