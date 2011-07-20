#!/usr/bin/env ruby
['open-uri', 'nokogiri'].each(&method(:require))

class String
  def titlecase
    downcase.split.map {|w| capitalization_exceptions.include?(w) ? w : w.capitalize}.join(" ").upfirst
  end

  def upfirst
    self[0,1].capitalize + self[1,length-1]
  end

  private
    def capitalization_exceptions
      ['of','a','the','and','an','or','nor','but','if','then','else','when','up','at','from','by','on','off','for','in','out','over','to']
    end	
end

if __FILE__ == $0
  url = 'http://www.terangdunia.net/injil-tuhan-yesus'
  doc = Nokogiri::HTML(open(url))
  i   = 1
  doc.xpath("//tr[@class='sectiontableentry1' or @class='sectiontableentry2']/td[2]/a").each do |node|
    ns = String.new(node.text.strip!)
    print "INSERT INTO toc ('id', 'title') VALUES('#{i}','#{ns.titlecase}');\n"
    i+=1
  end

#  (1..3).each{|x| puts x}
#  hash = {}
#  keys.size.times { |i| hash[ keys[i] ] = values[i] }
#  hash

end
