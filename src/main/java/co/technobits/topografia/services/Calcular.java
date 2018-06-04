package co.technobits.topografia.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.technobits.topografia.model.Datos;

@RestController
@RequestMapping("/api")
public class Calcular {
	
	@PostMapping(path = "/calcular", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Datos>> calcular(@RequestBody List<Datos> listaDatos){
		
		int n = listaDatos.size();
		
		GradosDecimales convertidor = new GradosDecimales();
		double V1 = convertidor.convertir(listaDatos.get(0).getAzimut());
		double VU = convertidor.convertir(listaDatos.get(n-1).getAzimut());
		double VR = 0;
		VR = V1 - VU;
		
		System.out.println("VR= " + VR);
		System.out.println("VR= " + convertidor.Reverse(VR));
		
		 double VaCambiar = VR/(n-1);
		 System.out.println("Vacambiar= " +VaCambiar);
		 System.out.println("Vacambiar= " +convertidor.Reverse(VaCambiar));
		 
		 for(int i = 0; i < n; i++) {
			 if(i == 0) {
				 double coorCoregida = convertidor.convertir(listaDatos.get(0).getAzimut());
				 String coorNueva = convertidor.Reverse(coorCoregida);
				 listaDatos.get(0).setAzimut(coorNueva);
			 }else {
				 double coorCoregida = convertidor.convertir(listaDatos.get(i).getAzimut()) + (VaCambiar * i);
				 String coorNueva = convertidor.Reverse(coorCoregida);
				 listaDatos.get(i).setAzimut(coorNueva);
			 }
			 
		 }
		
		return new ResponseEntity<List<Datos>>(listaDatos, HttpStatus.OK);
	}
	
	
}
