/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static java.util.Collections.swap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Juan Carlos Hermoso Quesada
 */
public class Qytetet {
    private ArrayList<Sorpresa> mazo = new ArrayList<>();
    private Tablero tablero;
    public static int MAX_JUGADORES = 4;
    static int NUM_SORPRESAS = 10;
    public static int NUM_CASILLAS = 20;
    static int PRECIO_LIBERTAD = 200;
    static int SALDO_SALIDA = 1000;
    private Dado dado = Dado.getInstance();
    private static final Qytetet instance = new Qytetet();
    private Jugador jugadorActual;
    private Sorpresa cartaActual;
    private ArrayList<Jugador> jugadores = new ArrayList();
    private EstadoJuego estado;
    
    public Qytetet() {
        
    }
    
    public static Qytetet getInstance() {
        return instance;
    }
        
    ArrayList<Sorpresa> getMazo() {
        return mazo;
    }
    
    private void inicializarTablero() {
        tablero = new Tablero();
    }
    
    private void inicializarCartasSorpresa() {
        //Convertirme
       mazo.add(new Sorpresa("Ahora eres especulador, puedes salir de la cárcel si pagas la fianza de 3000 pavos",3000,TipoSorpresa.CONVERTIRME));
       mazo.add(new Sorpresa("Ahora eres especulador, puedes salir de la cárcel si pagas la fianza de 5000 pavos",5000,TipoSorpresa.CONVERTIRME));
       //SalirCarcel
       mazo.add(new Sorpresa("¡A la cárcel!",tablero.getCarcel().getNumeroCasilla(),TipoSorpresa.IRACASILLA));
       mazo.add(new Sorpresa ("Un fan anónimo ha pagado tu fianza. Sales de la cárcel", 
               0, TipoSorpresa.SALIRCARCEL));
       //IrACasilla
       mazo.add(new Sorpresa("Ve a la casilla 4",4, TipoSorpresa.IRACASILLA));
       mazo.add(new Sorpresa("Ve a la casilla 11",11,TipoSorpresa.IRACASILLA));
       //PagarCobrar
       mazo.add(new Sorpresa("Recibes la cantidad de "+100,100, TipoSorpresa.PAGARCOBRAR));
       mazo.add(new Sorpresa("Se te resta la cantidad de "+100,-100,TipoSorpresa.PAGARCOBRAR));
       //PorCasaHotel
       mazo.add(new Sorpresa("Recibes 100 pavos por cada propiedad ",100,TipoSorpresa.PORCASAHOTEL));
       mazo.add(new Sorpresa("Se te restan 100 pavos por cada propiedad",-100,TipoSorpresa.PORCASAHOTEL));
       //PorJugador
       mazo.add(new Sorpresa("Recibes 100 pavos por cada jugador ", 100,TipoSorpresa.PORJUGADOR));
       mazo.add(new Sorpresa("Se te restan 100 pavos por cada jugador", -100,TipoSorpresa.PORJUGADOR));
       
       Collections.shuffle(mazo);
    }
         
    public Tablero getTablero() {
        return tablero;
    }
    
    void actuarSiEnCasillaEdificable() {
        boolean deboPagar = jugadorActual.deboPagarAlquiler();
        
        if (deboPagar) {
            jugadorActual.pagarAlquiler();
            
            if (jugadorActual.getSaldo()<=0) {
                setEstadoJuego(EstadoJuego.ALGUNJUGADORBANCARROTA);
            }
        }
        
        Casilla casilla = jugadorActual.getCasillaActual();
        Calle calle = (Calle)casilla;
        
        boolean tengoPropietario = calle.tengoPropietario();
    
        if (estado!=EstadoJuego.ALGUNJUGADORBANCARROTA) {
            if (tengoPropietario) {
                setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
            }
            else {
                setEstadoJuego(EstadoJuego.JA_PUEDECOMPRAROGESTIONAR);
            }
        }
    }
    
