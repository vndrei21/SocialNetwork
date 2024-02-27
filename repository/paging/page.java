package com.example.lab8.repository.paging;

public class page<E> {
    private Iterable<E> elementsonpage;
    private int totalnumberofelements;

    public page(Iterable<E> elems,int totalnumberofelements)
    {
        this.elementsonpage = elems;
        this.totalnumberofelements = totalnumberofelements;
    }

    public int getTotalnumberofelements() {
        return totalnumberofelements;
    }

    public Iterable<E> getElementsonpage() {
        return elementsonpage;
    }

    @Override
    public String toString() {
        return "page{" +
                "elementsonpage=" + elementsonpage +
                ", totalnumberofelements=" + totalnumberofelements +
                '}';
    }
}
