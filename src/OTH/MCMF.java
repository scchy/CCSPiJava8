package OTH;

import java.util.*;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName MCMF.java
 * @Description
 * @createTime 2023年03月03日 21:09:00
 */
public class MCMF {
    // 图: 边
    public static class Edge{
        public String eType;
        public Integer cap;
        public Integer capOrg;
        public Double cost;
        public Integer from;
        public Integer to;
        // 记录反向边的在to中的位置
        public Integer antiEdgeIndexInToV;
        public Edge(Integer from, Integer to, Integer cap, Double cost, Integer antiEdgeIndexInToV){
            this.eType = "forward"; // forward reverse;
            this.from = from;
            this.to = to;
            this.cap = cap;
            this.capOrg = cap;
            this.cost = cost;
            this.antiEdgeIndexInToV = antiEdgeIndexInToV;
        }

        public Edge(String eType, Integer from, Integer to, Integer cap, Double cost, Integer antiEdgeIndexInToV){
            this.eType = eType;
            this.from = from;
            this.to = to;
            this.cap = cap;
            this.capOrg = cap;
            this.cost = cost;
            this.antiEdgeIndexInToV = antiEdgeIndexInToV;
        }

        @Override
        public String toString(){
            if(eType.equals("forward")){
                return "F(" + from + " -> " + to + ")";
            }
            return "R(" + to + " <- " + from + ")";
        }
    }

    // 图: 节点
    public static class GVector{
        public String vName;
        public String vType;
        public Object vInfo;
        public Integer vID=-1;
        public GVector(String vName){
            this.vName = vName;
        }

        public GVector(String vType, String vName){
            this.vType = vType;
            this.vName = vName;
        }

        public GVector(String vType, String vName, Object vInfo){
            this.vType = vType;
            this.vName = vName;
            this.vInfo = vInfo;
        }
        @Override
        public boolean equals(Object o){
            // If the object is compared with itself then return true
            if (o == this) {
                return true;
            }
            if (!(o instanceof GVector)) {
                return false;
            }
            GVector c = (GVector) o;
            return this.vName.equals(c.vName);
        }

        @Override
        public String toString(){
            return this.vType + "( " + this.vName + " )" + "[ID: " + this.vID + "]";
        }
    }

    //  图: 邻接表
    public Map<Integer, List<Edge>> Graph = new HashMap<>();
    public Integer numVectors = 0;
    public List<GVector> vList = new ArrayList<>();
    private final Integer numNodes;
    public MCMF(List<Integer> fromList, List<Integer> toList, List<Integer> linkCap, List<Double> linkCost){
        int len = fromList.size();
        List<GVector> fromVectors = new ArrayList<>();
        List<GVector> toVectors = new ArrayList<>();
        for(int i = 0; i < len; i++){
            fromVectors.add(new GVector("simpleNode", fromList.get(i).toString()));
            toVectors.add(new GVector("simpleNode", toList.get(i).toString()));
        }
        completeGraph(fromVectors, toVectors, linkCap, linkCost);
        this.numNodes = numVectors + 5;
    }

    public void completeGraph(List<GVector> fromVectors, List<GVector> toVectors,
                              List<Integer> linkCap, List<Double> linkCost){
        int len = fromVectors.size();
        for(int i = 0; i < len; i++){
            GVector f = fromVectors.get(i);
            GVector t = toVectors.get(i);
            Integer cap = linkCap.get(i);
            Double cost = linkCost.get(i);
            int fIdx = vList.indexOf(f);
            fIdx = fIdx < 0 ? vList.size(): fIdx;
            if(!vList.contains(f)){
                vList.add(f);
                f.vID = fIdx;
                Graph.put(fIdx, new ArrayList<>());
                numVectors += 1;
            }
            int tIdx = vList.indexOf(t);
            tIdx = tIdx < 0 ? vList.size(): tIdx;
            if(!vList.contains(t)){
                vList.add(t);
                t.vID = tIdx;
                Graph.put(tIdx, new ArrayList<>());
                numVectors += 1;
            }

            Integer fAntiEdgeIdxInTo = Graph.getOrDefault(tIdx, new ArrayList<>()).size();
            Integer tAntiEdgeIdxInFrom = Graph.getOrDefault(fIdx, new ArrayList<>()).size();
            // 正边
            Graph.get(fIdx).add(new Edge(fIdx, tIdx, cap, cost, fAntiEdgeIdxInTo));
            // 反向边
            Graph.get(tIdx).add(new Edge("reverse", tIdx, fIdx, 0, -1.0 * cost, tAntiEdgeIdxInFrom));
        }

    }