    void actuarSiEnCasillaNoEdificable() {
        setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        Casilla casillaActual = jugadorActual.getCasillaActual();
                
        if (casillaActual.getTipo() == TipoCasilla.IMPUESTO) {
            jugadorActual.pagarImpuesto();
            
            if (jugadorActual.getSaldo() <= 0) {
                setEstadoJuego(EstadoJuego.ALGUNJUGADORBANCARROTA);
            }
        }
        else if (casillaActual.getTipo() == TipoCasilla.JUEZ) {
            System.out.println("\nTe vas a la cárcel\n");
            encarcelarJugador();
        }
        else if (casillaActual.getTipo() == TipoCasilla.SORPRESA){
            cartaActual = mazo.remove(0);
            setEstadoJuego(EstadoJuego.JA_CONSORPRESA);
        }
    }
    
    public void aplicarSorpresa() {
        setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
    
        if (cartaActual.getTipo() == TipoSorpresa.SALIRCARCEL) {
            jugadorActual.setCartaLibertad(cartaActual);
        }
        else {
            mazo.add(mazo.size()-1,cartaActual);
            
            if (cartaActual.getTipo() == TipoSorpresa.PAGARCOBRAR) {
                jugadorActual.modificarSaldo(cartaActual.getValor());
                
                if (jugadorActual.getSaldo()<=0) {
                    setEstadoJuego(EstadoJuego.ALGUNJUGADORBANCARROTA);
                }
            }
            else if (cartaActual.getTipo() == TipoSorpresa.IRACASILLA){
                boolean casillaCarcel = tablero.esCasillaCarcel(cartaActual.getValor());
                
                if (casillaCarcel) {
                    encarcelarJugador();
                }
                else {
                    mover(cartaActual.getValor());
                }
            }
            else if (cartaActual.getTipo() == TipoSorpresa.PORCASAHOTEL) {
                int cantidad = cartaActual.getValor();
                
                int numeroTotal = jugadorActual.cuantasCasasHotelesTengo();
                
                jugadorActual.modificarSaldo(cantidad*numeroTotal);
                
                if (jugadorActual.getSaldo()<=0) {
                    setEstadoJuego(EstadoJuego.ALGUNJUGADORBANCARROTA);
                }
            }
            else if (cartaActual.getTipo() == TipoSorpresa.PORJUGADOR) {
                for (int i = 0; i < jugadores.size(); i++) {
                    if (jugadorActual != jugadores.get(i)) {
                        jugadores.get(i).modificarSaldo(cartaActual.getValor());
                    }
                    
                    if (jugadores.get(i).getSaldo()<=0) {
                        setEstadoJuego(EstadoJuego.ALGUNJUGADORBANCARROTA);
                    }
                    
                    jugadorActual.modificarSaldo(cartaActual.getValor());
                    
                    if (jugadorActual.getSaldo()<=0) {
                        setEstadoJuego(EstadoJuego.ALGUNJUGADORBANCARROTA);
                    }
                }
            }
            else if (cartaActual.getTipo() == TipoSorpresa.CONVERTIRME) {
                Especulador especulador = jugadorActual.convertirme(cartaActual.getValor());
                System.out.println("Ahora es un especulador: " + especulador.toString());
                jugadores.set(jugadores.indexOf(jugadorActual), especulador);
                jugadorActual = especulador;
                
                
            }
        }
    }
    
    public boolean cancelarHipoteca(int numeroCasilla) {
        TituloPropiedad titulo = getTablero().getCasillas().get(numeroCasilla).getTitulo();
        boolean cancelar = false;
        
        if (titulo.isHipotecada()) {
            cancelar = jugadorActual.cancelarHipoteca(titulo);
        }
        else {
            System.out.println("\nLa propiedad de la casilla " + numeroCasilla + " no está hipotecada\n");
        }
        setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        
        return cancelar;
    }
    
