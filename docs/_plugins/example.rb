
module Jekyll
  class ExampleTag < Liquid::Tag
    def initialize(tag_name, args, tokens)
      super
      parts = args.split(" ")
      @filename = parts[0]
      @type = if parts.length == 2 then parts[1] else @filename.split(".")[-1] end
      path = "../examples/#{@filename}"
      if File.exists?(path)
        @content = File.read(path)
      else
        @content = "Not found #{path}"
      end
    end

    def render(context)
      escaped = @content.gsub("<", "&lt;").gsub(">", "&gt;")
      code = "<pre class=\"brush:#{@type}\"><code>\n#{escaped}\n</code></pre>"

      short_name = @filename.split("/")[-1]

      should_not_link = @filename.include?("target")
      if should_not_link then
        "#{short_name}\n\n#{code}"
      else
        link = "<a href=\"https://github.com/stephenh/tessell/blob/master/examples/#{@filename}\">#{short_name}</a>:"
        "#{link}\n\n#{code}"
      end
    end
  end
end

Liquid::Template.register_tag('example', Jekyll::ExampleTag)