    private int[] preV;
    private int[] preVE;
    private boolean[] visited;
    private double[] cost;
    public boolean spfa(Integer start, Integer end){
        Queue<Integer> queue = new LinkedList<>();
        // 用于查找路径
        Arrays.fill(visited, false);
        Arrays.fill(cost, Double.MAX_VALUE);
        Arrays.fill(preV, -1);
        Arrays.fill(preVE, -1);
        cost[start] = 0.0;
        queue.add(start);
        visited[start] = true;
        while(!queue.isEmpty()){
            int now = queue.poll();
            visited[now] = false;
            for(Edge e: Graph.get(now)){
                int c = e.cap;
                double cst = e.cost;
                int to = e.to;
                if(c > 0 && cost[to] > cost[now] + cst){
                    cost[to] = cost[now] + cst;
                    preV[to] = now;
                    preVE[to] = Graph.get(now).indexOf(e);
                    if(!visited[to]){
                        visited[to] = true;
                        queue.add(to);
                    }
                }
            }
        }
        return cost[end] < Double.MAX_VALUE ;
    }

    public List<String> findChangeEdge(){
        List<String> outList = new ArrayList<>();
        for(List<Edge> lEdges: Graph.values()){
            for(Edge e: lEdges){
                if(!e.cap.equals(e.capOrg) && e.cost > 0.0){
                    outList.add(e.from + "->" + e.to);
                }
            }
        }
        return outList;
    }
    public Map<String, Object> mcmf(Integer start, Integer end){
        this.preV = new int[numNodes];
        this.preVE = new int[numNodes];
        this.visited = new boolean[numNodes];
        this.cost = new double[numNodes];
        Map<String, Object> out = new HashMap<String, Object>(){{
                    put("maxFlow", 0.0);
                    put("minCost", 0.0);
        }};
        int s = vList.indexOf(new GVector(start.toString()));
        int e = vList.indexOf(new GVector(end.toString()));
        int pV, pVE;
        while(spfa(s, e)){
            int minFlow = Integer.MAX_VALUE;
            // 寻找cost最小路径-上限流量
            int now=e;
            while(now != s){
                pV = preV[now];
                pVE = preVE[now];
                // pre vector -> now vector 正向边
                int cp = Graph.get(pV).get(pVE).cap;
                minFlow = Math.min(minFlow, cp);
                now = pV;
            }
            // 更新边的cap
            now = e;
            while(now != s){
                pV = preV[now];
                pVE = preVE[now];
                // pre vector -> now vector 正向边
                Graph.get(pV).get(pVE).cap -= minFlow;
                // pre vector <- now vector 反向边
                int antiIdx = Graph.get(pV).get(pVE).antiEdgeIndexInToV;
                Graph.get(now).get(antiIdx).cap += minFlow;
                now = pV;
            }
            System.out.println("minFlow:" + minFlow);
            System.out.println("minCost:" + cost[e]);
            out.put("maxFlow", (Double) out.get("maxFlow") + 1.0 * minFlow);
            out.put("minCost", (Double) out.get("minCost") + cost[e] * 1.0 * minFlow);
        }
        List<String> outPath =  findChangeEdge();
        out.put("flowPath", outPath);
        return out;
    }

    public static void main(String[] args){
        List<Integer>  fList = Arrays.asList(0, 1, 0, 1, 2, 3, 3, 4);
        List<Integer> toList = Arrays.asList(1, 2, 3, 4, 5, 2, 4, 5);
        List<Double>    cost = Arrays.asList(0.0, 1.0, 0.0, 2.0, 0.0, 3.0, 10.0, 0.0);
        List<Integer> cap = Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1);
        MCMF mcmfFunc = new MCMF(fList, toList, cap, cost);
        Map<String, Object> out = mcmfFunc.mcmf(0, 5);
        System.out.println(out);
    }

}
