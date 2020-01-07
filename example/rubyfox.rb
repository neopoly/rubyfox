module Rubyfox
  def self.init(extension)
    puts "Loaded by Java class: #{extension.inspect}"
    Handler.new
  end

  class Handler
    def on_init

      puts "init"
    end

    def on_request(args)
      puts "request: #{args.inspect}"
    end

    def on_event(args)
      puts "event: #{args.inspect}"
    end

    def on_destroy
      puts "destroy"
    end
  end

end