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
public class Jugador implements Comparable{
    private boolean encarcelado = false;
    private String nombre;
    private int saldo = 7500;
    private Sorpresa cartaLibertad;
    private Casilla casillaActual;
    private ArrayList<TituloPropiedad> propiedades = new ArrayList();
    
    public Jugador(String nombre) {
        this.nombre = nombre;
    }
    
    public Jugador(Jugador otroJugador) {
        this.nombre = otroJugador.nombre;
        this.saldo = otroJugador.saldo;
        this.encarcelado = otroJugador.encarcelado;
        this.cartaLibertad = otroJugador.cartaLibertad;
        this.casillaActual = otroJugador.casillaActual;
        this.propiedades = new ArrayList<>(otroJugador.getPropiedades());
    } 
    
    @Override
    public int compareTo(Object otroJugador) {
        int otroCapital = ((Jugador) otroJugador).obtenerCapital();
        return otroCapital - obtenerCapital();
    }
        
    boolean cancelarHipoteca(TituloPropiedad titulo) {
        boolean cancelar = false;
       
        int cantidadRecibida = titulo.calcularCosteCancelar();
        
        if (this.saldo >= cantidadRecibida) {
            modificarSaldo(-cantidadRecibida);
            cancelar = titulo.cancelarHipoteca();
        }
       
        return cancelar;
    }
    
    boolean comprarTituloPropiedad() {
        boolean comprado = false;
        
        int costeCompra = casillaActual.getCoste();
        
        Calle calle = (Calle)casillaActual;
        
        if (costeCompra < saldo) {
            calle.asignarPropietarioJugador(this);
            comprado = true;
            propiedades.add(calle.getTitulo());
            modificarSaldo(-costeCompra);
        }
        
        return comprado;
    }
    
    int cuantasCasasHotelesTengo() {
        int casas_hoteles = 0;
        
        for (int i = 0; i < propiedades.size(); i++) {
            casas_hoteles += propiedades.get(i).getNumCasas();
            casas_hoteles += propiedades.get(i).getNumHoteles();
        }
        
        return casas_hoteles;
    }
        
    boolean deboPagarAlquiler() {
        boolean deboPagar = false;
        
        TituloPropiedad titulo = casillaActual.getTitulo();
        
        if (!esDeMiPropiedad(titulo) && titulo.tengoPropietario() && !titulo.propietarioEncarcelado()
           && !titulo.isHipotecada()) {
            deboPagar = true;
        } 
        
        return deboPagar;
    }
    
    Sorpresa devolverCartaLibertad() {
        Sorpresa auxiliar;
        auxiliar = cartaLibertad;
        cartaLibertad = null;
        
        return auxiliar;        
    }
    
    boolean edificarCasa(TituloPropiedad titulo) {
        boolean edificada = false;
                
        if (puedoEdificarCasa(titulo)) {
            int costeEdificarCasa = titulo.getPrecioEdificar();
            
            if (tengoSaldo(costeEdificarCasa)) {
                titulo.edificarCasa();
                modificarSaldo(-costeEdificarCasa);
                edificada = true;
            }
        }
        
        return edificada;
    }
    
    boolean edificarHotel(TituloPropiedad titulo) {
        boolean edificada = false;
        
        if (puedoEdificarHotel(titulo)) {
            int costeEdificarHotel = titulo.getPrecioEdificar();
            
            if (tengoSaldo(costeEdificarHotel)) {
                titulo.edificarHotel();
                modificarSaldo(-costeEdificarHotel);
                edificada = true;
            }
        }
        
        return edificada;
    }
    
    private void eliminarDeMisPropiedades(TituloPropiedad titulo) {
        propiedades.remove(titulo);
        titulo.setPropietario(null);
    }
    
    private boolean esDeMiPropiedad(TituloPropiedad titulo) {
        boolean contiene = false;
        
        for (int i = 0; i < propiedades.size(); i++) {
            if (propiedades.get(i).getNombre() == titulo.getNombre()) {
                contiene = true;
            }
        }
        
        return contiene;
    }
    
    boolean estoyEnCalleLibre() {
        throw new UnsupportedOperationException("Sin implementar");
    }
    
    Sorpresa getCartaLibertad() {
        return cartaLibertad;
    }
    
    public Casilla getCasillaActual() {
        return casillaActual;
    }
    
