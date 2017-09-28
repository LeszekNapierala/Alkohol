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
<br>
<b>Opis programu</b><br>
1. Instalacja 

2. Uruchomienie programu:
	
	w przeglądarce wpisujemy adres http://localhost:8080
	
3. Mamy dwie metody wyboru obliczeń alkoholu:
 - Obliczania alkoholu metodą objętościową
 - Obliczania alkoholu metodą wagową
 
4. Po wybraniu metody rozwija się tabela z formularzem do wprowadzenia danych.
	w formularzu z metoda objętościową uzupełniamy pola:
	 - Moc pozorna				(zakres pola 80 do 101,9)
	 - Temperatura zmierzona	(zakres -10 do 30ºC co 0,5ºC)
	 - dm3						(objętość liczba większa od 0)
	 - Temperatura w zbiorniku	(zakres -10 do 30ºC co 0,5ºC)
	w formularzu z metoda objętościową uzupełniamy pola:
	 - Moc pozorna				(zakres pola 80 do 101,9)
	 - Temperatura zmierzona	(zakres -10 do 30ºC co 0,5ºC)
	 - brutto					(waga brutto liczba większa od 0)
	 - tara						(waga tara liczba większa od 0 i mniejsza od brutto)
	 
5. Po naciśnięciu przycisku &lt;Oblicz&gt; tabela zwinie się i kliknięciu na daną metodę rozwinie się tabela z wyliczonymi wartościami.
6. Po naciśnięciu przycisku &lt;Wyczyść&gt; wszystkie pola z wpisanymi wartościami zostana wyczyszczone.
	Można ponownie wpisywać nowe wartości (patrz p.3 i p.4)
	
7. ## Licencja MIT
<font size="3">(c) Leszek Napierała
leszeknapieralaoborniki@gmail.com</font>
</body>
</html>
