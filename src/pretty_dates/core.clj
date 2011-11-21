(ns pretty-dates.core
  (:require [clj-time.core :as ct] 
            [clj-time.format :as cf]))

(defn readable-time-ago [time]
  (let [date     (if (string? time)
                   (cf/parse time)
                   time)
        diff     (ct/in-secs (ct/interval date (ct/now)))
        day-diff (int (/ diff 86400))
        worker   (fn [a b unit]
                   (let [amt (int (/ a b))]
                     (str amt " " unit (if (> amt 1) "s") " ago")))]
    (condp > day-diff
      1 (condp > diff
          60 (do (println diff) "just now")
          120 "1 minute ago"
          3600 (worker diff 60 "minute")
          7200 "1 hour ago"
          (worker diff 3600 "hour"))
      2 "Yesterday"
      7 (str day-diff " days ago")
      31 (worker day-diff 7 "week")
      365 (worker day-diff 31 "month")
      (worker day-diff 365 "year"))))
