(ns healthfinch.text
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def stop-words #{"I"
                  "a"
                  "about"
                  "an"
                  "and"
                  "are"
                  "as"
                  "at"
                  "be"
                  "but"
                  "by"
                  "com"
                  "for"
                  "from"
                  "how"
                  "in"
                  "is"
                  "it"
                  "of"
                  "on"
                  "or"
                  "that"
                  "the"
                  "this"
                  "to"
                  "was"
                  "what"
                  "when"
                  "where"
                  "who"
                  "will"
                  "with"})

(defn tokenize [string]
  (-> string
      str/lower-case
      (str/split #"[^\w-]+")
      ((partial remove stop-words))))

(defn count-words [string]
  (reduce (fn [counts token] (assoc counts token (inc (get counts token 0))))
          {}
          (tokenize string)))
