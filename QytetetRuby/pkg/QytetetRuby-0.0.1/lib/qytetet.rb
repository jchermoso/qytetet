#encoding: utf-8
require_relative "sorpresa"
require_relative "tipo_sorpresa"
require_relative "tablero"
require "singleton"
require_relative "jugador"
require_relative "dado"
require_relative "estado_juego"
require_relative "casilla"
require_relative "titulo_propiedad"

module ModeloQytetet
  class Qytetet
    include Singleton
    
    attr_accessor :estado, :jugadores, :mazo, :tablero, :jugadorActual, :cartaActual
    attr_reader :dado
    
    @@MAX_JUGADORES = 4
    @@NUM_SORPRESAS = 10
    @@NUM_CASILLAS = 20
    @@PRECIO_LIBERTAD = 200
    @@SALDO_SALIDA = 1000
     
    def initialize
      @mazo = Array.new
      @jugadores = Array.new
      @dado = Dado.instance
      @jugadorActual = nil 
      @cartaActual = nil
    end
    
    def inicializarCartasSorpresa
      @mazo << Sorpresa.new("Se te resta la cantidad 100",-100,TipoSorpresa::PAGARCOBRAR)
      @mazo << Sorpresa.new("Ahora eres especulador, puedes salir de la cárcel si pagas la fianza de 5000 pavos",5000,TipoSorpresa::CONVERTIRME)
      @mazo << Sorpresa.new("¡A la cárcel!", @tablero.carcel.numeroCasilla, TipoSorpresa::IRACASILLA)
      @mazo << Sorpresa.new("Un fan anónimo ha pagado tu fianza. Sales de la cárcel", 0, TipoSorpresa::SALIRCARCEL)
      @mazo << Sorpresa.new("Ve a la casilla 4", 4, TipoSorpresa::IRACASILLA)
      @mazo << Sorpresa.new("Ve a la casilla 11", 11, TipoSorpresa::IRACASILLA)
      @mazo << Sorpresa.new("Recibes la cantidad de 100",100,TipoSorpresa::PAGARCOBRAR)
      @mazo << Sorpresa.new("Recibes 100 pavos por cada jugador",100, TipoSorpresa::PORJUGADOR)
      @mazo << Sorpresa.new("Se te restan 100 pavos por cada jugador",-100, TipoSorpresa::PORJUGADOR)
      @mazo << Sorpresa.new("Recibes 100 pavos por cada propiedad",100, TipoSorpresa::PORCASAHOTEL)
      @mazo << Sorpresa.new("Se te restan 100 pavos por cada propiedad",100,TipoSorpresa::PORCASAHOTEL)
      
      @mazo.shuffle!
    end
    
    def iniciarTablero
      @tablero = Tablero.new
    end
    
    def actuarSiEnCasillaEdificable
      deboPagar = @jugadorActual.deboPagarAlquiler
      
      if deboPagar
        @jugadorActual.pagarAlquiler
        
        if @jugadorActual.saldo <= 0
          @estado = EstadoJuego::ALGUNJUGADORENBANCARROTA
        end
      end
      
      tengo_propietario = @jugadorActual.casillaActual.send(:tengoPropietario)
      
      if @estado != EstadoJuego::ALGUNJUGADORENBANCARROTA
        if tengo_propietario
          @estado = EstadoJuego::JA_PUEDEGESTIONAR
        else
          @estado = EstadoJuego::JA_PUEDECOMPRAROGESTIONAR
        end
      end
    end
    
    def actuarSiEnCasillaNoEdificable
      @estado = EstadoJuego::JA_PUEDEGESTIONAR
            
      if @jugadorActual.casillaActual.tipo == TipoCasilla::IMPUESTO
        @jugadorActual.send(:pagarImpuesto)
        
        if @jugadorActual.saldo <= 0
          @estado = EstadoJuego::ALGUNJUGADORENBANCARROTA
        end
      elsif (@jugadorActual.casillaActual.tipo) == TipoCasilla::JUEZ
        puts "\nSoy el juez, te vas a la cárcel\n"
        encarcelarJugador
      elsif @jugadorActual.casillaActual.tipo == TipoCasilla::SORPRESA
        @cartaActual = @mazo.at(0)
        @mazo.delete_at(0)
        @estado = EstadoJuego::JA_CONSORPRESA
      end
    end
    
    def aplicarSorpresa
      @estado = EstadoJuego::JA_PUEDEGESTIONAR
      
      if @cartaActual.tipo == TipoSorpresa::SALIRCARCEL
        @jugadorActual.cartaLibertad = @cartaActual
      else
        @mazo.push(@cartaActual)       
        
        if @cartaActual.tipo == TipoSorpresa::PAGARCOBRAR
          @jugadorActual.modificarSaldo(@cartaActual.valor)
          
          if @jugadorActual.saldo <= 0
            @estado = EstadoJuego::ALGUNJUGADORENBANCARROTA
          end
        
        elsif @cartaActual.tipo == TipoSorpresa::IRACASILLA
          casillaCarcel = @tablero.esCasillaCarcel(@cartaActual.valor)
          
          if casillaCarcel
            encarcelarJugador
          else
            mover(@cartaActual.valor)
          end
          
        elsif @cartaActual.tipo == TipoSorpresa::PORJUGADOR
          for i in 0..@jugadores.size-1
            if @jugadorActual != @jugadores.at(i)
              @jugadores.at(i).modificarSaldo(@cartaActual.valor)
            end
            
            if @jugadores.at(i).saldo <= 0
              @estado = EstadoJuego::ALGUNJUGADORENBANCARROTA
            end
            
            @jugadorActual.modificarSaldo(@cartaActual.valor)
            
            if @jugadorActual.saldo <= 0
              @estado = EstadoJuego::ALGUNJUGADORENBANCARROTA
            end
          end
        elsif @cartaActual.tipo == TipoSorpresa::PORCASAHOTEL
          cantidad = @cartaActual.valor
          numeroTotal = @jugadorActual.cuantasCasasHotelesTengo
          
          @jugadorActual.modificarSaldo(cantidad*numeroTotal)
        
          if @jugadorActual.saldo <= 0
            @estado = EstadoJuego::ALGUNJUGADORENBANCARROTA
          end
        
        elsif @cartaActual.tipo == TipoSorpresa::CONVERTIRME
          especulador = @jugadorActual.convertirme(@cartaActual.valor)
          i = @jugadores.index(@jugadorActual)
          @jugadores[i] = especulador
          @jugadorActual = especulador
        end
          
      end
    end
    
    def cancelarHipoteca(numeroCasilla)
      titulo = @tablero.casillas.at(numeroCasilla).titulo
      
      if titulo.hipotecada
        cancelar = @jugadorActual.cancelarHipoteca(titulo)
      else
        puts "\nLa propiedad de la casilla " + numeroCasilla.to_s+" no está hipotecada\n"
      end
      @estado = EstadoJuego::JA_PUEDEGESTIONAR
      
      return cancelar
    end
    
    def comprarTituloPropiedad
      comprado = @jugadorActual.comprarTituloPropiedad
      
      if comprado
        @estado = EstadoJuego::JA_PUEDEGESTIONAR
      end
      
      return comprado
    end
    
    def edificarCasa(numeroCasilla)
      edificada = false
      
      casilla = @tablero.obtenerCasillaNumero(numeroCasilla)
      titulo = casilla.titulo
      edificada = @jugadorActual.edificarCasa(titulo)
      
      if edificada
        @estado = EstadoJuego::JA_PUEDEGESTIONAR
      end
      
      return edificada
    end
    
    def edificarHotel(numeroCasilla)
      edificada = false
      
      casilla = @tablero.obtenerCasillaNumero(numeroCasilla)
      titulo = casilla.titulo
      edificada = @jugadorActual.edificarHotel(titulo)
      
      if edificada
        @estado = EstadoJuego::JA_PUEDEGESTIONAR
      end
      
      return edificada
    end
    
    def encarcelarJugador
      if (@jugadorActual.send(:deboIrACarcel))
        casillaCarcel = @tablero.carcel
        @jugadorActual.irACarcel(casillaCarcel)
        @estado = EstadoJuego::JA_ENCARCELADO
      else
        puts "\nTe has librado de la cárcel porque tenías la carta de libertad\n"
        carta = @jugadorActual.devolverCartaLibertad
        @mazo << carta
        @estado = EstadoJuego::JA_PUEDEGESTIONAR
      end
    end
    
    def hipotecarPropiedad(numeroCasilla)
      casilla = @tablero.obtenerCasillaNumero(numeroCasilla)
      titulo = casilla.titulo
      
      if !titulo.hipotecada
        @jugadorActual.hipotecarPropiedad(titulo)
      else
        puts "\nLa propiedad ya está hipotecada\n"
      end
      
      @estado = EstadoJuego::JA_PUEDEGESTIONAR
    end
    
    def inicializarJuego(nombres)
      iniciarTablero
      inicializarJugadores(nombres)
      inicializarCartasSorpresa
      salidaJugadores
    end
    
    def inicializarJugadores(nombres)
      nombres.each{|x| 
        j = Jugador.new(x)
        @jugadores << j
      }

    end
    
    def intentarSalirCarcel(metodo)
      if metodo == MetodoSalirCarcel::TIRANDODADO
        resultado = tirarDado
        
        puts "\nHa tocado un " + resultado.to_s+ "\n"
        
        if resultado >= 5
          @jugadorActual.encarcelado = false
        end
      elsif metodo == MetodoSalirCarcel::PAGANDOLIBERTAD
        @jugadorActual.pagarLibertad(@@PRECIO_LIBERTAD)
      end
      
      encarcelado = @jugadorActual.encarcelado
      
      if encarcelado
        @estado = EstadoJuego::JA_ENCARCELADO
      else
        @estado = EstadoJuego::JA_PREPARADO
      end
      
      return !encarcelado
    end
    
    def jugar
      tirarDado
      casillaFinal = @tablero.obtenerCasillaFinal(@jugadorActual.casillaActual,@dado.valor)
      mover(casillaFinal.numeroCasilla)
    end
    
    def mover(numCasillaDestino)
      casillaInicial = @jugadorActual.casillaActual
      @jugadorActual.casillaActual = @tablero.obtenerCasillaNumero(numCasillaDestino)
         
      if numCasillaDestino < casillaInicial.numeroCasilla.to_i
        @jugadorActual.modificarSaldo(@@SALDO_SALIDA)
      end
     
      if @jugadorActual.casillaActual.send(:soyEdificable)
        actuarSiEnCasillaEdificable
      else
        actuarSiEnCasillaNoEdificable
      end
      
    end
    
    def obtenerCasillasTablero
      raise NotImplementedError
    end
    
    def obtenerRanking
      @jugadores = @jugadores.sort
    end
    
    def obtenerSaldoJugadorActual
      return @jugadorActual.saldo
    end
    
    def salidaJugadores
      @jugadores.each{|x|
        x.casillaActual = @tablero.casillas[0]
      }
      @jugadorActual = @jugadores.sample
      
      @estado = EstadoJuego::JA_PREPARADO
      
    end
    
    def siguienteJugador
      ultima_pos = 0
      
      for i in 0..@jugadores.size
        if @jugadorActual == @jugadores.at(i)
        ultima_pos = i
        end
      end
      
      if ultima_pos == @jugadores.size-1
        ultima_pos = 0
      else
        ultima_pos = ultima_pos + 1
      end
      
      @jugadorActual = @jugadores.at(ultima_pos)
      
      
     if @jugadorActual.encarcelado
       @estado = EstadoJuego::JA_ENCARCELADOCONOPCIONDELIBERTAD
     else
       @estado = EstadoJuego::JA_PREPARADO
     end
    end
    
    def tirarDado
      @dado.tirar
    end
    
    def venderPropiedad(numeroCasilla)
      casilla = @tablero.obtenerCasillaNumero(numeroCasilla)
      @jugadorActual.venderPropiedad(casilla)
      @estado = EstadoJuego::JA_PUEDEGESTIONAR
    end
    
    def getValorDado
      @dado.valor
    end
    
    def obtenerPropiedadesJugador
      propiedades_jugador = Array.new
           
      for i in 0..@tablero.casillas.size-1
        for j in 0..@jugadorActual.propiedades.size-1
          if (@tablero.casillas.at(i).titulo == @jugadorActual.propiedades.at(j))
            propiedades_jugador << @tablero.casillas.at(i).numeroCasilla
          end
        end
      end
      
      return propiedades_jugador
    end
    
    def obtenerPropiedadesJugadorSegunEstadoHipoteca(estadoHipoteca)
      propiedades_jugador = Array.new
      numeros_casillas = Array.new
      
      propiedades_jugador << @jugadorActual.obtenerPropiedades(estadoHipoteca)
      
      for i in 0..propiedades_jugador.size-1
        if propiedades_jugador.at(i).hipotecada == estadoHipoteca
          for j in 0..@tablero.casillas.size-1
            if @tablero.casillas.at(j).titulo.nombre == propiedades_jugador.at(i).nombre
              numeros_casillas << @tablero.casillas.at(i).numeroCasilla
            end
          end
        end
      end
      
      return numeros_casillas
    end
    
     private :encarcelarJugador, :inicializarCartasSorpresa, :inicializarJugadores, :iniciarTablero,
              :salidaJugadores
    end
end
