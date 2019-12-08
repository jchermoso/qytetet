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
public abstract class Casilla {
    private int numeroCasilla;
    private int coste;
    private TipoCasilla tipo;
    private TituloPropiedad titulo;
    
    public abstract boolean tengoPropietario();
    
    public Casilla(int numeroCasilla, int coste, TipoCasilla tipo, TituloPropiedad titulo) {
        if (tipo == TipoCasilla.IMPUESTO) {
            coste = 100;
        }
        
        this.numeroCasilla = numeroCasilla;
        setCoste(coste);
        this.tipo = tipo;
        this.titulo = titulo;
    }

    public int getNumeroCasilla() {
        return numeroCasilla;
    }

    int getCoste() {
        return coste;
    }

    protected TipoCasilla getTipo() {
        return tipo;
    }

    protected TituloPropiedad getTitulo() {
        return titulo;
    }
    
    protected boolean soyEdificable() {
        return this.tipo == TipoCasilla.CALLE;
    }
    
    public void setCoste(int coste) {
        this.coste = coste;
    }
    
    @Override
    public String toString() {
        return "\nCasilla "+ numeroCasilla + "\n       Coste= " + coste + "  \n       Tipo= " + tipo + "\n       Titulo= " + titulo;
    }
}
