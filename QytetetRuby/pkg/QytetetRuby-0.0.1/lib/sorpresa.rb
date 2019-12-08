#encoding: utf-8

module ModeloQytetet
  class Sorpresa
    attr_reader :texto, :tipo, :valor
    
    def initialize(texto, valor, tipo)
      @texto = texto
      @valor = valor
      @tipo = tipo
    end
    
    def to_s
      "Texto: #{@texto} \n Valor: #{@valor} \n Tipo: #{@tipo}"
    end 
  end
end
