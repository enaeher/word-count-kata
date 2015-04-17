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

(defn tokenize
  "Given a string, returns a vector of tokens. Strips punctuation,
  folds case, and removes stop words."
  [string]
  (-> string
      str/lower-case
      (str/split #"[^\w]+")
      ((partial remove stop-words))))

(defn count-words
  "Given a string, returns a vector of pairs of the form [token
  occurrences] in descending order of number of occurrences. If N is
  provided, return a map of only the N most frequent words."
  ([string]
   (sort-by second (comparator >)
            (reduce (fn [counts token] (assoc counts token (inc (get counts token 0))))
                    {}
                    (tokenize string))))
  ([string n]
   (take n (count-words string))))
