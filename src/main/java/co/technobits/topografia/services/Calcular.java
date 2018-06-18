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
		 
		 double sumaN = 0;
		 double sumaS = 0;
		 double sumaE = 0;
		 double sumaW = 0;
		 
		 for(int i = 0; i < n; i++) {
			 if(i == 0) {
				 double coorCoregida = convertidor.convertir(listaDatos.get(0).getAzimut());
				 double distancia = listaDatos.get(0).getDistancia();
				 double[] proyecciones = proyecciones(distancia, coorCoregida);
				 // punto X
				 if(proyecciones[0] < 0) {
					 listaDatos.get(0).setW(proyecciones[0]);
					 sumaW = sumaW + proyecciones[0];
				 } else {
					 listaDatos.get(0).setE(proyecciones[0]);
					 sumaE = sumaE + proyecciones[0];
				 }
				 // punto Y
				 if(proyecciones[1] < 0) {
					 listaDatos.get(0).setS(proyecciones[1]);
					 sumaS = sumaS + proyecciones[1];
				 } else {
					 listaDatos.get(0).setN(proyecciones[1]);
					 sumaN = sumaN + proyecciones[1];
				 }
				 String coorNueva = convertidor.Reverse(coorCoregida);
				 listaDatos.get(0).setAzimut(coorNueva);
			 }else {
				 double coorCoregida = convertidor.convertir(listaDatos.get(i).getAzimut()) + (VaCambiar * i);
				 double distancia = listaDatos.get(i).getDistancia();
				 double[] proyecciones = proyecciones(distancia, coorCoregida);
				 // punto X
				 if(proyecciones[0] < 0) {
					 listaDatos.get(i).setW(proyecciones[0]);
					 sumaW = sumaW + proyecciones[0];
				 } else {
					 listaDatos.get(i).setE(proyecciones[0]);
					 sumaE = sumaE + proyecciones[0];
				 }
				 // punto Y
				 if(proyecciones[1] < 0) {
					 listaDatos.get(i).setS(proyecciones[1]);
					 sumaS = sumaS + proyecciones[1];
				 } else {
					 listaDatos.get(i).setN(proyecciones[1]);
					 sumaN = sumaN + proyecciones[1];
				 }
				 String coorNueva = convertidor.Reverse(coorCoregida);
				 listaDatos.get(i).setAzimut(coorNueva);
			 }
			 
		 }
		 
		 listaDatos.get(n-1).setN(sumaN);
		 listaDatos.get(n-1).setS(sumaS);
		 listaDatos.get(n-1).setE(sumaE);
		 listaDatos.get(n-1).setW(sumaW);
		 //Arreglo de coordenadas N y S
		 
		 double Ky = Math.abs((Math.abs(sumaN) - Math.abs(sumaS))/(Math.abs(sumaN) + Math.abs(sumaS)));
		 double Kx = Math.abs((Math.abs(sumaE) - Math.abs(sumaW))/(Math.abs(sumaE) + Math.abs(sumaW)));
		
		 
		 System.out.println("Ky = " + Ky);
		 System.out.println("Kx = " + Kx);
		 
		 for(int i = 0; i < n; i++) {
			
			 //Norte
			 if(listaDatos.get(i).getN() != 0) {
				 double Error = Math.abs(listaDatos.get(i).getN()) * Ky;
				 if(mayor(Math.abs(sumaN), Math.abs(sumaS))==1) {
					 System.out.println(Math.abs(listaDatos.get(i).getN()) +" - " +Error);
					 double R = Math.abs(listaDatos.get(i).getN()) - Error;
					 listaDatos.get(i).setN(R);
					 listaDatos.get(i).setCooN(1000+R);
				 }else {
					 System.out.println(Math.abs(listaDatos.get(i).getN()) +" + " +Error);
					 double R = Math.abs(listaDatos.get(i).getN()) + Error;
					 listaDatos.get(i).setN(R);
					 listaDatos.get(i).setCooN(1000+R);
				 }
			 }
			 //Sur
			 if(listaDatos.get(i).getS() != 0) {
				 double Error = Math.abs(listaDatos.get(i).getS()) * Ky;
				 if(mayor(Math.abs(sumaS),Math.abs(sumaN))==1) {
					 System.out.println(Math.abs(listaDatos.get(i).getS()) +" - " +Error);
					 double R = Math.abs(listaDatos.get(i).getS()) - Error;
					 listaDatos.get(i).setS(-R);
					 listaDatos.get(i).setCooN(1000-R);
				 }else {
					 System.out.println(Math.abs(listaDatos.get(i).getS()) +" + " +Error);
					 double R = Math.abs(listaDatos.get(i).getS()) + Error;
					 listaDatos.get(i).setS(-R);
					 listaDatos.get(i).setCooN(1000-R);
				 }
			 }
			 //Este
			 if(listaDatos.get(i).getE() != 0) {
				 double Error = Math.abs(listaDatos.get(i).getE()) * Kx;
				 if(mayor(Math.abs(sumaE), Math.abs(sumaW))==1) {
					 System.out.println(Math.abs(listaDatos.get(i).getE()) +" - " +Error);
					 double R = Math.abs(listaDatos.get(i).getE()) - Error;
					 listaDatos.get(i).setE(R);
					 listaDatos.get(i).setCooE(1000+R);
				 }else {
					 System.out.println(Math.abs(listaDatos.get(i).getE()) +" + " +Error);
					 double R = Math.abs(listaDatos.get(i).getE()) + Error;
					 listaDatos.get(i).setE(R);
					 listaDatos.get(i).setCooE(1000+R);
				 }
			 }
			 //Oeste
			 if(listaDatos.get(i).getW() != 0) {
				 double Error = Math.abs(listaDatos.get(i).getW()) * Kx;
				 if(mayor(Math.abs(sumaW),Math.abs(sumaE))==1) {
					 System.out.println(Math.abs(listaDatos.get(i).getW()) +" - " +Error);
					 double R = Math.abs(listaDatos.get(i).getW()) - Error;
					 listaDatos.get(i).setW(-R);
					 listaDatos.get(i).setCooE(1000-R);
				 }else {
					 System.out.println(Math.abs(listaDatos.get(i).getW()) +" + " +Error);
					 double R = Math.abs(listaDatos.get(i).getW()) + Error;
					 listaDatos.get(i).setW(-R);
					 listaDatos.get(i).setCooE(1000-R);
				 }
			 }
			 
		 }
		
		return new ResponseEntity<List<Datos>>(listaDatos, HttpStatus.OK);
	}
	
	public double[] proyecciones(double distancia, double angulo) {
		double X = distancia * Math.sin(Math.toRadians(angulo));
		double Y = distancia * Math.cos(Math.toRadians(angulo));
		double[] proyecciones = new double[2];
		proyecciones[0] = X;
		proyecciones[1] = Y;
		return proyecciones;
	}
	
	
	public int mayor(double n1, double n2) {
		if(n1>n2) {
			return 1;
		}else {
			return 2;
		}
	}
	
	
}
