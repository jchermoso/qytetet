/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorqytetet;
import java.util.ArrayList;
import modeloqytetet.EstadoJuego;
import modeloqytetet.MetodoSalirCarcel;
import modeloqytetet.Qytetet;
/**
 *
 * @author jc
 */
public class ControladorQytetet {
    private static final ControladorQytetet instance = new ControladorQytetet();
    private Qytetet modelo = Qytetet.getInstance();
    private static ArrayList<String> nombreJugadores = new ArrayList();
    
    private ControladorQytetet() {
        
    }
    
    public static ControladorQytetet getInstance() {
        return instance;
    }
    
    public void setNombreJugadores(ArrayList<String> nombreJugadores) {
        this.nombreJugadores = nombreJugadores;
    }
    
    public ArrayList<Integer> obtenerOperacionesJuegoValidas() {
        ArrayList<Integer> operaciones = new ArrayList();
        
        if (modelo.getJugadores().isEmpty()) {
            operaciones.add(OpcionMenu.INICIARJUEGO.ordinal());
        }
        
        if (modelo.getEstadoJuego() == EstadoJuego.JA_PREPARADO) {
            operaciones.add(OpcionMenu.JUGAR.ordinal());
        }
        
        if (modelo.getEstadoJuego() == EstadoJuego.JA_PUEDEGESTIONAR) {
            operaciones.add(OpcionMenu.PASARTURNO.ordinal());
            operaciones.add(OpcionMenu.VENDERPROPIEDAD.ordinal());
            operaciones.add(OpcionMenu.HIPOTECARPROPIEDAD.ordinal());
            operaciones.add(OpcionMenu.CANCELARHIPOTECA.ordinal());
            operaciones.add(OpcionMenu.EDIFICARCASA.ordinal());
            operaciones.add(OpcionMenu.EDIFICARHOTEL.ordinal());
        }
        
        if (modelo.getEstadoJuego() == EstadoJuego.JA_PUEDECOMPRAROGESTIONAR) {
            operaciones.add(OpcionMenu.PASARTURNO.ordinal());
            operaciones.add(OpcionMenu.VENDERPROPIEDAD.ordinal());
            operaciones.add(OpcionMenu.HIPOTECARPROPIEDAD.ordinal());
            operaciones.add(OpcionMenu.CANCELARHIPOTECA.ordinal());
            operaciones.add(OpcionMenu.EDIFICARCASA.ordinal());
            operaciones.add(OpcionMenu.EDIFICARHOTEL.ordinal());
            operaciones.add(OpcionMenu.COMPRARTITULOPROPIEDAD.ordinal());
        }
        
        if (modelo.getEstadoJuego() == EstadoJuego.JA_ENCARCELADOCONOPCIONDELIBERTAD) {
             operaciones.add(OpcionMenu.INTENTARSALIRCARCELTIRANDODADO.ordinal());  
             operaciones.add(OpcionMenu.INTENTARSALIRCARCELPAGANDOLIBERTAD.ordinal());
        }
        
        if (modelo.getEstadoJuego() == EstadoJuego.JA_ENCARCELADO) {
            operaciones.add(OpcionMenu.PASARTURNO.ordinal());
        }
        
        if (modelo.getEstadoJuego() == EstadoJuego.JA_CONSORPRESA) {
            operaciones.add(OpcionMenu.APLICARSORPRESA.ordinal());
        }
        
        if (modelo.getEstadoJuego() == EstadoJuego.ALGUNJUGADORBANCARROTA) {
            operaciones.add(OpcionMenu.OBTENERRANKING.ordinal());
        }
        
        operaciones.add(OpcionMenu.MOSTRARJUGADORACTUAL.ordinal());
        operaciones.add(OpcionMenu.MOSTRARJUGADORES.ordinal());
        operaciones.add(OpcionMenu.MOSTRARTABLERO.ordinal());
        operaciones.add(OpcionMenu.TERMINARJUEGO.ordinal());
        
        return operaciones;
    }
    
    public boolean necesitaElegirCasilla(int opcionMenu) {
        boolean necesita = false;
        OpcionMenu opcion = OpcionMenu.values()[opcionMenu];
        
        if (opcion == OpcionMenu.HIPOTECARPROPIEDAD || opcion == OpcionMenu.CANCELARHIPOTECA ||
            opcion == OpcionMenu.EDIFICARCASA || opcion == OpcionMenu.EDIFICARHOTEL ||
            opcion == OpcionMenu.VENDERPROPIEDAD) {
            necesita = true;
        }
        
        return necesita;
    }
    
