#encoding: utf-8

module ModeloQytetet
  class TituloPropiedad
    attr_reader :nombre, :alquilerBase, :factorRevalorizacion, :hipotecaBase, :precioEdificar,
                  :numHoteles, :numCasas, :precioCompra
    attr_accessor :hipotecada, :propietario
    
    def initialize(nombre, alquilerBase, factorRevalorizacion, hipotecaBase,
      precioEdificar, precioCompra)
      @nombre = nombre
      @alquilerBase = alquilerBase
      @factorRevalorizacion = factorRevalorizacion
      @hipotecaBase = hipotecaBase
      @precioEdificar = precioEdificar
      @hipotecada = false
      @numHoteles = 0
      @numCasas = 0
      @precioCompra = precioCompra
    end
        
    def calcularCosteCancelar
      return @hipotecaBase + @numCasas*0.5*@hipotecaBase+@numHoteles*@hipotecaBase
    end
    
    def calcularCosteHipotecar
      return calcularCosteCancelar*0.5
    end
    
    def calcularImporteAlquiler
      costeAlquiler = @alquilerBase + 2*(@numCasas*0.5 + @numHoteles*2)
      
      @propietario.modificarSaldo(costeAlquiler)
      
      return costeAlquiler      
    end
    
    def calcularPrecioVenta
      precioVenta = (@precioCompra + (@numCasas + @numHoteles) * @precioEdificar * @factorRevalorizacion)
      
      return precioVenta
    end
    
    def edificarCasa
      @numCasas = @numCasas + 1
    end
    
    def edificarHotel
      @numHoteles = @numHoteles + 1
    end
    
    def hipotecar
      @hipotecada = true
      costeHipoteca = calcularCosteHipotecar
      
      return costeHipoteca
    end
    
    def propietarioEncarcelado
      return @propietario.encarcelado
    end
    
    def tengoPropietario
      return @propietario!=nil
    end
    
    def cancelarHipoteca
      @hipotecada = false
      
      return true
    end
    
    def pagarAlquiler
      costeAlquiler = calcularImporteAlquiler
      @propietario.modificarSaldo(costeAlquiler)
      
      return costeAlquiler
    end
    
    def to_s
      "\n\nTituloPropiedad\n    Nombre:   "+ @nombre.to_s + "\n   NumeroCasilla:   "+@numeroCasilla.to_s+ "\n    AlquilerBase:   "+@alquilerBase.to_s+
      "\n   FactorRevalorización:   "+@factorRevalorizacion.to_s+"\n    HipotecaBase:   " +@hipotecaBase.to_s+ 
       "\n    PrecioEdificar:   " + @precioEdificar.to_s+
       "\n    Hipotecada:   "+@hipotecada.to_s+"\n    NumCasas:   "+@numCasas.to_s+"\n    NumHoteles:   "+@numHoteles.to_s
    end 
  end
end
