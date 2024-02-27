package com.example.lab8.DFS;




import com.example.lab8.domain.Utilizator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DFS {
    private LinkedList<Long> []adiacenta;
    private int NrComponenteConexe;
    private int V;
    ArrayList<ArrayList<Integer>> Components;
    List<Utilizator> utilizators;
    @SuppressWarnings("unchecked")


    /**
     *Constructor
     */
    public DFS(List<Utilizator> list)
    {
        Components = new ArrayList<>();
        NrComponenteConexe = 0;
        utilizators = list;
        V = list.size();
        System.out.println(V);
        adiacenta = new LinkedList[V+1];
        for (Utilizator user: list) {
            adiacenta[Math.toIntExact(user.getId())] = new LinkedList<>();

        }

    }

    /**
     * adauga o muchie
     * @param id1 primul nod
     * @param id2 al doilea nod
     */
    public void addEdge(Long id1,Long id2)
    {
        adiacenta[Math.toIntExact(id1)].add(id2);
        adiacenta[Math.toIntExact(id2)].add(id1);
    }

    /**
     * determina o componenta conexa
     * @param v id-ul
     * @param visited lista care retine daca varful a fost sau nu vizitat
     * @param al componenta conexa propriuzisa
     */
    public void DFSUtil(Long v, boolean[] visited, ArrayList<Integer>al)
    {
        visited[v.intValue()] = true;
        al.add(v.intValue());
        Iterator<Long> it = adiacenta[v.intValue()].iterator();
        while (it.hasNext())
        {
            Long n = it.next();
            if(!visited[n.intValue()])
                DFSUtil(n,visited,al);
        }
    }

    /**
     * functia de dfs
     * @return nr de componente conexe
     */
    public int dfs()
    {
        boolean[] visited = new boolean[V+1];

        for (Utilizator user:utilizators)
        {
            ArrayList<Integer> al = new ArrayList<>();
            if(!visited[Math.toIntExact(user.getId())]) {
                DFSUtil(user.getId(), visited, al);
                NrComponenteConexe++;
                Components.add(al);
            }

        }
        return NrComponenteConexe;
    }

    /**
     *
     * @return lista persoanelor are fac parte din comunitatea maxima
     */
    public List<Utilizator> comunitate_max()
    {
        int max = -1;
        ArrayList<Integer>listMax = new ArrayList<>();
        for (ArrayList<Integer> al: Components) {
            if(max < al.size()) {
                max = al.size();
                listMax = al;
            }
        }
        List<Utilizator> utilizators1 = new ArrayList<>();
        for (int id:listMax) {
            utilizators1.add(utilizators.get(id));
        }
        return utilizators1;
    }

}
