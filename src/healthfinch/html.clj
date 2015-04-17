(ns healthfinch.html
  (:import [net.htmlparser.jericho Source TextExtractor]))

(defn parse
  "Wraps the Jericho Source constructor, which can take a string or a
  java.net.URL, among other things."
  [html-string]
  (Source. html-string))

(defn parse-url
  "Given a URL, retrieves it and returns the parsed HTML."
  [url]
  (parse (java.net.URL. url)))

(defn extract-text
  "Returns only the text in the parse-html object."
  [^net.htmlparser.jericho.Source parsed-html]
  (str (TextExtractor. parsed-html)))