    public boolean comprarTituloPropiedad() {
        boolean comprado = jugadorActual.comprarTituloPropiedad();
        
        if (comprado) {
            setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        }
        
        return comprado;
    }
    
    public boolean edificarCasa(int numeroCasilla) {
        boolean edificada = false;
        
        Casilla casilla = tablero.obtenerCasillaNumero(numeroCasilla);
        TituloPropiedad titulo = casilla.getTitulo();
        edificada = jugadorActual.edificarCasa(titulo);
        
        if (edificada) {
            setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        }
        
        return edificada;
    }
    
    public boolean edificarHotel(int numeroCasilla) {
        boolean edificada = false;
        
        Casilla casilla = tablero.obtenerCasillaNumero(numeroCasilla);
        TituloPropiedad titulo = casilla.getTitulo();
        edificada = jugadorActual.edificarHotel(titulo);
        
        if (edificada) {
            setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        }
        
        return edificada;
    }
    
    private void encarcelarJugador() {
        if (jugadorActual.deboIrACarcel()) {
            Casilla casillaCarcel = tablero.getCarcel(); 
            jugadorActual.irACarcel(casillaCarcel);
            setEstadoJuego(EstadoJuego.JA_ENCARCELADO);
        }
        else {
            System.out.println("\nTe has librado de la cárcel porque tenías la carta de libertad\n");
            Sorpresa carta = jugadorActual.devolverCartaLibertad();
            mazo.add(carta);
            setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        }
    }
    
    public Sorpresa getCartaActual() {
        return cartaActual;
    }
    
    Dado getDado() {
        return dado;
    }
    
    public EstadoJuego getEstadoJuego() {
        return estado;
    }
    
