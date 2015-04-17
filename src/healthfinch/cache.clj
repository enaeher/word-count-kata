(ns healthfinch.cache
  (:require [healthfinch.text :as text]
            [healthfinch.html :as html]))

(def stored-urls (atom {}))

(defn word-counts-for-url* [url]
  (text/count-words (html/extract-text (html/parse-url url)) 10))

(defn word-counts-for-url
  "Retrieves counts for the ten most frequent words at the given URL
  from either the URL cache or, in the event of a cache miss, the URL
  itself."
  [url]
  (or (@stored-urls url)
      (get (swap! stored-urls assoc url (word-counts-for-url* url)) url)))
