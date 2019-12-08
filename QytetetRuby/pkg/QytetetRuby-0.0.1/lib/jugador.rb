#encoding: utf-8

require_relative "titulo_propiedad"
require_relative "casilla"
require_relative "especulador"

module ModeloQytetet
  class Jugador
    attr_accessor :cartaLibertad, :casillaActual, :encarcelado, :nombre, :propiedades, :saldo
    
    def initialize(nombre, saldo = 7500, encarcelado = false, propiedades = Array.new, carta = nil, casilla = nil)
      @nombre = nombre
      @saldo = saldo
      @encarcelado = encarcelado
      @propiedades = propiedades
      @cartaLibertad = carta
      @casillaActual = casilla
    end
    
    def <=>(otroJugador)            
      otroJugador.obtenerCapital <=> obtenerCapital     
    end
    
    def cancelarHipoteca(titulo)
      cancelar = false
      cantidadRecibida = titulo.calcularCosteCancelar
      
      if @saldo >= cantidadRecibida
        modificarSaldo(-cantidadRecibida)
        cancelar = titulo.cancelarHipoteca
      end
      
      return cancelar
    end
    
    def comprarTituloPropiedad
      comprado = false
      
      costeCompra = @casillaActual.coste
      
      if costeCompra < @saldo
        @casillaActual.asignarPropietarioJugador(self)
        comprado = true
        @propiedades << @casillaActual.titulo
        modificarSaldo(-costeCompra)
      end
      
      return comprado
    end
    
    def cuantasCasasHotelesTengo
      casas_hoteles = 0
      
      propiedades.each{|x|
        casas_hoteles = x.numCasas + 1
        casas_hoteles = x.numHoteles + 1
      }
      
      return casas_hoteles
    end
    
    def deboPagarAlquiler
      deboPagar = false
      titulo = @casillaActual.titulo
      
      if !esDeMiPropiedad(titulo) && titulo.tengoPropietario && !titulo.propietarioEncarcelado && !titulo.hipotecada
        deboPagar = true
      end
      
      return deboPagar
    end
    
    def deboIrACarcel
      deboIr = true
      
      if tengoCartaLibertad
        deboIr = false
      end
      
      return deboIr
    end
    
    def devolverCartaLibertad
      auxiliar = @cartaLibertad
      @cartaLibertad = nil
      
      return auxiliar
    end
    
    def edificarCasa(titulo)
      edificada = false
      
      if puedoEdificarCasa(titulo)
        costeEdificarCasa = titulo.precioEdificar
        
        if tengoSaldo(costeEdificarCasa)
          titulo.edificarCasa
          modificarSaldo(-costeEdificarCasa)
          edificada = true
        end
      end
      
      return edificada;
    end
    
    def edificarHotel(titulo)
      edificada = false
      
      if puedoEdificarHotel(titulo)
        costeEdificarHotel = titulo.precioEdificar
        
        if tengoSaldo(costeEdificarHotel)
          titulo.edificarHotel
          modificarSaldo(-costeEdificarHotel)
          edificada = true
        end
      end
      
      return edificada;
    end
    
    def eliminarDeMisPropiedades(titulo)
      @propiedades.delete(titulo)
      titulo.propietario = nil
    end
    
    def esDeMiPropiedad(titulo)
      contiene = false
 
      @propiedades.each {|x| 
        if x.nombre == titulo.nombre then contiene = true end}
      
      return contiene
    end
    
    def estoyEnCalleLibre
      raise NotImplementedError
    end
        
    def hipotecarPropiedad(titulo)
      costeHipoteca = titulo.hipotecar
      modificarSaldo(costeHipoteca)
    end
    
    def irACarcel(casilla)
      @casillaActual = casilla
      @encarcelado = true
    end
    
    def modificarSaldo(cantidad)
      @saldo = @saldo + cantidad
      
      return @saldo
    end
    
    def obtenerCapital
      capital = 0
      
      @propiedades.each{|x| capital = capital + (x.precioCompra + x.numCasas+ x.numHoteles)*x.precioEdificar
          if x.hipotecada
            capital = capital - x.hipotecaBase
          end}
      
      capital = capital + @saldo
      
      return capital
    end
    
    def obtenerPropiedades(hipotecada)
      obtener_propiedades = Array.new
      
      @propiedades.each{|x|
        if x.hipotecada == hipotecada 
          obtener_propiedades << x
        end}
      
      return obtener_propiedades
    end
    
    def pagarAlquiler
      costeAlquiler = @casillaActual.pagarAlquiler
      
      puts "\nEsta casilla tiene dueño, pagas el alquiler de " + costeAlquiler.to_s + " euros"
      
      modificarSaldo(-costeAlquiler)
    end
    
    def pagarImpuesto
      @saldo = @saldo - @casillaActual.coste
    end
    
    def pagarLibertad(cantidad)
      tengoSaldo = tengoSaldo(cantidad)
      
      if tengoSaldo
        @encarcelado = false
        modificarSaldo(-cantidad)
      end
    end
    
    def tengoCartaLibertad
      tengocartaLibertad = false
      
      if @cartaLibertad!=nil
        tengocartaLibertad = true
      end
      
      return tengocartaLibertad
    end
    
    def tengoSaldo(cantidad)
      if @saldo > cantidad
        return true
      else
        return false
      end
    end
    
    def venderPropiedad(casilla)
      titulo = casilla.titulo
      eliminarDeMisPropiedades(titulo)
      precioVenta = titulo.calcularPrecioVenta
      modificarSaldo(precioVenta)
    end
    
    def convertirme(fianza)
      especulador = Especulador.new(self,fianza)
      
      return especulador
    end
    
    def puedoEdificarCasa(titulo) 
      puedo_edificar = false
      
      if titulo.numCasas < 4
        puedo_edificar = true
      end
      
      return puedo_edificar
    end
    
    def puedoEdificarHotel(titulo)
      puedo_edificar = false
      
      if titulo.numHoteles < 4 && titulo.numCasas == 4
        puedo_edificar = true
      end
      
      return puedo_edificar
    end
    
    def to_s
      "Nombre " +@nombre+" \n Saldo #{@saldo} \n CartaLibertad #{@cartaLibertad} \n CasillaActual #{@casillaActual} \n Encarcelado #{@encarcelado} \n Propiedades #{@propiedades}"
    end
    
    protected  :deboIrACarcel, :pagarImpuesto, :puedoEdificarCasa,
               :puedoEdificarHotel, :tengoSaldo
    private :eliminarDeMisPropiedades, :esDeMiPropiedad
  end
end