    public Jugador getJugadorActual() {
        return jugadorActual;
    }
    
    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }
    
    public int getValorDado() {
        return dado.getValor();
    }
    
    public void hipotecarPropiedad(int numeroCasilla) {
        Casilla casilla = tablero.obtenerCasillaNumero(numeroCasilla);
        TituloPropiedad titulo = casilla.getTitulo();
        
        if (!titulo.isHipotecada()) {
            jugadorActual.hipotecarPropiedad(titulo);
        }
        else {
            System.out.println("\nLa propiedad ya está hipotecada\n");
        }
        setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
    }
    
    public void inicializarJuego(ArrayList<String> nombres) {
        inicializarTablero();
        inicializarJugadores(nombres);
        inicializarCartasSorpresa();
        salidaJugadores();
    }
    
    private void inicializarJugadores(ArrayList<String> nombres) {
        for (String s: nombres) {
            Jugador j = new Jugador(s);
            jugadores.add(j);
        }
    }
    
    public boolean intentarSalirCarcel(MetodoSalirCarcel metodo) {
        if (metodo == MetodoSalirCarcel.TIRANDODADO) {
            int resultado = tirarDado();
            
            System.out.println("\nHa tocado un " + resultado + "\n");
            
            if (resultado >= 5) {
                jugadorActual.setEncarcelado(false);
            }
        }
        else if (metodo == MetodoSalirCarcel.PAGANDOLIBERTAD) {
            jugadorActual.pagarLibertad(PRECIO_LIBERTAD);
        }
        
        boolean encarcelado = jugadorActual.getEncarcelado();
        
        if (encarcelado) {
            setEstadoJuego(EstadoJuego.JA_ENCARCELADO);
        }
        else {
            setEstadoJuego(EstadoJuego.JA_PREPARADO);
        }
        
        return !encarcelado;
    }
    
    public void jugar() {
        tirarDado();
        Casilla casillaFinal = tablero.obtenerCasillaFinal(jugadorActual.getCasillaActual(), dado.getValor());
        mover(casillaFinal.getNumeroCasilla());
    }
    
    void mover(int numCasillaDestino) {
        Casilla casillaInicial = jugadorActual.getCasillaActual();
        Casilla casillaFinal = tablero.obtenerCasillaNumero(numCasillaDestino);
        jugadorActual.setCasillaActual(casillaFinal);
                        
        if (numCasillaDestino < casillaInicial.getNumeroCasilla()) {
            jugadorActual.modificarSaldo(SALDO_SALIDA);
        }
        
        if (casillaFinal.soyEdificable()) {
            actuarSiEnCasillaEdificable();
        }
        else {
            actuarSiEnCasillaNoEdificable();
        }
    }
    
    /*public Casilla obtenerCasillasTablero() {
        throw new UnsupportedOperationException("Sin implementar");
    }*/
    
    /*public void obtenerRanking() {
 
    }*/
    
    public int obtenerSaldoJugadorActual() {
        return jugadorActual.getSaldo();
    }
    
    private void salidaJugadores() {
        for (int i = 0; i < jugadores.size(); i++) {
            jugadores.get(i).setCasillaActual(tablero.getCasillas().get(0));
        }
        
        Random rand = new Random();
        
        jugadorActual = jugadores.get(rand.nextInt(jugadores.size()-1));
        
        estado = EstadoJuego.JA_PREPARADO;
    }
    
    private void setCartaActual(Sorpresa cartaActual) {
        this.cartaActual = cartaActual;
    }
    
    private void setEstadoJuego(EstadoJuego estado) {
        this.estado = estado;
    }
    
    public void siguienteJugador() {        
        int ultima_pos = 0;
        
        for (int i = 0; i < jugadores.size(); i++) {
            if (jugadorActual == jugadores.get(i)) {
                ultima_pos = i;
            }
        }
        
        if (ultima_pos == jugadores.size()-1) {
            ultima_pos = 0;
        }
        else {
            ultima_pos++;
        }
        
        jugadorActual = jugadores.get(ultima_pos);
        
        if (jugadorActual.getEncarcelado()) {
            estado = EstadoJuego.JA_ENCARCELADOCONOPCIONDELIBERTAD;
        }
        else {
            estado = EstadoJuego.JA_PREPARADO;
        }
        
    }
    
    int tirarDado() {
        return dado.tirar();
    }
    
    public void venderPropiedad(int numeroCasilla) {
        Casilla casilla = tablero.obtenerCasillaNumero(numeroCasilla);
        jugadorActual.venderPropiedad(casilla);
        setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
    }

    public static int getMAX_JUGADORES() {
        return MAX_JUGADORES;
    }
    
    public ArrayList<Integer> obtenerPropiedadesJugador() {
        ArrayList<Integer> propiedades_jugador = new ArrayList();
        
        for (int i = 0; i < tablero.getCasillas().size(); i++) {
            for (int j = 0; j < jugadorActual.getPropiedades().size(); j++) {
                if (tablero.getCasillas().get(i).getTitulo() == jugadorActual.getPropiedades().get(j)) {
                    propiedades_jugador.add(tablero.getCasillas().get(i).getNumeroCasilla());
                }
            }
            
        }
        return propiedades_jugador;
    }  
    
    public ArrayList<Integer> obtenerPropiedadesJugadorSegunEstadoHipoteca(boolean estadoHipoteca) {
        ArrayList<TituloPropiedad> propiedades_jugador = new ArrayList();
        ArrayList<Integer> numeros_casillas = new ArrayList();

        propiedades_jugador = jugadorActual.obtenerPropiedades(estadoHipoteca);

        for (int i = 0; i < propiedades_jugador.size(); i++) {
            if (propiedades_jugador.get(i).isHipotecada() == estadoHipoteca) {
                for (int j = 0; j < tablero.getCasillas().size(); j++) {
                    if (tablero.getCasillas().get(j).getTitulo().getNombre() == propiedades_jugador.get(i).getNombre()) {
                        numeros_casillas.add(tablero.getCasillas().get(i).getNumeroCasilla());
                    }
                }
            }
        }

        return numeros_casillas;
    }
    
    
}

