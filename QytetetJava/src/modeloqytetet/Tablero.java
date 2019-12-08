/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

import java.util.ArrayList;

/**
 *
 * @author Juan Carlos Hermoso Quesada
 */
public class Tablero {
    private ArrayList<Casilla> casillas = new ArrayList();
    private Casilla carcel;
    
    public Tablero() {
        inicializar();
    }

    ArrayList<Casilla> getCasillas() {
        return casillas;
    }

    Casilla getCarcel() {
        return carcel;
    }

    @Override
    public String toString() {
        return "TABLERO" + "\nCasillas=" + casillas;
    }
    
    private void inicializar() {   
        casillas = new ArrayList();
        
        casillas.add(new OtraCasilla(0, TipoCasilla.SALIDA));
        casillas.add(new Calle(1,new TituloPropiedad("Calle la Rocka", 50, 10, 150, 250,250)));
        casillas.add(new OtraCasilla(2,TipoCasilla.CARCEL));
        casillas.add(new Calle(3,new TituloPropiedad("Calle el Zaguán", 50, 10, 150, 250,250)));
        casillas.add(new OtraCasilla(4, TipoCasilla.SORPRESA));
        casillas.add(new Calle(5,new TituloPropiedad("Calle la Guarida",  50, 10, 150, 250,250)));
        casillas.add(new OtraCasilla(6, TipoCasilla.SORPRESA));
        casillas.add(new Calle(7, new TituloPropiedad("Calle la Stukas",  50, 10, 150, 250,250)));
        casillas.add(new OtraCasilla(8, TipoCasilla.SORPRESA));
        casillas.add(new Calle(9,new TituloPropiedad("Calle la Mala Vida", 70, 10, 350, 350,350)));
        casillas.add(new OtraCasilla(10, TipoCasilla.IMPUESTO));
        casillas.add(new Calle(11,new TituloPropiedad("Calle el Refugio",  70, 10, 350, 350,350)));
        casillas.add(new OtraCasilla(12, TipoCasilla.JUEZ));
        casillas.add(new Calle(13,new TituloPropiedad("Calle el Soho", 70, 10, 350, 350,350)));
        casillas.add(new Calle(14, new TituloPropiedad("Calle el Engranaje", 70, 10, 350, 350,350)));
        casillas.add(new Calle(15,new TituloPropiedad("Calle el Brujo",  100, 20, 550, 550,550)));
        casillas.add(new OtraCasilla(16, TipoCasilla.PARKING));
        casillas.add(new Calle(17,new TituloPropiedad("Calle el Playmobil", 100, 20, 550, 550,550)));
        casillas.add(new Calle(18,new TituloPropiedad("Calle el Meneillo", 100, 20, 550, 550,550)));
        casillas.add(new Calle(19,new TituloPropiedad("Calle el Bubión", 100, 20, 550, 550,550)));
        
        carcel = casillas.get(2);
    }
    
    boolean esCasillaCarcel(int numeroCasilla) {
        if(numeroCasilla == carcel.getNumeroCasilla()) {
            return true;
        }
        else {
            return false;
        }
    }
    
    Casilla obtenerCasillaFinal(Casilla casilla, int desplazamiento) {
        int posicion;
        posicion = casilla.getNumeroCasilla();
               
        for (int i = 0; i < desplazamiento; i++) {
            posicion += 1;
            
            if (posicion == casillas.size()) {
                posicion = 0;
            }
        }
        
        
        return casillas.get(posicion);
    }
    
    Casilla obtenerCasillaNumero(int numeroCasilla) {
        return casillas.get(numeroCasilla);
    }
}
