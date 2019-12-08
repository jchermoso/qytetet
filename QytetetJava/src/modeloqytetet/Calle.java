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
public class Calle extends Casilla{
    
    public Calle(int numeroCasilla, TituloPropiedad titulo) {
        super(numeroCasilla, titulo.getPrecioCompra(), TipoCasilla.CALLE, titulo);
    }
        
     @Override  
    protected TipoCasilla getTipo() {
        return super.getTipo();
    }
    @Override
    protected TituloPropiedad getTitulo() {
        return super.getTitulo();
    }
    
    void asignarPropietarioJugador(Jugador jugador) {
      getTitulo().setPropietario(jugador);
    }
    
    public int pagarAlquiler() {
        return getTitulo().pagarAlquiler();
    }
    @Override
    protected boolean soyEdificable() {
        return super.soyEdificable();
    }
    
    @Override
    public boolean tengoPropietario() {
        return getTitulo().tengoPropietario();
    }
}
