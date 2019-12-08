#encoding: utf-8
require "singleton"

module ModeloQytetet
  class Dado
    include Singleton 
    
    attr_reader :valor
    
    def initialize
      
    end
    
    def tirar
      numero = Random.new
      
      @valor = 1 +numero.rand(6)
    end
    
  end
end
