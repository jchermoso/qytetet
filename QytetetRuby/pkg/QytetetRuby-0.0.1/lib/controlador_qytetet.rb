#encoding: utf-8
require "singleton"
require_relative "qytetet"
require_relative "opcion_menu"
require_relative "estado_juego"
require_relative "metodo_salir_carcel"

module ControladorQytetet
  class ControladorQytetet
    attr_accessor :nombreJugadores, :modelo
   
    include Singleton
    
    def initialize
      @modelo = ModeloQytetet::Qytetet.instance
      @nombreJugadores = Array.new
    end
    
    def obtenerOperacionesJuegoValidas
      operaciones = Array.new
      
      if @modelo.jugadores.empty?
        operaciones << OpcionMenu.index(:iniciar_juego)
      end
      
      if @modelo.estado == ModeloQytetet::EstadoJuego::JA_PREPARADO
        operaciones << OpcionMenu.index(:jugar)
      end
      
      if @modelo.estado == ModeloQytetet::EstadoJuego::JA_PUEDEGESTIONAR
        operaciones << OpcionMenu.index(:pasar_turno)
        operaciones << OpcionMenu.index(:vender_propiedad)
        operaciones << OpcionMenu.index(:hipotecar_propiedad)
        operaciones << OpcionMenu.index(:cancelar_hipoteca)
        operaciones << OpcionMenu.index(:edificar_casa)
        operaciones << OpcionMenu.index(:edificar_hotel)
      end
      
      if @modelo.estado == ModeloQytetet::EstadoJuego::JA_PUEDECOMPRAROGESTIONAR
        operaciones << OpcionMenu.index(:pasar_turno)
        operaciones << OpcionMenu.index(:vender_propiedad)
        operaciones << OpcionMenu.index(:hipotecar_propiedad)
        operaciones << OpcionMenu.index(:cancelar_hipoteca)
        operaciones << OpcionMenu.index(:edificar_casa)
        operaciones << OpcionMenu.index(:edificar_hotel)
        operaciones << OpcionMenu.index(:comprar_titulo_propiedad)
      end
      
      if @modelo.estado == ModeloQytetet::EstadoJuego:: JA_ENCARCELADOCONOPCIONDELIBERTAD
        operaciones << OpcionMenu.index(:intentar_salir_carcel_pagando)
        operaciones << OpcionMenu.index(:intentar_salir_carcel_tirando)
      end
      
      if @modelo.estado == ModeloQytetet::EstadoJuego::JA_ENCARCELADO
        operaciones << OpcionMenu.index(:pasar_turno)
      end
      
      if @modelo.estado == ModeloQytetet::EstadoJuego::JA_CONSORPRESA
        operaciones << OpcionMenu.index(:aplicar_sorpresa)
      end     
      
      if @modelo.estado == ModeloQytetet::EstadoJuego::ALGUNJUGADORENBANCARROTA
        operaciones << OpcionMenu.index(:obtener_ranking)
      end 
      
      operaciones << OpcionMenu.index(:mostrar_jugador_actual)
      operaciones << OpcionMenu.index(:mostrar_jugadores)
      operaciones << OpcionMenu.index(:mostrar_tablero)
      operaciones << OpcionMenu.index(:terminar_juego)
            
      return operaciones
    end
    
    def necesitaElegirCasilla(opcionMenu)
      necesita = false
      opcion = OpcionMenu.at(opcionMenu)
      
      if (opcion == :hipotecar_propiedad || opcion == :cancelar_hipoteca ||
          opcion == :edificar_casa || opcion == :edificar_hotl ||
          opcion == :vender_propiedad)
          necesita = true
      end
      
      return necesita
    end
    
    def obtenerCasillasValidas(opcionMenu)
      casillas_validas = Array.new
      propiedades = Array.new
      se_puede = necesitaElegirCasilla(opcionMenu)
      
      if se_puede
        casillas_validas = @modelo.obtenerPropiedadesJugador
      elsif se_puede && opcionMenu == 6
        casillas_validas = @modelo.obtenerPropiedadesJugadorSegunEstadoHipoteca(false)      
      elsif se_puede && opcionMenu == 7
        casillas_validas == @modelo.obtenerPropiedadesJugadorSegunEstadoHipoteca(true)
      end
      
      return casillas_validas
    end
    
    def realizarOperacion(opcionElegida, casillaElegida)
      mensaje = nil
      opcion = OpcionMenu.at(opcionElegida)
      
      
      if opcion == :iniciar_juego
        @modelo.inicializarJuego(@nombreJugadores)
        
        for i in 0..@modelo.jugadores.size-1
          puts "\nJugador " + @modelo.jugadores.at(i).nombre+"\n"  
        end
        
        mensaje = "\n---JUEGO INICIADO---\n"
        
      elsif opcion == :aplicar_sorpresa
        puts @modelo.cartaActual
        @modelo.aplicarSorpresa      
        
      elsif opcion == :comprar_titulo_propiedad
        if @modelo.comprarTituloPropiedad
          mensaje = "La propiedad ha sido comprada"
        else
          mensaje = "La propiedad no ha sido comprada"
        end
        
      elsif opcion == :edificar_casa
        if @modelo.edificarCasa(casillaElegida)
            mensaje = "La casa ha sido edificada"
        else
            mensaje = "La casa no ha sido edificada"
        end
        
      elsif opcion == :edificar_hotel
        if @modelo.edificarHotel(casillaElegida)
          mensaje = "El hotel ha sido edificado"
        else
          mensaje = "El hotel no has sido edificado"
        end
        
      elsif opcion == :hipotecar_propiedad
        @modelo.hipotecarPropiedad(casillaElegida)
        #mensaje = "La propiedad de la casilla " + casillaElegida.to_s + " ha sido hipotecada"
      
      elsif opcion == :cancelar_hipoteca
        if @modelo.cancelarHipoteca(casillaElegida)
          mensaje = "La hipoteca ha sido cancelada"
        else
          mensaje = "La hipoteca no ha sido cancelada"
        end
        
      elsif opcion == :intentar_salir_carcel_pagando
        if @modelo.intentarSalirCarcel(ModeloQytetet::MetodoSalirCarcel::PAGANDOLIBERTAD)
          mensaje = "Ha salido de la carcel"
        else
          mensaje = "No ha salido de la carcel"
        end 
        
      elsif opcion == :intentar_salir_carcel_tirando
        if @modelo.intentarSalirCarcel(ModeloQytetet::MetodoSalirCarcel::TIRANDODADO)
          mensaje = "Ha salido de la carcel"
        else
          mensaje = "No ha salido de la carcel"
        end
      elsif opcion == :jugar
        @modelo.jugar
        mensaje = "\nHa tocado un " + @modelo.getValorDado.to_s + ", vas a la casilla " + @modelo.jugadorActual.casillaActual.numeroCasilla.to_s + "\n"
        puts @modelo.tablero.casillas.at(@modelo.jugadorActual.casillaActual.numeroCasilla.to_i).to_s
      elsif opcion == :obtener_ranking
        @modelo.obtenerRanking
        mensaje = "\nEl ganador ha sido:\n" + @modelo.jugadores.at(0).to_s + "\n"
        
      elsif opcion == :pasar_turno
        puts "\nEl jugador "+ @modelo.jugadorActual.nombre.to_s + " pasa el turno"
        @modelo.siguienteJugador
        mensaje = "\nEs el turno de " + @modelo.jugadorActual.nombre + "\n"
      
      elsif opcion == :terminar_juego
        mensaje = "\nJuego terminado\n"
        exit
       
      elsif opcion == :vender_propiedad
        @modelo.venderPropiedad(casillaElegida)
        mensaje = "\nLa propiedad de la casilla " + casillaElegida.to_s + " ha sido vendida.\n"
        
      elsif opcion == :mostrar_jugador_actual
        mensaje = "\nEl jugador actual es: \n" + @modelo.jugadorActual.to_s
        
      elsif opcion == :mostrar_jugadores
        mensaje = "\nJUGADORES\n" + @modelo.jugadores.to_s
          
      elsif opcion == :mostrar_tablero
        mensaje = "\nTABLERO\n" + @modelo.tablero.to_s
      end
      
      return mensaje
    end
  end
end

