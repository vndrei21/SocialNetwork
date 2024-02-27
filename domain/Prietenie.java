package com.example.lab8.domain;

import java.time.LocalDateTime;


public class Prietenie extends Entity<Tuple<Long,Long>> {

    LocalDateTime date;
    Long prieten1,prieten2;
    public Prietenie(Long id1,Long id2, LocalDateTime dateTime) {
        prieten1 = id1;
        prieten2 = id2;
        this.date = dateTime;

    }

    /**
     *
     * @return the date when the friendship was created
     */
    public Tuple<Long,Long> get_id()
    {
        Tuple<Long,Long> l = new Tuple<>(this.prieten1,this.prieten2);
        return l;
    }
    public LocalDateTime getDate() {
        return date;
    }
    
    @Override
public String toString()
    {
        String s = "Primul id:" + prieten1.toString() + " | Al doilea id: "+ prieten2.toString() + "| Data:" + date.toString();
            return s;
    }

}
