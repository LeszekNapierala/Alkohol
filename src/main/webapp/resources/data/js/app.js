
//1. ukryte domyślnie wszystkie elementy **dd**.
//2. Po kliknięciu w element **dt** elementy **dd**: rozwijały się, jeśli są ukryte, zwijały się, jeśli są widoczne.


$(function(){
	var ex3 = $('.data-to-be-entered');
	//var isVisible = true;
	ex3.find('dd').hide();
	var dts = ex3.find('dt');
	dts.on('click',function(){

		$(this).next().toggle();
	});

});
