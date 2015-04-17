(ns healthfinch.html-test
  (:require [healthfinch.html :refer :all]
            [healthfinch.text :as text]
            [clojure.test :refer :all]))

(def reasonable-html "<div><p>The quick <abbr title=\"not a title\">brown</abbr> fox <em>jumps</em> over the <str>lazy</str> dog.</p></div>")
(def unreasonable-html "<div><p>The quick brown fox</div> <font style=\"\">jumps</p> over the <str>lazy</str> dog.</p></div>")

(deftest text-extraction
  (testing "Extracting text from HTML fragments"
    (is (= "The quick brown fox jumps over the lazy dog."
           (extract-text (parse reasonable-html)))
        "Didn't return the right results for correct html")
    (is (= "The quick brown fox jumps over the lazy dog."
           (extract-text (parse unreasonable-html)))
        "Didn't return the right results for \"tag soup\" html")))
