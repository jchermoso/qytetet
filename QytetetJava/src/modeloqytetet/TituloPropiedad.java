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
public class TituloPropiedad {
    private String nombre;
    private boolean hipotecada = false;
    private int alquilerBase = 0;
    private float factorRevalorizacion = 0;
    private int hipotecaBase = 0;
    private int precioEdificar = 0;
    private int numHoteles = 0;
    private int numCasas = 0;
    private int precioCompra = 0;
    private Jugador propietario;
    
    public TituloPropiedad(String nombre, int alquilerBase,
    float factorRevalorizacion, int hipotecaBase, int precioEdificar, int precioCompra) {
        this.nombre = nombre;
        this.alquilerBase = alquilerBase;
        this.factorRevalorizacion = factorRevalorizacion;
        this.hipotecaBase = hipotecaBase;
        this.precioEdificar = precioEdificar;
        this.precioCompra = precioCompra;
    }

    String getNombre() {
        return nombre;
    }

    boolean isHipotecada() {
        return hipotecada;
    }

    void setHipotecada(boolean hipotecada) {
        this.hipotecada = hipotecada;
    }
    
    int pagarAlquiler() {
        int costeAlquiler = this.calcularImporteAlquiler();
        propietario.modificarSaldo(costeAlquiler);
        
        return costeAlquiler;
    }

    int getAlquilerBase() {
        return alquilerBase;
    }

    float getFactorRevalorizacion() {
        return factorRevalorizacion;
    }

    int getHipotecaBase() {
        return hipotecaBase;
    }

    int getPrecioEdificar() {
        return precioEdificar;
    }

    int getNumHoteles() {
        return numHoteles;
    }

    int getNumCasas() {
        return numCasas;
    }

    int getPrecioCompra() {
        return precioCompra;
    }
    
    int calcularCosteCancelar() {
        return (int) (getHipotecaBase() + getNumCasas()*0.5*getHipotecaBase()+getNumHoteles()*getHipotecaBase());    
    }
    
    int calcularCosteHipotecar() {
        return (int) (calcularCosteCancelar()*0.5);
    }
    
    int calcularImporteAlquiler() {
        int costeAlquiler = (int) (alquilerBase + 2*(numCasas*0.5+numHoteles*2));
        
        propietario.modificarSaldo(costeAlquiler);
        
        return costeAlquiler;
    }
    
    int calcularPrecioVenta() {
        int precioVenta = (int) (precioCompra + (numCasas + numHoteles) * precioEdificar * factorRevalorizacion);
        
        return precioVenta;
    }
    
    boolean cancelarHipoteca() {
        hipotecada = false;
        
        return true;
    }
    
    
    void edificarCasa() {
        numCasas++;
    }
    
    void edificarHotel() {
        numHoteles++;
    }
    
    Jugador getPropietario() {
        return propietario;
    }
    
    int hipotecar() {
        setHipotecada(true);
        int costeHipoteca = calcularCosteHipotecar();
        
        return costeHipoteca;
    }
    
    boolean propietarioEncarcelado() {
       return propietario.getEncarcelado();
    }
    
    void setPropietario(Jugador propietario) {
        this.propietario = propietario;
    }
    
    boolean tengoPropietario() {
        return propietario!=null;
    }

    @Override
    public String toString() {
        return "TituloPropiedad\n" + "\n       Nombre=" + nombre + "\n       Hipotecada=" + hipotecada + "\n       AlquilerBase=" + alquilerBase + "\n       FactorRevalorizacion=" + factorRevalorizacion + "\n       HipotecaBase=" + hipotecaBase + "\n       PrecioEdificar=" + precioEdificar + "\n       NumHoteles=" + numHoteles + "\n       NumCasas=" + numCasas + "\n       PrecioCompra=" + precioCompra;
    }
    
    
}
    
