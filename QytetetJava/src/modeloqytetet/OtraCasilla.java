package modeloqytetet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Juan Carlos Hermoso Quesada
 */
public class OtraCasilla extends Casilla {
    
    public OtraCasilla(int numeroCasilla, TipoCasilla tipo) {
        super(numeroCasilla, 0,tipo,null);
    }

    protected TipoCasilla getTipo() {
        return super.getTipo();
    }

    protected boolean soyEdificable() {
        return super.soyEdificable();
    }

    protected TituloPropiedad getTitulo() {
        return super.getTitulo();
    }

  
    public boolean tengoPropietario() {
        return false;
    }

}
    

