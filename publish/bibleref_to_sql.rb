#!/usr/bin/env ruby

[ 'open-uri', 'nokogiri', 'ruby-debug', 'enumerator', 'net/http', 'uri',
  'scanf', 'pp', 'ap', # => # awesome_print
  'rb-lib/string'].each(&method(:require))

REFERENCE   = "Matius 9:32-34"
$start_no   = 1
ITJ_CHAPTER = 33
TOC_URL     = "http://bibledbdata.org/onlinebibles/indonesian_tb/index.htm"

class Application
  def run()
    references = REFERENCE.split(',')
    references.each { |item| parse(item.strip, item != references.last) }
  end

  def parse(ref, trailingNL)
    # break REFERENCE down to sub-components
    
    # format across chapters
    reference = ref.scan(/(\D+)\s(\d+):(\d+)-(\d+):(\d+)/).flatten
    theBook, start_chapter, start_verse, end_chapter, end_verse = reference
    
    if !theBook.nil?
      if Integer(start_chapter) > Integer(end_chapter)
        raise "Error in chapter range"
      end

      chapter = Integer(start_chapter)
      begin
        runOnce(true, true, theBook, chapter.to_s, start_verse, getEndChapter(theBook, chapter))
        chapter += 1
      end while chapter < Integer(end_chapter)

      runOnce(true, trailingNL, theBook, end_chapter, 1, end_verse)
      return
    end

    if theBook.nil?
      # format range within a chapter
      reference = ref.scan(/(\D+)\s(\d+):(\d+)-(\d+)/).flatten
      theBook, chapter, start_verse, end_verse = reference
      if Integer(start_verse) > Integer(end_verse)
        raise "Error in verse range"
      end
    end

    if theBook.nil?
      # format single verse
      reference = ref.scan(/(\D+)\s(\d+):(\d+)/).flatten
      theBook, chapter, start_verse = reference
      end_verse = start_verse
    end

    runOnce(true, trailingNL, theBook, chapter, start_verse, end_verse)
  end

  def runOnce(header, footer, theBook, chapter, start_verse, end_verse)
    # 1st HTTP request
    url = URI.parse(TOC_URL)
    req = Net::HTTP::Get.new(url.path)
    res = Net::HTTP.start(url.host, url.port) { |http| http.request(req) }

    # DEBUG
    if $DEBUG
      books = res.body.gsub("\n", "").scan(/<br><br>([^\[]+)\s+?\[/).flatten
      ap books.index theBook.upcase
    end

    # parse TOC for book
    book  = res.body.gsub("\n", "").scan(/<br><br>#{theBook.upcase}\s+?\[<[^b]*br><br>/)[0]
    doc   = Nokogiri::HTML::fragment(book)
    elem  = doc.xpath(".//a").select { |item| item.text == "#{chapter}" }

    # 2nd HTTP request +parse
    url = File.join(Pathname.new(TOC_URL).dirname, elem[0]["href"])

    verses = fetch(url).gsub("\n", "").scan(/<blockquote>.*<\/blockquote>/)[0].scan(/#{chapter}:[^<]+<br>/).unshift("")[Integer(start_verse)..Integer(end_verse)]
    
    # generate SQL
    start_no = $start_no
    
    if header:
      puts "INSERT INTO book ('chapter', 'verse', 'content') VALUES(#{ITJ_CHAPTER}, #{start_no}, '#{theBook} #{chapter}');"
      start_no += 1
      puts "INSERT INTO book ('chapter', 'verse', 'content') VALUES(#{ITJ_CHAPTER}, #{start_no}, '');"
      start_no += 1
    end
    
    verses.each do |line|
      puts "INSERT INTO book ('chapter', 'verse', 'content') VALUES(#{ITJ_CHAPTER}, #{start_no}, '#{line[(line.index(':')+1)..-1].gsub("<br>","")}');"
      start_no += 1
    end

    if footer:
      puts "INSERT INTO book ('chapter', 'verse', 'content') VALUES(#{ITJ_CHAPTER}, #{start_no}, '');"
      start_no += 1
    end
    
    $start_no = start_no
  end

  def getEndChapter(theBook, chapter)
    # 1st HTTP request
    url = URI.parse(TOC_URL)
    req = Net::HTTP::Get.new(url.path)
    res = Net::HTTP.start(url.host, url.port) { |http| http.request(req) }

    # parse TOC for book
    book  = res.body.gsub("\n", "").scan(/<br><br>#{theBook.upcase}\s+?\[<[^b]*br><br>/)[0]
    doc   = Nokogiri::HTML::fragment(book)
    elem  = doc.xpath(".//a").select { |item| item.text == "#{chapter}" }

    # 2nd HTTP request
    url = File.join(Pathname.new(TOC_URL).dirname, elem[0]["href"])
    verses = fetch(url).gsub("\n", "").scan(/<blockquote>.*<\/blockquote>/)[0].scan(/#{chapter}:[^<]+<br>/).unshift("")
    last_verse = verses[-1].scan(/#{chapter}:[0-9]+/)[0].strip.split(':')[1]
    
    return Integer(last_verse)
  end

  def fetch(uri_str, limit = 10)
    # You should choose better exception.
    raise ArgumentError, 'HTTP redirect too deep' if limit == 0
    response = Net::HTTP.get_response(URI.parse(uri_str))
    case response
    when Net::HTTPSuccess     then response
    when Net::HTTPRedirection then fetch(response['location'], limit - 1)
    else
      response.error!
    end
    return response.body
  end
end
  
if __FILE__ == $PROGRAM_NAME
  app = Application.new
  app.run()
end
