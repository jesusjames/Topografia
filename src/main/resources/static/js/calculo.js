function vertices(){
	var cantVertices = $('#vertices').val();
	if(cantVertices < 0 || cantVertices == ""){
		swal("Error!", "Primero debe ingresar el numero de vertices", "error").then((value) => {
			$('#vertices').focus();
		});
		
		return false;
	}
	s="";
	for(var i = 1; i <= cantVertices; i++){
		if(i == cantVertices){
			s = s + '<div class="row">';
			s = s + '<div class="col-6">';
			s = s + '<div class="form-group">';
			s = s + '<label for="Distancia">Distancia en m #'+ i +'</label>';
			s += '<input type="number" class="form-control" id="distancia'+ i +'" placeholder="eje: 20.36" value="0" disabled>';
			s += '</div>';
			s += '</div>';
			s += '<div class="col-6">';
			s += '<div class="form-group">';
			s += '<label for="azimut">Azimut #'+ i + '</label>';
			s += '<input type="text" class="form-control" id="azimut'+ i +'" placeholder="eje: 257 14 40">';
			s += '</div>';
			s += '</div>';
			s += '</div>';
		}else{
			s = s + '<div class="row">';
			s = s + '<div class="col-6">';
			s = s + '<div class="form-group">';
			s = s + '<label for="Distancia">Distancia en m #'+ i +'</label>';
			s += '<input type="number" class="form-control" id="distancia'+ i +'" placeholder="eje: 20.36">';
			s += '</div>';
			s += '</div>';
			s += '<div class="col-6">';
			s += '<div class="form-group">';
			s += '<label for="azimut">Azimut #'+ i + '</label>';
			s += '<input type="text" class="form-control" id="azimut'+ i +'" placeholder="eje: 257 14 40">';
			s += '</div>';
			s += '</div>';
			s += '</div>';
		}
		
	}
	$('#datos-vertices').html(s);
	console.log(cantVertices);
}

function calcular(){
	$('#resultados').empty();
	var cantVertices = $('#vertices').val();
	for(var i = 1; i <= cantVertices; i++){
		if($('#distancia'+ i).val() < 0 || $('#distancia'+ i).val() == ""){
			swal("Error!", "Debe ingresar la distancia del vertice"+ i, "error").then((value) => {
				$('#distancia'+ i).focus();
			});
			return false;
		}
		if($('#azimut'+ i).val() < 0 || $('#azimut'+ i).val() == ""){
			swal("Error!", "Debe ingresar el azimut del vertice " + i, "error").then((value) => {
				$('#azimut'+ i).focus();
			});
			return false;
		}
	}
	jsonObj = [];
	
	for(var i = 1; i <= cantVertices; i++){
		var miObjeto = new Object();
		miObjeto.vertice = i;
		let distancia = parseFloat($('#distancia'+ i).val());
		miObjeto.distancia = distancia
		miObjeto.azimut = $('#azimut'+ i).val();
		jsonObj.push(miObjeto);
	}
	var myString = JSON.stringify(jsonObj);
	console.log(myString);
	console.log(jsonObj);
	var string="";
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url:'/api/calcular',
		data: myString,
		dataType: "json",
		 beforeSend: function(objeto){
		$("#loader").html("Cargando...");
	  },
		success:function(data){
			console.log(data);
			$.each(data, function (key, entry) {
				string =  '<tr><td class="font-weight-medium">';
		        string += entry.vertice;
		        string += '</td><td>';
		        string += entry.distancia;
		        string += '</td><td>';
		        string += entry.azimut;
		        string += '</td><td class="text-success">';
		        string += entry.n;
		        string += '</td><td class="text-danger">';
		        string += entry.s;
		        string += '</td><td class="text-success">';
		        string += entry.e;
		        string += '</td><td class="text-danger">';
		        string += entry.w;
		        string += '</td><td>';
		        string += entry.cooN;
		        string += '</td><td>';
		        string += entry.cooE;
		        string += '</td></tr>';
		        $('#resultados').append(string);
			})
			
		
			$("#loader").html("");
		}
	})
	
	
}

function reset() {
	location.reload();
}