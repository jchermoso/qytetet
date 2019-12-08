/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

/**
 *
 * @author Juan Carlos Hermoso Quesada
 */
public class Especulador extends Jugador{
    private int fianza;
    
    public Especulador(int fianza, Jugador jugador) {
        super(jugador);
        this.fianza = fianza;
    }
    
    @Override
    protected void pagarImpuesto() {
        modificarSaldo(-(super.getCasillaActual().getCoste())/2);
    }
        
    @Override
    protected Especulador convertirme(int fianza) {
        return this;
    }
    
    @Override
    protected boolean deboIrACarcel() {
        boolean deboIrACarcel = false;
        
        if (super.deboIrACarcel() == true && pagarFianza() == false) {
            deboIrACarcel = true;
        }
        
        return deboIrACarcel;
    }
    
    private boolean pagarFianza() {
        boolean pagada = false;
        
        if (tengoSaldo(fianza)) {
            System.out.println("\nTe has librado de ir a la cárcel\n");
            modificarSaldo(-fianza);
            pagada = true;
        }
        
        return pagada;
    }
    
    @Override
    protected boolean puedoEdificarCasa(TituloPropiedad titulo) {
        boolean puedo_edificar = false;
        
        if (titulo.getNumCasas() < 8) {
            puedo_edificar = true;
        }
        
        return puedo_edificar;
    }
    
    @Override
    protected boolean puedoEdificarHotel(TituloPropiedad titulo) {
        boolean puedo_edificar = false;
        
        if (titulo.getNumHoteles() < 8 && titulo.getNumCasas() >= 4) {
            puedo_edificar = true;
        }
        
        return puedo_edificar;
    }

    @Override
    public String toString() {
        return "Especulador "+super.toString() + "\nFianza= " + fianza;
    }
}