    public ArrayList<Integer> obtenerCasillasValidas(int opcionMenu) {
        ArrayList<Integer> casillas_validas = new ArrayList();
        ArrayList<String> propiedades = new ArrayList();
        
        boolean se_puede = necesitaElegirCasilla(opcionMenu);
        
        if (se_puede) {
            casillas_validas = modelo.obtenerPropiedadesJugador();
        }
        else if (se_puede && opcionMenu == 6) {
            casillas_validas = modelo.obtenerPropiedadesJugadorSegunEstadoHipoteca(false);
        }
        else if (se_puede && opcionMenu == 7) {
            casillas_validas = modelo.obtenerPropiedadesJugadorSegunEstadoHipoteca(true);
        }
        
        return casillas_validas;
    }
    
    public String realizarOperacion(int opcionElegida, int casillaElegida) {
        String mensaje = null;
        
        OpcionMenu opcion = OpcionMenu.values()[opcionElegida];
        
        if (opcion == OpcionMenu.INICIARJUEGO) {
            modelo.inicializarJuego(nombreJugadores);
            
            for (int i = 0; i < modelo.getJugadores().size(); i++) {
                System.out.println("\nJugador "+(i+1)+": "+modelo.getJugadores().get(i).getNombre()+"\n");
            }
            
            mensaje = "---JUEGO INICIADO---";
        }
        
        else if (opcion == OpcionMenu.APLICARSORPRESA) {
            System.out.println(modelo.getCartaActual());
            modelo.aplicarSorpresa();
        }
        
        else if (opcion == OpcionMenu.CANCELARHIPOTECA) {
            if (modelo.cancelarHipoteca(casillaElegida)) {
               mensaje = "La hipoteca ha sido cancelada";
            }
            else {
                mensaje = "La hipoteca no ha sido cancelada";
            }
        }
        
        else if (opcion == OpcionMenu.COMPRARTITULOPROPIEDAD) {
            if (modelo.comprarTituloPropiedad()) {
                mensaje = "La propiedad ha sido comprada";
            }  
            else {
                mensaje = "La propiedad no ha sido comprada";
            }
        }
        
        else if (opcion == OpcionMenu.EDIFICARCASA) {
            if (modelo.edificarCasa(casillaElegida)) {
                mensaje = "La casa ha sido edificada";
            }
            else {
                mensaje = "La casa no ha sido edificada";
            }
        }
        
        else if (opcion == OpcionMenu.EDIFICARHOTEL) {
            if (modelo.edificarHotel(casillaElegida)) {
                mensaje = "El hotel ha sido edificado";
            }
            else {
                mensaje = "El hotel no ha sido edificado";
            }
        }
        
        else if (opcion == OpcionMenu.HIPOTECARPROPIEDAD) {
            modelo.hipotecarPropiedad(casillaElegida);
            mensaje = "La propiedad de la casilla " + casillaElegida + " ha sido hipotecada";
        }
        
        else if (opcion == OpcionMenu.INTENTARSALIRCARCELPAGANDOLIBERTAD) {
            if (modelo.intentarSalirCarcel(MetodoSalirCarcel.PAGANDOLIBERTAD)) {
                mensaje = "Ha salido de la carcel";
            }
            else {
                mensaje = "No ha salido de la carcel";
            }
        }
        
        else if (opcion == OpcionMenu.INTENTARSALIRCARCELTIRANDODADO) {
            if (modelo.intentarSalirCarcel(MetodoSalirCarcel.TIRANDODADO)) {
                mensaje = "Ha salido de la carcel";
            }
            else {
                mensaje = "No ha salido de la carcel";
            }
        }
        
        else if (opcion == OpcionMenu.JUGAR) {
            modelo.jugar();
            mensaje = "\nHa tocado un " + modelo.getValorDado() + ", vas a la casilla " + modelo.getJugadorActual().getCasillaActual().getNumeroCasilla()+"\n";
        }
        
        else  if (opcion == OpcionMenu.OBTENERRANKING) {
            mensaje = "\nJuego terminado\n";
        }
        
        else if (opcion == OpcionMenu.PASARTURNO) {
            System.out.println("\nEl jugador " + modelo.getJugadorActual().getNombre() + " pasa el turno");
            modelo.siguienteJugador();
            mensaje = "\nEs el turno de "+ modelo.getJugadorActual().getNombre() + "\n";
                         
        }
        
        else if (opcion == OpcionMenu.TERMINARJUEGO) {
            System.exit(0);
        }
        
        else if (opcion == OpcionMenu.VENDERPROPIEDAD) {
            modelo.venderPropiedad(casillaElegida);
            mensaje = "\nLa propiedad de la casilla " + casillaElegida + " ha sido vendida.\n";
        }
        
        else if (opcion == OpcionMenu.MOSTRARJUGADORACTUAL) {
            mensaje = "\nEl jugador actual es:\n" + modelo.getJugadorActual();
        }
        
        else if (opcion == OpcionMenu.MOSTRARTABLERO) {
            mensaje = "\nTABLERO\n" + modelo.getTablero();
        }
        
        else if (opcion == OpcionMenu.MOSTRARJUGADORES) {
            mensaje = "\nJUGADORES\n" + modelo.getJugadores();
        }
        
        return mensaje;
    }

}