    boolean getEncarcelado() {
        return encarcelado;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public ArrayList<TituloPropiedad> getPropiedades() {
        return propiedades;
    }
    
    public int getSaldo() {
        return saldo;
    }
    
    void hipotecarPropiedad(TituloPropiedad titulo) {
        int costeHipoteca = titulo.hipotecar();
        this.modificarSaldo(costeHipoteca);
    }
    
    void irACarcel(Casilla casilla) {
        setCasillaActual(casilla);
        setEncarcelado(true);
    }
    
    int modificarSaldo(int cantidad) {
        saldo += cantidad;
        
        return saldo;
    }
    
    int obtenerCapital() {
        int capital = 0;
        
        for (int i = 0; i < propiedades.size(); i++) {
            capital += (propiedades.get(i).getNumCasas() + propiedades.get(i).getNumHoteles())*propiedades.get(i).getPrecioEdificar() + propiedades.get(i).getPrecioCompra();
            
            if (propiedades.get(i).isHipotecada()) {
                capital -= propiedades.get(i).getHipotecaBase();
            }
        }
        return capital+saldo;
    }
    
    ArrayList<TituloPropiedad> obtenerPropiedades(boolean hipotecada) {
        ArrayList<TituloPropiedad> obtener_propiedades = new ArrayList();
        
        for (int i = 0; i < propiedades.size(); i++) {
            if (propiedades.get(i).isHipotecada() == hipotecada) {
                obtener_propiedades.add(propiedades.get(i));
            }
        }
        
        return obtener_propiedades;
    }
    
    void pagarAlquiler() {
        Calle calle = (Calle)casillaActual;
        
        int costeAlquiler = calle.pagarAlquiler();
        
        System.out.println("\nEsta casilla tiene dueño, pagas el alquiler de "+ costeAlquiler + " euros.\n");
        
        modificarSaldo(-costeAlquiler);
    }   
    
    protected void pagarImpuesto() {
        saldo -= casillaActual.getCoste();
    }
    
    void pagarLibertad(int cantidad) {
        boolean tengoSaldo = tengoSaldo(cantidad);
        
        if (tengoSaldo) {
            setEncarcelado(false);
            modificarSaldo(-cantidad);
        }
    }
    
    void setCartaLibertad(Sorpresa carta) {
        this.cartaLibertad = carta;
    }
    
    void setCasillaActual(Casilla casilla) {
        this.casillaActual = casilla;
    }
    
    void setEncarcelado(boolean encarcelado) {
        this.encarcelado = encarcelado;
    }
    
    boolean tengoCartaLibertad() {
        boolean tengocartalibertad = false;
        
        if (cartaLibertad!=null) {
            tengocartalibertad = true;
        }
        
        return tengocartalibertad;
    }
    
    protected boolean tengoSaldo(int cantidad) {
        return saldo > cantidad;
    }
    
    void venderPropiedad(Casilla casilla) {
        TituloPropiedad titulo = casilla.getTitulo();
        eliminarDeMisPropiedades(titulo);
        int precioVenta = titulo.calcularPrecioVenta();
        modificarSaldo(precioVenta);
    }
    
    protected Especulador convertirme(int fianza) {
        
        Especulador especulador = new Especulador(fianza,this);
        
        return especulador;
    }
    
    @Override
    public String toString() {
        return "Jugador " + nombre + "\n      Saldo= " + saldo + "\n      Encarcelado= " + encarcelado + "\n      Capital= "+ this.obtenerCapital() +"\n      CartaLibertad= " + cartaLibertad + "\n\n      CasillaActual= " + casillaActual + "\n\n      PROPIEDADES\n " + propiedades;
    }
    
    protected boolean deboIrACarcel() {
        boolean deboIrACarcel;
        
        if (tengoCartaLibertad()) {
            deboIrACarcel = false;
        }
        else {
            deboIrACarcel = true;
        }
        
        return deboIrACarcel;
    }
    
    protected boolean puedoEdificarCasa(TituloPropiedad titulo) {
        boolean puedo_edificar = false;
        
        if (titulo.getNumCasas() < 4) {
            puedo_edificar = true;
        }
        
        return puedo_edificar;
    }
    
    protected boolean puedoEdificarHotel(TituloPropiedad titulo) {
        boolean puedo_edificar = false;
        
        if (titulo.getNumHoteles() < 4 && titulo.getNumCasas() == 4) {
            puedo_edificar = true;
        }
        
        return puedo_edificar;    
    }
    
    
       
    
}
