package com.lu.pojo.GraphEntity;

public class MyGraph{
    public Point[] point;
    public int[] visted;
    public int numPoint;
    public int numEdeges;
    public MyGraph() {}
    public MyGraph(int numPoint,int numEdeges)
    {
        this.numPoint=numPoint;
        this.numEdeges=numEdeges;
        point=new Point[numPoint];
        visted=new int[numPoint];
    }
    public void createMyGraph(MyGraph MyGraph,int numPoint,int numEdeges,int EdegesPoint[][])
    {
        for(int i=0;i<numPoint;i++)
        {
            MyGraph.visted[i]=0;
            MyGraph.point[i]=new Point(i);
        }
        for(int i=0;i<numEdeges;i++)
        {
            EdegeNode a=new EdegeNode(EdegesPoint[i][1],EdegesPoint[i][2]);
            a.nextEdge=MyGraph.point[EdegesPoint[i][0]].firstArc;
            MyGraph.point[EdegesPoint[i][0]].firstArc=a;
        }
    }

    public void DFS(MyGraph MyGraph,int m)
    {
        EdegeNode a = null;
        MyGraph.visted[m]=1;
        a=MyGraph.point[m].firstArc;
        while(a!=null)
        {
            if(MyGraph.visted[a.adjvex]==0)
                DFS(MyGraph,a.adjvex);
            a=a.nextEdge;
        }
    }


}
